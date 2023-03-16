package com.example.capstone_android

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.MapView
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class MapFragment : Fragment() {
    lateinit var fusedLocation:FusedLocationProviderClient
    var mapview: MapView?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_map,container,false)
        mapview=view.map_fragment
        mapview?.onCreate(savedInstanceState)
        val geocoder=Geocoder(requireContext())
        var HActivity = activity as HomeActivity
        fusedLocation= LocationServices.getFusedLocationProviderClient(HActivity)


        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            Toast.makeText(activity, "위치 권한을 허용해주세요", Toast.LENGTH_LONG).show()
            }
        else{

        }

        return view
    }




    override fun onStart() {
        super.onStart()
        mapview?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapview?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapview?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapview?.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapview?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapview?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapview?.onLowMemory()
    }
}