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
import com.example.capstone_android.databinding.FragmentMeetingRoomPostingsBinding
import com.example.capstone_android.databinding.FragmentShowPostingBinding
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
            findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_showPostingFragment)
        }

        registerForContextMenu(recyclerViewCommentPosting)
        viewModel.addItem(Comment(null,"asdf","asddfgdf",1000,"","adsf"))
        viewModel.addItem(Comment(null,"asdf","fdsgfh",100002,"","asdf"))
        viewModel.addItem(Comment(null,"asdf","asddfgdf",1000,"","adsf"))
        viewModel.addItem(Comment(null,"asdf","fdsgfh",100002,"","asdf"))

        //데이터 얻어와서 ui에 반영
        initDataAndUI()

    }
    private fun initDataAndUI(){
        document_id = arguments?.getString("document_id").toString()
        println("document_id ----------- ${document_id}")
        postingCollection.document(document_id).get().addOnSuccessListener {
            val writer_uid = it.data?.get("writer_uid").toString()
            val upload_time = it.data?.get("upload_time")
            val title = it.data?.get("title").toString()
            val text = it.data?.get("text").toString()
            addWriterSnapShot(writer_uid)
            initPostingUI(writer_uid, upload_time as Long,title,text)
        }
        postingCollection.document(document_id).addSnapshotListener { value, error ->
            val writer_uid = value?.data?.get("writer_uid").toString()
            val upload_time = value?.data?.get("upload_time")
            val title = value?.data?.get("title").toString()
            val text = value?.data?.get("text").toString()
            updatePostingUI(writer_uid, upload_time as Long,title,text)
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