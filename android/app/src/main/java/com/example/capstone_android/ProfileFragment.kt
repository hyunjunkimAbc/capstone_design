package com.example.capstone_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.MeetingList.CreateCompetition
import com.example.capstone_android.SearchAddress.SearchMap
import com.example.capstone_android.SetProfile.MyMeetingRoomActivity
import com.example.capstone_android.SetProfile.SetProfileActivity
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.Util.getImageResult
import com.example.capstone_android.databinding.FragmentProfileSignedInBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_main2.view.*
import okhttp3.internal.notify


class ProfileFragment : Fragment() {
    var hobbydata:ArrayList<String> = arrayListOf()
    private lateinit var binding: FragmentProfileSignedInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileSignedInBinding.inflate(inflater, container, false)


        return binding.root
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserUid: String
    private var storage = FirebaseStorage.getInstance()
    private var db = FirebaseFirestore.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        storage = Firebase.storage

        val currentUser = auth.currentUser
        inittext()
        if (currentUser != null) { // 로그인 한 경우
            println("Sign in")
            currentUserUid = currentUser.uid
            val recyclerView:RecyclerView=binding.setprofilerecycler
            if(SingleTonData.userInfo?.interest_array!=null){
                hobbydata=SingleTonData.userInfo?.interest_array!!
            }

            recyclerView.adapter=HobbyImageIconAdapter2()
            recyclerView.layoutManager= LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            binding.editinterest.setOnClickListener{
                val intent=Intent(activity, SelectHobbyActivity::class.java)
                intent.putExtra("key","change")
                startActivityForResult(intent,1)
            }
            // 정보 수정 버튼 설정
            binding.buttonModifyProfile.setOnClickListener {
                val intent = Intent(activity, SetProfileActivity::class.java)
                intent.putExtra("create", "hello")
                startActivityForResult(intent, 99)
            }
            binding.buttonMyApplyCompetition.setOnClickListener{
                (activity as HomeActivity).gotoCompetitionFragment()
            }
            binding.buttonMyCompetition.setOnClickListener {
                (activity as HomeActivity).gotoCompetitionCreateFragment()
            }
            binding.buttonMyPlaceRental.setOnClickListener{
                (activity as HomeActivity).gotoReservationListFragment()
            }
            binding.allowPlaceBtn.setOnClickListener{
                (activity as HomeActivity).gotoReservationAllowFragment()
            }
            binding.buttonLogOut.setOnClickListener{
                logOut()
            }
            binding.buttonMyMeetingRoom.setOnClickListener{
                val intent = Intent(activity, MyMeetingRoomActivity::class.java)
                startActivityForResult(intent, 99)
            }
        } else { // 로그인 하지 않은 경우
            initData_NotSigned(view)
            println("NOT Sign in")
        }

    }

    // 로그인 한 경우 초기화 하는 함수
    fun initData_Signed(rootView: View){ // 로그인 한 경우 초기화 함수


    }


    fun inittext(){
        Log.d(TAG, SingleTonData.userInfo.toString())
        binding.phoneNumber.text=SingleTonData.userInfo?.email
        binding.nickname.text=SingleTonData.userInfo?.nickname
        binding.birth.text=SingleTonData.userInfo?.birthday
        binding.buttonMylocation.text=SingleTonData.userInfo?.address
        binding.introduceComment.text=SingleTonData.userInfo?.profile_message

        if(SingleTonData.userInfo?.photoUri!=null){
            Glide.with(requireContext()).load(SingleTonData.userInfo?.photoUri). apply(RequestOptions().transform(
                CenterCrop(), CircleCrop()))
                .placeholder(R.drawable.loadingicon)
                .error(R.drawable.userprofile)
                .into(binding.userImage1)
        }
        else {
            Log.d(TAG,"이미지오류")
            binding.userImage1.setImageResource(R.drawable.userprofile)
        }
    }
    // 로그인 하지 않은 경우 초기화 및 로그인 화면으로 이동하는 함수
    fun initData_NotSigned(rootView: View){ // 로그인 하지 않은 경우 초기화 함수

        logIn()
    }



    private fun logOut() { // 로그아웃 시 실행 함수. 로그아웃 하고 로그인 화면으로 넘어감
        auth.signOut()
        // 로그인 화면으로 넘기기
        logIn()
    }

    private fun logIn() { // 로그인 화면으로 이동
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 스택을 제거
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateinterest(){
        hobbydata.clear()
        for(data in SingleTonData.userInfo?.interest_array!!){
            hobbydata.add(data)
        }
        binding.setprofilerecycler.adapter?.notifyDataSetChanged()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==99){
            val activity =requireActivity() as HomeActivity
            activity.updateaddress()
            inittext()
        }
        else if(requestCode==1&&resultCode==-1){
            val activity =requireActivity() as HomeActivity
            updateinterest()
            activity.homeupdateinterest()
        }

    }
    inner class HobbyImageIconAdapter2: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_main2,parent,false)
            return CustomViewHolders(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewhold=(holder as HobbyImageIconAdapter2.CustomViewHolders).itemView
            viewhold.hobbyiconimage.setImageResource(getImageResult(hobbydata[position]))
            viewhold.iconimagetext.text=hobbydata[position]

        }
        inner class CustomViewHolders(view: View) : RecyclerView.ViewHolder(view) {
        }
        override fun getItemCount(): Int {
            return hobbydata.size
        }
    }

    inner class ButtonListener : View.OnClickListener{ // 로그아웃, 회원가입, 모임 관리 버튼

        override fun onClick(v: View?) {
            val mActivity = activity as HomeActivity

            when(v?.id){
                // 나의 신청 대회 버튼 클릭 시 신청 대회 목록 확인하는 프래그먼트로 이동
                /*R.id.button_myApplyCompetition -> {
                    activity.changeFragment(1)
                }*/

                // 나의 개설 대회 버튼 클릭 시 개설 대회 목록 확인하는 프래그먼트로 이동
                /*R.id.button_myCompetition -> {
                    activity.changeFragment(2)
                }*/

                // 내 모임 버튼 클릭 시 가입한 모임 목록 확인하는 프래그먼트로 이동
                /*R.id.button_myMeetingRoom -> {
                    activity.changeFragment(3)
                }*/
                R.id.button_myApplyCompetition -> {
                    (activity as HomeActivity).gotoCompetitionFragment()
                }
                // 나의 장소 대여 버튼 클릭 시 대여한 장소 목록 확인하는 프래그먼트로 이동
                R.id.button_myPlaceRental -> {
                    (activity as HomeActivity).gotoReservationListFragment()
                }
                R.id.allowPlaceBtn->{
                    (activity as HomeActivity).gotoReservationAllowFragment()
                }

                // 내 주소 버튼 클릭 시 주소 변경 화면으로 이동
                R.id.button_mylocation -> {
                    val intent = Intent(requireActivity(), SearchMap::class.java)
                    intent.apply {
                        this.putExtra("create","hello")
                    }
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) // 액티비티 애니메이션 없앰
                    startActivityForResult(intent, 101)
                }
                // 로그아웃하고 로그인 화면으로 이동
                R.id.button_logOut -> logOut()
            }
        }
    }
}