package com.example.capstone_android

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.FragmentMyApplyBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyApplyCompetitionFragment : Fragment(){

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : MyApplyCompetitionViewModel by viewModels<MyApplyCompetitionViewModel>()
    private var _binding: FragmentMyApplyBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //getter 함수 null이 아닌 ui 객체 리턴
    val db = Firebase.firestore
    var isInit =false
    var isInitAboutMember =false
    var myNickName ="star1"//닉네임은 일단 알고 있다고 가정함 login Activity에서 넘겨 받아야 함
    var cnt =0

    private var currentUserUid = Firebase.auth.uid
    private var rootRef = Firebase.storage.reference

    private var userCollection = db.collection("user")
    private var competitionCollection = db.collection("competition_room")

    private var document_id : String = ""

    var memCurruntCnt =0
    var memCntOfFirebase =0
    class DataForapplyUI(
        val max: Any?, val title: Any?, val upload_time: Any?,
        val category: Any?
    )

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
        _binding = FragmentMyApplyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //비동기 처리시 기다리게 하는 등의 방법을 사용해서 게시판은 업로드한 순으로 보여준다.

        val adapter = MyApplyCompetitionAdapter(viewModel)

        val RecyclerView = binding.applyRecyclerView
        RecyclerView.adapter = adapter
        RecyclerView.layoutManager = LinearLayoutManager(activity)
        RecyclerView.setHasFixedSize(true)
        viewModel.itemsListData.observe(viewLifecycleOwner ){
            adapter.notifyDataSetChanged()
        }

        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            //ItemDialog(it).show
            //val i =viewModel.itemClickEvent.value
            //val bundle = bundleOf("document_id" to viewModel.items[i!!].document_id)
            //findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_showPostingFragment,bundle)

            val i =viewModel.itemClickEvent.value
            println("---------------${viewModel.items[i!!].title}")

            //테스트라서 이렇게 했지만 나중에는 동경님 activity로 옮겨야 함
            val intent = Intent(requireActivity(), ChangeDataTestActivity::class.java)

            intent.putExtra("collectionName", "competition_room")
            intent.putExtra("competition_room_id",viewModel.items[i!!].document_id)
            startActivity(intent)
        }

        registerForContextMenu(RecyclerView)

        val colName = activity?.intent?.getStringExtra("collectionName")
        //val colName = activity?.intent?.getStringExtra("competition_room")
        competitionCollection = db.collection(colName!!)
        //데이터 얻어와서 ui에 반영
        initDataAndUI()
    }

    private fun initDataAndUI(){
        //document_id = activity?.intent?.getStringExtra("document_id").toString()

        currentUserUid?.let {
            userCollection.document(it).get().addOnSuccessListener {
                if(it["competition_id_list"] ==null){
                    return@addOnSuccessListener
                }
                val competition_id_list = it["competition_id_list"] as List<String>

                for (competition_id in competition_id_list){
                    competitionCollection.document(competition_id).get().addOnSuccessListener {
                        val title =it["title"]
                        val info_text = it["info_text"]
                        val upload_time = it["upload_time"]
                        //val writer_uid = it["writer_uid"]
                        val document_id = it.id

                        addPostingToRecyclerView(title as String, info_text as String, upload_time as Long,document_id)
                    }.addOnFailureListener {
                        println("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
                        //updateInitCnt(false)
                    }
                }

            }
        }


    }

    fun addPostingToRecyclerView(title:String, info_text: String, timePosting:Long,document_id:String){

        currentUserUid?.let {
            userCollection.document(it).get().addOnSuccessListener {
                //val nickname = it.data?.get("nickname")
                var competitionImage = rootRef.child("competition_room/${document_id}.jpg")
                competitionImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        for(posting in viewModel.items){//중복 검사
                            if (posting.document_id == document_id){
                                return@addOnCompleteListener
                            }
                        }
                        viewModel.addItem(Apply(bmp,
                            title, info_text, timePosting,document_id))
                        //updateInitCnt(true)
                        //addUserSnapShot(writer_uid)
                    }else{
                        var ref = rootRef.child("competition_room/default.jpg")
                        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                            if(it.isSuccessful){
                                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                                for(Apply in viewModel.items){
                                    if (Apply.document_id == document_id){
                                        return@addOnCompleteListener
                                    }
                                }
                                viewModel.addItem(Apply(bmp,
                                   title, info_text, timePosting,document_id))
                                //updateInitCnt(true)
                                //addUserSnapShot(writer_uid)
                            }else{
                                println("undefined err")
                            }
                        }
                    }
                }

            }.addOnFailureListener {
                print("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
                //updateInitCnt(false)
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