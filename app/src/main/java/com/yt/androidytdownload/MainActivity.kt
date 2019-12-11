package com.yt.androidytdownload

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.yt.androidytdownload.enum.Kind
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var kind: Kind = Kind.MP4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup.check(radioButton_video.id)
        progressBar2.visibility=View.GONE

        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this,"GRANTED", LENGTH_LONG).show()
        else
            Toast.makeText(this,"NOT GRANTED", LENGTH_LONG).show()
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

        val url:String = editText.text.toString()


        val python: Python = Python.getInstance()
        val pyObj: PyObject = python.getModule("main")


        Thread(Runnable {
             pyObj.callAttr("doDownload",url,kind.toString())
        }).start()






    }

}
