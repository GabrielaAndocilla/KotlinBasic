package com.example.fotovideo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_capt_foto.*
import java.io.File
import java.util.jar.Manifest


private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private const val IMAGE_PICK_CODE = 1000
private const val PERMISSION_CODE = 1000

private lateinit var photofile: File

class CaptFoto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capt_foto)

        btnCamara.setOnClickListener {
            val takePic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photofile = getPhotoFile(FILE_NAME)


            val fileProv = FileProvider.getUriForFile(this, "com.example.fotovideo.fileprovider", photofile)
            takePic.putExtra(MediaStore.EXTRA_OUTPUT, fileProv)

            if(takePic.resolveActivity(this.packageManager) != null){
                startActivityForResult(takePic, REQUEST_CODE)

            }
            else{
                Toast.makeText(this, "Unable to Open Camera", Toast.LENGTH_SHORT).show()
            }


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
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
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


    private fun getPhotoFile(fileName: String): File {
        var externalF = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(fileName,".jpg", externalF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //val takenPicture = data?.extras?.get("data") as Bitmap
            val takenPicture = BitmapFactory.decodeFile(photofile.absolutePath)
            ImgShow.setImageBitmap(takenPicture)


        }else{
            if(requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
                //val takenPicture = data?.extras?.get("data") as Bitmap
                ImgShow.setImageURI(data?.data)
            }
            else{
                super.onActivityResult(requestCode, resultCode, data)

            }


        }
    }
}