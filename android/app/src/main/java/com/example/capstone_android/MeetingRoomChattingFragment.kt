package com.example.capstone_android

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.FragmentMeetingRoomChattingBinding
import com.example.capstone_android.databinding.FragmentShowPostingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MeetingRoomChattingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeetingRoomChattingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : MeetingRoomChattingViewModel by viewModels<MeetingRoomChattingViewModel>()
    private var _binding: FragmentMeetingRoomChattingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var document_id =""
    val db = Firebase.firestore
    var rootRef = Firebase.storage.reference
    val userCollection = db.collection("user")
    val commentCollection = db.collection("comment")
    val meetingRoomCollection = db.collection("meeting_room")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    var numOfChatting =-1
    var initChatCnt =0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        println("onCreateView")
        _binding = FragmentMeetingRoomChattingBinding.inflate(inflater,container,false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MeetingRoomChattingAdapter(viewModel)
        //val meetingMembersRecyclerView = v.findViewById<RecyclerView>(R.id.meetingMembersRecyclerView)
        val recyclerViewCommentChatting = binding.chattingRecyclerView
        recyclerViewCommentChatting.adapter = adapter
        recyclerViewCommentChatting.layoutManager = LinearLayoutManager(activity)
        recyclerViewCommentChatting.setHasFixedSize(true)
        viewModel.itemsListData.observe(viewLifecycleOwner ){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
        }
        registerForContextMenu(recyclerViewCommentChatting)
        //데이터 얻어와서 ui에 반영
        initDataAndUI()

        println("onViewCreated ${viewModel.items.size}")
    }
    private fun initDataAndUI(){
        document_id = activity?.intent?.getStringExtra("meeting_room_id").toString()

        println("document_id ----------- ${document_id}")
        println("uid =-------- ${Firebase.auth.uid}")
        binding.uploadChattingBtn.setOnClickListener {
            //editText 에 있는 거 받아서 comment컬랙션에 add

            val inputText = binding.chattingEditText.text
            val time = System.currentTimeMillis()
            val itemMap = hashMapOf(
                "comment_text" to inputText.toString(),
                "upload_time" to time,
                "writer_uid" to Firebase.auth.uid
            )

            commentCollection.add(itemMap).addOnSuccessListener {
                // posting 컬랙션에 배열 추가해서 적용 set사용

                val document_id_comment = it.id
                meetingRoomCollection.document(document_id).update("chatting_id_list" , FieldValue.arrayUnion(document_id_comment))
            }

        }
        meetingRoomCollection.document(document_id).get().addOnSuccessListener {
            val comment_id_list = it.data?.get("chatting_id_list")
            println("comment_id_list addOnSuccessListener ${comment_id_list}")

            //댓글 가져오기 리사이클러 뷰에 반영 ok
            if(comment_id_list==null){
                return@addOnSuccessListener
            }
            getCommentsToRecyclerView(comment_id_list!!)
            meetingRoomCollection.document(document_id).addSnapshotListener { value, error ->
                if(numOfChatting ==-1){
                    println("아직 초기화 안됨 meetingRoomCollection addSnapshotListener -1")
                    return@addSnapshotListener
                }
                if (initChatCnt < numOfChatting){
                    println("아직 초기화 안됨 meetingRoomCollection addSnapshotListener <")
                    return@addSnapshotListener
                }
                val comment_id_list = value?.data?.get("chatting_id_list")
                println("comment_id_list addSnapshotListener ${comment_id_list}")
                if(comment_id_list==null){
                    return@addSnapshotListener
                }
                val commentIdListListString :List<String> = comment_id_list as List<String>
                if(commentIdListListString.size <=0){
                    return@addSnapshotListener
                }
                updateRecyclerView(commentIdListListString[commentIdListListString.size-1])
                //미팅룸의 댓글 리스트가 변하는 순간은 댓글이 추가되는 경우 밖에 없을때 정상 작동
                //댓글 삭제 기능이 추가 되면 if문으로 분기 처리를 해주어야 함
            }
            //댓글에 있는 맴버들도 snapShot 추가 ok
        }

    }
    private fun updateRecyclerView(comment_id: String){
        println("updateRecyclerView")
        commentCollection.document(comment_id.trim()).get().addOnSuccessListener {
            var writer_uid = ""
            var upload_time :Long
            var comment_text = ""

            writer_uid = "${it["writer_uid"]}"
            upload_time = it["upload_time"] as Long
            comment_text = "${it["comment_text"]}"
            userCollection.document(writer_uid).get().addOnSuccessListener {
                updateNumOfChatting(true)
                addUserToRecyclerView("${it["nickname"]}",writer_uid,upload_time,comment_text,comment_id)
                addUserSnapShot(writer_uid)
            }
        }
    }
    private fun getCommentsToRecyclerView(comment_id_list:Any){
        val commentIdListListString :List<String> = comment_id_list as List<String>
        numOfChatting = commentIdListListString.size
        for(commentId in commentIdListListString){
            commentCollection.document(commentId.trim()).get().addOnSuccessListener {
                var writer_uid = ""
                var upload_time :Long
                var comment_text = ""

                writer_uid = "${it["writer_uid"]}"
                upload_time = it["upload_time"] as Long
                comment_text = "${it["comment_text"]}"
                userCollection.document(writer_uid).get().addOnSuccessListener {
                    addUserToRecyclerView("${it["nickname"]}",writer_uid,upload_time,comment_text,commentId)
                    addUserSnapShot(writer_uid)
                }.addOnFailureListener {
                    updateInitCnt(false)
                }
            }.addOnFailureListener {
                updateInitCnt(false)
            }

        }

    }
    fun addUserToRecyclerView(nickname: String,writer_uid:String,upload_time:Long, comment_text:String,commemt_id:String){
        var userProfileImage = rootRef.child("user_profile_image/${writer_uid}.jpg")
        userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                for(comment in viewModel.items){//중복 검사 이미 그 맴버가 있는 데 또 추가 할 수 없다.
                    if (comment.document_id == commemt_id){
                        return@addOnCompleteListener
                    }
                }
                viewModel.addItem(ChattingData(bmp,nickname,comment_text,upload_time,writer_uid,commemt_id))
                updateInitCnt(true)
            }else{
                var ref = rootRef.child("user_profile_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        for(comment in viewModel.items){//중복 검사
                            if (comment.document_id == commemt_id){
                                return@addOnCompleteListener
                            }
                        }
                        viewModel.addItem(ChattingData(bmp,nickname,comment_text,upload_time,writer_uid,commemt_id))
                        updateInitCnt(true)
                    }else{
                        println("undefined err")
                    }
                }
            }
        }
    }
    fun updateUserToRecyclerview(i:Int,writer_uid:String,nickname:String, comment:ChattingData){
        var userProfileImage = rootRef.child("user_profile_image/${writer_uid}.jpg")
        userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                viewModel.updateItem(i, ChattingData(bmp,nickname,comment.commentText,comment.timePosting
                    ,comment.writer_uid,comment.document_id))
            }else{
                var ref = rootRef.child("user_profile_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        viewModel.updateItem(i, ChattingData(bmp,nickname,comment.commentText,comment.timePosting
                            ,comment.writer_uid,comment.document_id))
                    }else{
                        println("undefined err")
                    }
                }
            }
        }
    }

    @Synchronized
    fun updateInitCnt(isSuccess :Boolean,isSort :Boolean = true){//임계 영역
        if(isSuccess){
            if(isSort){
                if (initChatCnt+1 >= numOfChatting){// 마지막 것을 받아왔을때 정렬한다.
                    viewModel.items.sortBy{
                        it.timePosting
                    }
                    viewModel.itemsListData.value = viewModel.items

                }
            }

            initChatCnt++ //비동기로 추가 될때 마다 업데이트
        }else{
            if(isSort){
                if (initChatCnt-1 >= numOfChatting){// 마지막 것을 받아왔을때 정렬한다.
                    viewModel.items.sortBy{
                        it.timePosting
                    }
                    viewModel.itemsListData.value = viewModel.items

                }
            }
            initChatCnt--
        }

    }
    @Synchronized
    fun updateNumOfChatting(isPlus:Boolean){
        if(isPlus){
            numOfChatting++
        }else{
            numOfChatting--
        }
    }
    fun addUserSnapShot(writer_uid :String){
        userCollection.document(writer_uid.trim()).addSnapshotListener { snapshot, error ->
            if(initChatCnt < numOfChatting){
                println("아직 초기화 안됨 userCollection addSnapshotListener")
                return@addSnapshotListener
            }
            val nickname = snapshot?.data?.get("nickname")
            println("addSnapShot -------- getnickname ${nickname}")
            val uid = snapshot?.data?.get("uid") as String
            var i=0
            for(comment in viewModel.items){
                if(comment.writer_uid == uid){
                    updateUserToRecyclerview(i,uid.trim(),
                        nickname as String, comment
                    )
                }
                i++
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
         * @return A new instance of fragment MeetingRoomChattingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MeetingRoomChattingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}