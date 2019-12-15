package com.yt.androidytdownload.tasks

import android.app.AlertDialog
import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.google.gson.Gson
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.enums.Kind
import com.yt.androidytdownload.util.ContextKeeper
import com.yt.androidytdownload.util.cutChars
import com.yt.androidytdownload.util.deleteWhen
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ValidTask : AsyncTask<Void, Void, Boolean>,AbstractTask {


    override fun doExecute(){
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }


    var videoDetails: VideoDetails
    val url: String
    val kind: Kind
    val python: Python
    val pyObj: PyObject
    val progressBar: ProgressBar
    val port:Int

    override fun onPreExecute() {
        progressBar.visibility = View.VISIBLE
        Thread(Runnable {
            pyObj.callAttr("getVideoInfo", url, kind,port)
        }).start()
    }

    constructor(
        progressBar: ProgressBar,
        python: Python,
        moduleName: String,
        url: String,
        kindstr: Kind,
        port:Int
    ) {
        this.progressBar = progressBar
        this.videoDetails = VideoDetails("", "", "")
        this.url = url
        this.kind = kindstr
        this.python = python
        this.pyObj = python.getModule(moduleName)
        this.port = port
    }


    override fun doInBackground(vararg p0: Void?): Boolean {


        val socket: DatagramSocket = DatagramSocket(port)
        var running: Boolean = true
        var buffer = ByteArray(1024)
        var packet: DatagramPacket = DatagramPacket(buffer, buffer.size)

        var result = true
        while (running) {

            socket.receive(packet)

            val addr: InetAddress = packet.address
            val port = packet.port

            packet = DatagramPacket(buffer, buffer.size, addr, port)
            val received: String = String(packet.data, 0, packet.length)

            println("ValidTask message:"+received)

            when {
                received.contains("error") -> {
                    result = false; running = false; }
                received.contains("title") -> {
                    this.videoDetails = Gson().fromJson(deleteWhen(received, '}'), VideoDetails::class.java)
                    running = false
                }
            }
        }
        socket.close()

        return result
    }

    override fun onPostExecute(result: Boolean?) {
        progressBar.visibility = View.INVISIBLE

        if (result != null) {
            if (result) {

                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(ContextKeeper.context)
                alertDialog.setTitle("Download")
                alertDialog.setMessage("Are you sure you wanna download below video?\nTitle:" + videoDetails.title + "\nSize(MB):" + videoDetails.file_size)
                    .setCancelable(false)
                    .setPositiveButton("Yes", { dialog, which ->

                        AlertDialog.Builder(ContextKeeper.context).setTitle("MP3")
                            .setMessage("Would you like to convert file to mp3?(In large files it may even takes few minutes)")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Yes",
                                { dialog, which ->;startDownload(true) })
                            .setNegativeButton("No", { dialog, which -> startDownload() })
                            .create()
                            .show()
                    })

                    .setNegativeButton("No",
                        { dialog, which -> dialog.cancel();ContextKeeper.downloadQueueEmpty = true })
                    .create()
                    .show()

            } else
                Toast.makeText(
                    ContextKeeper.context,
                    "Cannot download video check your URL or internet connection",
                    Toast.LENGTH_LONG
                ).show()
            ContextKeeper.downloadQueueEmpty = true
        } else {
            Toast.makeText(ContextKeeper.context, "Error-Result value is null", Toast.LENGTH_LONG).show()
            ContextKeeper.downloadQueueEmpty = true
        }

    }

    private fun startDownload(convertToMp3:Boolean = false) {
        videoDetails.title=cutChars(videoDetails.title)
        Toast.makeText(ContextKeeper.context, "Download " + videoDetails.title + " started", Toast.LENGTH_LONG).show()
        Thread(Runnable {
            pyObj.callAttr("doDownload", url, kind.toString().toLowerCase(),port, videoDetails.title)
        }).start()

        ContextKeeper.taskProcess.doAction(url,kind,"downloadtask",videoDetails,convertToMp3,port)
    }


}