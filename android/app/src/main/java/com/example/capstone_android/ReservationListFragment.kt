package com.example.capstone_android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.place_rental.databinding.FragmentReservationlistBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReservationListFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentReservationlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ReservationViewModel by viewModels<ReservationViewModel>()

    val db = Firebase.firestore
    var stRef = Firebase.storage.reference
    var place_id = activity?.intent?.getStringExtra("place_rental_room_id") ?: "9cAZZmIcuNFcGfoK5oen"
    var bmp: Bitmap? = null
    //lateinit var auth : FirebaseAuth

    var isInit =false               //
    var isInitAboutMember =false    //
    var cnt =0                      //


    val userCollection = db.collection("user")
    val reservationCollection = db.collection("reservation")
    val placeCollection = db.collection("place_rental_room")
    var meetingRoomId = ""  //
    var memCurruntCnt =0    //
    var memCntOfFirebase =0 //
    class DataForPostingUI(val infoText:Any?,val max:Any?,val memberList:Any?,
                           val title:Any?,val upload_time:Any?,val category:Any?) //
    var numOfChatting =-1       //
    var initChatCnt =0          //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {
        val adapter = ReservationListAdapter(viewModel)
        binding.reserlist.adapter = adapter
        binding.reserlist.layoutManager = LinearLayoutManager(activity)
        binding.reserlist.setHasFixedSize(true)
        viewModel.itemsListData.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
        }
        registerForContextMenu(binding.reserlist)

        //데이터 얻어와서 ui에 반영
        initDataAndUI()
    }

    private fun initDataAndUI(){
        db.collection("place_rental_room").document(place_id).get().addOnSuccessListener {
            if(it["reservation_id_list"] == null){
                return@addOnSuccessListener
            }
            val reservation_id_list = it["reservation_id_list"] as List<String>
            //if(viewModel.items.size == reservation_id_list.size) {//개수 변화 없으면 아무것도 하지 않음
            //}
            //else {
                viewModel.items.clear()
            var placeImageRes = stRef.child("place_image/${place_id}.jpg")
            placeImageRes.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                if (it.isSuccessful) {
                    bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result.size)
                }
                for (reservationHistory in reservation_id_list) {
                    db.collection("reservation").document(reservationHistory).get()
                        .addOnSuccessListener {
                            var reserDocument = it.id
                            var placeImage = bmp!!
                            var placeName = it["placeName"].toString()
                            var reservatorName = it["reservatorName"].toString()
                            var requestDate = it["requestDate"].toString()
                            var startReservedSchedule = it["startReservedSchedule"].toString()
                            var endReservedSchedule = it["endReservedSchedule"].toString()
                            var numOfPeople = it["numOfPeople"].toString().toInt()
                            var okCheck = it["okCheck"].toString().toBoolean()
                            var requestToday = it["requestToday"].toString()
                            var requestTime = it["requestTime"].toString().toLong()

                            viewModel.addItem(
                                ReservationData(
                                    reserDocument,
                                    placeImage,
                                    placeName,
                                    reservatorName,
                                    requestDate,
                                    startReservedSchedule,
                                    endReservedSchedule,
                                    numOfPeople,
                                    okCheck,
                                    requestToday,
                                    requestTime
                                )
                            )
                            //addPostingToRecyclerView(writer_uid as String, text as String, upload_time as Long,document_id)
                        }.addOnFailureListener {
                            println("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
                            //updateInitCnt(false)
                        }
                }
            }
            //}
        }
    }

/*
    private fun initDataAndUI(){
        userCollection.document(Firebase.auth.uid.toString()).get().addOnSuccessListener {
            if(it["place"])
        }




        userCollection.document(Firebase.auth.uid.toString()).get().addOnSuccessListener {
            if(it["place_id_list"]==null){
                return@addOnSuccessListener
            }
            val place_id_list = it["place_id_list"] as List<String>
            //numOfChatting = place_id_list.size
            for (place_id in place_id_list){
                placeCollection.document(place_id).get().addOnSuccessListener {
                    //val upload_time = it["upload_time"]
                    //val writer_uid = it["writer_uid"]
                    //val document_id = it.id
                    val reservation_id_list = it["reservation_id_list"] as List<String>
                    numOfChatting = reservation_id_list.size
                    for (reservation_id in reservation_id_list){
                        reservationCollection.document(reservation_id).get().addOnSuccessListener {
                            val placeName = it["place_name"]
                            //val reservatorName = it["uid"]
                            //val reserDate = it[]
                            //val reserTime = ""
                            val numOfPeople = it["num_of_people"]
                            var okCheck = it["ok_check"]
                            viewModel.addItem(ReservationData(
                                placeName as String, numOfPeople as Int, okCheck as Boolean)

                            //addPlaceToRecyclerView(placeName as String, numOfPeople as Int, okCheck as Boolean
                            /*writer_uid as String, title as String, upload_time as Long, document_id*/)
                        }
                    }
                }.addOnFailureListener {
                    println("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
                    //updateInitCnt(false)
                }
            }
        }

        placeCollection/*.document("${Firebase.auth.uid}")*/.get().addOnSuccessListener{
            //println(it.documents)
            for(d in it){
                if(d["writer_uid"]==){//내가 등록한 장소 있음
                    println("check2")
                    println("문서uid = ${d.id}")
                    //예약목록 띄우기
                    reservationCollection.get().addOnSuccessListener {
                        //println("문서uid = ${d.id}")
                        /*
                        for(d2 in it){
                            if(d2["place_uid"]==d.id){

                            }
                        }*/
                    }
                }else {
                    //println("내가 등록한 장소 없음")
                }
            }
        }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
            }
    }


    fun addPlaceToRecyclerView(placeName:String, numOfPeople:Int, okCheck:Boolean
    /*writer_uid:String, postingText:String,/:Long,document_id:String*/){
        //place_uid = document_id
        reservationCollection.get().addOnSuccessListener {
            for(d in it) {
                if (d["place_uid"] == document_id) {
                    viewModel.addItem(ReservationData(/*bmp,*/
                        placeName as String, postingText, timePosting,document_id,writer_uid))
                    //updateInitCnt(true)
                    //addUserSnapShot(writer_uid)
                }
            }
            /*val nickname = it.data?.get("nickname")
            var userProfileImage = stRef.child("user_profile_image/${writer_uid}.jpg")
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
                    updateInitCnt(true)
                    addUserSnapShot(writer_uid)
                }else{
                    var ref = stRef.child("user_profile_image/default.jpg")
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
                            updateInitCnt(true)
                            addUserSnapShot(writer_uid)
                        }else{
                            println("undefined err")
                        }
                    }
                }
            }*/

        }.addOnFailureListener {
            print("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
            //updateInitCnt(false)
        }

    }*/
/*
    fun addUserSnapShot(writer_uid: String){
        userCollection.document(writer_uid).addSnapshotListener { value, error ->
            if(initChatCnt < numOfChatting){
                println("아직 초기화 안됨 userCollection addSnapshotListener m r p")
                return@addSnapshotListener
            }
            val nickname =value?.data?.get("nickname")
            val uid = value?.data?.get("uid")
            var userProfileImage = stRef.child("user_profile_image/${uid}.jpg")
            var bmp: Bitmap
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
                    var ref = stRef.child("user_profile_image/default.jpg")
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
    }*/
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
            ReservationListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}