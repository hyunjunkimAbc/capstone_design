package com.example.capstone_android

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.FragmentCompetitionUserApplicationBinding
import com.example.capstone_android.databinding.FragmentEditMeetingInfoBinding
import com.example.capstone_android.databinding.FragmentMeetingRoomPostingsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text

class CompetitionUserApplicationFragment : Fragment(){
    private var _binding: FragmentCompetitionUserApplicationBinding? = null
    private val binding get() = _binding!!

    //private val auth = Firebase.auth
    private val db = Firebase.firestore

    private var rootRef = Firebase.storage.reference
    private var currentUserUid = Firebase.auth.uid

    private val userCollection = db.collection("user")
    private val competitionRoomCollection = db.collection("competition_room")

    private var userNickname : TextView = binding.competitionUserNickName // 유저 닉네임
    private var userBirth : TextView = binding.competitonUserBirth // 유저 생년월일
    private var userLocation : TextView = binding.competitionUserLocation // 유저 위치
    private var userEmail : TextView = binding.competitionUserEmail // 유저 이메일

    private var competitionTitle : TextView = binding.competitionTitle // 대회명
    private var competitionTime : TextView = binding.competitionTime // 대회 시작시간
    private var competitionLocation : TextView = binding.competitionLocation // 대회 장소

    private var document_id : String = "" // 이전 프래그먼트에서 넘어옴

    private var max_num : Int = 0 // 대회 최대 신청 가능 인원
    private var current_num : Int = 0 // 대회 현재 신청 인원

    private data class Info( // 정보
        var nickname: String = "이름", // 닉네임
        var birthday: String = "생년월일", // 생년월일
        var address: String = "나의 위치", // 위치
        var email: String = "전화번호", // 이메일

        var title: String = "대회명", // 대회명
        var startTime: String = "시작시간", // 대회 시작시간
        var endTime: String = "종료시간", // 대회 종료시간
        var location: String = "장소", // 대회 장소
        var member_list: List<String>  = arrayListOf(), // 현재 인원
        var max: String = "최대인원", // 최대인원

        var edit_time: Long = 0 // 수정시간
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompetitionUserApplicationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 테스트용
        document_id = activity?.intent?.getStringExtra("meeting_room_id").toString()

        // 이전 프래그먼트로 부터 주소 받기
        //document_id = activity?.intent?.getStringExtra("meeting_room_id").toString()

        // 사용자 정보 및 대회 정보 불러오기
        getUserInfo()
        getCompetitionInfo()

        //
        binding.buttonApply.setOnClickListener(){
            if(current_num < max_num) { // 최대 인원보다 적으면 신청 가능
                sendInfo() // 정보 보내고 뒤로 가기
            }
            else{ // 최대 인원 초과하면 신청 불가능 & 토스트 메세지
                // 뒤로 보내기
                // 실패 토스트 출력
            }
        }



    }

    private fun getUserInfo(){ // 사용자 정보 불러오기
        currentUserUid?.let {
            userCollection.document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val applyInfo = document.toObject(Info::class.java)
                        applyInfo?.let {
                            userNickname.setText(it.nickname)
                            userBirth.setText(it.birthday)
                            userLocation.setText(it.address)
                            userEmail.setText(it.email)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error loading profile information: ${exception.message}")
                }
        }
    }

    private fun getCompetitionInfo() { // 대회 정보 불러오기
        competitionRoomCollection.document(document_id).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val applyInfo =
                        document.toObject(Info::class.java)
                    applyInfo?.let {
                        competitionTitle.setText(it.title)
                        competitionTime.setText(it.startTime + " ~ " + it.endTime)
                        competitionLocation.setText(it.location)
                        max_num = it.max.toInt()
                        current_num = it.member_list.size
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error loading profile information: ${exception.message}")
            }
    }

    private fun sendInfo() { // 유저 콜렉션에 정보 보내기
        currentUserUid?.let {
            userCollection.document(it)
                .update("competition_id_list", FieldValue.arrayUnion(document_id)).addOnSuccessListener {
                 // 유저 컬렉션에 넣기
                competitionRoomCollection.document(document_id)
                    .update("member_list", FieldValue.arrayUnion(currentUserUid))
                    .addOnSuccessListener {
                        // 대회 컬렉션에 넣기
                        //findNavController().navigate() // 뒤로 가기
                    }
            }
        }
    }

}