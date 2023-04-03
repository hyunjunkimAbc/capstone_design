package com.example.capstone_android





import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_searchmap.view.*
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class SearchMapFragment:Fragment() {
    var bitmap: Bitmap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val name=arguments?.getString("name")
        val address=arguments?.getString("address")
        val noorLat=arguments?.getDouble("xdis")
        val noorLon=arguments?.getDouble("ydis")
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_searchmap,container,false)


        val mapView = MapView(activity)
        val mapViewContainer = view.map_view as ViewGroup
        mapViewContainer.addView(mapView)
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(noorLat!!, noorLon!!),true);
        mapView.setZoomLevel(1,true)



        view.confirm.setOnClickListener{
        val intent= Intent()
        intent.putExtra("address",address)
        intent.putExtra("name",name)
        intent.putExtra("disy",noorLon)
        intent.putExtra("disx",noorLat)
        activity?.setResult(RESULT_OK,intent)
        activity?.finish()
    }
        return view
    }

}