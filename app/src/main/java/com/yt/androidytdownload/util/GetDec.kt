package com.yt.androidytdownload.util

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
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

fun startConvertion(param:String,from:String,to:String,notification: MyNotification){

    val fMpeg: FFmpeg = FFmpeg.getInstance(ContextKeeper.context)
    val cmd = arrayOf(param,from,to)

    try{

        fMpeg.execute(cmd,object: ExecuteBinaryResponseHandler(){
            override fun onFinish() {
            }

            override fun onSuccess(message: String?) {
                println("success")
            }

            override fun onFailure(message: String?) {
                println("failure")
            }

            override fun onProgress(message: String?) {
                println("progress:"+message)
            }

            override fun onStart() {
                super.onStart()
            }
        })

    }
    catch(e: FFmpegCommandAlreadyRunningException){
        println("FFmpegCommandAlreadyRunningException:"+e.message)
    }






}
