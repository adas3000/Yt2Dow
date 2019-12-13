package com.yt.androidytdownload.tasks

import android.os.AsyncTask
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.enum.SocketResult
import com.yt.androidytdownload.util.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DownloadTask : AsyncTask<String, String, SocketResult> {


    var videoDetails:VideoDetails = VideoDetails("","","")
    val notification:MyNotification
    val downloadButton:Button
    var convertToMp3:Boolean

    constructor(notification: MyNotification,downloadButton:Button) {
        this.notification = notification
        this.downloadButton = downloadButton
        this.convertToMp3 = false
    }



    override fun onPreExecute() {
        notification.setTitle(videoDetails.title)
        notification.setContentText("Size(MB):"+videoDetails.file_size)
        notification.builder.setProgress(100,0,false)
        notification.makeNotification()
    }

    override fun doInBackground(vararg p0: String?): SocketResult? {

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


            when {
                received.contains("100%") -> {publishProgress(received);running = false}
                received.matches(".*\\d.*".toRegex()) -> publishProgress(received) //check whether has some decimals
            }


        }
        socket.close()

        return SocketResult.SUCCESS
    }

    override fun onProgressUpdate(vararg values: String?) {
        if (values[0] != null) {
            Log.d("Received:", values[0].toString())
            notification.builder.setProgress(100,GetDecFromStr(values[0].toString()),false)
            notification.makeNotification()
        }
    }


    override fun onPostExecute(result: SocketResult?) {

        notification.builder.setContentText("")
            .setProgress(100, 100, true)


        if(convertToMp3)
        startConvertion("-i",videoDetails.file_path,videoDetails.file_path.replace(".mp4",".mp3"),notification)



        if (result != null && result!=SocketResult.SUCCESS)
                Toast.makeText(ContextKeeper.context, "Error occurred check whether url is valid.", Toast.LENGTH_LONG).show()

        downloadButton.isClickable = true
    }
}