package com.example.capstone_android

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.FragmentMeetingRoomPostingsBinding
import com.example.capstone_android.databinding.FragmentShowPostingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowPostingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowPostingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : ShowPostingViewModel by viewModels<ShowPostingViewModel>()
    private var _binding: FragmentShowPostingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var document_id =""
    val db = Firebase.firestore
    var rootRef = Firebase.storage.reference
    val postingCollection = db.collection("posting")
    val userCollection = db.collection("user")
    val commentCollection = db.collection("comment")
    var postingWriterUid =""

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
        _binding = FragmentShowPostingBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //비동기 처리시 기다리게 하는 등의 방법을 사용해서 게시판은 업로드한 순으로 보여준다.

        val adapter = ShowPostingAdapter(viewModel)
        //val meetingMembersRecyclerView = v.findViewById<RecyclerView>(R.id.meetingMembersRecyclerView)
        val recyclerViewCommentPosting = binding.recyclerViewCommentPosting
        recyclerViewCommentPosting.adapter = adapter
        recyclerViewCommentPosting.layoutManager = LinearLayoutManager(activity)
        recyclerViewCommentPosting.setHasFixedSize(true)
        viewModel.itemsListData.observe(viewLifecycleOwner ){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
            //findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_showPostingFragment)
        }

        registerForContextMenu(recyclerViewCommentPosting)
        //viewModel.addItem(Comment(null,"asdf","asddfgdf",1000,"","adsf"))
        //viewModel.addItem(Comment(null,"asdf","fdsgfh",100002,"","asdf"))
        //viewModel.addItem(Comment(null,"asdf","asddfgdf",1000,"jx2EKi4ed9hoy73X0LTI7TcZd8B2","adsf"))
        //viewModel.addItem(Comment(null,"asdf","fdsgfh",100002,"","asdf"))

        //데이터 얻어와서 ui에 반영
        initDataAndUI()

    }
    private fun initDataAndUI(){
        document_id = arguments?.getString("document_id").toString() //posting id 얻어왔음

        println("document_id ----------- ${document_id}")
        println("uid =-------- ${Firebase.auth.uid}")
        binding.commentUploadBtn.setOnClickListener {
            //editText 에 있는 거 받아서 comment컬랙션에 add

            val inputText = binding.commentEditText.text
            val time = System.currentTimeMillis()
            val itemMap = hashMapOf(
                "comment_text" to inputText.toString(),
                "upload_time" to time,
                "writer_uid" to Firebase.auth.uid
            )
            commentCollection.add(itemMap).addOnSuccessListener {
                // posting 컬랙션에 배열 추가해서 적용 set사용

                val document_id_comment = it.id
                postingCollection.document(document_id).update("comment_id_list" , FieldValue.arrayUnion(document_id_comment))
            }

        }
        postingCollection.document(document_id).get().addOnSuccessListener {
            val writer_uid = it.data?.get("writer_uid").toString()
            postingWriterUid = writer_uid
            binding.editPostingButton.setOnClickListener {
                if(Firebase.auth.uid == postingWriterUid){
                    val bundle = bundleOf("document_id" to document_id)
                    findNavController().navigate(R.id.action_showPostingFragment_to_editPostingFragment,bundle)
                }else{
                    Toast.makeText(activity?.applicationContext,"글 작성자가 아닙니다. 접근할 수 없습니다.",Toast.LENGTH_LONG).show()
                }
            }
            val upload_time = it.data?.get("upload_time")
            val title = it.data?.get("title").toString()
            val text = it.data?.get("text").toString()
            val comment_id_list = it.data?.get("comment_id_list")
            addWriterSnapShot(writer_uid)
            initPostingUI(writer_uid, upload_time as Long,title,text)
            //댓글 가져오기 리사이클러 뷰에 반영 ok
            getCommentsToRecyclerView(comment_id_list!!)
        }
        postingCollection.document(document_id).addSnapshotListener { value, error ->
            val writer_uid = value?.data?.get("writer_uid").toString()
            val upload_time = value?.data?.get("upload_time")
            val title = value?.data?.get("title").toString()
            val text = value?.data?.get("text").toString()
            updatePostingUI(writer_uid, upload_time as Long,title,text)
            //댓글 추가 혹은 삭제 되면 업데이트 수정기능은 아직 보류한다는 가정하에 추가 기능만 작동
            // 만약 삭제 기능 추가 되면 코드도 바뀌어야 함
            val comment_id_list = value.data?.get("comment_id_list") as List<String>
            if(viewModel.items.size == comment_id_list.size){//개수 변화 없으면 아무것도 하지 않음
                println("탈출----")
            }else if(viewModel.items.size <comment_id_list.size){//맴버가 추가 되면(개수 증가) 새로 받아오기
                //맨뒤에 있는 것이 새로운 맴버일때 정상 동작함
                addPostingComment(comment_id_list[comment_id_list.size-1])
            }else if(viewModel.items.size > comment_id_list.size){//맴버가 사라지면 그 맴버는 리사이클러에서 지우기
                println("삭제 ------")
                var isInFirebase =false
                for(comment in viewModel.items){
                    isInFirebase =false
                    for (commentID in comment_id_list){
                        if(comment.document_id == commentID){
                            isInFirebase = true
                        }
                    }
                    if(isInFirebase){
                        continue
                    }else{//firebase에는 없는데 viewmodel에는 맴버가 있는 상황 그 맴버는 지워주면 된다
                        viewModel.deleteItem(comment)
                        break
                    }
                }
            }
            //getCommentsToRecyclerView(comment_id_list!!)
        }
        //댓글에 있는 맴버들도 snapShot 추가 ok
    }
    private fun addPostingComment(commemt_id: String){
        commentCollection.document(commemt_id.trim()).get().addOnSuccessListener {
            var writer_uid = ""
            var upload_time :Long
            var comment_text = ""

            writer_uid = "${it["writer_uid"]}"
            upload_time = it["upload_time"] as Long
            comment_text = "${it["comment_text"]}"
            userCollection.document(writer_uid).get().addOnSuccessListener {
                addUserToRecyclerView("${it["nickname"]}",writer_uid,upload_time,comment_text,commemt_id)
            }
        }
    }
    private fun addWriterSnapShot(writer_uid: String){
        userCollection.document(writer_uid).addSnapshotListener { value, error ->
            binding.nicknamePostingShow.text = "${value?.data?.get("nickname")}"
            var ref = rootRef.child("user_profile_image/${value?.data?.get("uid")}.jpg")
            ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful){
                    val bmp =
                        BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.profileImgPostingShow.setImageBitmap(bmp)
                }else{
                    var ref = rootRef.child("user_profile_image/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp =
                                BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                            binding.profileImgPostingShow.setImageBitmap(bmp)
                        }else{
                            println("undefined err")
                        }
                    }
                    println("undefined err")
                }
            }
        }
    }
    private fun updatePostingUI(writer_uid:String,upload_time:Long,title:String,text:String){
        val writer_uid = writer_uid

        binding.timePostingShow.text = "${SimpleDateFormat("yyyy-MM-dd").format(upload_time)}"
        binding.titlePostingShow.text = "${title}"

        binding.textPostingShow.text = "${text}"

        var ref = rootRef.child("posting_image/${document_id}.jpg")
        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp =
                    BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                binding.imageViewPostingShow.setImageBitmap(bmp)
            }else{
                var ref = rootRef.child("posting_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp =
                            BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                        binding.imageViewPostingShow.setImageBitmap(bmp)
                    }else{
                        println("undefined err")
                    }
                }
                println("undefined err")
            }
        }
    }
    private fun initPostingUI(writer_uid:String,upload_time:Long,title:String,text:String){
        val writer_uid = writer_uid
        userCollection.document(writer_uid as String).get().addOnSuccessListener {
            binding.nicknamePostingShow.text = "${it.data?.get("nickname")}"
            var ref = rootRef.child("user_profile_image/${it.data?.get("uid")}.jpg")
            ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful){
                    val bmp =
                        BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.profileImgPostingShow.setImageBitmap(bmp)
                }else{
                    var ref = rootRef.child("user_profile_image/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp =
                                BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                            binding.profileImgPostingShow.setImageBitmap(bmp)
                        }else{
                            println("undefined err")
                        }
                    }
                    println("undefined err")
                }
            }
        }

        binding.timePostingShow.text = "${SimpleDateFormat("yyyy-MM-dd").format(upload_time)}"
        binding.titlePostingShow.text = "${title}"

        binding.textPostingShow.text = "${text}"

        var ref = rootRef.child("posting_image/${document_id}.jpg")
        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp =
                    BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                binding.imageViewPostingShow.setImageBitmap(bmp)
            }else{
                var ref = rootRef.child("posting_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp =
                            BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                        binding.imageViewPostingShow.setImageBitmap(bmp)
                    }else{
                        println("undefined err")
                    }
                }
                println("undefined err")
            }
        }
    }
    private fun getCommentsToRecyclerView(comment_id_list:Any){
        val commentIdListListString :List<String> = comment_id_list as List<String>
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
                }

                userCollection.document(writer_uid.trim()).addSnapshotListener { snapshot, error ->

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
                viewModel.addItem(Comment(bmp,nickname,comment_text,upload_time,writer_uid,commemt_id))
                viewModel.items.sortBy{
                    it.timePosting
                }
                viewModel.itemsListData.value = viewModel.items
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
                        viewModel.addItem(Comment(bmp,nickname,comment_text,upload_time,writer_uid,commemt_id))
                        viewModel.items.sortBy{
                            it.timePosting
                        }
                        viewModel.itemsListData.value = viewModel.items
                    }else{
                        println("undefined err")
                    }
                }
            }
        }
    }
    fun updateUserToRecyclerview(i:Int,writer_uid:String,nickname:String, comment:Comment){
        var userProfileImage = rootRef.child("user_profile_image/${writer_uid}.jpg")
        userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if(it.isSuccessful){
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                viewModel.updateItem(i, Comment(bmp,nickname,comment.commentText,comment.timePosting
                    ,comment.writer_uid,comment.document_id))
            }else{
                var ref = rootRef.child("user_profile_image/default.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        viewModel.updateItem(i, Comment(bmp,nickname,comment.commentText,comment.timePosting
                            ,comment.writer_uid,comment.document_id))
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
         * @return A new instance of fragment ShowPostingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowPostingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}