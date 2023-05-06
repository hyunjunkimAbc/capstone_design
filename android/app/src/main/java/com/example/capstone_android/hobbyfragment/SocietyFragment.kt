package com.example.capstone_android.hobbyfragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone_android.R
import com.example.capstone_android.SelectHobbyActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_musicfragment.view.*
import kotlinx.android.synthetic.main.fragment_societyfragment.view.*

class SocietyFragment:Fragment() {
    lateinit var db: FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_societyfragment, container, false)
        val thisactivity=activity as SelectHobbyActivity
        db = Firebase.firestore
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.friendcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "친구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.cafecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "카페")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.beercheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "술 한잔")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.coinsongcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "코노")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.foodcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "맛집탐방")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for (data in thisactivity.allhobbylist) {
                if (data == "친구") {
                    view.friendcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.friendcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "카페") {
                    view.cafecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.cafecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "술 한잔") {
                    view.beercheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.beercheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "코노") {
                    view.coinsongcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.coinsongcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "맛집탐방") {
                    view.foodcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.foodcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
       return view
    }
}