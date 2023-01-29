package com.example.capstone_android

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.databinding.FragmentMeetingRoomInfoBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MeetingRoomInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeetingRoomInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : MeetingRoomInfoViewModel by viewModels<MeetingRoomInfoViewModel>()

    private var _binding: FragmentMeetingRoomInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //getter 함수 null이 아닌 ui 객체 리턴
    val db = Firebase.firestore
    var isInitAboutInfo =false
    var isInitAboutMember =false
    var myNickName ="star1"//닉네임은 일단 알고 있다고 가정함 login Activity에서 넘겨 받아야 함

    var rootRef = Firebase.storage.reference

    val meetingRoomCollection = db.collection("meeting_room")
    val userCollection = db.collection("user")
    var meetingRoomId = ""
    var memCurruntCnt =0
    var memCntOfFirebase =0
    class DataForUI(val infoText:Any?,val max:Any?,val memberList:Any?,
                    val title:Any?,val upload_time:Any?,val category:Any?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //v = inflater.inflate(R.layout.fragment_meeting_room_info, container, false)
        _binding = FragmentMeetingRoomInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //meetingroominfo
        val adapter = MeetingRoomInfoAdapter(viewModel)
        //val meetingMembersRecyclerView = v.findViewById<RecyclerView>(R.id.meetingMembersRecyclerView)
        val meetingMembersRecyclerView = binding.meetingMembersRecyclerView
        meetingMembersRecyclerView.adapter = adapter
        meetingMembersRecyclerView.layoutManager = LinearLayoutManager(activity)
        meetingMembersRecyclerView.setHasFixedSize(true)
        viewModel.itemsListData.observe(viewLifecycleOwner ){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
        }

        registerForContextMenu(meetingMembersRecyclerView)

        //데이터 얻어와서 ui에 반영
        initDataAndUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("------ondestroyView")
        //_binding = null//메모리 누수 방지

    }
    @SuppressLint("SetTextI18n")
    private fun initDataAndUI(){
        //val meetingRoomId = viewModel.meetingRoomId 모임 설명 이미지 얻어오기
        meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id").toString()
        //미팅룸 정보 얻어오기
        meetingRoomCollection.document(meetingRoomId).get().addOnSuccessListener {

            println("-------- meetingRoomCollection addOnSuccessListener")
            println("----- num ${viewModel.items.size}")
            val infoText = it.data?.get("info_text")
            val max =it.data?.get("max")
            val memberList = it.data?.get("member_list")
            val friendUidArr :List<String> = memberList as List<String>
            //println("----------${friendUidArr}")
            //var numOfMember = it.data?.get("num_of_member")
            //memCntOfFirebase = friendUidArr.size

            //var postingIdList ="" //String ArrayList로 MeetingRoomPostingsFragment에서 사용할 데이터 이기 때문에 보류
            val title =it.data?.get("title")
            val upload_time =it.data?.get("upload_time")
            val category = it.data?.get("category")

            val dataForUI = DataForUI(infoText,max,memberList,title,upload_time,category)
            updateInfoUI(dataForUI)

            if(viewModel.items.size>0){//초기화 할때 0이상이면 예전에 저장했던 정보가 있는 것임
                return@addOnSuccessListener
            }
            for(friendUid in friendUidArr){
                userCollection.document(friendUid.trim()).get().addOnSuccessListener {
                    var profileMessage = ""
                    var nickname = ""
                    var uid = ""

                    uid = "${it["uid"]}"
                    profileMessage = "${it["profile_message"]}"
                    nickname = "${it["nickname"]}"
                    addUserToRecyclerView(uid,nickname,profileMessage)
                    memCurruntCnt++
                    if(memCntOfFirebase==memCurruntCnt){
                        println("------ all success ${memCurruntCnt}/${memCntOfFirebase}")
                        isInitAboutMember = true
                    }

                }
                //미팅룸의 회원들의 정보가 변했을때
                userCollection.document(friendUid.trim()).addSnapshotListener { snapshot, error ->
                    if(isInitAboutMember==false){
                        return@addSnapshotListener
                    }
                    val nickname = snapshot?.data?.get("nickname")
                    val profileMessage =snapshot?.data?.get("profile_message")
                    var i=0
                    for(member in viewModel.items){
                        if(friendUid.trim() == member.uid.trim()){
                            updateUserToRecyclerview(i,friendUid.trim(),
                                nickname as String, profileMessage as String
                            )
                            break
                        }
                        i++
                    }

                }
            }

        }
        //미팅룸의 정보가 변했을때
        meetingRoomCollection.document( meetingRoomId ).addSnapshotListener { snapshot, error ->
            println("${snapshot?.id} ${snapshot?.data?.get("info_text")}")
            println("-------- meetingRoomCollection addSnapshotListener")
            val infoText =snapshot?.data?.get("info_text")
            val max =snapshot?.data?.get("max")
            val memberList = snapshot?.data?.get("member_list")
            val friendUidArr :List<String> = memberList as List<String>

            //var postingIdList ="" //String ArrayList로 MeetingRoomPostingsFragment에서 사용할 데이터 이기 때문에 보류
            val title =snapshot?.data?.get("title")
            val upload_time =snapshot?.data?.get("upload_time")
            val category = snapshot?.data?.get("category")
            val dataForUI = DataForUI(infoText,max,memberList,title,upload_time,category)

            //viewmodel 기존에 있는거 삭제 해야 함
            updateInfoUI(dataForUI)
            updateMember(friendUidArr)


        }

    }
    fun updateInfoUI(dataForUI: DataForUI){

        val friendUidArr :List<String> = dataForUI.memberList as List<String>
        //println("----------${friendUidArr}")
        //var numOfMember = it.data?.get("num_of_member")
        memCntOfFirebase = friendUidArr.size
        meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id") ?: ""
        var meetingInfoImage = rootRef.child("meeting_info/${meetingRoomId}.jpg")
        meetingInfoImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                binding.meetingInfoImage.setImageBitmap(bmp)
            }else{
                var ref = rootRef.child("meeting_info/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        binding.meetingInfoImage.setImageBitmap(bmp)
                    }else{
                        println("undefined err")
                    }
                }
            }
        }

        binding.meetingRoomText.text = dataForUI.infoText.toString()
        binding.numOfPeople.text = "${memCntOfFirebase}(현재 인원) / ${dataForUI.max.toString()}(최대 인원)"
        binding.uploadTime.text = "최종 업로드 ${SimpleDateFormat("yyyy-MM-dd").format(dataForUI.upload_time as Long)}"
        binding.meetingRoomTitle.text = "모임 명: ${dataForUI.title.toString()}"
        binding.category.text = "카테고리: ${dataForUI.category.toString()}"

        isInitAboutInfo = true
        //memberList를 확실히 viewModel에 저장한 후에 recyclerview를 불러와야 함
    }

    @Synchronized
    fun updateMember(friendUidArr: List<String>){
        if(viewModel.items.size == friendUidArr.size){//개수 변화 없으면 아무것도 하지 않음
            println("탈출----")
        }else if(viewModel.items.size <friendUidArr.size){//맴버가 추가 되면(개수 증가) 새로 받아오기
            //맨뒤에 있는 것이 새로운 맴버일때 정상 동작함 but 2번 추가된다.
            userCollection.document(friendUidArr[friendUidArr.size-1]).get().addOnSuccessListener {
                println("---userCollection addOnSuccessListener in addSnapshotListener")
                var profileMessage = ""
                var nickname = ""
                var uid = ""

                uid = "${it["uid"]}"
                profileMessage = "${it["profile_message"]}"
                nickname = "${it["nickname"]}"

                addUserToRecyclerView(uid,nickname,profileMessage)
            }

        }else if(viewModel.items.size > friendUidArr.size){//맴버가 사라지면 그 맴버는 리사이클러에서 지우기
            println("삭제 ------")
            var isInFirebase =false
            for(member in viewModel.items){
                isInFirebase =false
                for (friendUid in friendUidArr){
                    if(member.uid == friendUid){
                        isInFirebase = true
                    }
                }
                if(isInFirebase){
                    continue
                }else{//firebase에는 없는데 viewmodel에는 맴버가 있는 상황 그 맴버는 지워주면 된다
                    viewModel.deleteItem(member)
                    break
                }
            }
        }
    }

    fun addUserToRecyclerView(uid:String,nickname:String, profileMessage:String){
        var userProfileImage = rootRef.child("user_profile_image/${uid}.jpg")
        userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                for(member in viewModel.items){//중복 검사 이미 그 맴버가 있는 데 또 추가 할 수 없다.
                    if (member.uid == uid){
                        return@addOnCompleteListener
                    }
                }
                viewModel.addItem(Member(bmp,nickname,profileMessage,uid))
            }else{
                var ref = rootRef.child("user_profile_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        for(member in viewModel.items){//중복 검사
                            if (member.uid == uid){
                                return@addOnCompleteListener
                            }
                        }
                        viewModel.addItem(Member(bmp,nickname,profileMessage,uid))
                    }else{
                        println("undefined err")
                    }
                }
            }
        }
    }
    fun updateUserToRecyclerview(i:Int,uid:String,nickname:String, profileMessage:String){
        var userProfileImage = rootRef.child("user_profile_image/${uid}.jpg")
        userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                viewModel.updateItem(i,Member(bmp,nickname,profileMessage,uid))
            }else{
                var ref = rootRef.child("user_profile_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        viewModel.updateItem(i,Member(bmp,nickname,profileMessage,uid))
                    }else{
                        println("undefined err")
                    }
                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MeetingRoomInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MeetingRoomInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


