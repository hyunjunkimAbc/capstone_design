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
import kotlinx.android.synthetic.main.fragment_petfragment.view.*

class PetFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_petfragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.dogcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "강아지")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.catcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "고양이")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.hamstercheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "햄스터")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.hedgehogcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "고슴도치")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.fishcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "물고기")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.parrotcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "앵무새")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.squirrelcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "다람쥐")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.lizardcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "도마뱀")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.snakecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "뱀")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.tarantualcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "거미")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for(data in thisactivity.allhobbylist) {
                if (data == "강아지") {
                    view.dogcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.dogcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "고양이") {
                    view.catcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.catcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "고슴도치") {
                    view.hedgehogcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.hedgehogcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "햄스터") {
                    view.hamstercheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.hamstercheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "물고기") {
                    view.fishcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.fishcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "앵무새") {
                    view.parrotcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.parrotcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "다람쥐") {
                    view.squirrelcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.squirrelcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "도마뱀") {
                    view.lizardcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.lizardcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "뱀") {
                    view.snakecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.snakecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "거미") {
                    view.tarantualcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.tarantualcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}