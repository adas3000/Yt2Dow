package com.yt.androidytdownload.util

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File
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

fun cutChars(str: String):String{

    var answer = str
    println(answer)
    answer = answer.replace("[^A-Za-z0-9 ]", "")
    println(answer)
    val re = Regex("[^A-Za-z0-9 ]")
    answer = re.replace(answer, "")
    return answer
}

fun startConvertion(param:String,from:String,to:String,notification: MyNotification){

    val fMpeg: FFmpeg = FFmpeg.getInstance(ContextKeeper.context)

    val from_2 = from.replace(";","").replace("|","").replace("/","")



    val cmd = arrayOf(param,from_2,to)

    val file_title = notification.title

    notification.builder.setProgress(100,100,true)
    notification.setTitle("Converting:"+notification.title)
    notification.setContentText("Converting...")
    notification.makeNotification()
    try{

        fMpeg.execute(cmd,object: ExecuteBinaryResponseHandler(){
            override fun onFinish() {
                notification.builder.setProgress(0,0,false)
                notification.makeNotification()
            }

            override fun onSuccess(message: String?) {
                println("success")
                notification.setContentText("Downloaded")
                notification.builder.setProgress(0,0,false)

                val file_ToRemove:File = File(from_2)

                if(!file_ToRemove.delete())
                    println("cannot remove old file")

                val file:File = File(to)
                val map:MimeTypeMap = MimeTypeMap.getSingleton()


                val ext:String = MimeTypeMap.getFileExtensionFromUrl(file.name)
                var type:String? = map.getMimeTypeFromExtension(ext)

                if(type==null) type ="*/*"

                val intent:Intent = Intent(Intent.ACTION_VIEW)
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                val uri:Uri = Uri.fromFile(file)

                intent.setDataAndType(uri,type)
                val pendingIntent:PendingIntent = PendingIntent.getActivity(ContextKeeper.context,0,intent,0)


                notification.builder.setContentIntent(pendingIntent)
                notification.makeNotification()
            }

            override fun onFailure(message: String?) {
                Toast.makeText(ContextKeeper.context,"Couldn't convert file:"+file_title,Toast.LENGTH_LONG).show()
                notification.setContentText("Downloaded")
                notification.builder.setProgress(0,0,false)
                val file:File = File(from_2)
                val map:MimeTypeMap = MimeTypeMap.getSingleton()


                val ext:String = MimeTypeMap.getFileExtensionFromUrl(file.name)
                var type:String? = map.getMimeTypeFromExtension(ext)

                if(type==null) type ="*/*"

                val intent:Intent = Intent(Intent.ACTION_VIEW)
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                val uri:Uri = Uri.fromFile(file)

                intent.setDataAndType(uri,type)
                val pendingIntent:PendingIntent = PendingIntent.getActivity(ContextKeeper.context,0,intent,0)


                notification.builder.setContentIntent(pendingIntent)
                notification.makeNotification()

                println("failure")
            }

            override fun onProgress(message: String?) {
                println("progress:"+message)
            }
        })

    }
    catch(e: FFmpegCommandAlreadyRunningException){
        println("FFmpegCommandAlreadyRunningException:"+e.message)
    }







}
