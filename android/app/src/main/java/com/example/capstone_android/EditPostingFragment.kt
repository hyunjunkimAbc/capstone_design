package com.example.capstone_android

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.capstone_android.databinding.FragmentEditPostingBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditPostingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPostingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentEditPostingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var document_id =""
    val db = Firebase.firestore
    var rootRef = Firebase.storage.reference
    val postingCollection = db.collection("posting")
    val userCollection = db.collection("user")
    val commentCollection = db.collection("comment")
    val meetingRoomCollection = db.collection("meeting_room")
    var title =""
    var text =""
    var userInputImgUri : Uri? = null
    var time : Long =0
    var commentsListString :Any? = null
    var writer_uid :Any? =null

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
        _binding = FragmentEditPostingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        document_id = arguments?.getString("document_id").toString() //posting id 얻어옴
        (activity as MeetingRoomActivity).setPostingDocumentId(document_id)
        initDataAndUI()
    }
    private fun initDataAndUI(){
        postingCollection.document(document_id).get().addOnSuccessListener {
            binding.postingTitleEditTextPostingEdit.setText("${it["title"]}")
            binding.postingTextMultilinePostingEdit.setText("${it["text"]}")
            commentsListString = it["comment_id_list"]
            writer_uid = it["writer_uid"]

            var ref = rootRef.child("posting_image/${document_id}.jpg")
            ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful){
                    val bmp =
                        BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.imageButtonToPostingPostingEdit.setImageBitmap(bmp)
                }else{
                    var ref = rootRef.child("posting_image/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp =
                                BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                            binding.imageButtonToPostingPostingEdit.setImageBitmap(bmp)
                        }else{
                            println("undefined err")
                        }
                    }
                    println("undefined err")
                }
            }
        }

        binding.uploadPostingBtnPostingEdit.setOnClickListener {

            title = binding.postingTitleEditTextPostingEdit.text.toString()
            text = binding.postingTextMultilinePostingEdit.text.toString()
            time = System.currentTimeMillis()

            if(userInputImgUri != null){
                val uploadImageRef = rootRef.child("posting_image/${document_id}.jpg")
                //동일한 사람이 동시에 2번 업로드 할수는 없다는 가정하에 코딩함
                uploadImageRef?.putFile(userInputImgUri!!)?.addOnSuccessListener{
                    upload()
                }
            }else{
                upload()
            }

        }
        binding.imageButtonToPostingPostingEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            startActivityForResult(intent, 1)
        }
    }
    private fun upload(){
        val docData = hashMapOf(
            "comment_id_list" to commentsListString,
            "text" to text,
            "title" to title,
            "upload_time" to time,
            "writer_uid" to writer_uid
        )
        postingCollection.document("${document_id}").set(docData).addOnSuccessListener {
            //meetingroom에 배열에도 반영
            val bundle = bundleOf("document_id" to document_id)
            findNavController().navigate(R.id.action_editPostingFragment_to_showPostingFragment,bundle)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == 1){
                userInputImgUri = data?.data
                try{
                    var bitmapUserSelect = MediaStore.Images.Media.getBitmap(activity?.contentResolver,userInputImgUri)
                    binding.imageButtonToPostingPostingEdit.setImageBitmap(bitmapUserSelect)

                } catch (e:Exception){
                    e.printStackTrace()
                }
            }else{
                println("undefined")
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
         * @return A new instance of fragment EditPostingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditPostingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}