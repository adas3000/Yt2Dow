package com.yt.androidytdownload.tasks

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.google.gson.Gson
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.enum.CheckStatus
import com.yt.androidytdownload.util.SocketPort
import com.yt.androidytdownload.util.deleteWhen
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ValidTask : AsyncTask<Void, Void, Boolean> {


    private val context: Context

    var videoDetails: VideoDetails
    val url: String
    val kindstr: String
    val python: Python
    val pyObj: PyObject
    val downloadTask:DownloadTask

    override fun onPreExecute() {
        downloadTask.downloadButton.isClickable = false
        Thread(Runnable {
            pyObj.callAttr("getVideoInfo", url, kindstr)
        }).start()
    }

    constructor(context: Context,downloadTask: DownloadTask,python:Python,moduleName:String,url: String, kindstr: String) {
        this.context = context
        this.videoDetails = VideoDetails("", "")
        this.url = url
        this.downloadTask = downloadTask
        this.kindstr = kindstr
        this.python = python
        this.pyObj = python.getModule(moduleName)
    }


    override fun doInBackground(vararg p0: Void?): Boolean {


        val socket: DatagramSocket = DatagramSocket(SocketPort.Port.port)
        var running: Boolean = true
        var buffer = ByteArray(256)
        var packet: DatagramPacket = DatagramPacket(buffer, buffer.size)

        var result = true
        while (running) {

            socket.receive(packet)

            val addr: InetAddress = packet.address
            val port = packet.port

            packet = DatagramPacket(buffer, buffer.size, addr, port)
            val received: String = String(packet.data, 0, packet.length)

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
        if (result != null) {
            if (result) {

                var doDownload = false
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
                alertDialog.setMessage("Are you sure you wanna download below video?\nTitle:" + videoDetails.title + "\nSize(MB):" + videoDetails.file_size)
                    .setCancelable(false)
                    .setPositiveButton("Yes", { dialog, which -> doDownload = true })
                    .setNegativeButton("No",{dialog,which->downloadTask.downloadButton.isClickable=true })
                    .show()

                if(!doDownload) return

                Toast.makeText(context, "Downloading starting", Toast.LENGTH_LONG).show()
                Thread(Runnable {
                    pyObj.callAttr("doDownload", url, kindstr)
                }).start()
                downloadTask.execute()
            } else
                Toast.makeText(
                    context,
                    "Cannot download video check your URL or internet connection",
                    Toast.LENGTH_LONG
                ).show()
        } else
            Toast.makeText(context, "Error-Result value is null", Toast.LENGTH_LONG).show()

    }


}