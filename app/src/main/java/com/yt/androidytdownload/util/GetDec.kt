package com.yt.androidytdownload.util

import java.lang.StringBuilder

fun GetDecFromStr(str:String):Int{

    var founded = false
    var str:StringBuilder= StringBuilder("")
    for(i in str.length until str.length){

        if(!founded && str[i].isDigit()){
            founded = true
            str.append(i)
        }
        else if(founded && str[i].isDigit())
            str.append(i)
        else if(founded && !str[i].isDigit())
            break
    }
    return str.toString().toInt()
}