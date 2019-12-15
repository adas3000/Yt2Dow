package com.yt.androidytdownload

import android.Manifest
import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.yt.androidytdownload.enums.Kind
import com.yt.androidytdownload.util.MyNotification
import kotlinx.android.synthetic.main.activity_main.*
import android.os.StrictMode
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
import com.yt.androidytdownload.factory.TaskFactory
import com.yt.androidytdownload.tasks.TaskProcess
import com.yt.androidytdownload.util.ContextKeeper


class MainActivity : AppCompatActivity() {

    private var kind: Kind = Kind.MP4
    private lateinit var taskProcess: TaskProcess
    var hasPermissions = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskProcess = TaskProcess(TaskFactory(button_download,progressBar_circle,this))


        ContextKeeper.context = this
        ContextKeeper.taskProcess = taskProcess

        setUi()
        checkPermission()
        loadFFmpeg()
        setNotificationManager()

    }

    fun setNotificationManager() {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        MyNotification.notificationManager = notificationManager
    }


    fun checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            hasPermissions = true
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setUi() {
        radioGroup.check(radioButton_video.id)
        progressBar_circle.visibility = View.INVISIBLE
    }

    fun onRadioButtonClick(view: View) {


        val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)

        when (radio.id) {
            R.id.radioButton_audio -> {
                kind = Kind.MP3
            }
            R.id.radioButton_video -> {
                kind = Kind.MP4
            }
        }

    }


    fun onDownloadClick(view: View) {

        if (!hasPermissions) {
            Toast.makeText(this, "No permission to write in storage.Set permission in settins.", Toast.LENGTH_LONG)
                .show()
            return
        }

        if (!ContextKeeper.downloadQueueEmpty) {
            Toast.makeText(this, "Wait till current file will be downloaded.", Toast.LENGTH_LONG).show()
            return
        }

        var notification: MyNotification = MyNotification(
            "com.yt.androidyt.download.channel", "androidytdownloadsChannel"
            , this
        )
        notification.setIcon(R.mipmap.ic_yt_icon_foreground).setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val url: String = editText.text.toString()


        taskProcess.doAction(url,kind,"validtask")
    }


    fun showUnsupportedDialog() {

        val dialog: AlertDialog.Builder = AlertDialog.Builder(ContextKeeper.context)
        dialog.setIcon(R.mipmap.ic_launcher)
            .setTitle("Device not supported")
            .setMessage("Your device doesn't support ffmpeg")
            .setCancelable(false)
            .setPositiveButton("Ok", { dialogInterface, i ->
                this.finish()
            })
            .create()
            .show()
    }

    fun loadFFmpeg() {
        val fFmpeg: FFmpeg = FFmpeg.getInstance(ContextKeeper.context)
        try {
            fFmpeg.loadBinary(object : LoadBinaryResponseHandler() {

                override fun onSuccess() {
                    super.onSuccess()
                    println("FFmpeg loaded sucessfully")
                }

                override fun onFailure() {
                    showUnsupportedDialog()
                }

            })
        } catch (e: FFmpegNotSupportedException) {
            showUnsupportedDialog()
        }
    }

}
