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
import kotlinx.android.synthetic.main.fragment_gamefragment.view.*

class GameFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_gamefragment, container, false)
        val thisactivity = activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.aoscheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "AOS")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.rpgcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "RPG")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.fpscheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "FPS")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.cardgamecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "카드게임")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.braingamecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "두뇌심리")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.sportgamecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "스포츠게임")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.racinggamecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "레이싱게임")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.nintendocheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "닌텐도|플스")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for(data in thisactivity.allhobbylist) {
                if (data == "AOS") {
                    view.aoscheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.aoscheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "RPG") {
                    view.rpgcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.rpgcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "FPS") {
                    view.fpscheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.fpscheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "카드게임") {
                    view.cardgamecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.cardgamecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "두뇌심리") {
                    view.braingamecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.braingamecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "스포츠게임") {
                    view.sportgamecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.sportgamecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "레이싱게임") {
                    view.racinggamecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.racinggamecheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "닌텐도|플스") {
                    view.nintendocheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.nintendocheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}