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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.capstone_android.data.*
import com.example.capstone_android.databinding.FragmentEditMeetingInfoBinding
import com.example.capstone_android.databinding.FragmentEditPostingBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditMeetingInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditMeetingInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentEditMeetingInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var document_id =""
    val db = Firebase.firestore
    var rootRef = Firebase.storage.reference
    val postingCollection = db.collection("posting")
    val userCollection = db.collection("user")
    val commentCollection = db.collection("comment")
    var meetingRoomCollection = db.collection("lighting_meeting_room")
    var title =""
    var text =""
    var max =""
    var userInputImgUri : Uri? = null
    var time : Long =0
    var commentsListString :Any? = null
    var writer_uid :Any? =null
    var category :Any? =null
    var chatting_id_list :Any? =null
    var member_list :Any? =null
    var posting_id_list :Any? =null
    val categoryItems =
        arrayListOf<String>(
            "운동", "여행", "음악", "사교/직업", "독서",
            "요리", "사진", "게임", "댄스", "차/오토바이",
            "반려동물", "공예", "봉사활동", "공부/자기개발"
        )
    var address: Any? = null

    var positionx = 0.2
    var positiony = 0.1
    var startTime :Long= 0
    var endTime :Long= 0
    var reservation_uid_list:Any? =null
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
        _binding = FragmentEditMeetingInfoBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        document_id = arguments?.getString("document_id").toString() //posting id 얻어옴
        //(activity as MeetingRoomActivity).setMeetingRoomId(document_id)
        val colName = activity?.intent?.getStringExtra("collectionName")
        meetingRoomCollection = db.collection(colName!!)
        initDataAndUI()
    }
    private fun initDataAndUI(){

        val customAdaptper = activity?.let { ArrayAdapter<String>(it.applicationContext,android.R.layout.simple_spinner_dropdown_item, categoryItems) }
        val spinnerMeetingRoomCategory = binding.spinnerMeetingRoomCategory
        spinnerMeetingRoomCategory.adapter = customAdaptper
        spinnerMeetingRoomCategory.setSelection(0)
        spinnerMeetingRoomCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                category = categoryItems[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        meetingRoomCollection.document(document_id).get().addOnSuccessListener {
            binding.postingTitleEditTextMeetingInfoEdit.setText("${it["title"]}")
            binding.postingTextMultilineMeetingInfoEdit.setText("${it["info_text"]}")
            binding.maxMeetingInfoEdit.setText("${it["max"]}")
            category = "${it["category"]}"
            writer_uid = "${it["writer_uid"]}"
            chatting_id_list = it["chatting_id_list"]

            posting_id_list = it["posting_id_list"]
            address = "${it["address"]}"

            //commentsListString = it["comment_id_list"]
            //writer_uid = it["writer_uid"] //주석 했지만 나중에는 사용할 수도 있음
            val collectionName = activity?.intent?.getStringExtra("collectionName").toString()
            if(collectionName == MeetingRoomDataManager.collectionNameOfLightingMeetingRoom){
                positionx = it["positionx"] as Double
                positiony = it["positiony"] as Double
                positionx = 100.0
                positiony =100.5
                member_list = it["member_list"] as ArrayList<String>

            }else if(collectionName == MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom){
                positionx = it["positionx"] as Double
                positiony = it["positiony"] as Double
                startTime = it["start_time"] as Long
                endTime = it["end_time"] as Long
                member_list = it["member_list"] as ArrayList<String>
            }else if(collectionName == MeetingRoomDataManager.collectionNameOfPlaceRental){
                positionx = it["positionx"] as Double
                positiony = it["positiony"] as Double
                reservation_uid_list = it["reservation_uid_list"]
            }else if(collectionName == MeetingRoomDataManager.collectionNameOfCompetition){
                positionx = it["positionx"] as Double
                positiony = it["positiony"] as Double

                member_list = it["member_list"] as ArrayList<String>
            }else{
                println("정의 하지 않은 컬랙션 이름입니다.")
                return@addOnSuccessListener
            }

            var ref = rootRef.child("meeting_info/${document_id}.jpg")
            ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful){
                    val bmp =
                        BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                    binding.imageButtonToPostingMeetingInfoEdit.setImageBitmap(bmp)
                }else{
                    var ref = rootRef.child("meeting_info/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp =
                                BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                            binding.imageButtonToPostingMeetingInfoEdit.setImageBitmap(bmp)
                        }else{
                            println("undefined err")
                        }
                    }
                    println("undefined err")
                }
            }
        }

        binding.uploadPostingBtnMeetingInfoEdit.setOnClickListener {

            title = binding.postingTitleEditTextMeetingInfoEdit.text.toString()
            text = binding.postingTextMultilineMeetingInfoEdit.text.toString()
            max = binding.maxMeetingInfoEdit.text.toString()
            //max가 숫자인지 검사 필요 숫자 아니면 토스트 띄우고 즉시 리턴
            if(!Pattern.matches("^[0-9]*$",max)){
                Toast.makeText(activity,"최대 인원은 숫자만 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            time = System.currentTimeMillis()

            if(userInputImgUri != null){
                val uploadImageRef = rootRef.child("meeting_info/${document_id}.jpg")
                //동일한 사람이 동시에 2번 업로드 할수는 없다는 가정하에 코딩함
                uploadImageRef?.putFile(userInputImgUri!!)?.addOnSuccessListener{
                    upload()
                }
            }else{
                upload()
            }

        }
        binding.imageButtonToPostingMeetingInfoEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            startActivityForResult(intent, 1)
        }
    }
    private fun upload(){
        val docData = hashMapOf(
            "category" to category,
            "chatting_id_list" to chatting_id_list,
            "info_text" to text,
            "max" to max,
            "member_list" to member_list,
            "posting_id_list" to posting_id_list,
            "title" to title,
            "upload_time" to time,
            "writer_uid" to writer_uid
        )

        val collectionName = activity?.intent?.getStringExtra("collectionName").toString()
        var meetingRoomData = MeetingRoomData()
        if(collectionName == MeetingRoomDataManager.collectionNameOfLightingMeetingRoom){
            meetingRoomData = LightingMeetingRoomData()
            meetingRoomData = meetingRoomData as LightingMeetingRoomData
            meetingRoomData.positionx = positionx
            meetingRoomData.positiony =positiony
            meetingRoomData.member_list = member_list as ArrayList<String>

        }else if(collectionName == MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom){
            meetingRoomData = PeriodicMeetingRoomData()
            meetingRoomData = meetingRoomData as PeriodicMeetingRoomData
            meetingRoomData.positionx = positionx
            meetingRoomData.positiony = positiony
            meetingRoomData.member_list = member_list as ArrayList<String>
            meetingRoomData.start_time=0
            meetingRoomData.end_time =0
        }else if(collectionName == MeetingRoomDataManager.collectionNameOfPlaceRental){
            meetingRoomData = PlaceRentalRoom()
            meetingRoomData = meetingRoomData as PlaceRentalRoom
            meetingRoomData.positionx = positionx
            meetingRoomData.positiony = positiony
            meetingRoomData.reservation_uid_list = reservation_uid_list as ArrayList<String>
        }else if(collectionName == MeetingRoomDataManager.collectionNameOfCompetition){
            meetingRoomData = CompetitionRoomData()
            meetingRoomData = meetingRoomData as CompetitionRoomData
            meetingRoomData.positionx = positionx
            meetingRoomData.positiony = positiony
            meetingRoomData.member_list = member_list as ArrayList<String>
        }else{
            println("정의 하지 않은 컬랙션 이름입니다.")
            return
        }
        meetingRoomData.category = category as String
        meetingRoomData.chatting_id_list = chatting_id_list as ArrayList<String>
        meetingRoomData.info_text = text as String
        meetingRoomData.max = max
        meetingRoomData.posting_id_list = posting_id_list as ArrayList<String>
        meetingRoomData.title = title
        meetingRoomData.upload_time = time
        meetingRoomData.writer_uid = writer_uid as String
        meetingRoomData.address = address as String


        meetingRoomCollection.document("${document_id}").set(meetingRoomData).addOnSuccessListener {
            //meetingroom에 배열에도 반영
            //val bundle = bundleOf("document_id" to document_id)
            //findNavController().navigate(R.id.action_editMeetingInfoFragment_to_meetingRoomInfoFragment ,bundle)
            findNavController().navigate(R.id.action_editMeetingInfoFragment_to_meetingRoomInfoFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == 1){
                userInputImgUri = data?.data
                try{
                    var bitmapUserSelect = MediaStore.Images.Media.getBitmap(activity?.contentResolver,userInputImgUri)
                    binding.imageButtonToPostingMeetingInfoEdit.setImageBitmap(bitmapUserSelect)

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
         * @return A new instance of fragment EditMeetingInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditMeetingInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
