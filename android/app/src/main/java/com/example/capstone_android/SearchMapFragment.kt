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
import com.skt.Tmap.TMapMarkerItem
import com.skt.Tmap.TMapPoint
import com.skt.Tmap.TMapView
import kotlinx.android.synthetic.main.fragment_searchmap.view.*


class SearchMapFragment:Fragment() {
    var bitmap: Bitmap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val thisactivity=activity as SearchMap
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_searchmap,container,false)
        val tmapview:TMapView= TMapView(activity)
        val name=arguments?.getString("name")
        val address=arguments?.getString("address")
        val noorLat=arguments?.getDouble("xdis")
        val noorLon=arguments?.getDouble("ydis")
        println(noorLat)
        tmapview.setSKTMapApiKey("VwYv1tFJtY1v9qhvVmkP92XdfO8UF8Kj3Hu83jRL")

        view.map_view.addView(tmapview)
        tmapview.setCenterPoint(noorLon!!, noorLat!!)


        val tMapPoint1: TMapPoint= TMapPoint(noorLat, noorLon) // SKT타워

        val marker = TMapMarkerItem()

        val mBitmap = getBitmapFromVectorDrawable(activity as SearchMap, R.drawable.marker)

        marker.canShowCallout=true
        marker.autoCalloutVisible=true
        marker.calloutTitle=name
        marker.icon=mBitmap

        marker.tMapPoint = tMapPoint1 // 마커의 좌표 지정
        tmapview.zoomLevel=17
        tmapview.addMarkerItem("marker",marker)



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
    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}