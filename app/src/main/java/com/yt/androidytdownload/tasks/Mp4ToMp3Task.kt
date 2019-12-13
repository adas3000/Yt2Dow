package com.yt.androidytdownload.tasks

import android.app.Notification
import android.os.AsyncTask
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.util.ContextKeeper

class Mp4ToMp3Task : AsyncTask<String,String,Void> {

    var videoDetails:VideoDetails
    val notification:Notification

    constructor(videoDetails:VideoDetails,notification:Notification){
        this.videoDetails = videoDetails
        this.notification = notification
    }


    override fun onPreExecute() {



    }


    override fun doInBackground(vararg p0: String?): Void {

        val cmd:List<String> = listOf(p0[0].toString(),p0[1].toString(),p0[2].toString())

        val fMpeg:FFmpeg = FFmpeg.getInstance(ContextKeeper.context)

        try{

            fMpeg.execute(cmd.toTypedArray(),object:ExecuteBinaryResponseHandler(){
                override fun onFinish() {
                    super.onFinish()
                }

                override fun onSuccess(message: String?) {
                    super.onSuccess(message)
                }

                override fun onFailure(message: String?) {
                    super.onFailure(message)
                }

                override fun onProgress(message: String?) {
                    super.onProgress(message)
                }

                override fun onStart() {
                    super.onStart()
                }
            })

        }
        catch(e:FFmpegCommandAlreadyRunningException){
            println("FFmpegCommandAlreadyRunningException:"+e.message)
        }


        return Void.TYPE.newInstance()
    }

    override fun onProgressUpdate(vararg values: String?) {


    }

    override fun onPostExecute(result: Void?) {




    }

}