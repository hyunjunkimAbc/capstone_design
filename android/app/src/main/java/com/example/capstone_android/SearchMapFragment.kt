package com.example.capstone_android

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_searchmap.view.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView



class SearchMapFragment:Fragment() {

    var eventListener = MarkerEventListener()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_searchmap,container,false)
        val mapview=MapView(activity)
        val mapViewContainer = view.map_view as ViewGroup
        mapViewContainer.addView(mapview)
        val address=arguments?.getString("address")
        val disy=arguments?.getDouble("ydis")
        val disx=arguments?.getDouble("xdis")
        val name=arguments?.getString("name")
        mapview.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(disy!!, disx!!),true);
        mapview.setZoomLevel(1,true)
        val point= MapPOIItem()
        point.itemName=name
        point.mapPoint=MapPoint.mapPointWithGeoCoord(disy,disx)
        point.markerType=MapPOIItem.MarkerType.BluePin
        point.selectedMarkerType=MapPOIItem.MarkerType.RedPin
        mapview.addPOIItem(point)
        mapview.setPOIItemEventListener(eventListener)

        view.confirm.setOnClickListener{
            val intent= Intent()
            val addresslong=address?.split(" ")
            val bigadderss= addresslong?.get(0).plus(" ").plus(addresslong?.get(1))
            intent.putExtra("address",bigadderss)
            intent.putExtra("name",name)
            intent.putExtra("disy",disy)
            intent.putExtra("disx",disx)
            activity?.setResult(RESULT_OK,intent)
            activity?.finish()
            println(bigadderss)
            println("정상종료")
        }
        return view
    }
    class MarkerEventListener(): MapView.POIItemEventListener {
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            //println("마커클릭")
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
            println("마커클릭")

        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {

        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

        }
    }

}