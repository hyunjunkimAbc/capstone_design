package com.example.capstone_android

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.MeetingList.*
import com.example.capstone_android.Util.MainMenuId
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.Util.getImageResult
import com.example.capstone_android.com.example.capstone_android.MeetingRoomDataManager
import com.example.capstone_android.data.ClubData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*

import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_main2.view.*


class DetailViewFragment: Fragment() {
    lateinit var db : FirebaseFirestore
    var clubdata:ArrayList<ClubData> = arrayListOf()
    var hobbydata:ArrayList<String> = arrayListOf()
    var address:String?=null
    var openkey:String?=null
    private lateinit var viewModel: MeetingViewModel
    private lateinit var PeriodiclistAdapter : ListperiodicAdapter
    private lateinit var LightlistAdapter:ListLightAdapter
    private lateinit var PlacelistAdapter:ListPlaceAdapter
    private lateinit var CompetitionAdapter:ListCompetitionAdapter
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"디테일뷰 생성")
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_main,container,false)
        db= Firebase.firestore
        view.progressBar.visibility=View.GONE
        openkey=arguments?.getString("openkey")
        Log.d(ContentValues.TAG,"디테일뷰 오픈키는 = ${openkey}")
        val fab:FloatingActionButton=view.CreateClub
        val changefab:FloatingActionButton=view.Settingbtn
        val recyclerView1:RecyclerView=view.room_recyclerview
        val recyclerView2:RecyclerView=view.hobby_recyclerview
        val nestedscrollview:NestedScrollView=view.nestedScrollView
        nestedscrollview.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(scrollY>oldScrollY){
                    fab.hide()
                }
                if(scrollY<oldScrollY){
                    fab.show()
                }
            }

        })  //스크롤뷰 올라가면 버튼 생기고 내리면 버튼 사라지게

        changefab.setOnClickListener{
            val intent=Intent(activity, SelectHobbyActivity::class.java)
            intent.putExtra("key","change")
            startActivityForResult(intent,1)
        } //관심사 재설정버튼

        fab.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                if(MainMenuId.periodic == openkey) {
                    val intent = Intent(activity, CreateActivity::class.java)
                    intent.putExtra("create", "hello")
                    startActivityForResult(intent, 9)
                } //주기적모임이면 주기적 모임 생성 액티비티
                else if(MainMenuId.light==openkey){
                    val intent = Intent(activity, CreateLightMeeting::class.java)
                    intent.putExtra("create", "hello")
                    startActivityForResult(intent, 8)
                }
                else if(MainMenuId.place==openkey){
                    //재웅님 장소버튼 추가해주세오
                    val intent = Intent(activity, AddPlaceActivity::class.java)
                    intent.putExtra("create", "hello")
                     startActivityForResult(intent, 7)
                }
                else if(MainMenuId.competition==openkey){
                    val intent = Intent(activity, CreateCompetition::class.java)
                    intent.putExtra("create", "hello")
                    startActivityForResult(intent, 6)
                }
            } else {
                Toast.makeText(activity, "스토리지 읽기 권한이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
        if(SingleTonData.userInfo?.interest_array!=null){
            hobbydata=SingleTonData.userInfo?.interest_array!!
        }

        view.username.text=SingleTonData.userInfo?.nickname+"님을"

        if(MainMenuId.periodic == openkey) {
            viewModel=ViewModelProvider(this)[MeetingViewModel::class.java]
            recyclerView1.layoutManager = LinearLayoutManager(activity)
            PeriodiclistAdapter=ListperiodicAdapter(emptyList())
            recyclerView1.adapter = PeriodiclistAdapter
            PeriodiclistAdapter.setItemClickListener(object:ListperiodicAdapter.MeetingItemClickListener{
                override fun onClick(v: View, position: Int) {
                    SingleTonData.clubdata[position].Uid?.let {
                        gotoMeetingRoomActivity(MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom,it)
                    }
                    println(SingleTonData.clubdata[position].Uid)
                }
            })
            viewModel.MeetingItemList.observe(viewLifecycleOwner) { data ->
                PeriodiclistAdapter.submmeetingitList(data)
            }
            view.welcome_user.text="위한 모임 추천"
            view.explain_set.text="모임에 가입해 취미생활을 즐겨보세요"
            viewModel.loadMeetingData()
        }
        else if(MainMenuId.light==openkey){
            viewModel=ViewModelProvider(this)[MeetingViewModel::class.java]
            recyclerView1.layoutManager = LinearLayoutManager(activity)
            LightlistAdapter= ListLightAdapter(emptyList())
            recyclerView1.adapter = LightlistAdapter
            LightlistAdapter.setItemClickListener(object:ListLightAdapter.LightMeetingItemClickListener{
                override fun onClick(v: View, position: Int) {
                    Log.d(TAG,"번개모임 아이템 클릭")
                    SingleTonData.lightdata[position].uid?.let {
                        gotoMeetingRoomActivity(
                            MeetingRoomDataManager.collectionNameOfLightingMeetingRoom,
                            it
                        )
                    }
                }
            })
            viewModel.LightMeetingItemList.observe(viewLifecycleOwner) { data ->
                LightlistAdapter.sublightmmeetingitList(data)
            }
            view.welcome_user.text="위한 번개모임 추천"
            view.explain_set.text="언제 어디서든 취미활동을 즐기세요"
            viewModel.loadLightMeetingData()
        }
        else if(MainMenuId.place==openkey){
            Log.d(TAG,"장소리스트 뿌리기")
            viewModel=ViewModelProvider(this)[MeetingViewModel::class.java]
            recyclerView1.layoutManager = LinearLayoutManager(activity)
            PlacelistAdapter=ListPlaceAdapter(emptyList())
            recyclerView1.adapter = PlacelistAdapter
            PlacelistAdapter.setPlaceItemClickListener(object:ListPlaceAdapter.PlaceItemClickListener{
                override fun onClick(v: View, position: Int) {
                    Log.d(TAG,"장소아이템 클릭")
                    SingleTonData.placedata[position].Uid?.let {
                        gotoMeetingRoomActivity(
                            MeetingRoomDataManager.collectionNameOfPlaceRental,
                            it
                        )
                    }
                }
            })
            viewModel.PlaceItemList.observe(viewLifecycleOwner){data->
                PlacelistAdapter.subPlaceList(data)
            }
            view.welcome_user.text="위한 모임장소 추천"
            view.explain_set.text="모임에 필요한 장소를 골라보세요"
            viewModel.loadPlaceData()
        }
        else if(MainMenuId.competition==openkey){
            Log.d(TAG,"대회 리스트 뿌리기")
            viewModel=ViewModelProvider(this)[MeetingViewModel::class.java]
            recyclerView1.layoutManager = LinearLayoutManager(activity)
            CompetitionAdapter= ListCompetitionAdapter(emptyList())
            recyclerView1.adapter = CompetitionAdapter
            CompetitionAdapter.setCompetitionItemClickListener(object:ListCompetitionAdapter.CompetitionItemClickListener{
                override fun onClick(v: View, position: Int) {
                    Log.d(TAG,"대회 아이템 클릭")
                    SingleTonData.competitiondata[position].Uid?.let {
                        gotoMeetingRoomActivity(
                            MeetingRoomDataManager.collectionNameOfCompetition,
                            it
                        )
                    }
                }
            })
            viewModel.CompetitionItemList.observe(viewLifecycleOwner){data->
                CompetitionAdapter.subCompetitionList(data)
            }
            view.welcome_user.text="위한 대회 추천"
            view.explain_set.text="취미에서 대회로 도전해보세요!!"
            viewModel.loadCompetitionData()
        }

        recyclerView2.adapter=HobbyImageIconAdapter()
        recyclerView2.layoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
        view.refresh_layout.setOnRefreshListener {
            //clubdata.shuffle()
            if(MainMenuId.periodic==openkey){
                update()
                refresh_layout.isRefreshing = false
            }else if(MainMenuId.light==openkey){
                updatelight()
                refresh_layout.isRefreshing = false
            }
            else if(MainMenuId.place==openkey){
                updateplace()
                refresh_layout.isRefreshing=false
            }
            else if(MainMenuId.competition==openkey){
                updateCompetition()
                refresh_layout.isRefreshing=false
            }
        }
        return view
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==9){
            update()
        }
        else if(requestCode==8){
            updatelight()
        }
        else if(requestCode==7){
            updateplace()
        }
        else if(requestCode==6){
            updateCompetition()
        }
        else if(requestCode==1&&resultCode==-1){
            if(MainMenuId.periodic==openkey){
                update()
                update_interest()
                val activity=requireActivity() as HomeActivity
                activity.addremovemapmarker()
            }
            else if(MainMenuId.light==openkey){
                updatelight()
                update_interest()
                val activity=requireActivity() as HomeActivity
                activity.addremovemapmarker()
            }
            else if(MainMenuId.place==openkey){
                updateplace()
                update_interest()
                val activity=requireActivity() as HomeActivity
                activity.addremovemapmarker()
            }
            else if(MainMenuId.competition==openkey){
                updateCompetition()
                update_interest()
                val activity=requireActivity() as HomeActivity
                activity.addremovemapmarker()
            }
        }

    }
    fun gotoMeetingRoomActivity(colName:String,meetingRoomUid:String){
        println("5 21 collectionName ${colName} meeting_room_id ${meetingRoomUid}")
        var intent= Intent(context, MeetingRoomActivity::class.java)
        println("5-26-1 ${meetingRoomUid}")
        intent.putExtra("collectionName",colName)
        intent.putExtra("meeting_room_id", meetingRoomUid)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update_interest(){
        hobbydata.clear()
        for(data in SingleTonData.userInfo?.interest_array!!){
            hobbydata.add(data)
        }
        view?.hobby_recyclerview?.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    fun update() {
        viewModel.loadMeetingData()
    }
    fun updatelight() {
        viewModel.loadLightMeetingData()
    }
    fun updateplace(){
        viewModel.loadPlaceData()
    }
    fun updateCompetition(){
        viewModel.loadCompetitionData()
    }
    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor", "SetTextI18n")

    inner class HobbyImageIconAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_main2,parent,false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewholder=(holder as HobbyImageIconAdapter.CustomViewHolder).itemView
            viewholder.hobbyiconimage.setImageResource(getImageResult(hobbydata[position]))
            viewholder.iconimagetext.text=hobbydata[position]
            viewholder.hobbyimage.setOnClickListener(ButtonListener(hobbydata[position]))
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }
        override fun getItemCount(): Int {
            return hobbydata.size
        }
    }
    inner class ButtonListener(val hobby: String):View.OnClickListener{
        override fun onClick(v: View?) {

        }

    }
    override fun onDestroy() {
        Log.d(TAG,"디테일뷰종료")
        super.onDestroy()

    }
}