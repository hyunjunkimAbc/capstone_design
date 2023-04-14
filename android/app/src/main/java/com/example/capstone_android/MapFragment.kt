package com.example.capstone_android

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.Util.getImageResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.itemmapfragment.view.*


class MapFragment : Fragment(),OnMapReadyCallback {
    var disx:String?=null
    var disy:String?=null
    lateinit var fusedLocation:FusedLocationProviderClient
    lateinit var db : FirebaseFirestore
    private lateinit var mapView: com.naver.maps.map.MapView
    var normalMarkers:ArrayList<Marker> = arrayListOf()
    val checkMarkers:ArrayList<Marker> = arrayListOf()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_map,container,false)

        return view
    }

    override fun onViewCreated(view: View,@Nullable  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {

        val infoWindow = InfoWindow()
        naverMap.setOnMapClickListener { point, coord ->
           println("지도가 클릭되고있씁니다")
        }
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(view?.map_recyclerview)


        val mylayout:LayoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
        view?.map_recyclerview?.layoutManager=mylayout
        view?.map_recyclerview?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
                    val snapPosition = recyclerView.layoutManager?.getPosition(snapView!!)
                    println("포지션 값은$snapPosition")
                    // 해당 포지션에 있는 마커 클릭 이벤트 발생시키기
                    // ...
                    checkMarkers.clear()
                    checkMarkers.add(normalMarkers[snapPosition!!])

                    checkMarkers.forEach{
                            marker ->
                        marker.zIndex=marker.zIndex+1
                        marker.isHideCollidedMarkers = true
                        marker.isForceShowIcon = true
                    }
                    val coord=LatLng(SingleTonData.clubdata[snapPosition].positionx!!,SingleTonData.clubdata[snapPosition].positiony!!)
                    val cameraUpdate= CameraUpdate.scrollTo(coord) .animate(CameraAnimation.Easing)
                    naverMap.moveCamera(cameraUpdate)
                    infoWindow.open(checkMarkers[0])
                }
            }
        })


        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                // 정보 창이 열린 마커의 tag를 텍스트로 노출하도록 반환
                return infoWindow.marker?.tag as CharSequence? ?: ""
            }
        }
     naverMap.setOnMapClickListener{_,_->
         infoWindow.close()
         view?.map_recyclerview?.adapter=null
     }



        val coord=LatLng(SingleTonData.clubdata[0].positionx!!,SingleTonData.clubdata[0].positiony!!)
        val cameraUpdate= CameraUpdate.scrollTo(coord) .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

        SingleTonData.clubdata.forEachIndexed{index,it->
            val marker=Marker()
            marker.position=LatLng(it.positionx!!,it.positiony!!)
            marker.width=150
            marker.height=150
            marker.tag=it.title
            marker.icon= OverlayImage.fromResource(getImageResult(it.category!!))
            marker.map=naverMap
            marker.setOnClickListener { overlay->
                val markers = overlay as Marker
                if (markers.infoWindow == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(markers)
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close()
                }
                if(  view?.map_recyclerview?.adapter==null)
                    view?.map_recyclerview?.adapter=MapViewRecyclerViewAdapter()
                view?.map_recyclerview?.scrollToPosition(index)
                println(index)
                true
            }
            normalMarkers.add(marker)

        }

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
    inner class MapViewRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.itemmapfragment,parent,false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewholder=(holder as CustomViewHolder).itemView
            Glide.with(holder.itemView.context).load(SingleTonData.clubdata[position].imageUrl).into(viewholder.imageViewmap)
            viewholder.explainmap.text=SingleTonData.clubdata[position].info_text
            viewholder.clubtitle.text=SingleTonData.clubdata[position].title
            viewholder.mapcategory.text=SingleTonData.clubdata[position].category
        }

        override fun getItemCount(): Int {
           return SingleTonData.clubdata.size
        }

    }

}