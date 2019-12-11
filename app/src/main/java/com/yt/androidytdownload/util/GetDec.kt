package com.yt.androidytdownload.util

import java.lang.StringBuilder

fun GetDecFromStr(str:String):Int{

    var founded = false
    var sBstr:StringBuilder= StringBuilder("")


    for(i in 0 until str.length){

        if(!founded && str[i].isDigit()){
            founded = true
            sBstr.append(str[i])
        }
        else if(founded && str[i].isDigit())
            sBstr.append(str[i])
        else if(founded && !str[i].isDigit())
            break
    }
    return sBstr.toString().toInt()
}