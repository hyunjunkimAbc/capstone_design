package com.example.capstone_android.Util

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.example.capstone_android.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun String?.isJsonObject():Boolean{
    return this?.startsWith("{")==true && this.endsWith("}")
}

fun String?.isJsonArray():Boolean{
    return this?.startsWith("[")==true && this.endsWith("]")
}


fun EditText.textChangesToFlow(): Flow<CharSequence?>{


    return callbackFlow<CharSequence?> {
        val listener=object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
              Unit
            }
            override fun afterTextChanged(s: Editable?) {
                Unit
            }
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG,"onTextChanged() /textChangeToFlow() 에 달려있는 텍스트 와쳐 /text : $text")
                //들어온 텍스트 값 내보내기
                trySend(text).isSuccess
            }
        }
        //edittext에 작성한 리스너 달아주기
        addTextChangedListener(listener)
        //콜백이 사라질(삭제) 때 실행됨
        awaitClose{
            Log.d(TAG,"textChangeToFlow() await 실행")
            removeTextChangedListener(listener)
        }
    }.onStart {
        Log.d(TAG,"textChangeToFlow() onstart 발동")
        //코틀린 플로우 emit으로 이벤트 전달  좀더 공부 emit으로 데이터 방출?
        emit(text)
    }
}
fun getImageResult(value: String): Int {
    return when(value) {
        "농구" -> R.drawable.sport_basketball
        "축구" ->R.drawable.sport_soccer
        "탁구" ->R.drawable.sport_pingpong
        "테니스" -> R.drawable.sport_tennis
        "배드민턴" ->R.drawable.sport_badminton
        "야구" ->R.drawable.sport_baseball
        "볼링"->R.drawable.sport_bowling
        "자전거"->R.drawable.sport_bicycle
        "골프" ->R.drawable.sport_golf
        "런닝" ->R.drawable.sport_running
        "수영" ->R.drawable.sport_swim
        "배구" ->R.drawable.sport_volleyball
        "요가" ->R.drawable.sport_yoga
        "태권|유도" ->R.drawable.sport_taekwonudo
        "복싱" -> R.drawable.sport_box
        "무술"->R.drawable.sport_musul
        "승마"->R.drawable.sport_horse
        "헬스" ->R.drawable.sport_hells
        "롤러|보드"->R.drawable.sport_rollerboard
        "스키|보드" ->R.drawable.sport_skiboard
        "당구"->R.drawable.sport_danggu
        "등산"->R.drawable.sport_hiking
        "수상레저"->R.drawable.sport_leisure
        "세계여행" ->R.drawable.trip_worldtravel
        "국내여행" ->R.drawable.trip_domestictravel
        "밴드" ->R.drawable.music_band
        "피아노" ->R.drawable.music_piano
        "드럼" ->R.drawable.music_drum
        "바이올린" ->R.drawable.music_violin
        "기타" ->R.drawable.music_guitar
        "노래" ->R.drawable.music_sing
        "작곡" ->R.drawable.music_musicwriter
        "힙합" ->R.drawable.music_hip_hop
        "버스킹" ->R.drawable.music_busking
        "콘서트" ->R.drawable.music_concert
        "디제잉" ->R.drawable.music_djing
        "런치패드" ->R.drawable.music_launch_pad
        "색소폰" ->R.drawable.music_saxophone
        "친구"->R.drawable.society_friends
        "카페" ->R.drawable.society_cafe
        "술 한잔" ->R.drawable.society_beer
        "코노" ->R.drawable.society_coinsong
        "맛집탐방" ->R.drawable.society_food
        "독서" ->R.drawable.book_reading
        "글쓰기"->R.drawable.book_writing
        "토론"->R.drawable.book_discussion
        else -> R.drawable.icon_art
    }
}
