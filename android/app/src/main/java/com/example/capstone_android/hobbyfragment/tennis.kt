package com.example.capstone_android.hobbyfragment

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.widget.AppCompatImageButton
import com.example.capstone_android.R


@SuppressLint("ResourceAsColor")
class tennis(context: Context): AppCompatImageButton(context) {

    init{
        setImageResource(R.drawable.sport_tennis)
        scaleType=ScaleType.FIT_XY
        setBackgroundResource(R.color.transparent)
    }


}



