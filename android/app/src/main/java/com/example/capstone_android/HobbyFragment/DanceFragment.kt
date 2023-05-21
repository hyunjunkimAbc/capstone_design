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
import kotlinx.android.synthetic.main.fragment_cookfragment.view.*
import kotlinx.android.synthetic.main.fragment_dancefragment.view.*

class DanceFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_dancefragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.poppingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "팝핀")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.rockingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "락킹")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.bboyingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "비보잉")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.wacckingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "왁킹")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.hiphopdancecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "힙합댄스")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.housedancecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "하우스")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.moderndancecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "현대무용")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.koreadancecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "한국무용")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.kpopcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "K-POP")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.valletcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "발레")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.dancesportcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "댄스스포츠")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.jazzdancecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "재즈")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.hulacheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "발리댄스")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.jazzdancecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "재즈")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.aerobicscheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "에어로빅")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for(data in thisactivity.allhobbylist) {
                if (data == "팝핀") {
                    view.poppingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.poppingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "락킹") {
                    view.rockingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.rockingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "비보잉") {
                    view.bboyingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.bboyingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "왁킹") {
                    view.wacckingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.wacckingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "힙합댄스") {
                    view.hiphopdancecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.hiphopdancecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "하우스") {
                    view.housedancecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.housedancecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "크럼프") {
                    view.krumpcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.krumpcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "현대무용") {
                    view.moderndancecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.moderndancecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "한국무용") {
                    view.koreadancecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.koreadancecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "K-POP") {
                    view.kpopcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.kpopcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "발레") {
                    view.valletcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.valletcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "댄스스포츠") {
                    view.dancesportcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.dancesportcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "발리댄스") {
                    view.hulacheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.hulacheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "재즈") {
                    view.jazzdancecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.jazzdancecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "에어로빅") {
                    view.aerobicscheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.aerobicscheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}