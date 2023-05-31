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
        "봉사활동"->R.drawable.society_volunteer
        "독서" ->R.drawable.book_reading
        "글쓰기"->R.drawable.book_writing
        "토론"->R.drawable.book_discussion
        "한식"->R.drawable.cook_koreafood
        "중식"->R.drawable.cook_chinafood
        "일식"->R.drawable.cook_japanfood
        "양식"->R.drawable.cook_usfood
        "제과제빵"->R.drawable.cook_bread
        "칵테일" ->R.drawable.cook_cocktail
        "와인"->R.drawable.cook_wine
        "사진"->R.drawable.camera_camera
        "영상제작"->R.drawable.camera_editing
        "AOS"->R.drawable.game_aos
        "RPG"->R.drawable.game_rpg
        "FPS"->R.drawable.game_fps
        "카드게임"->R.drawable.game_card
        "두뇌심리"->R.drawable.game_brain
        "스포츠게임"->R.drawable.game_sport
        "레이싱게임"->R.drawable.game_racing
        "닌텐도|플스"->R.drawable.game_nintendo
        "팝핀"->R.drawable.dance_popping
        "락킹"->R.drawable.dance_rocking
        "비보잉"->R.drawable.dance_b_boying
        "왁킹"->R.drawable.dance_waacking
        "힙합댄스"->R.drawable.dance_hiphopdance
        "하우스"->R.drawable.dance_house
        "크럼프"->R.drawable.dance_krump
        "현대무용"->R.drawable.dance_modern
        "한국무용"->R.drawable.dance_korea
        "K-POP"->R.drawable.dance_kpop
        "발레"->R.drawable.dance_vallet
        "댄스스포츠"->R.drawable.dance_dancesport
        "발리댄스"->R.drawable.dance_hula
        "재즈"->R.drawable.dance_jazz
        "에어로빅"->R.drawable.dance_aerobics
        "자동차"->R.drawable.car_car
        "오토바이"->R.drawable.car_motorbike
        "강아지"->R.drawable.pet_dog
        "고양이"->R.drawable.pet_cat
        "고슴도치"->R.drawable.pet_hedgehog
        "햄스터"->R.drawable.pet_hamster
        "물고기"->R.drawable.pet_fish
        "앵무새"->R.drawable.pet_parrot
        "다람쥐"->R.drawable.pet_squirrel
        "도마뱀"->R.drawable.pet_lizard
        "뱀"->R.drawable.pet_snake
        "거미"->R.drawable.pet_tarantula
        "미술"->R.drawable.art_art
        "공방"->R.drawable.art_bracelet
        "도예"->R.drawable.art_porcelain
        "자수"->R.drawable.art_crossstitch
        "꽃"->R.drawable.art_flower
        "화장품"->R.drawable.art_cosmetics
        "가구"->R.drawable.art_furniture
        "스터디"->R.drawable.study_study
        "언어"->R.drawable.study_languages
        "동기부여"->R.drawable.study_motivated
        "스피치"->R.drawable.study_speech
        "IT"->R.drawable.job_it
        "디자인"->R.drawable.job_designer
        "의료"->R.drawable.job_medicine
        "화학"->R.drawable.job_chemistry
        "금융"->R.drawable.job_finance
        "건설"->R.drawable.job_constructor
        "법"->R.drawable.job_law
        "패션"->R.drawable.job_dress
        else -> R.drawable.hobby
    }
}
