package com.yt.androidytdownload.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.util.GetDecFromStr
import com.yt.androidytdownload.util.SocketPort
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DownloadTask : AsyncTask<String, String, String> {


    private val context: Context
    private val progressBar: ProgressBar
    private var videoDetails:VideoDetails

    constructor(context: Context, progressBar: ProgressBar) {
        this.context = context
        this.progressBar = progressBar
        this.videoDetails = VideoDetails("","")
    }

    fun getVideoDetails():VideoDetails{
        return this.videoDetails
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg p0: String?): String? {

        val socket: DatagramSocket = DatagramSocket(SocketPort.Port.port)
        var running: Boolean = true
        var buffer = ByteArray(256)
        var packet: DatagramPacket = DatagramPacket(buffer, buffer.size)


        while (running) {

            socket.receive(packet)

            val addr: InetAddress = packet.address
            val port = packet.port

            packet = DatagramPacket(buffer, buffer.size, addr, port)
            val received: String = String(packet.data, 0, packet.length)

            publishProgress(received)

            if (received.contains("error")){
                socket.close()
                return received
            }
            else if (received.contains("100%")) {
                running = false
            }
        }
        socket.close()

        return "success"
    }

    override fun onProgressUpdate(vararg values: String?) {
        if (values[0] != null) {
                Log.d("Recived:", values[0])
                progressBar.progress = GetDecFromStr(values[0].toString())

        }
    }


    override fun onPostExecute(result: String?) {
        if(result != null){
            if(result.contains("success")){
                Toast.makeText(context, "Downloaded!", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(context,"Error occurred check whether url is valid.\nError message:"+result,
                    Toast.LENGTH_LONG).show()
            }
        }
        progressBar.visibility = View.INVISIBLE
        progressBar.progress=0
    }
}