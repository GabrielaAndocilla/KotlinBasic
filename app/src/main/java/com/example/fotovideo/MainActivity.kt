package com.example.fotovideo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        IrCamara.setOnClickListener{

            val intent:Intent = Intent(this, CaptFoto::class.java)
            startActivity(intent)
        }
        IrVideo.setOnClickListener {

            val intent:Intent = Intent(this, CaptVideo::class.java)
            startActivity(intent)
        }

        IrApi.setOnClickListener {

            val intent:Intent = Intent(this, apiservicesweb::class.java)
            startActivity(intent)
        }
    }


}