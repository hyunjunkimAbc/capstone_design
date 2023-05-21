
package com.example.capstone_android.MeetingList

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.capstone_android.MeetingList.*
import com.example.capstone_android.MeetingRoomActivity
import com.example.capstone_android.MeetingRoomDataManager
import com.example.capstone_android.R
import com.example.capstone_android.Util.MainMenuId
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.Util.getImageResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.itemmapfragment.view.*
import org.apache.log4j.chainsaw.Main


class MapFragment : Fragment(),OnMapReadyCallback {
    var disx:String?=null
    var disy:String?=null
    lateinit var fusedLocation:FusedLocationProviderClient
    lateinit var db : FirebaseFirestore
    private lateinit var mapView: com.naver.maps.map.MapView
    var normalMarkers:ArrayList<Marker> = arrayListOf()
    val checkMarkers:ArrayList<Marker> = arrayListOf()
    private var openkey:String?=null
    private var infoWindow:InfoWindow?=null
    lateinit var navermap : NaverMap
    private lateinit var viewModel: MeetingViewModel
    private lateinit var mapRecyclerView:RecyclerView
    private lateinit var periodicmapadapter: ListperiodicMapAdapter
    private lateinit var lightmapadapter:ListLightMapAdapter
    private lateinit var placemapadapter:ListPlaceMapAdapter
    private lateinit var competitionmapadapter:ListCompetitionMapAdapter
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_map,container,false)

        return view
    }

    override fun onViewCreated(view: View,@Nullable  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openkey=arguments?.getString("openkey")
        infoWindow = InfoWindow()
        viewModel= ViewModelProvider(this)[MeetingViewModel::class.java]
        mapRecyclerView=view.map_recyclerview
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        naverMap.minZoom = 13.0
        naverMap.maxZoom = 18.0
        navermap=naverMap
        infoWindow?.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                // 정보 창이 열린 마커의 tag를 텍스트로 노출하도록 반환
                return infoWindow.marker?.tag as CharSequence? ?: ""
            }
        }

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(mapRecyclerView)

        if(openkey==MainMenuId.periodic){
            Log.d(TAG,"주기적모임 맵 호출")
            val mylayout:LayoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            mapRecyclerView.layoutManager=mylayout
            mapRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val snapPosition = recyclerView.layoutManager?.getPosition(snapView!!)
                        println("포지션 값은$snapPosition 입니다")
                        // 해당 포지션에 있는 마커 클릭 이벤트 발생시키기
                        // ...
                        if(checkMarkers.isNotEmpty()){
                            checkMarkers[0].zIndex=0
                            checkMarkers[0].isHideCollidedMarkers = true
                            checkMarkers[0].isForceShowIcon = false
                        }
                        checkMarkers.clear()
                        checkMarkers.add(normalMarkers[snapPosition!!])

                        checkMarkers.forEach{
                                marker ->
                            marker.zIndex=1
                            marker.isHideCollidedMarkers = true
                            marker.isForceShowIcon = true
                        }
                        val coord=LatLng(SingleTonData.clubmapdata[snapPosition].positionx!!,SingleTonData.clubmapdata[snapPosition].positiony!!)
                        val cameraUpdate= CameraUpdate.scrollTo(coord) .animate(CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)
                        infoWindow?.open(checkMarkers[0])
                    }
                }
            })
            periodicmapadapter= ListperiodicMapAdapter(emptyList())
            periodicmapadapter.setPeriodicMapItemClickListener(object:ListperiodicMapAdapter.PeriodicMapItemClickListener{
                override fun onClick(v: View, position: Int) {
                    Log.d(TAG,"장소 지도 아이템 클릭")
                    println("주기적 모임 클릭")
                    SingleTonData.clubmapdata[position].Uid?.let {
                        gotoMeetingRoomActivity(
                            MeetingRoomDataManager.collectionNameOfPeriodicMeetingRoom,
                            it
                        )
                    }

                }

            })
            viewModel.MeetingMapItemList.observe(viewLifecycleOwner) { data ->
                periodicmapadapter.submapitemlist(data)
                Log.d(TAG, SingleTonData.clubmapdata.toString())
                if(SingleTonData.clubmapdata.isNotEmpty()) {
                    val coord =
                        LatLng(SingleTonData.clubmapdata[0].positionx!!, SingleTonData.clubmapdata[0].positiony!!)
                    val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                    navermap.moveCamera(cameraUpdate)
                }
                SingleTonData.clubmapdata.forEachIndexed { index, it ->
                    val marker = Marker()
                    marker.position = LatLng(it.positionx!!, it.positiony!!)
                    marker.width = 150
                    marker.height = 150
                    marker.tag = it.title
                    marker.icon = OverlayImage.fromResource(getImageResult(it.category!!))
                    marker.map = navermap
                    marker.setOnClickListener { overlay ->
                        val markers = overlay as Marker
                        if (markers.infoWindow == null) {
                            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                            infoWindow?.open(markers)
                        } else {
                            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                            infoWindow?.close()
                        }
                        if (mapRecyclerView.adapter == null) {
                            mapRecyclerView.adapter = periodicmapadapter
                        }
                        mapRecyclerView.scrollToPosition(index)
                        val coord = LatLng(
                            SingleTonData.clubmapdata[index].positionx!!,
                            SingleTonData.clubmapdata[index].positiony!!
                        )
                        val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                        navermap.moveCamera(cameraUpdate)
                        true
                    }
                    normalMarkers.add(marker)
                }
            }
            addAllMarkers()
        }
        else if(openkey==MainMenuId.light){
            Log.d(TAG,"번개모임 맵 호출?")
            val mylayout:LayoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            mapRecyclerView.layoutManager=mylayout
            mapRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val snapPosition = recyclerView.layoutManager?.getPosition(snapView!!)
                        println("포지션 값은$snapPosition 입니다")
                        // 해당 포지션에 있는 마커 클릭 이벤트 발생시키기
                        // ...
                        if(checkMarkers.isNotEmpty()){
                            checkMarkers[0].zIndex=0
                            checkMarkers[0].isHideCollidedMarkers = true
                            checkMarkers[0].isForceShowIcon = false
                        }
                        checkMarkers.clear()
                        checkMarkers.add(normalMarkers[snapPosition!!])

                        checkMarkers.forEach{
                                marker ->
                            marker.zIndex=1
                            marker.isHideCollidedMarkers = true
                            marker.isForceShowIcon = true
                        }
                        val coord=LatLng(SingleTonData.lightmapdata[snapPosition].positionx!!,SingleTonData.lightmapdata[snapPosition].positiony!!)
                        val cameraUpdate= CameraUpdate.scrollTo(coord) .animate(CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)
                        infoWindow?.open(checkMarkers[0])
                    }
                }

            })
            lightmapadapter= ListLightMapAdapter(emptyList())
            lightmapadapter.setLightMapItemClickListener(object:ListLightMapAdapter.LightMapItemClickListener{
                override fun onClick(v: View, position: Int) {
                    Log.d(TAG,"장소 지도 아이템 클릭")
                    println("번개 모임 클릭")
                    SingleTonData.lightmapdata[position].uid?.let {
                        gotoMeetingRoomActivity(
                            MeetingRoomDataManager.collectionNameOfLightingMeetingRoom,
                            it
                        )
                    }

                }

            })
            viewModel.LightMeetingMapItemList.observe(viewLifecycleOwner) { data ->
                lightmapadapter.submaplightitem(data)
                if(SingleTonData.lightmapdata.isNotEmpty()) {
                    val coord =
                        LatLng(SingleTonData.lightmapdata[0].positionx!!, SingleTonData.lightmapdata[0].positiony!!)
                    val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                    navermap.moveCamera(cameraUpdate)
                }
                SingleTonData.lightmapdata.forEachIndexed { index, it ->
                    val marker = Marker()
                    marker.position = LatLng(it.positionx!!, it.positiony!!)
                    marker.width = 150
                    marker.height = 150
                    marker.tag = it.title
                    marker.icon = OverlayImage.fromResource(getImageResult(it.category!!))
                    marker.map = navermap
                    marker.setOnClickListener { overlay ->
                        val markers = overlay as Marker
                        if (markers.infoWindow == null) {
                            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                            infoWindow?.open(markers)
                        } else {
                            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                            infoWindow?.close()
                        }
                        if (mapRecyclerView.adapter == null) {
                            mapRecyclerView.adapter = lightmapadapter
                        }
                        mapRecyclerView.scrollToPosition(index)
                        val coord = LatLng(
                            SingleTonData.lightmapdata[index].positionx!!,
                            SingleTonData.lightmapdata[index].positiony!!
                        )
                        val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                        navermap.moveCamera(cameraUpdate)
                        true
                    }
                    normalMarkers.add(marker)
                }
            }
            addAllMarkers()
        }
        else if(MainMenuId.place==openkey){
            Log.d(TAG,"장소대여 맵 호출?")
            val mylayout:LayoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            mapRecyclerView.layoutManager=mylayout
            mapRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val snapPosition = recyclerView.layoutManager?.getPosition(snapView!!)
                        println("포지션 값은$snapPosition 입니다")
                        // 해당 포지션에 있는 마커 클릭 이벤트 발생시키기
                        // ...
                        if(checkMarkers.isNotEmpty()){
                            checkMarkers[0].zIndex=0
                            checkMarkers[0].isHideCollidedMarkers = true
                            checkMarkers[0].isForceShowIcon = false
                        }
                        checkMarkers.clear()
                        checkMarkers.add(normalMarkers[snapPosition!!])

                        checkMarkers.forEach{
                                marker ->
                            marker.zIndex=1
                            marker.isHideCollidedMarkers = true
                            marker.isForceShowIcon = true
                        }
                        val coord=LatLng(SingleTonData.placemapdata[snapPosition].positionx!!,SingleTonData.placemapdata[snapPosition].positiony!!)
                        val cameraUpdate= CameraUpdate.scrollTo(coord) .animate(CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)
                        infoWindow?.open(checkMarkers[0])
                    }
                }

            })
            placemapadapter=ListPlaceMapAdapter(emptyList())
            placemapadapter.setPlaceItemClickListener(object:ListPlaceMapAdapter.PlaceMapItemClickListener{
                override fun onClick(v: View, position: Int) {
                     Log.d(TAG,"장소 지도 아이템 클릭")
                    SingleTonData.placemapdata[position].Uid?.let {
                        gotoMeetingRoomActivity(
                            MeetingRoomDataManager.collectionNameOfPlaceRental,
                            it
                        )
                    }

                }

            })
            viewModel.PlaceMapItemList.observe(viewLifecycleOwner){data->
                placemapadapter.subplacemap(data)
                if(SingleTonData.placemapdata.isNotEmpty()) {
                    val coord =
                        LatLng(SingleTonData.placemapdata[0].positionx!!, SingleTonData.placemapdata[0].positiony!!)
                    val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                    navermap.moveCamera(cameraUpdate)
                }
                SingleTonData.placemapdata.forEachIndexed { index, it ->
                    val marker = Marker()
                    marker.position = LatLng(it.positionx!!, it.positiony!!)
                    marker.width = 150
                    marker.height = 150
                    marker.tag = it.title
                    marker.icon = OverlayImage.fromResource(getImageResult(it.category!!))
                    marker.map = navermap
                    marker.setOnClickListener { overlay ->
                        val markers = overlay as Marker
                        if (markers.infoWindow == null) {
                            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                            infoWindow?.open(markers)
                        } else {
                            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                            infoWindow?.close()
                        }
                        if (mapRecyclerView.adapter == null) {
                            mapRecyclerView.adapter = placemapadapter
                        }
                        mapRecyclerView.scrollToPosition(index)
                        val coord = LatLng(
                            SingleTonData.placemapdata[index].positionx!!,
                            SingleTonData.placemapdata[index].positiony!!
                        )
                        val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                        navermap.moveCamera(cameraUpdate)
                        true
                    }
                    normalMarkers.add(marker)
                }
            }
            addAllMarkers()
        }
        else if(MainMenuId.competition==openkey){
            Log.d(TAG,"대회 맵 호출?")
            val mylayout:LayoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            mapRecyclerView.layoutManager=mylayout
            mapRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val snapPosition = recyclerView.layoutManager?.getPosition(snapView!!)
                        println("포지션 값은$snapPosition 입니다")
                        // 해당 포지션에 있는 마커 클릭 이벤트 발생시키기
                        // ...
                        if(checkMarkers.isNotEmpty()){
                            checkMarkers[0].zIndex=0
                            checkMarkers[0].isHideCollidedMarkers = true
                            checkMarkers[0].isForceShowIcon = false
                        }
                        checkMarkers.clear()
                        checkMarkers.add(normalMarkers[snapPosition!!])

                        checkMarkers.forEach{
                                marker ->
                            marker.zIndex=1
                            marker.isHideCollidedMarkers = true
                            marker.isForceShowIcon = true
                        }
                        val coord=LatLng(SingleTonData.competitionmapdata[snapPosition].positionx!!,SingleTonData.competitionmapdata[snapPosition].positiony!!)
                        val cameraUpdate= CameraUpdate.scrollTo(coord) .animate(CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)
                        infoWindow?.open(checkMarkers[0])
                    }
                }

            })
            competitionmapadapter=ListCompetitionMapAdapter(emptyList())
            competitionmapadapter.setCompetitionMapItemClickListener(object:ListCompetitionMapAdapter.CompetitionMapItemClickListener{
                override fun onClick(v: View, position: Int) {
                    Log.d(TAG,"대회 아이템 클릭")
                    SingleTonData.competitionmapdata[position].Uid?.let {
                        gotoMeetingRoomActivity(
                            MeetingRoomDataManager.collectionNameOfCompetition,
                            it
                        )
                    }
                }
            })
            viewModel.CompetitionMapItemList.observe(viewLifecycleOwner){data->
                competitionmapadapter.subCompetitionMapList(data)
                if(SingleTonData.competitionmapdata.isNotEmpty()) {
                    val coord =
                        LatLng(SingleTonData.competitionmapdata[0].positionx!!, SingleTonData.competitionmapdata[0].positiony!!)
                    val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                    navermap.moveCamera(cameraUpdate)
                }
                SingleTonData.competitionmapdata.forEachIndexed { index, it ->
                    val marker = Marker()
                    marker.position = LatLng(it.positionx!!, it.positiony!!)
                    marker.width = 150
                    marker.height = 150
                    marker.tag = it.title
                    marker.icon = OverlayImage.fromResource(getImageResult(it.category!!))
                    marker.map = navermap
                    marker.setOnClickListener { overlay ->
                        val markers = overlay as Marker
                        if (markers.infoWindow == null) {
                            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                            infoWindow?.open(markers)
                        } else {
                            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                            infoWindow?.close()
                        }
                        if (mapRecyclerView.adapter == null) {
                            mapRecyclerView.adapter = competitionmapadapter
                        }
                        mapRecyclerView.scrollToPosition(index)
                        val coord = LatLng(
                            SingleTonData.competitionmapdata[index].positionx!!,
                            SingleTonData.competitionmapdata[index].positiony!!
                        )
                        val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                        navermap.moveCamera(cameraUpdate)
                        true
                    }
                    normalMarkers.add(marker)
                }
            }
            addAllMarkers()
        }

        naverMap.setOnMapClickListener{_,_->
            infoWindow?.close()
            mapRecyclerView.adapter=null
        }

        //데이터가 있따면 마커1의 위치로 지도위치 초기화



    }
    fun gotoMeetingRoomActivity(colName:String,meetingRoomUid:String){
        println("5 21 collectionName ${colName} meeting_room_id ${meetingRoomUid}")
        var intent= Intent(context, MeetingRoomActivity::class.java)
        intent.putExtra("collectionName",colName)
        intent.putExtra("meeting_room_id", meetingRoomUid)
        startActivity(intent)
    }
     fun addAllMarkers(){
        if(openkey==MainMenuId.periodic) {
            mapRecyclerView.adapter=null
            viewModel.loadMeetingMapData()
        }
        else if(openkey==MainMenuId.light){
            mapRecyclerView.adapter=null
            viewModel.loadLightMeetingMapData()
        }
         else if(openkey==MainMenuId.place){
            mapRecyclerView.adapter=null
            viewModel.loadPlaceMapData()
        }
         else if(openkey==MainMenuId.competition){
            mapRecyclerView.adapter=null
            viewModel.loadCompetitionMapData()
        }

    }
     fun removeAllMarkers(){
        for(marker in normalMarkers){
            marker.map=null
        }
        normalMarkers.clear()
        checkMarkers.clear()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"지도 디스트로이 발동")
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}
