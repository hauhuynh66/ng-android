package com.app.activity.cv

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.app.ngn.R
import com.app.util.CVUtils.Companion.sift
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CVActivity : AppCompatActivity() {
    private val dir = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera"
    private lateinit var cameraResult : ActivityResultLauncher<Uri>
    private lateinit var photoURI : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_cv)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        photoURI = FileProvider.getUriForFile(this,"com.app.activity.cv.CVActivity.provider", createImageFile()!!)
        val image = findViewById<ImageView>(R.id.ac_cv_camera_pic)
        val transformed = findViewById<ImageView>(R.id.ac_cv_pic_transformed)
        cameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it == true) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoURI)
                image.setImageBitmap(bitmap)
                //transformed.setImageBitmap(gaussianFilter(bitmap))
                transformed.setImageBitmap(sift(bitmap))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cv_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cv_menu_camera->{
                cameraResult.launch(photoURI)
            }
            else->{

            }
        }
        return true
    }

    private fun createImageFile() : File?{
        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        val name = formatter.format(Date()).plus("_IMG.jpg")
        val file = File(dir, name)
        return if(!file.exists()){
            val success = file.createNewFile()
            if(success){
                file
            }else{
                null
            }
        }else{
            file
        }
    }
}