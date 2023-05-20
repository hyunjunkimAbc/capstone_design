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
import kotlinx.android.synthetic.main.fragment_tripfragment.view.*

class CookFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_cookfragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")){
            view.koreafoodcheckbox.setOnClickListener{
                val intent = Intent()
                intent.putExtra("hobby", "한식")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.japancheckbox.setOnClickListener{
                val intent=Intent()
                intent.putExtra("hobby","일식")
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
            }
            view.chinafoodcheckbox.setOnClickListener{
                val intent=Intent()
                intent.putExtra("hobby","중식")
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
            }
            view.usfoodcheckbox.setOnClickListener{
                val intent=Intent()
                intent.putExtra("hobby","양식")
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
            }
            view.bakingcheckbox.setOnClickListener{
                val intent=Intent()
                intent.putExtra("hobby","제과제빵")
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
            }
            view.cocktailcheckbox.setOnClickListener{
                val intent=Intent()
                intent.putExtra("hobby","칵테일")
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
            }
            view.winecheckbox.setOnClickListener{
                val intent=Intent()
                intent.putExtra("hobby","와인")
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
            }
        }
        else{
            for(data in thisactivity.allhobbylist) {
                if (data == "한식") {
                    view.koreafoodcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.koreafoodcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "일식") {
                    view.japancheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.japancheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "중식") {
                    view.chinafoodcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.chinafoodcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "양식") {
                    view.usfoodcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.usfoodcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "제과제빵") {
                    view.bakingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.bakingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "칵테일") {
                    view.cocktailcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.cocktailcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "와인") {
                    view.winecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.winecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}