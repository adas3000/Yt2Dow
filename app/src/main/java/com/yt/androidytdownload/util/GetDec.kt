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

fun deleteWhen(from:String,whenFind:Char):String{

    val buffer = StringBuilder()

    var i:Int = 0;
    while(from.get(i)!=whenFind){
        buffer.append(from.get(i++))
    }


    return buffer.append(whenFind).toString()
}

fun strToJson(str:String,clazz: Class<*>){

    val fields = clazz.fields

    println(fields.size)

}

fun parseFFMpegOnProgressStr(str:String):Int{

    val builder:StringBuilder = StringBuilder()

    var founded = false
    for(i in 0 until str.length){
        if(str[i].isDigit() && !founded){
            founded = true
            builder.append(str[i])
        }
        else if(str[i].isDigit() && founded)
            builder.append(str[i])
        else if(!str[i].isDigit() && founded)
            break
    }
    return builder.toString().toInt()
}