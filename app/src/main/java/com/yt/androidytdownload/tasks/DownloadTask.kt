package com.yt.androidytdownload.tasks

import android.os.AsyncTask
import android.widget.Button
import android.widget.Toast
import com.yt.androidytdownload.Model.VideoDetails
import com.yt.androidytdownload.enums.Kind
import com.yt.androidytdownload.enums.SocketResult
import com.yt.androidytdownload.util.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DownloadTask : AsyncTask<String, String, SocketResult> , AbstractTask {

    override fun doExecute() {
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    val videoDetails: VideoDetails
    val notification: MyNotification
    val downloadButton: Button
    var convertToMp3: Boolean
    val port: Int
    val fileKind: Kind


    private var lastValue: Int

    constructor(notification: MyNotification, downloadButton: Button, port: Int, fileKind: Kind, videoDetails: VideoDetails,convertToMp3:Boolean = false) {
        this.videoDetails = videoDetails
        this.notification = notification
        this.downloadButton = downloadButton
        this.convertToMp3 = convertToMp3
        this.lastValue = 0
        this.port = port
        this.fileKind = fileKind
    }


    override fun onPreExecute() {
        notification.setTitle(videoDetails.title)
        notification.setContentText("Size(MB):" + videoDetails.file_size)
        notification.builder.setProgress(100, lastValue, false)
        notification.makeNotification()
    }

    override fun doInBackground(vararg p0: String?): SocketResult? {

        val socket: DatagramSocket = DatagramSocket(port)
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
                received.contains("100%") -> {
                    publishProgress(received);running = false
                }
                received.matches(".*\\d.*".toRegex()) -> publishProgress(received) //check whether has some decimals
            }


        }
        socket.close()

        return SocketResult.SUCCESS
    }

    override fun onProgressUpdate(vararg values: String?) {
        if (values[0] != null) {
            //Log.d("Received:", values[0].toString())

            val currentValue: Int = GetDecFromStr(values[0].toString())

            if (currentValue == lastValue || !(currentValue >= lastValue + 5)) return
            else lastValue = currentValue

            notification.builder.setProgress(100, currentValue, false)
            notification.makeNotification()
        }
    }


    override fun onPostExecute(result: SocketResult?) {

        if (convertToMp3) {
            val from = videoDetails.file_path + "/" + videoDetails.title + ".mp4"
            startConvertion("-i", from, from.replace("mp4", "mp3"), notification)
        } else {

            setNotificationOnTheEnd(notification, videoDetails.file_path + "/" + videoDetails.title + ".mp4")
        }


        if (result != null && result != SocketResult.SUCCESS)
            Toast.makeText(
                ContextKeeper.context,
                "Error occurred check whether url is valid.",
                Toast.LENGTH_LONG
            ).show()

        downloadButton.isClickable = true
    }
}