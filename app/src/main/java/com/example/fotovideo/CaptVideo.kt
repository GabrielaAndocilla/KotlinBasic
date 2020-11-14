package com.example.fotovideo

import android.R.attr.description
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_capt_foto.*
import kotlinx.android.synthetic.main.activity_capt_video.*
import kotlinx.android.synthetic.main.activity_capt_video.btnGaleria
import java.io.File
import java.lang.Object

private const val VIDEO_PICK_CODE = 1000
private const val PERMISSION_CODE = 1000

class CaptVideo : AppCompatActivity() {
    lateinit var videoUri: Uri

    val REQUEST_VIDEO_CAPTURE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capt_video)
        btnVideo.setOnClickListener {
            recordVideo()

        }
        btnGaleria.setOnClickListener {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permission, PERMISSION_CODE)

                }
                else{
                    pickImageFromGallery()

                }
            }
            else{
                pickImageFromGallery()
            }

        }
    }
    private fun pickImageFromGallery(){

        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        startActivityForResult(Intent.createChooser(intent, "Select Video"),VIDEO_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()

                }
                else{
                    Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show()

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun recordVideo() {
        val videoFile = createVideoFile()

        if(videoFile != null){

            videoUri = FileProvider.getUriForFile(this,"com.example.fotovideo.fileprovider", videoFile)

            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
            startActivityForResult(intent,REQUEST_VIDEO_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK){
            VideoShow.setVideoURI(videoUri)
            VideoShow.start()
        }else{
            if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_PICK_CODE) {
                if (data?.data != null) {

                    VideoShow.setVideoURI( data?.data)
                    VideoShow.start()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun createVideoFile(): File {
        val Filename = "MyVideo"

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)

        return File.createTempFile(
            Filename, ".mp4",storageDir
        )
    }

}