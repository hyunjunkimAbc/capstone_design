package com.example.capstone_android.hobbyfragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.example.capstone_android.R
import com.example.capstone_android.SelectHobbyActivity
import com.example.capstone_android.data.SignUpData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create.view.*
import kotlinx.android.synthetic.main.fragment_sportfragment.view.*

class SportFragment:Fragment() {
    lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_sportfragment, container, false)
        val thisactivity=activity as SelectHobbyActivity
        db = Firebase.firestore
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.basketballcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "농구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.soccercheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "축구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.tennischeckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "테니스")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.pingpongcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "탁구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.badmintoncheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "배드민턴")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.baseballcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "야구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.bawlingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "볼링")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.bicyclecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "자전거")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.golfcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "골프")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.runcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "런닝")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.swimcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "수영")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.volleyballcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "배구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.yogacheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "요가")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.taekunudocheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "태권|유도")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.boxcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "복싱")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.musulcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "무술")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.horsecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "승마")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.hellscheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "헬스")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.rollerboardcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "롤러|보드")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.skiboardcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "스키|보드")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.danggucheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "당구")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.hikecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "등산")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.leisurecheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "수상레저")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        } else {
            println("관심사 재설성문")

                    for (data in thisactivity.allhobbylist) {
                        if (data == "농구") {
                            view.basketballcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.basketballcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "축구") {
                            view.soccercheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.soccercheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "탁구") {
                            view.pingpongcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.pingpongcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "테니스") {
                            view.tennischeckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.tennischeckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "배드민턴") {
                            view.badmintoncheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.badmintoncheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "야구") {
                            view.baseballcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.baseballcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "볼링") {
                            view.bawlingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.bawlingcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "자전거") {
                            view.bicyclecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.bicyclecheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "골프") {
                            view.golfcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.golfcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "런닝") {
                            view.runcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.runcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "수영") {
                            view.swimcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.swimcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "배구") {
                            view.volleyballcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.volleyballcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "요가|필라테스") {
                            view.yogacheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.yogacheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "태권도|유도") {
                            view.taekunudocheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.taekunudocheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "복싱") {
                            view.boxcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.boxcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "무술") {
                            view.musulcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.musulcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "승마") {
                            view.horsecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.horsecheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "헬스") {
                            view.hellscheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.hellscheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "롤러|보드") {
                            view.rollerboardcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.rollerboardcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "스키|보드") {
                            view.skiboardcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.skiboardcheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "당구") {
                            view.danggucheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.danggucheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "등산") {
                            view.hikecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.hikecheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                        if (data == "수상레저") {
                            view.leisurecheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                            view.leisurecheckbox.setOnCheckedChangeListener{_,ischecked->
                                if(ischecked) thisactivity.myhobbylist.add(data)
                                else thisactivity.myhobbylist.remove(data)
                            }
                        }
                    }
                }

        return view
    }
}

