package com.example.capstone_android.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.example.capstone_android.R


class button(context: Context) : AppCompatImageButton(context) {
    override fun setImageResource(resId: Int) {
        super.setImageResource(R.drawable.icon_photo)
    }

    override fun setBackground(background: Drawable?) {
        super.setBackgroundResource(R.drawable.shape_for_circle_button)
    }
}