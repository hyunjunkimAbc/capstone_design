package com.example.capstone_android

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.SearchAddress.SearchMap
import com.example.capstone_android.data.*
import com.example.capstone_android.databinding.FragmentEditMeetingInfoBinding
import com.example.capstone_android.databinding.FragmentEditPostingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
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
    var category =""
    var chatting_id_list :Any? =null
    var member_list :Any? =null
    var posting_id_list :Any? =null
    val categoryItems =
        arrayListOf<String>(
            "농구", "축구", "탁구", "테니스", "배드민턴",
            "야구", "볼링", "자전거", "골프", "런닝",
            "수영", "배구", "요가|필라테스", "태권도|유도", "복싱",
            "무술", "승마", "헬스", "롤러|보드", "스키|보드", "당구","등산","수상레저",
            "세계여행","국내여행","밴드","피아노","드럼","바이올린","기타","노래","작곡","힙합"
            ,"버스킹","콘서트","디제잉","런치패드","색소폰","친구","카페","술 한잔","코노","맛집탐방","봉사활동"
            ,"독서","글쓰기","토론","한식","일식","중식","양식","제과제빵","칵테일","와인","사진","영상제작"
            ,"AOS","RPG","FPS","카드게임","두뇌심리","스포츠게임","레이싱게임","닌텐도|플스","팝핀","비보잉","락킹"
            ,"왁킹","힙합댄스","하우스","크럼프","현대무용","한국무용","K-POP","발레","댄스스포츠","발리댄스","재즈","에어로빅"
            ,"자동차","오토바이","강아지","고양이","고슴도치","햄스터","물고기","앵무새","다람쥐","도마뱀","뱀","거미"
            ,"미술","공방","도예","자수","꽃","화장품","가구","스터디","언어","동기부여","스피치"
            ,"IT","디자인","의료","화학","금융","건설","법","패션"
        )
    var address: Any? = null

    var positionx = 0.2
    var positiony = 0.1
    var startTime :String= ""
    var endTime :String= ""
    var startTimeStrGlobal =""
    var endTimeStrGlobal =""
    var startTimeCompetition :String= ""
    var endTimeCompetition :String= ""
    var reservation_uid_list:Any? =null
    var meetingRoomGenerator: MeetingRoomViewGenerator? =null
    var meetingRoomFactory : AbstractMeetingRoomFactory? = null
    var date:String =""

    var detailad =""
    var addressname =""
    var addresscheck = false
    var placeName =""
    var detailaddress = "서울 서대문구 가좌로 85"
    var location =""
    var imageUrl =""
    var Uid = ""

    inner abstract class AbstractMeetingRoomFactory{
        abstract fun createMeetingRoomViewGenerator():MeetingRoomViewGenerator
    }
    inner class LightningMeetingRoomFactory :AbstractMeetingRoomFactory(){
        override fun createMeetingRoomViewGenerator(): MeetingRoomViewGenerator {
            return LightingMeetingRoomGenerator()
        }
    }
    inner class PeriodicMeetingRoomFactory :AbstractMeetingRoomFactory(){
        override fun createMeetingRoomViewGenerator(): MeetingRoomViewGenerator {
            return PeriodicMeetingRoomGenerator()
        }
    }
    inner class PlaceRentalRoomFactory :AbstractMeetingRoomFactory(){
        override fun createMeetingRoomViewGenerator(): MeetingRoomViewGenerator {
            return PlaceRentalRoomGenerator()
        }
    }
    inner class CompetitionRoomFactory : AbstractMeetingRoomFactory() {
        override fun createMeetingRoomViewGenerator(): MeetingRoomViewGenerator {
            return CompetitionRoomGenerator()
        }
    }
    inner class MeetingRoomController{
        fun addEnterMeetingListener(){

        }
        fun setRecyclerView(){
        }
        fun addAdditionerViewAndAssignData(max:String){
            //binding.numOfPeople.text = "${memCntOfFirebase}(현재 인원) / ${max}(최대 인원)"
            binding.maxMeetingInfoEdit.setText(max)
        }
        fun removeMax(){
            binding.editMeetingRoomLinearAdditional.removeView(binding.linearMaxHoriz)
        }
        fun removeStartEndTime(){
            binding.editMeetingRoomLinearAdditional.removeView(binding.editTextStartTime)
            binding.editMeetingRoomLinearAdditional.removeView(binding.editTextTextEndTime)

        }
    }
    inner abstract class MeetingRoomViewGenerator{
        abstract fun getViewInflated()
        abstract fun addAdditionerViewAndAssignData(it: DocumentSnapshot)
        abstract fun getViewByEachMeetingRoom():MeetingRoomData
        abstract fun processEachMeetingRoom():Boolean
        open fun initDataAndUI() {
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
                //binding.maxMeetingInfoEdit.setText("${it["max"]}")
                category = "${it["category"]}"
                writer_uid = "${it["writer_uid"]}"
                chatting_id_list = it["chatting_id_list"]

                posting_id_list = it["posting_id_list"]
                address = "${it["address"]}"
                positionx = it["positionx"] as Double
                positiony = it["positiony"] as Double

                category = it["category"] as String
                var index = categoryItems.indexOf(category)
                spinnerMeetingRoomCategory.setSelection(index)

                binding.button10.setText("${address} (변경하려면 클릭하세요)")
                //commentsListString = it["comment_id_list"]
                //writer_uid = it["writer_uid"] //주석 했지만 나중에는 사용할 수도 있음
                //변경 테스트 하고 싶으면 if문 조건절에서 positionx 등에 변화를 주면 됨
                addAdditionerViewAndAssignData(it)
                val colName = activity?.intent?.getStringExtra("collectionName")

                var ref = rootRef.child("${colName}/${document_id}.jpg")
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp =
                            BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                        binding.imageButtonToPostingMeetingInfoEdit.setImageBitmap(bmp)
                    }else{
                        var ref = rootRef.child("${colName}/default.jpg")
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
                //max = binding.maxMeetingInfoEdit.text.toString()
                //max가 숫자인지 검사 필요 숫자 아니면 토스트 띄우고 즉시 리턴
                if(!processEachMeetingRoom()){
                    return@setOnClickListener
                }
                if (!addresscheck) {
                    Toast.makeText(activity, "장소를 정해주세요", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                time = System.currentTimeMillis()

                val colName = activity?.intent?.getStringExtra("collectionName")

                if(userInputImgUri != null){
                    val uploadImageRef = rootRef.child("${colName}/${document_id}.jpg")
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
            binding.button10.setOnClickListener {
                val intent = Intent(activity, SearchMap::class.java)
                intent.putExtra("create","hello")
                startActivityForResult(intent,9)
            }
        }
        private fun upload(){
            val collectionName = activity?.intent?.getStringExtra("collectionName").toString()
            var meetingRoomData = getViewByEachMeetingRoom()

            meetingRoomData.category = category as String
            meetingRoomData.chatting_id_list = chatting_id_list as ArrayList<String>
            meetingRoomData.info_text = text as String

            meetingRoomData.posting_id_list = posting_id_list as ArrayList<String>
            meetingRoomData.title = title
            meetingRoomData.upload_time = time
            meetingRoomData.writer_uid = writer_uid as String
            meetingRoomData.address = address as String
            meetingRoomData.positionx = positionx
            meetingRoomData.positiony = positiony

            meetingRoomCollection.document("${document_id}").set(meetingRoomData, SetOptions.merge()).addOnSuccessListener {
                //meetingroom에 배열에도 반영
                //val bundle = bundleOf("document_id" to document_id)
                //findNavController().navigate(R.id.action_editMeetingInfoFragment_to_meetingRoomInfoFragment ,bundle)

                findNavController().navigate(R.id.action_editMeetingInfoFragment_to_meetingRoomInfoFragment)
            }
        }

    }
    inner class LightingMeetingRoomGenerator: MeetingRoomViewGenerator() {
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }

        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot) {
            /*
            positionx = it["positionx"] as Double
            positiony = it["positiony"] as Double
            member_list = it["member_list"] as ArrayList<String> //view
             */
            if(it["max"]!=null){
                max = it["max"] as String
                binding.maxMeetingInfoEdit.setText(max)
            }
            member_list = it["member_list"] as ArrayList<String>

            if(it["start_time"] !=null && it["end_time"] !=null){
                val startTime2 = it["start_time"] as String //view
                val endTime2 = it["end_time"] as String //view
                //val startTimeStr ="${SimpleDateFormat("yyyy-MM-dd").format(startTime2)}"
                //val endTimeStr = "${SimpleDateFormat("yyyy-MM-dd").format(endTime2)}"
                startTime = startTime2
                endTime = endTime2
                date = it["date"] as String

                binding.editTextStartTime.hint = "(모임 시작 시간)${date} ${startTime} 형식으로 입력"
                binding.editTextTextEndTime.hint = "(모임 종료 시간)${date} ${endTime} 형식으로 입력"
                //datepicker로 변경 해야 함
            }
            if (it["max"] !=null){
                val max =  it["max"] as String
                MeetingRoomController().addAdditionerViewAndAssignData(max)
            }
        }

        override fun getViewByEachMeetingRoom(): MeetingRoomData {

            var meetingRoomData = LightingMeetingRoomData()
            meetingRoomData.addressname = addressname
            meetingRoomData.member_list = member_list as ArrayList<String>
            meetingRoomData.start_time = startTime
            meetingRoomData.end_time = endTime
            meetingRoomData.max = max
            meetingRoomData.date = date
            return meetingRoomData
        }

        override fun processEachMeetingRoom():Boolean {
            max = binding.maxMeetingInfoEdit.getText().toString()
            if(!Pattern.matches("^[0-9]*$",max)){
                Toast.makeText(activity,"최대 인원은 숫자만 가능합니다.", Toast.LENGTH_SHORT).show()
                return false
            }
            if(!Pattern.matches("^\\d{8} \\d{2}:\\d{2}$",binding.editTextStartTime.getText())){
                binding.editTextStartTime.setText(startTimeStrGlobal)
                Toast.makeText(activity,"(모임 시작 시간)${date} 08:30 형식으로 입력", Toast.LENGTH_SHORT).show()
                return false
            }else{
                val cal = Calendar.getInstance()
                val str = binding.editTextStartTime.getText()
                val split = str.split(' ')
                //cal.set(split[0].toInt(),split[1].toInt()-1,split[2].toInt())
                startTime = split[1]

            }
            if(!Pattern.matches("^\\d{8} \\d{2}:\\d{2}\$",binding.editTextTextEndTime.getText())){
                binding.editTextTextEndTime.setText(endTimeStrGlobal)
                Toast.makeText(activity,"(모임 시작 시간)${date} 09:30 형식으로 입력", Toast.LENGTH_SHORT).show()
                return false
            }else{
                val cal = Calendar.getInstance()
                val str = binding.editTextTextEndTime.getText()
                val split = str.split(' ')
                //cal.set(split[0].toInt(),split[1].toInt()-1,split[2].toInt())
                endTime =split[1]
                date =split[0]
            }
            return true
        }


    }
    inner class PeriodicMeetingRoomGenerator:MeetingRoomViewGenerator(){
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }


        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot) {
            MeetingRoomController().removeStartEndTime()
            //개선 요망

            if(it["max"]!=null){
                max = it["max"] as String
                binding.maxMeetingInfoEdit.setText(max)
            }
            member_list = it["member_list"] as ArrayList<String>
            if (it["member_list"] !=null && it["max"] !=null){
                val max =  it["max"] as String
                MeetingRoomController().addAdditionerViewAndAssignData(max)
            }
        }

        override fun getViewByEachMeetingRoom(): MeetingRoomData {
            var meetingRoomData = PeriodicMeetingRoomData()

            meetingRoomData.member_list = member_list as ArrayList<String>
            meetingRoomData.max = max
            return meetingRoomData
        }

        override fun processEachMeetingRoom() :Boolean{
            max = binding.maxMeetingInfoEdit.getText().toString()
            if(!Pattern.matches("^[0-9]*$",max)){
                Toast.makeText(activity,"최대 인원은 숫자만 가능합니다.", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }


    }
    inner class PlaceRentalRoomGenerator:MeetingRoomViewGenerator(){
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }

        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot){
            MeetingRoomController().removeMax()
            MeetingRoomController().removeStartEndTime()
            reservation_uid_list = it["reservation_uid_list"] as ArrayList<String>
            max = it["max"] as String
            imageUrl = it["imageUrl"] as String
            Uid = it["uid"] as String
        }

        override fun getViewByEachMeetingRoom(): MeetingRoomData {
            var meetingRoomData = PlaceRentalRoom()
            meetingRoomData.max =max
            meetingRoomData.addressdetail =detailaddress
            meetingRoomData.imageUrl = imageUrl
            meetingRoomData.Uid = Uid
            meetingRoomData.reservation_uid_list = reservation_uid_list as ArrayList<String>
            return meetingRoomData
        }

        override fun processEachMeetingRoom():Boolean {
            return true
        }


    }
    inner class CompetitionRoomGenerator:MeetingRoomViewGenerator(){
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }

        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot) {

            member_list = it["member_list"] as ArrayList<String>
            if(it["start_time"] !=null && it["end_time"] !=null){
                val startTime2 = it["start_time"] as String //view
                val endTime2 = it["end_time"] as String //view

                startTimeCompetition = startTime2
                endTimeCompetition = endTime2
                binding.editTextStartTime.setText(startTime2)
                binding.editTextTextEndTime.setText(endTime2)

                binding.editTextStartTime.hint = "(대회 시작 시간) ${startTimeCompetition} 형식으로 입력"
                binding.editTextTextEndTime.hint = "(대회 종료 시간) ${endTimeCompetition} 형식으로 입력"
                //datepicker로 변경 해야 함
            }
        }

        override fun getViewByEachMeetingRoom(): MeetingRoomData {
            var meetingRoomData = CompetitionRoomData()

            meetingRoomData.start_time = startTimeCompetition
            meetingRoomData.end_time = endTimeCompetition
            meetingRoomData.location=detailaddress.plus(" ").plus(addressname)
            meetingRoomData.member_list = member_list as ArrayList<String>
            return meetingRoomData
        }

        override fun processEachMeetingRoom() :Boolean{
            if(!Pattern.matches("^\\d{4}년\\d{2}월\\d{2}일\\d{2}시\\d{2}분$",binding.editTextStartTime.getText())){
                binding.editTextStartTime.setText(startTimeCompetition)
                Toast.makeText(activity,"시작 날짜는 1999년03월01일01시00분의 형식으로 입력해주세요", Toast.LENGTH_SHORT).show()
                return false
            }
            if(!Pattern.matches("^\\d{4}년\\d{2}월\\d{2}일\\d{2}시\\d{2}분$",binding.editTextTextEndTime.getText())){
                binding.editTextStartTime.setText(endTimeCompetition)
                Toast.makeText(activity,"시작 날짜는 1999년03월01일01시00분의 형식으로 입력해주세요", Toast.LENGTH_SHORT).show()
                return false
            }

            startTimeCompetition = binding.editTextStartTime.getText().toString()
            endTimeCompetition = binding.editTextTextEndTime.getText().toString()
            return true
        }
    }

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

        if (MeetingRoomDataManager.collectionNameOfLightingMeetingRoom ==colName){
            meetingRoomFactory = LightningMeetingRoomFactory()
        }else if(MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom == colName){
            meetingRoomFactory = PeriodicMeetingRoomFactory()
        }else if(MeetingRoomDataManager.collectionNameOfCompetition ==colName){
            meetingRoomFactory = CompetitionRoomFactory()
        }else if(MeetingRoomDataManager.collectionNameOfPlaceRental ==colName){
            meetingRoomFactory = PlaceRentalRoomFactory()
        }
        meetingRoomGenerator = meetingRoomFactory?.createMeetingRoomViewGenerator()
        meetingRoomGenerator?.initDataAndUI()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                userInputImgUri = data?.data
                try {
                    var bitmapUserSelect = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver,
                        userInputImgUri
                    )
                    binding.imageButtonToPostingMeetingInfoEdit.setImageBitmap(bitmapUserSelect)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }else if (requestCode==9){
                addresscheck=true

                address= data?.extras?.getString("address").toString()
                detailaddress= data?.extras?.getString("detailad").toString()
                addressname = data?.extras?.getString("name").toString()

                positionx = data?.extras?.getDouble("disx")!!
                positiony = data?.extras?.getDouble("disy")!!

                binding.button10.setText("${address} (변경하려면 클릭하세요)")
                binding.button10.invalidate()
            } else {
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