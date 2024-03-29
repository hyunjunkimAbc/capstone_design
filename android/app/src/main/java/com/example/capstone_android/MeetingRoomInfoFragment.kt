package com.example.capstone_android

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.databinding.FragmentMeetingRoomInfoBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
    val binding get() = _binding!!
    //getter 함수 null이 아닌 ui 객체 리턴
    val db = Firebase.firestore
    var isInitAboutInfo =false
    var isInitAboutMember =false
    var myNickName ="star1"//닉네임은 일단 알고 있다고 가정함 login Activity에서 넘겨 받아야 함

    var rootRef = Firebase.storage.reference

    var meetingRoomCollection=db.collection("lighting_meeting_room")
    val userCollection = db.collection("user")
    var meetingRoomId = ""
    var memCurruntCnt =0
    var memCntOfFirebase =0
    var writerUid =""
    class DataForUI(val infoText:Any?,val max:Any?,val memberList:Any?,
                    val title:Any?,val upload_time:Any?,val category:Any?)
    var numOfChatting =-1
    var initChatCnt =0
    var numOfCurrentUsers =0
    var numOfMaxUsers =0
    var meetingRoomMembers :List<String>? =null
    var max =""
    var title=""
    var imageUrl=""
    var meetingRoomGenerator:MeetingRoomViewGenerator? =null
    var meetingRoomFactory :AbstractMeetingRoomFactory? = null
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
    inner class MeetingRoomMemberController{
        fun addEnterMeetingListener(){
            binding.enterMeetingRoomBtn.setOnClickListener {
                meetingRoomCollection.document(meetingRoomId).get().addOnSuccessListener {
                    val max =it.data?.get("max")
                    val memberList = it.data?.get("member_list")
                    if(memberList!= null){
                        numOfCurrentUsers = (memberList as List<String>).size
                    }else{
                        numOfCurrentUsers = 0
                    }
                    numOfMaxUsers = Integer.parseInt(max.toString())
                    userCollection.document("${Firebase.auth.uid}").get().addOnSuccessListener {
                        if(numOfCurrentUsers+1 > numOfMaxUsers){
                            Toast.makeText(activity?.applicationContext,"최대 인원을 넘었습니다.",Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }
                        val meeting_room_id_list = it["meeting_room_id_list"]
                        var isDuple = false
                        if(meeting_room_id_list !=null){

                            for(meetingRoomUid in meeting_room_id_list as List<String>){
                                if(meetingRoomId == meetingRoomUid){
                                    isDuple = true
                                }
                            }
                        }
                        if(isDuple){
                            Toast.makeText(activity?.applicationContext,"이미 가입된 모임 입니다.",Toast.LENGTH_SHORT).show()
                        }else{//meeting_room_id_list가 널이거나 중복된 meeting room이 없는 경우
                            //posting 컬랙션에도 추가 해야 함 member_list
                            meetingRoomCollection.document("${meetingRoomId}").update("member_list" , FieldValue.arrayUnion(Firebase.auth.uid)).addOnSuccessListener {
                                //Toast.makeText(activity?.applicationContext,"가입 성공",Toast.LENGTH_SHORT).show()
                                userCollection.document("${Firebase.auth.uid}").update("meeting_room_id_list" , FieldValue.arrayUnion(meetingRoomId)).addOnSuccessListener {
                                    val text="님이 모임 ${title}에 가입되었습니다"
                                    val username=SingleTonData.userInfo?.nickname
                                    val timestamp=Date().time.toString()
                                    val data=hashMapOf("RealUserUid" to "${Firebase.auth.uid}","UserUid" to arrayListOf<String>("${ Firebase.auth.uid}", writerUid)  ,"message" to text,"timestamp" to timestamp,"isRead" to false,"imageUrl" to imageUrl,"username" to username)
                                    db.collection("userAlarm").document().set(data, SetOptions.merge()).addOnSuccessListener {
                                            Toast.makeText(activity?.applicationContext,"가입 성공",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        fun setRecyclerView(){
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
        }
        fun addAdditionerViewAndAssignData(memberList:List<String>,max:String){
            if(memberList == null){
                binding.numOfPeople.text = "${memCntOfFirebase}(현재 인원) / ${max}(최대 인원)"
                isInitAboutInfo = true
                return
            }

            var friendUidArr :List<String> = memberList as List<String>
            //println("----------${friendUidArr}")
            //var numOfMember = it.data?.get("num_of_member")
            memCntOfFirebase = friendUidArr.size
            binding.numOfPeople.text = "${memCntOfFirebase}(현재 인원) / ${max}(최대 인원)"
            isInitAboutInfo = true


            if(viewModel.items.size>0){//초기화 할때 0이상이면 예전에 저장했던 정보가 있는 것임
                return
            }
            if(memberList == null){
                return
            }
            friendUidArr = memberList as List<String>
            numOfChatting = friendUidArr.size

            for(friendUid in friendUidArr){
                userCollection.document(friendUid.trim()).get().addOnSuccessListener {
                    var profileMessage = ""
                    var nickname = ""
                    var uid = ""
                    var editTime = 0.toLong()

                    uid = "${it["uid"]}"
                    profileMessage = "${it["profile_message"]}"
                    nickname = "${it["nickname"]}"
                    editTime = it["edit_time"] as Long
                    addUserToRecyclerView(uid,nickname,profileMessage,editTime)
                    memCurruntCnt++
                    if(memCntOfFirebase==memCurruntCnt){
                        println("------ all success ${memCurruntCnt}/${memCntOfFirebase}")
                        isInitAboutMember = true
                    }
                    //미팅룸의 회원들의 정보가 변했을때
                    userCollection.document(friendUid.trim()).addSnapshotListener { snapshot, error ->
                        if(isInitAboutMember==false){
                            return@addSnapshotListener
                        }
                        if(numOfChatting ==-1){
                            println("아직 초기화 안됨 userCollection addSnapshotListener -1 mrI")
                            return@addSnapshotListener
                        }
                        if (initChatCnt < numOfChatting){
                            println("아직 초기화 안됨 userCollection addSnapshotListener < mrI")
                            return@addSnapshotListener
                        }
                        val nickname = snapshot?.data?.get("nickname")
                        val profileMessage =snapshot?.data?.get("profile_message")
                        val editTime = snapshot?.data?.get("edit_time") as Long
                        var i=0
                        for(member in viewModel.items){
                            if(friendUid.trim() == member.uid.trim()){
                                updateUserToRecyclerview(i,friendUid.trim(),
                                    nickname as String, profileMessage as String
                                    ,editTime)
                                break
                            }
                            i++
                        }

                    }
                }.addOnFailureListener {
                    updateInitCnt(false)
                }

            }
        }
        @Synchronized
        fun updateMember(friendUidArr: List<String>){
            if(viewModel.items.size == friendUidArr.size){//개수 변화 없으면 아무것도 하지 않음
                println("탈출----")
            }else if(viewModel.items.size <friendUidArr.size){//맴버가 추가 되면(개수 증가) 새로 받아오기
                //맨뒤에 있는 것이 새로운 맴버일때 정상 동작함 but 2번 추가된다.
                userCollection.document(friendUidArr[friendUidArr.size-1]).get().addOnSuccessListener {
                    println("---userCollection addOnSuccessListener in addSnapshotListener")
                    var profileMessage = ""
                    var nickname = ""
                    var uid = ""
                    var editTime =0.toLong()
                    uid = "${it["uid"]}"
                    profileMessage = "${it["profile_message"]}"
                    nickname = "${it["nickname"]}"
                    editTime = it["edit_time"] as Long

                    updateNumOfChatting(true)
                    addUserToRecyclerView(uid,nickname,profileMessage,editTime)
                }

            }else if(viewModel.items.size > friendUidArr.size){//맴버가 사라지면 그 맴버는 리사이클러에서 지우기
                println("삭제 ------")
                var isInFirebase =false
                for(member in viewModel.items){
                    isInFirebase =false
                    for (friendUid in friendUidArr){
                        if(member.uid == friendUid){
                            isInFirebase = true
                        }
                    }
                    if(isInFirebase){
                        continue
                    }else{//firebase에는 없는데 viewmodel에는 맴버가 있는 상황 그 맴버는 지워주면 된다
                        viewModel.deleteItem(member)
                        //numOfChatting--
                        updateNumOfChatting(false)
                        updateInitCnt(false,false)
                        break
                    }
                }
            }
        }

        fun addUserToRecyclerView(uid:String,nickname:String, profileMessage:String,editTime :Long){
            var userProfileImage = rootRef.child("user_profile_image/${uid}.jpg")
            userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                    for(member in viewModel.items){//중복 검사 이미 그 맴버가 있는 데 또 추가 할 수 없다.
                        if (member.uid == uid){
                            return@addOnCompleteListener
                        }
                    }
                    viewModel.addItem(Member(bmp,nickname,profileMessage,uid,editTime))
                    updateInitCnt(true)
                }else{
                    var ref = rootRef.child("user_profile_image/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                            for(member in viewModel.items){//중복 검사
                                if (member.uid == uid){
                                    return@addOnCompleteListener
                                }
                            }
                            viewModel.addItem(Member(bmp,nickname,profileMessage,uid,editTime))
                            updateInitCnt(true)
                        }else{
                            println("undefined err")
                        }
                    }
                }
            }
        }
        fun updateUserToRecyclerview(i:Int,uid:String,nickname:String, profileMessage:String,editTime: Long){
            var userProfileImage = rootRef.child("user_profile_image/${uid}.jpg")
            userProfileImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                if(it.isSuccessful){
                    val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                    viewModel.updateItem(i,Member(bmp,nickname,profileMessage,uid,editTime))
                    viewModel.items.sortByDescending{
                        it.editTime
                    }
                    viewModel.itemsListData.value = viewModel.items
                }else{
                    var ref = rootRef.child("user_profile_image/default.jpg")
                    ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                            viewModel.updateItem(i,Member(bmp,nickname,profileMessage,uid,editTime))
                            viewModel.items.sortByDescending{
                                it.editTime
                            }
                            viewModel.itemsListData.value = viewModel.items
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
                        viewModel.items.sortByDescending{
                            it.editTime
                        }
                        viewModel.itemsListData.value = viewModel.items

                    }
                }
                initChatCnt++ //비동기로 추가 될때 마다 업데이트
            }else{
                if(isSort){
                    if (initChatCnt-1 >= numOfChatting){// 마지막 것을 받아왔을때 정렬한다.
                        viewModel.items.sortByDescending{
                            it.editTime
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
    }
    inner abstract class MeetingRoomViewGenerator{
        abstract fun getViewInflated()
        abstract fun setRecyclerView()
        abstract fun addEnterMeetingListener()
        abstract fun checkIfNeedToCheckMember():Boolean
        abstract fun addAdditionerViewAndAssignData(it:DocumentSnapshot)
        open fun initDataAndUI(){

            val colName = activity?.intent?.getStringExtra("collectionName")
            meetingRoomCollection = db.collection(colName!!)

            //val meetingRoomId = viewModel.meetingRoomId 모임 설명 이미지 얻어오기
            meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id").toString()


            setRecyclerView()


            addEnterMeetingListener()

            println("5 21 colName ${colName} meeting_room_id ${meetingRoomId}")
            //미팅룸 정보 얻어오기
            println("meetingRoomId ${meetingRoomId}")
            meetingRoomCollection.document(meetingRoomId).get().addOnSuccessListener {
                val maxTemp = "${it["max"]}"
                binding.meetingRoomText.text = "${it["info_text"]}"
                binding.uploadTime.text = "최종 업로드 ${SimpleDateFormat("yyyy-MM-dd").format(it["upload_time"] as Long)}"
                binding.meetingRoomTitle.text = "모임 명: ${it["title"]}"
                binding.category.text = "카테고리: ${it["category"]}"
                binding.affiliatedArea.text = "${it["address"]}"
                title= "${it["title"]}"
                imageUrl="${it["imageUrl"]}"
                writerUid = it.data?.get("writer_uid") as String

                val colName = activity?.intent?.getStringExtra("collectionName")

                meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id") ?: ""
                var meetingInfoImage = rootRef.child("${colName}/${meetingRoomId}.jpg")
                println("5-26-2 ${colName} ${meetingRoomId}")
                meetingInfoImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        binding.meetingInfoImage.setImageBitmap(bmp)
                    }else{
                        var ref = rootRef.child("${colName}/default.jpg")
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

                binding.editMeetingRoomInfoBtn.setOnClickListener {
                    if(Firebase.auth.uid == writerUid){
                        val bundle = bundleOf("document_id" to meetingRoomId)
                        findNavController().navigate(R.id.action_meetingRoomInfoFragment_to_editMeetingInfoFragment ,bundle)
                    }else{
                        Toast.makeText(activity,"작성자가 아닙니다. 접근할 수 없습니다.", Toast.LENGTH_LONG).show()
                    }
                }

                //commentsListString = it["comment_id_list"]
                //writer_uid = it["writer_uid"] //주석 했지만 나중에는 사용할 수도 있음
                //변경 테스트 하고 싶으면 if문 조건절에서 positionx 등에 변화를 주면 됨

                addAdditionerViewAndAssignData(it)
                //member list의 경우는 없으면 그냥 출력 안해 버리면 됨
                // start time end time의 경우에는

                //memberList를 확실히 viewModel에 저장한 후에 recyclerview를 불러와야 함
                //미팅룸의 정보가 변했을때
                meetingRoomCollection.document( meetingRoomId ).addSnapshotListener { snapshot, error ->
                    if(checkIfNeedToCheckMember()){
                        if(numOfChatting ==-1){
                            println("아직 초기화 안됨 meetingRoomCollection addSnapshotListener -1 mrI")
                            return@addSnapshotListener
                        }
                        if (initChatCnt < numOfChatting){
                            println("아직 초기화 안됨 meetingRoomCollection addSnapshotListener < mrI")
                            return@addSnapshotListener
                        }
                        val memberList = snapshot?.data?.get("member_list")
                        if(memberList ==null){
                            return@addSnapshotListener
                        }
                        val friendUidArr :List<String> = memberList as List<String>
                        MeetingRoomMemberController().updateMember(friendUidArr)
                        //viewmodel 기존에 있는거 삭제 해야 함
                    }

                    val maxTemp = snapshot?.data?.get("max")
                    binding.meetingRoomText.text = "${snapshot?.data?.get("info_text")}"
                    binding.uploadTime.text = "최종 업로드 ${SimpleDateFormat("yyyy-MM-dd").format(snapshot?.data?.get("upload_time") as Long)}"
                    binding.meetingRoomTitle.text = "모임 명: ${snapshot?.data?.get("title")}"
                    binding.category.text = "카테고리: ${snapshot?.data?.get("category")}"
                    binding.affiliatedArea.text = "${snapshot?.data?.get("address")}"

                    //val dataForUI = DataForUI(infoText,max,memberList,title,upload_time,category)
                    //updateInfoUI(dataForUI)


                    val colName = activity?.intent?.getStringExtra("collectionName")

                    meetingRoomId = activity?.intent?.getStringExtra("meeting_room_id") ?: ""
                    var meetingInfoImage = rootRef.child("${colName}/${meetingRoomId}.jpg")
                    meetingInfoImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                        if(it.isSuccessful){
                            val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                            binding.meetingInfoImage.setImageBitmap(bmp)
                        }else{
                            var ref = rootRef.child("${colName}/default.jpg")
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


                }

            }


        }



    }
    inner class LightingMeetingRoomGenerator: MeetingRoomViewGenerator() {
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }

        override fun setRecyclerView() {
            MeetingRoomMemberController().setRecyclerView()
        }

        override fun addEnterMeetingListener() {
            MeetingRoomMemberController().addEnterMeetingListener()
        }


        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot) {
            /*
            positionx = it["positionx"] as Double
            positiony = it["positiony"] as Double
            member_list = it["member_list"] as ArrayList<String> //view
             */
            if(it["start_time"] !=null && it["end_time"] !=null){
                val date = it["date"] as String
                var startTime = it["start_time"] as String //view
                var endTime = it["end_time"] as String //view
                startTime = "${date} ${startTime}"
                endTime = "${date} ${endTime}"


                val TextView = TextView(activity?.applicationContext).apply { // 새로운 버튼 객체 생성
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = "모임 시작 시간 : ${startTime}"
                }
                val TextView2 = TextView(activity?.applicationContext).apply { // 새로운 버튼 객체 생성
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = "모임 종료 시간 : ${endTime}"
                }
                //val linearLayout = binding.root.findViewById<LinearLayout>(R.id.linearLayoutForEachInfo)

                binding.linearLayoutForEachInfo.addView(TextView)
                binding.linearLayoutForEachInfo.addView(TextView2)
            }
            if (it["member_list"] !=null && it["max"] !=null){
                val member_list = it["member_list"] as ArrayList<String>
                val max =  it["max"] as String

                MeetingRoomMemberController().addAdditionerViewAndAssignData(member_list,max)
            }
        }

        override fun checkIfNeedToCheckMember(): Boolean {
            return true
        }

    }
    inner class PeriodicMeetingRoomGenerator:MeetingRoomViewGenerator(){
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }

        override fun setRecyclerView() {
            MeetingRoomMemberController().setRecyclerView()
        }

        override fun addEnterMeetingListener() {
            MeetingRoomMemberController().addEnterMeetingListener()
        }

        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot) {
            /*
            positionx = it["positionx"] as Double
            positiony = it["positiony"] as Double
            _binding = binding as FragmentLightingMeetingRoomBinding
            startTime = it["start_time"] as Long //view
            endTime = it["end_time"] as Long //view

            member_list = it["member_list"] as ArrayList<String>//view

             */
            //개선 요망
            if (it["member_list"] !=null && it["max"] !=null){
                val member_list = it["member_list"] as ArrayList<String>
                val max =  it["max"] as String

                MeetingRoomMemberController().addAdditionerViewAndAssignData(member_list,max)
            }
        }

        override fun checkIfNeedToCheckMember(): Boolean {
            return true
        }

    }
    inner class PlaceRentalRoomGenerator:MeetingRoomViewGenerator(){
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }

        override fun setRecyclerView() {

        }

        override fun addEnterMeetingListener() {
            binding.enterMeetingRoomBtn.text ="예약 하기"
            binding.enterMeetingRoomBtn.setOnClickListener {
                println("장소 예약 화면으로 이동")
                val bundle = bundleOf("place_rental_room_id" to meetingRoomId)
                findNavController().navigate(R.id.action_meetingRoomInfoFragment_to_reservationRequestFragment ,bundle)
            }
        }

        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot) {
            /*
            positionx = it["positionx"] as Double
            positiony = it["positiony"] as Double
            reservation_uid_list = it["reservation_uid_list"]*/
            binding.linearLayoutAreaNumOfPeople.removeView(binding.numOfPeople)
        }

        override fun checkIfNeedToCheckMember(): Boolean {
            return false
        }

    }
    inner class CompetitionRoomGenerator:MeetingRoomViewGenerator(){
        override fun getViewInflated() {
            TODO("Not yet implemented")
        }

        override fun setRecyclerView() {
        }

        override fun addEnterMeetingListener() {
            binding.enterMeetingRoomBtn.text ="대회 참여 하기"
            binding.enterMeetingRoomBtn.setOnClickListener {
                println("대회 참여 화면으로 이동")
                findNavController().navigate(R.id.action_meetingRoomInfoFragment_to_competitionUserApplicationFragment)
            }

        }

        override fun addAdditionerViewAndAssignData(it: DocumentSnapshot) {
            /*
            positionx = it["positionx"] as Double
            positiony = it["positiony"] as Double

            member_list = it["member_list"] as ArrayList<String> //view

             */
            if(it["start_time"] !=null && it["end_time"] !=null){
                val startTime = it["start_time"] as String //view
                val endTime = it["end_time"] as String //view

                val TextView = TextView(activity?.applicationContext).apply { // 새로운 버튼 객체 생성
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = "모임 시작 시간 : ${startTime}"
                }
                val TextView2 = TextView(activity?.applicationContext).apply { // 새로운 버튼 객체 생성
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = "모임 종료 시간 : ${endTime}"
                }
                //val linearLayout = binding.root.findViewById<LinearLayout>(R.id.linearLayoutForEachInfo)

                binding.linearLayoutForEachInfo.addView(TextView)
                binding.linearLayoutForEachInfo.addView(TextView2)
            }
            binding.linearLayoutAreaNumOfPeople.removeView(binding.numOfPeople)
        }

        override fun checkIfNeedToCheckMember(): Boolean {
            return false
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
        //v = inflater.inflate(R.layout.fragment_meeting_room_info, container, false)
        //val temp = LightingMeetingRoomControlStrategy()
        //_binding = temp.getViewBinding(inflater,container) as FragmentMeetingRoomInfoBinding?
        _binding = FragmentMeetingRoomInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //meetingroominfo
        //데이터 얻어와서 ui에 반영
        val colName = activity?.intent?.getStringExtra("collectionName")

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

    override fun onDestroyView() {
        super.onDestroyView()
        println("------ondestroyView")
        //_binding = null//메모리 누수 방지

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