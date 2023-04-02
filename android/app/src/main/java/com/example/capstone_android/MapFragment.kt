package com.example.capstone_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import com.skt.Tmap.TMapView
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.fragment_map.view.map_view
import kotlinx.android.synthetic.main.fragment_searchmap.view.*


class MapFragment : Fragment() {
    var disx:String?=null
    var disy:String?=null
    lateinit var fusedLocation:FusedLocationProviderClient
    lateinit var db : FirebaseFirestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_map,container,false)
        val tmapview: TMapView = TMapView(activity)
        tmapview.setSKTMapApiKey("VwYv1tFJtY1v9qhvVmkP92XdfO8UF8Kj3Hu83jRL")
        view.map_view.addView(tmapview)
        return view
    }




    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onLowMemory() {
        super.onLowMemory()

    }
}