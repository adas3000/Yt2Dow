package com.yt.androidytdownload.tasks

import android.content.Context
import android.os.AsyncTask
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.enum.CheckStatus
import com.yt.androidytdownload.util.SocketPort
import com.yt.androidytdownload.util.deleteWhen
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ValidTask : AsyncTask<Void, Void, Boolean> {


    private val context:Context

    var videoDetails:VideoDetails
    var status:CheckStatus
    val progressBar:ProgressBar


    override fun onPreExecute() {

        

    }

    constructor(context: Context,progressBar: ProgressBar){
        this.context = context
        this.progressBar = progressBar
        this.status = CheckStatus.Checking
        this.videoDetails = VideoDetails("", "")
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
                received.contains("error") -> {result = false ; running = false ; status = CheckStatus.Error}
                received.contains("title") -> {this.videoDetails = Gson().fromJson(deleteWhen(received,'}'),
                    VideoDetails::class.java)
                    status = CheckStatus.Ok
                    running = false
                }
            }
        }
        socket.close()

        return result
    }

    override fun onPostExecute(result: Boolean?) {
        if(result!=null){
            if(result){



            }
            else
                Toast.makeText(context,"Cannot download video check your URL or internet connection",Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(context,"Error-Result value is null",Toast.LENGTH_LONG).show()

    }


}