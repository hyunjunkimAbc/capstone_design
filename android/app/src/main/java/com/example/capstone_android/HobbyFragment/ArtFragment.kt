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
import kotlinx.android.synthetic.main.fragment_artfragment.view.*
import kotlinx.android.synthetic.main.fragment_petfragment.view.*

class ArtFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_artfragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.artcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "미술")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.braceletcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "공방")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.porcelaincheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "도예")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.crosstichcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "자수")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.flowercheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "꽃")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.cosmeticscheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "화장품")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.furniturecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "가구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else {
            for (data in thisactivity.allhobbylist) {
                if (data == "미술") {
                    view.artcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.artcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "공방") {
                    view.braceletcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.braceletcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "도예") {
                    view.porcelaincheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.porcelaincheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "자수") {
                    view.crosstichcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.crosstichcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "꽃") {
                    view.flowercheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.flowercheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "화장품") {
                    view.cosmeticscheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.cosmeticscheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "가구") {
                    view.furniturecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.furniturecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}