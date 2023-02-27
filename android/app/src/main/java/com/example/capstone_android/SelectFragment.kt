package com.example.capstone_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone_android.R

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create.view.*
import kotlinx.android.synthetic.main.fragment_selecthobby.view.*

class SelectFragment:Fragment() {
    lateinit var db : FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_selecthobby,container,false)
        val mActivity2 = activity as CreateActivity
        db= Firebase.firestore
        view.sportsImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"운동")
        }
        view.tripImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"여행")
        }
        view.musicImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"음악")
        }
        view.jobImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"사교")
        }
        view.readImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"독서")
        }
        view.cookImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"요리")
        }
        view.photoImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"사진")
        }
        view.gameImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"게임")
        }
        view.danceImageButton.setOnClickListener{
            mActivity2.changeFragment(2,"댄스")
        }
        view.carImageButton.setOnClickListener {
            mActivity2.changeFragment(2,"자동차")
        }
        view.petImageButton.setOnClickListener {
            mActivity2.changeFragment(2,"애완동물")
        }
        view.artImageButton.setOnClickListener {
            mActivity2.changeFragment(2,"공예")
        }
        view.volunteerImageButton.setOnClickListener {
            mActivity2.changeFragment(2,"봉사활동")
        }
        view.studyImageButton.setOnClickListener {
            mActivity2.changeFragment(2,"스터디그룹")
        }

        return view
    }
}