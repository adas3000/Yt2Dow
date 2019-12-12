package com.yt.androidytdownload.tasks

import android.app.NotificationManager
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.R
import com.yt.androidytdownload.enum.SocketResult
import com.yt.androidytdownload.util.GetDecFromStr
import com.yt.androidytdownload.util.MyNotification
import com.yt.androidytdownload.util.SocketPort
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DownloadTask : AsyncTask<String, String, SocketResult> {


    private val context: Context
    var videoDetails:VideoDetails = VideoDetails("","")
    val notification:MyNotification
    val downloadButton:Button

    constructor(context: Context, notification: MyNotification,downloadButton:Button) {
        this.context = context
        this.notification = notification
        this.downloadButton = downloadButton
    }



    override fun onPreExecute() {
        notification.setTitle(videoDetails.title)
        notification.setContentText(videoDetails.file_size)
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

        notification.builder.setContentTitle("Download complete").setContentText("")
            .setProgress(100, 100, true)
        notification.makeNotification()



        if (result != null) {
            if (result==SocketResult.SUCCESS)
                Toast.makeText(context, "Downloaded!", Toast.LENGTH_LONG).show()
             else
                Toast.makeText(context, "Error occurred check whether url is valid.", Toast.LENGTH_LONG).show()
        }
        downloadButton.isClickable = true
    }
}