package com.yt.androidytdownload.tasks

import android.content.Context
import android.os.AsyncTask
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
    val progressBar: ProgressBar
    val url: String
    val kindstr: String
    val python: Python
    val pyObj: PyObject

    override fun onPreExecute() {
        Thread(Runnable {
            pyObj.callAttr("getVideoInfo", url, kindstr)
        }).start()
    }

    constructor(context: Context, progressBar: ProgressBar, url: String, kindstr: String) {
        this.context = context
        this.progressBar = progressBar
        this.videoDetails = VideoDetails("", "")
        this.url = url
        this.kindstr = kindstr
        this.python = Python.getInstance()
        this.pyObj = python.getModule("main")
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
                Toast.makeText(context,"Downloading starting",Toast.LENGTH_LONG).show()
                Thread(Runnable {
                    pyObj.callAttr("doDownload", url, kindstr)
                }).start()
                val downloadTask: DownloadTask = DownloadTask(context, progressBar)
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