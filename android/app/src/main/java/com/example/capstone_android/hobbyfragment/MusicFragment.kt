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
import kotlinx.android.synthetic.main.fragment_sportfragment.view.*

class MusicFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_musicfragment, container, false)
        val thisactivity=activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.bandcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "밴드")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.pianocheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "피아노")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.drumcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "드럼")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.violincheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "바이올린")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.guitarcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "기타")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.singcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "노래")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.musicwritercheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "작곡")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.hiphopcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "힙합")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.buskingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "버스킹")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.concertcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "콘서트")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.djingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "디제잉")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.launchpadcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "런치패드")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.saxophonecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "색소폰")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            println("이거맞지?")
            for (data in thisactivity.allhobbylist) {
                if (data == "밴드") {
                    view.bandcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.bandcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "피아노") {
                    view.pianocheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.pianocheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "드럼") {
                    view.drumcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.drumcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "바이올린") {
                    view.violincheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.violincheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "기타") {
                    view.guitarcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.guitarcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "노래") {
                    view.singcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.singcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "작곡") {
                    view.musicwritercheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.musicwritercheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "힙합") {
                    view.hiphopcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.hiphopcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "버스킹") {
                    view.buskingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.buskingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "콘서트") {
                    view.concertcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.concertcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "디제잉") {
                    view.djingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.djingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "런치패드") {
                    view.launchpadcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.launchpadcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "색소폰") {
                    view.saxophonecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.saxophonecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}