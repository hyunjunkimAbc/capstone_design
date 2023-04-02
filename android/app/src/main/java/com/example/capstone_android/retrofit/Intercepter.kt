package com.example.capstone_android.retrofit

fun String?.isJsonObject():Boolean{
    return this?.startsWith("{")==true && this.endsWith("}")
}

fun String?.isJsonArray():Boolean{
    return this?.startsWith("[")==true && this.endsWith("]")
}