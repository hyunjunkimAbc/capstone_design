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
    var meetingRoomCollection = db.collection("lighting_meeting_room")
    val postingCollection = db.collection("posting")
    val userCollection = db.collection("user")
    var meetingRoomId = ""
    var memCurruntCnt =0
    var memCntOfFirebase =0
    class DataForPostingUI(val infoText:Any?,val max:Any?,val memberList:Any?,
                           val title:Any?,val upload_time:Any?,val category:Any?)
    var numOfChatting =-1
    var initChatCnt =0

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
        val colName = activity?.intent?.getStringExtra("collectionName")
        meetingRoomCollection = db.collection(colName!!)
        //데이터 얻어와서 ui에 반영
        initDataAndUI()
    }
    private fun initDataAndUI(){
        meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id").toString()
        binding.floatingActionButtonAddPosting.setOnClickListener {
            val bundle = bundleOf("meeting_room_document_id" to meetingRoomId)
            findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_meetingRoomPostingAddFragment,bundle)
        }
        meetingRoomCollection.document(meetingRoomId).get().addOnSuccessListener {
            if(it["posting_id_list"] ==null){
                return@addOnSuccessListener
            }
            val posting_id_list = it["posting_id_list"] as List<String>
            numOfChatting = posting_id_list.size
            for (posting_id in posting_id_list){
                postingCollection.document(posting_id).get().addOnSuccessListener {
                    val title = it["title"]
                    val text =it["text"]
                    val upload_time = it["upload_time"]
                    val writer_uid = it["writer_uid"]
                    val document_id = it.id

                    addPostingToRecyclerView(writer_uid as String, text as String, upload_time as Long,document_id,title as String)
                }.addOnFailureListener {
                    println("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
                    updateInitCnt(false)
                }
            }
            meetingRoomCollection.document(meetingRoomId).addSnapshotListener { value, error ->
                if(numOfChatting ==-1){
                    println("아직 초기화 안됨 meetingRoomCollection addSnapshotListener -1 pf")
                    return@addSnapshotListener
                }
                if (initChatCnt < numOfChatting){
                    println("아직 초기화 안됨 meetingRoomCollection addSnapshotListener < pf")
                    return@addSnapshotListener
                }
                //댓글 추가 혹은 삭제 되면 업데이트 수정기능은 아직 보류한다는 가정하에 추가 기능만 작동
                // 만약 삭제 기능 추가 되면 코드도 바뀌어야 함
                val posting_id_list = value?.data?.get("posting_id_list") as List<String>

                if(viewModel.items.size == posting_id_list.size){//개수 변화 없으면 아무것도 하지 않음
                    println("탈출----")
                }else if(viewModel.items.size <posting_id_list.size){//맴버가 추가 되면(개수 증가) 새로 받아오기
                    //맨뒤에 있는 것이 새로운 맴버일때 정상 동작함
                    //numOfChatting++
                    updateNumOfChatting(true)
                    addPosting(posting_id_list[posting_id_list.size-1])
                }else if(viewModel.items.size > posting_id_list.size){//맴버가 사라지면 그 맴버는 리사이클러에서 지우기
                    println("삭제 ------")
                    var isInFirebase =false
                    for(posting in viewModel.items){
                        isInFirebase =false
                        for (postingID in posting_id_list){
                            if(posting.document_id == postingID){
                                isInFirebase = true
                            }
                        }
                        if(isInFirebase){
                            continue
                        }else{//firebase에는 없는데 viewmodel에는 맴버가 있는 상황 그 맴버는 지워주면 된다
                            viewModel.deleteItem(posting)
                            updateNumOfChatting(false)
                            // init카운트도 업데이트 해야 하나?
                            updateInitCnt(false,false)
                            break
                        }
                    }
                }
            }
        }

    }
    private fun addPosting(posting_id: String){
        postingCollection.document(posting_id.trim()).get().addOnSuccessListener {
            var writer_uid = ""
            var upload_time :Long
            var text = ""
            var document_id = ""
            var title =""
            title = "${it["title"]}"
            writer_uid = "${it["writer_uid"]}"
            upload_time = it["upload_time"] as Long
            text = "${it["text"]}"
            document_id = it.id
            userCollection.document(writer_uid).get().addOnSuccessListener {
                addPostingToRecyclerView(writer_uid as String, text as String, upload_time as Long,document_id,title)
            }
        }
    }

    fun addPostingToRecyclerView(writer_uid:String, postingText:String,timePosting:Long,document_id:String,title:String){

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
                        nickname as String, postingText, timePosting,document_id,writer_uid,title))
                    updateInitCnt(true)
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
                                nickname as String, postingText, timePosting,document_id,writer_uid,title))
                            updateInitCnt(true)
                            addUserSnapShot(writer_uid)
                        }else{
                            println("undefined err")
                        }
                    }
                }
            }

        }.addOnFailureListener {
            print("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
            updateInitCnt(false)
        }

    }
    fun addUserSnapShot(writer_uid: String){
        userCollection.document(writer_uid).addSnapshotListener { value, error ->
            if(initChatCnt < numOfChatting){
                println("아직 초기화 안됨 userCollection addSnapshotListener m r p")
                return@addSnapshotListener
            }
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
                                    nickname as String,posting.postingText,posting.timePosting,posting.document_id,posting.writer_uid,posting.title)
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
                                            nickname as String,posting.postingText,posting.timePosting,posting.document_id,posting.writer_uid,posting.title)
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
    @Synchronized
    fun updateInitCnt(isSuccess :Boolean,isSort :Boolean = true){//임계 영역
        if(isSuccess){
            if(isSort){
                if (initChatCnt+1 >= numOfChatting){// 마지막 것을 받아왔을때 정렬한다.

                    viewModel.items.sortByDescending{
                        it.timePosting
                    }
                    viewModel.itemsListData.value = viewModel.items

                }
            }
            initChatCnt++ //비동기로 추가 될때 마다 업데이트
        }else{
            if(isSort){
                if (initChatCnt-1 >= numOfChatting){// 마지막 것을 받아왔을때 정렬한다.
                    viewModel.items.sortByDescending{
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