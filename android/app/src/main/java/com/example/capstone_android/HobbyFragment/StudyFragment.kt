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
import kotlinx.android.synthetic.main.fragment_petfragment.view.*
import kotlinx.android.synthetic.main.fragment_studyfragment.view.*

class StudyFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_studyfragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.studycheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "스터디")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.languagescheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "언어")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.motivatedcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "동기부여")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.speechcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "스피치")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for(data in thisactivity.allhobbylist) {
                if (data == "스터디") {
                    view.studycheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.studycheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "언어") {
                    view.languagescheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.languagescheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "동기부여") {
                    view.motivatedcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.motivatedcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "스피치") {
                    view.speechcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.speechcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}