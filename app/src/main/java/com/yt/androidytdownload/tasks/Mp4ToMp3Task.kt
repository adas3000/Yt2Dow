package com.yt.androidytdownload.tasks

import android.os.AsyncTask
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.util.ContextKeeper
import com.yt.androidytdownload.util.MyNotification
import com.yt.androidytdownload.util.parseFFMpegOnProgressStr

class Mp4ToMp3Task : AsyncTask<String,String,Boolean> {

    var videoDetails:VideoDetails
    val notification:MyNotification
    val maxValue:Int
    val multipleBy:Int

    constructor(videoDetails:VideoDetails,notification:MyNotification,multipleBy:Int){
        this.videoDetails = videoDetails
        this.notification = notification
        this.multipleBy = multipleBy
        this.maxValue = (videoDetails.file_size.toFloat()*multipleBy).toInt()
    }


    override fun onPreExecute() {
        notification.builder.setContentText("Converting to mp3...")
        notification.builder.setProgress(maxValue,0,false)
        notification.makeNotification()
    }


    override fun doInBackground(vararg p0: String?): Boolean {

        val cmd:List<String> = listOf(p0[0].toString(),p0[1].toString(),p0[2].toString())

        val fMpeg:FFmpeg = FFmpeg.getInstance(ContextKeeper.context)

        try{

            fMpeg.execute(cmd.toTypedArray(),object:ExecuteBinaryResponseHandler(){
                override fun onFinish() {
                }

                override fun onSuccess(message: String?) {
                    println("success")
                }

                override fun onFailure(message: String?) {
                    println("failure")
                }

                override fun onProgress(message: String?) {
                    println(message)
                    publishProgress(message)
                }

                override fun onStart() {
                    super.onStart()
                }
            })

        }
        catch(e:FFmpegCommandAlreadyRunningException){
            println("FFmpegCommandAlreadyRunningException:"+e.message)
        }


        return true
    }

    override fun onProgressUpdate(vararg values: String?) {

        var value = values[0]

        if(value!=null){
            //notification.builder.setProgress(maxValue, parseFFMpegOnProgressStr(value),false)
            notification.builder.setProgress(100,100,true)
            notification.makeNotification()
        }


    }

    override fun onPostExecute(result: Boolean?) {




    }

}