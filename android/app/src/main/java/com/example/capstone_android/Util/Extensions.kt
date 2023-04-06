package com.example.capstone_android.Util

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
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