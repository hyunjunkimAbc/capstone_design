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
import kotlinx.android.synthetic.main.fragment_carfragment.view.*
import kotlinx.android.synthetic.main.fragment_cookfragment.view.*

class CarFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_carfragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.carcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "자동차")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.motorbikecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "오토바이")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for(data in thisactivity.allhobbylist) {
                if (data == "자동차") {
                    view.carcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.carcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "오토바이") {
                    view.motorbikecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.motorbikecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}