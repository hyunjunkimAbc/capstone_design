package com.example.capstone_android.MainHomeActivity

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.ActivityConciergeBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.*
import com.example.capstone_android.SearchResult.MainSearchView
import com.example.capstone_android.Util.MainMenuId
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.data.BannerItem
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.data.SignUpData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.itemlight.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ConciergeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityConciergeBinding.inflate(layoutInflater)
    }
    val db = Firebase.firestore
    var rootRef = Firebase.storage.reference
    private val viewModelLightingMeetingRoom : LightingViewModel by viewModels<LightingViewModel>()
    private val viewModelPeriodicMeetingRoom : PeriodicViewModel by viewModels<PeriodicViewModel>()
    private val viewModelPlaceRentalRoom : PlaceRentalViewModel by viewModels<PlaceRentalViewModel>()
    private val viewModelCompetitionRoom : CompetitionViewModel by viewModels<CompetitionViewModel>()
    private val collectionNameOfLightingMeetingRoom =
        "lighting_meeting_room"
    private val collectionNameOfPeriodicMeetingRoom =
        "periodic_meeting_room"
    private val collectionNameOfPlaceRental =
        "place_rental_room"
    private val collectionNameOfCompetition =
        "competition_room"
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewModel: BannerViewModel
    private lateinit var hobbydata:ArrayList<String>
    var address:String?=null
    var mainclubdata:ArrayList<ClubData> =arrayListOf()
    private var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainhomeprogressbar.visibility= View.GONE
        binding.homeUserAddress.setOnClickListener{
            val intent=Intent(this, AddressActivity::class.java)
            intent.putExtra("key","createuser")
            startActivityForResult(intent,10)
        }
        binding.Mainmeetingbutton.setOnClickListener{
            var intent= Intent(this, HomeActivity::class.java)
            intent.putExtra("openkey","periodic")
            startActivityForResult(intent,1)
        }
        binding.Lightningmeetingbutton.setOnClickListener{
            var intent= Intent(this, HomeActivity::class.java)
            intent.putExtra("openkey","light")
            startActivityForResult(intent,1)
        }
        binding.locationbutton.setOnClickListener{
            var intent= Intent(this, HomeActivity::class.java)
            intent.putExtra("openkey","place")
            startActivityForResult(intent,1)
        }
        binding.Competitionbutton.setOnClickListener{
            var intent= Intent(this, HomeActivity::class.java)
            intent.putExtra("openkey","competition")
            startActivityForResult(intent,1)
        }
        binding.mainsearchview.clearFocus()
        binding.mainsearchview.setOnQueryTextFocusChangeListener { _, hasExpaned ->
            when(hasExpaned) {
                true -> {
                    binding.mainsearchview.clearFocus()
                    var intent= Intent(this, MainSearchView::class.java)
                    startActivity(intent)
                }
                false ->{

                }
            }
        }
        setRecyclerView(LightingAdapter(viewModelLightingMeetingRoom),viewModelLightingMeetingRoom,binding.Lightningmeetingrecyclerview)
        setRecyclerView(PeriodicAdapter(viewModelPeriodicMeetingRoom),viewModelPeriodicMeetingRoom,binding.mainhomemeetingrecyclerview )
        setRecyclerView(PlaceRentalAdapter(viewModelPlaceRentalRoom),viewModelPlaceRentalRoom,binding.placeRantalRecyclerView )
        setRecyclerView(CompetitionAdapter(viewModelCompetitionRoom),viewModelCompetitionRoom,binding.CompetitionRecyclerView )


        addToRecyclerView(
            MeetingRoomDataManager.collectionNameOfLightingMeetingRoom,
            viewModelLightingMeetingRoom)

        addToRecyclerView(
            MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom,
            viewModelPeriodicMeetingRoom)
        addToRecyclerView(
            MeetingRoomDataManager.collectionNameOfPlaceRental,
            viewModelPlaceRentalRoom)
        addToRecyclerView(
            MeetingRoomDataManager.collectionNameOfCompetition,
            viewModelCompetitionRoom)
        setContentView(binding.root)
        binding.mainhomeRefreshLayout.setOnRefreshListener{
            binding.mainhomeRefreshLayout.isRefreshing = false
        }
        viewModel = ViewModelProvider(this)[BannerViewModel::class.java]
        viewModel.setBannerItems(
            listOf(BannerItem(R.drawable.banner1), BannerItem(R.drawable.banner2), BannerItem(R.drawable.banner3), BannerItem(
                R.drawable.banner4
            )))
        initViewPager2()
        subscribeObservers()
        autoScrollViewPager()
        address=SingleTonData.userInfo?.address
        viewModel.setuseraddress(address!!)

    }
    private fun initViewPager2() {
        binding.adviewpager.apply {
            viewPagerAdapter = ViewPagerAdapter()
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    isRunning=true
                    binding.bannertxtnumber.text = "${position+1}"
                    viewModel.setCurrentPosition(position)
                }
            })
        }
    }


    private fun subscribeObservers() {
        viewModel.useraddress.observe(this, Observer { userad->
            binding.homeUserAddress.text=userad
        })
        viewModel.bannerItemList.observe(this, Observer { bannerItemList ->
            viewPagerAdapter.submitList(bannerItemList)
        })
        viewModel.currentPosition.observe(this, Observer { currentPosition ->
            binding.adviewpager.currentItem = currentPosition
        })
    }
    private fun autoScrollViewPager() {
        lifecycleScope.launch {
            whenResumed {
                while (isRunning) {
                    delay(3000)
                    viewModel.getcurrentPosition()?.let {
                        viewModel.setCurrentPosition((it.plus(1)) % 4)
                    }
                }
            }
        }
    }
    private fun RoadData(){
        binding.mainhomeprogressbar.visibility= View.VISIBLE
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun ClearData(){

        //binding.constraintContainPrograss.removeView(binding.mainhomeprogressbar)
        //this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        println("remove 성공")
        binding.mainhomeprogressbar.visibility=View.GONE
        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        println("다ㅣ시시작")
    }

    fun inputDataAndStartActivity(collectionName:String){
        val intent = Intent(this, HomeActivity::class.java)
        //테스트라서 이렇게 했지만 나중에는 동경님 activity로 옮겨야 함
        intent.putExtra("collectionName", collectionName)
        startActivity(intent)//test
    }
    fun addToRecyclerView(collectionName: String,viewModel: ConciergeViewModel){
        var meetingRoomCollection=db.collection(collectionName)
        println("test-----------------------")
        println(collectionName)
        meetingRoomCollection.limit(10).get().addOnSuccessListener {
            for (meetingRoom in it){
                println(meetingRoom)
                viewModel.addItem(
                    MeetingRoom(
                        meetingRoom.data["title"] as String,meetingRoom.id,collectionName,meetingRoom.data["info_text"] as String,meetingRoom.data["imageUrl"] as String, applicationContext,
                        meetingRoom.data["category"] as String)
                )
                /*

                var meetingInfoImage = rootRef.child("${collectionName}/${meetingRoom.id}.jpg")
                meetingInfoImage.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                        viewModel.addItem(
                            MeetingRoom(bmp,
                            meetingRoom.data["title"] as String,meetingRoom.id,collectionName,meetingRoom.data["info_text"] as String,meetingRoom.data["imageUrl"] as String)
                        )
                    }else{
                        var ref = rootRef.child("${collectionName}/default.jpg")
                        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
                            if(it.isSuccessful){
                                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result.size)
                                viewModel.addItem(MeetingRoom(bmp,meetingRoom.data["title"] as String,meetingRoom.id,collectionName,meetingRoom.data["info_text"] as String,meetingRoom.data["imageUrl"] as String))
                            }else{
                                println("undefined err")
                            }
                        }
                    }
                }*/

            }
        }
    }
    fun setRecyclerView(adapter: ConciergeAdapter, viewModel: ConciergeViewModel, view: androidx.recyclerview.widget.RecyclerView){
        //val meetingMembersRecyclerView = v.findViewById<RecyclerView>(R.id.meetingMembersRecyclerView)
        val MeetingRecyclerView = view
        MeetingRecyclerView.adapter = adapter
        MeetingRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        MeetingRecyclerView.setHasFixedSize(true)

        viewModel.itemsListData.observe(this){
            adapter.notifyDataSetChanged()
        }
        viewModel.itemClickEvent.observe(this){
            //ItemDialog(it).show
            val i =viewModel.itemClickEvent.value
            println("---------------${viewModel.items[i!!].title}")
            val intent = Intent(this, MeetingRoomActivity::class.java)
            //테스트라서 이렇게 했지만 나중에는 동경님 activity로 옮겨야 함
            intent.putExtra("collectionName", viewModel.items[i!!].col_name)
            intent.putExtra("meeting_room_id",viewModel.items[i!!].document_id)
            startActivity(intent)
            //val bundle = bundleOf("document_id" to viewModelLightingMeetingRoom.items[i!!].document_id)
            //findNavController().navigate(R.id.action_meetingRoomPostingsFragment_to_showPostingFragment,bundle)
        }
        registerForContextMenu(MeetingRecyclerView)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        address=SingleTonData.userInfo?.address
        viewModel.setuseraddress(address!!)
    }
}