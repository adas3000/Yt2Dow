package com.yt.androidytdownload.tasks

import android.app.AlertDialog
import android.os.AsyncTask
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.google.gson.Gson
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.util.ContextKeeper
import com.yt.androidytdownload.util.cutChars
import com.yt.androidytdownload.util.deleteWhen
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ValidTask : AsyncTask<Void, Void, Boolean> {


    var videoDetails: VideoDetails
    val url: String
    val kindstr: String
    val python: Python
    val pyObj: PyObject
    val downloadTask: DownloadTask
    val progressBar: ProgressBar
    val port:Int

    override fun onPreExecute() {
        downloadTask.downloadButton.isClickable = false
        progressBar.visibility = View.VISIBLE
        Thread(Runnable {
            pyObj.callAttr("getVideoInfo", url, kindstr,port)
        }).start()
    }

    constructor(
        downloadTask: DownloadTask,
        progressBar: ProgressBar,
        python: Python,
        moduleName: String,
        url: String,
        kindstr: String,
        port:Int
    ) {
        this.progressBar = progressBar
        this.videoDetails = VideoDetails("", "", "")
        this.url = url
        this.downloadTask = downloadTask
        this.kindstr = kindstr
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
                            .setMessage("Would you like to convert file to mp3?")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Yes",
                                { dialog, which -> downloadTask.convertToMp3 = true;startDownload() })
                            .setNegativeButton("No", { dialog, which -> startDownload() })
                            .create()
                            .show()
                    })

                    .setNegativeButton("No",
                        { dialog, which -> dialog.cancel();downloadTask.downloadButton.isClickable = true })
                    .create()
                    .show()

            } else
                Toast.makeText(
                    ContextKeeper.context,
                    "Cannot download video check your URL or internet connection",
                    Toast.LENGTH_LONG
                ).show()
        } else
            Toast.makeText(ContextKeeper.context, "Error-Result value is null", Toast.LENGTH_LONG).show()


        downloadTask.downloadButton.isClickable = true
    }

    private fun startDownload() {
        videoDetails.title=cutChars(videoDetails.title)
        Toast.makeText(ContextKeeper.context, "Download " + videoDetails.title + " started", Toast.LENGTH_LONG).show()
        Thread(Runnable {
            pyObj.callAttr("doDownload", url, kindstr,port, videoDetails.title)
        }).start()
        downloadTask.videoDetails = videoDetails
        downloadTask.execute()
    }


}