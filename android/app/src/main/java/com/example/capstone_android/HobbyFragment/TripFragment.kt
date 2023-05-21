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
import kotlinx.android.synthetic.main.fragment_sportfragment.view.*
import kotlinx.android.synthetic.main.fragment_tripfragment.view.*

class TripFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_tripfragment, container, false)
        val thisactivity=activity as SelectHobbyActivity
        val test=arguments?.getString("key")
        if (test.equals("select")){
            view.worldtravelcheckbox.setOnClickListener{
                val intent = Intent()
                intent.putExtra("hobby", "세계여행")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.domestictravelcheckbox.setOnClickListener{
                val intent=Intent()
                intent.putExtra("hobby","국내여행")
                activity?.setResult(Activity.RESULT_OK,intent)
                activity?.finish()
            }
        }
        else{
            //관심사 재설정문
            for(data in thisactivity.allhobbylist){
                if(data=="세계여행"){
                    view.worldtravelcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.worldtravelcheckbox.setOnCheckedChangeListener{ _, ischecked->
                        if(ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if(data=="국내여행"){
                    view.domestictravelcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.domestictravelcheckbox.setOnCheckedChangeListener{_,ischecked->
                        if(ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}