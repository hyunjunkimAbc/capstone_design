package com.example.capstone_android.hobbyfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone_android.R

class TripFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_tripfragment, container, false)
        return view
    }
}