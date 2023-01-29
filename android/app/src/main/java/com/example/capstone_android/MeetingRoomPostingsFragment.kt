package com.example.capstone_android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.FragmentMeetingRoomInfoBinding
import com.example.capstone_android.databinding.FragmentMeetingRoomPostingsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MeetingRoomPostingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeetingRoomPostingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : MeetingRoomPostingsViewModel by viewModels<MeetingRoomPostingsViewModel>()
    private var _binding: FragmentMeetingRoomPostingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //getter 함수 null이 아닌 ui 객체 리턴
    val db = Firebase.firestore
    var isInit =false
    var isInitAboutMember =false
    var myNickName ="star1"//닉네임은 일단 알고 있다고 가정함 login Activity에서 넘겨 받아야 함
    var cnt =0

    var rootRef = Firebase.storage.reference
    val meetingRoomCollection = db.collection("meeting_room")
    val postingCollection = db.collection("posting")
    val userCollection = db.collection("user")
    var meetingRoomId = ""
    var memCurruntCnt =0
    var memCntOfFirebase =0
    class DataForPostingUI(val infoText:Any?,val max:Any?,val memberList:Any?,
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
        _binding = FragmentMeetingRoomPostingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //비동기 처리시 기다리게 하는 등의 방법을 사용해서 게시판은 업로드한 순으로 보여준다.

        val adapter = MeetingRoomPostingsAdapter(viewModel)
        //val meetingMembersRecyclerView = v.findViewById<RecyclerView>(R.id.meetingMembersRecyclerView)
        val meetingMembersRecyclerView = binding.postingRecyclerView
        meetingMembersRecyclerView.adapter = adapter
        meetingMembersRecyclerView.layoutManager = LinearLayoutManager(activity)
        meetingMembersRecyclerView.setHasFixedSize(true)
        viewModel.itemsListData.observe(viewLifecycleOwner ){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
            val bundle = bundleOf("document_id" to viewModel.items[i!!].document_id)
            findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_showPostingFragment,bundle)
        }

        registerForContextMenu(meetingMembersRecyclerView)
        //viewModel.addItem(Posting(null,"asdf","asddfgdf",1000,"","adsf"))
        //viewModel.addItem(Posting(null,"asdf","fdsgfh",100002,"","asdf"))

        //데이터 얻어와서 ui에 반영
        initDataAndUI()
    }
    private fun initDataAndUI(){
        meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id").toString()
        meetingRoomCollection.document(meetingRoomId).get().addOnSuccessListener {
            val posting_id_list = it["posting_id_list"] as List<String>
            for (posting_id in posting_id_list){
                postingCollection.document(posting_id).get().addOnSuccessListener {
                    val text =it["text"]
                    val upload_time = it["upload_time"]
                    val writer_uid = it["writer_uid"]
                    val document_id = it.id

                    addUserToRecyclerView(writer_uid as String, text as String, upload_time as Long,document_id)
                }
            }
        }

        postingCollection.addSnapshotListener { value, error ->
            if(value == null){
                return@addSnapshotListener
            }
            for(d in value!!.documentChanges){
                if ("${d.type}" =="REMOVED"){
                    for(posting in viewModel.items){
                        if(d.document.id == posting.document_id){
                            viewModel.deleteItem(posting)
                            break
                        }
                    }
                }else if("${d.type}" =="MODIFIED"){
                    val posting = viewModel.findItem(d.document.id)
                    val i = viewModel.items.indexOf(posting)
                    if (posting != null) {
                        viewModel.updateItem(i,
                            Posting(posting.icon,posting.nickname,d.document["text"].toString(), d.document["upload_time"] as Long,posting.document_id,posting.writer_uid))
                        viewModel.items.sortByDescending {
                            it.timePosting
                        }
                        viewModel.itemsListData.value = viewModel.items
                    }
                }else if("${d.type}" =="ADDED"){
                    //추가 하기
                    val text =d.document["text"]
                    val upload_time = d.document["upload_time"]
                    val writer_uid = d.document["writer_uid"]
                    val document_id = d.document.id

                    addUserToRecyclerView(writer_uid as String, text as String, upload_time as Long,document_id)

                }
            }
        }

    }

    fun addUserToRecyclerView(writer_uid:String, postingText:String,timePosting:Long,document_id:String){

        userCollection.document(writer_uid).get().addOnSuccessListener {
            val nickname = it.data?.get("nickname")
            var userProfileImage = rootRef.child("user_profile_image/${writer_uid}.jpg")
            userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                    for(posting in viewModel.items){//중복 검사
                        if (posting.document_id == document_id){
                            return@addOnCompleteListener
                        }
                    }
                    viewModel.addItem(Posting(bmp,
                        nickname as String, postingText, timePosting,document_id,writer_uid))
                    viewModel.items.sortByDescending {
                        it.timePosting
                    }
                    viewModel.itemsListData.value = viewModel.items
                    addUserSnapShot(writer_uid)

                }else{
                    var ref = rootRef.child("user_profile_image/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                            for(posting in viewModel.items){
                                if (posting.document_id == document_id){
                                    return@addOnCompleteListener
                                }
                            }
                            viewModel.addItem(Posting(bmp,
                                nickname as String, postingText, timePosting,document_id,writer_uid))
                            viewModel.items.sortByDescending {
                                it.timePosting
                            }
                            viewModel.itemsListData.value = viewModel.items
                            addUserSnapShot(writer_uid)
                        }else{
                            println("undefined err")
                        }
                    }
                }
            }

        }

    }
    fun addUserSnapShot(writer_uid: String){
        userCollection.document(writer_uid).addSnapshotListener { value, error ->
            val nickname =value?.data?.get("nickname")
            val uid = value?.data?.get("uid")
            var userProfileImage = rootRef.child("user_profile_image/${uid}.jpg")
            var bmp:Bitmap
            userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful) {
                    bmp =
                        BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    var i=0
                    for (posting in viewModel.items){
                        if(posting.writer_uid == uid){
                            viewModel.updateItem(i,
                                Posting(bmp,
                                    nickname as String,posting.postingText,posting.timePosting,posting.document_id,posting.writer_uid)
                            )
                        }
                        i++
                    }

                }else{
                    var ref = rootRef.child("user_profile_image/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            bmp =
                                BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                            var i=0
                            for (posting in viewModel.items){
                                if(posting.writer_uid == uid){
                                    viewModel.updateItem(i,
                                        Posting(bmp,
                                            nickname as String,posting.postingText,posting.timePosting,posting.document_id,posting.writer_uid)
                                    )
                                }
                                i++
                            }
                        }else{
                            println("undefined err")
                        }
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
         * @return A new instance of fragment MeetingRoomPostingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MeetingRoomPostingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}