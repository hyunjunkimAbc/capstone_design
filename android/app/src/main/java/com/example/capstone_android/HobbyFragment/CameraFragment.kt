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
import kotlinx.android.synthetic.main.fragment_camerafragment.view.*
import kotlinx.android.synthetic.main.fragment_cookfragment.view.*

class CameraFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_camerafragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.cameracheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "사진")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.editingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "영상제작")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for(data in thisactivity.allhobbylist) {
                if (data == "사진") {
                    view.cameracheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.cameracheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "영상제작") {
                    view.editingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.editingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}