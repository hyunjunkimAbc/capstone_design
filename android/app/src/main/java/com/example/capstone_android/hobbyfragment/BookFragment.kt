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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_bookfragment.view.*
import kotlinx.android.synthetic.main.fragment_musicfragment.view.*

class BookFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_bookfragment, container, false)
        val thisactivity=activity as SelectHobbyActivity
        val test = arguments?.getString("key")
        if (test.equals("select")) {
            view.readingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "독서")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.writingcheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "글쓰기")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            view.discussioncheckbox.setOnClickListener {
                val intent = Intent()
                intent.putExtra("hobby", "토론")
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
        else{
            for (data in thisactivity.allhobbylist) {
                if (data == "독서") {
                    view.readingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.readingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "글쓰기") {
                    view.writingcheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.writingcheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
                if (data == "토론") {
                    view.discussioncheckbox.isChecked = thisactivity.myhobbylist.contains(data)
                    view.discussioncheckbox.setOnCheckedChangeListener { _, ischecked ->
                        if (ischecked) thisactivity.myhobbylist.add(data)
                        else thisactivity.myhobbylist.remove(data)
                    }
                }
            }
        }
        return view
    }
}