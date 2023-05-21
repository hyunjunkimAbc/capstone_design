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
import kotlinx.android.synthetic.main.fragment_jobfragment.view.*

class JobFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_jobfragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.itcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "IT")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.designercheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "디자인")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.medicinecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "의료")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.chemistrycheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "화학")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.financecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "금융")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.constructorcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "건설")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.lawyercheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "법")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.fashioncheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "패션")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for (data in thisactivity.allhobbylist) {
                if (data == "IT") {
                    view.itcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.itcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "디자인") {
                    view.designercheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.designercheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "의료") {
                    view.medicinecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.medicinecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "화학") {
                    view.chemistrycheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.chemistrycheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "금융") {
                    view.financecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.financecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "건설") {
                    view.constructorcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.constructorcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "법") {
                    view.lawyercheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.lawyercheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "패션") {
                    view.fashioncheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.fashioncheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}