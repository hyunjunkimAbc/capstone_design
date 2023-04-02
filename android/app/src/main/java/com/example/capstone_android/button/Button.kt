package com.example.capstone_android.button

import android.content.Context

import android.widget.GridLayout
import androidx.appcompat.widget.AppCompatButton

import com.example.capstone_android.R


class BadmintonButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        this.layoutParams=param
        param.marginStart=10
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_badminton,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="배드민턴"
    }
}
class SoccerButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_soccer,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="축구"
    }
}
class BaseballButton (context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_baseball,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="야구"
    }
}
class TennisButton(context: Context):AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_tennis,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="테니스"
    }
}
class PingpongButton (context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_pingpong,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="탁구"
    }
}
class BasketballButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_basketball,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="농구"
    }
}
class BowlingButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_bowling,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="볼링"
    }
}
class BicycleButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_bicycle,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="자전거"
    }
}
class GolfButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_golf,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="골프"
    }
}
class RunningButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_running,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="런닝"
    }
}
class SwimButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_swim,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="수영"
    }
}
class VolleyballButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_volleyball,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="배구"
    }
}
class YogaButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_yoga,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="요가|필라테스"
    }
}
class TaekwondoButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_taekwonudo,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="태권|유도"
    }
}
class BoxButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_box,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="복싱"
    }
}
class MusulButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_musul,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="무술"
    }
}
class HorseButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_horse,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="승마"
    }
}
class HellsButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_hells,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="헬스"
    }
}
class RollerboardButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_rollerboard,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="롤러|보드"
    }
}
class SkiboardButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_skiboard,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="스키|보드"
    }
}
class DangguButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_danggu,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="당구"
    }
}
class HikingButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_hiking,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="등산"
    }
}
class LeisureButton(context: Context): AppCompatButton(context) {
    init{
        val param= GridLayout.LayoutParams()
        param.marginStart=10
        this.layoutParams=param
        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sport_leisure,0,0)
        setBackgroundResource(R.drawable.shape_for_circle_button)
        text="수상|레저"
    }
}