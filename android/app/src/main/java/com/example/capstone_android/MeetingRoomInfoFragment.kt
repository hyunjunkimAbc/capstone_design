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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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
    //val friendCommit = db.collection("friendCommit")
    //val userProfiles = db.collection("userProfils")

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
        _binding = null//메모리 누수 방지
    }
    @SuppressLint("SetTextI18n")
    private fun initDataAndUI(){
        //val meetingRoomId = viewModel.meetingRoomId
        val meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id") ?: ""
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
        //미팅룸 정보 얻어오기
        meetingRoomCollection.document(meetingRoomId).get().addOnSuccessListener {

            var infoText = it.data?.get("info_text")
            var max =it.data?.get("max")
            var memberList = it.data?.get("member_list")//String ArrayList로 viewmodel에 저장
            var numOfMember = it.data?.get("num_of_member")
            //var postingIdList ="" //String ArrayList로 MeetingRoomPostingsFragment에서 사용할 데이터 이기 때문에 보류
            var title =it.data?.get("title")
            var upload_time =it.data?.get("upload_time")
            var category = it.data?.get("category")
            viewModel.memberListArrStr = "$memberList"
            println("${viewModel.memberListArrStr} -------")
            binding.meetingRoomText.text = infoText.toString()
            binding.numOfPeople.text = "${numOfMember.toString()}(현재 인원) / ${max.toString()}(최대 인원)"
            binding.uploadTime.text = "작성 시간: ${upload_time.toString()}"
            binding.meetingRoomTitle.text = "모임 명: ${title.toString()}"
            binding.category.text = "카테고리: ${category.toString()}"


            isInitAboutInfo = true
            //memberList를 확실히 viewModel에 저장한 후에 recyclerview를 불러와야 함
        }

        //미팅룸의 정보가 변했을때
        meetingRoomCollection.addSnapshotListener { value, error ->
            if (value ==null) return@addSnapshotListener
            for(d in value!!.documentChanges){
                if ("${d.type}" =="REMOVED"){
                    //미팅룸 삭제 시에 작동 다른 미팅룸이 삭제 되었을때는 신경쓸 필요 없고
                    // 현재 미팅룸이 삭제 되었을때는 아직 정의 되지 않음

                }else if("${d.type}" =="MODIFIED"){
                    //미팅룸 정보 수정시에 작동

                }else if("${d.type}" =="ADDED"){
                    //다른 미팅룸이 추가 되었을때 현재 미팅룸에서 뭔가 작동할 이유가 아직 없음
                    if(isInitAboutInfo){//처음에 초기화 할때는 작동안되게 함

                    }
                }
            }
        }

        //맴버 정보 불러오기
        userCollection.get().addOnSuccessListener {
            if(viewModel.items.size >0){//만약 이 값이 0 이상이면 이미 받아온 데이터가 있는 상황
                println("이미 있음---------")
                return@addOnSuccessListener
            }
            for(user in it) {// 게시글 문서들을 다 받아와서 문서마다 추가한다.
                // 맴버를 가입한 순서대로 불러오는 것이 의미가 있을지는 잘 모르겠음
                var profileMessage = ""
                var nickname = ""
                var uid = ""

                uid = "${user["uid"]}"
                profileMessage = "프로필 메시지: ${user["profile_message"]}"
                nickname = "이름: ${user["nickname"]}"
                addUserToRecyclerView(uid,nickname,profileMessage)
            }
            //println("${post.id}  ${post["title"]} ${post["nickName"]}");
            isInitAboutMember = true
        }

        //미팅룸의 회원들의 정보가 변했을때
        userCollection.addSnapshotListener { value, error ->
            if (value ==null) return@addSnapshotListener
            for(d in value!!.documentChanges){
                if ("${d.type}" =="REMOVED"){
                    /*
                    for(k in viewModel.items){
                        if(k.nickName == "${d.document.id}"){//ㅕuser id == 문서 id
                            viewModel.deleteItem(k)
                        }
                    }*/
                    //해당 회원이 탈퇴 할 경우에 대해서 정의 하지 않았음
                }else if("${d.type}" =="MODIFIED"){
                    //친구 관계 수정 되었을때(아직 하지 않음), 신규 맴버 추가 되었을때
                    var i =0;
                    for(k in viewModel.items) {
                        System.out.println("k.nickname "+k.nickname+"docu id"  +"${d.document.id}" );
                        System.out.println("ok");
                        if ( k.uid == "${d.document["uid"]}" ) {//데이터 변경자의 게시물일때와 로그인 사용자의 데이터가 변경될때만 변경하면됨
                            //updatePostingData(k.postId,k.title,k.nickName,i) //글 작성자와 본인과의 관계와 데이터 변경자의 관계를 고려 해야 함
                        }else{// 나머지 모든 경우는 패스
                        }
                        i++
                        /*
                        if ( k.nickName == "${d.document["nickName"]}" || "${d.document["nickName"]}" ==myNickName) {//데이터 변경자의 게시물일때와 로그인 사용자의 데이터가 변경될때만 변경하면됨
                            updatePostingData(k.postId,k.title,k.nickName,i) //글 작성자와 본인과의 관계와 데이터 변경자의 관계를 고려 해야 함
                            System.out.println("stat "+k.statCode) //윗 줄은 friendCommit의 nickName이 회원정보 수정으로 변동 될수 있어서 그렇게 처리함
                        }else{// 나머지 모든 경우는 패스
                        }
                        i++ //모든 게시물을 다 조회한다.*/

                    }
                }else if("${d.type}" =="ADDED"){
                    if(viewModel.items.size ==0){
                        return@addSnapshotListener
                    }
                    /*
                    if(isInit){//새로운 회원이 추가되어서 friendCommit 에 목록이 추가 되는 경우 게시글 을 쓴 것이 아니라 추가 할 필요 없다.
                        var title =""
                        var nickname=""
                        var id = ""
                        id ="${d.document.id}"
                        title = "${d.document.data["title"]}"
                        nickname ="${d.document.data["nickName"]}"
                        addPostingData(id, title, nickname)
                    }*/
                }
            }
        }


    }
    fun addUserToRecyclerView(uid:String,nickname:String, profileMessage:String){
        var userProfileImage = rootRef.child("user_profile_image/${uid}.jpg")
        userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                viewModel.addItem(Member(bmp,nickname,profileMessage,uid))
            }else{
                var ref = rootRef.child("user_profile_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        viewModel.addItem(Member(bmp,nickname,profileMessage,uid))
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