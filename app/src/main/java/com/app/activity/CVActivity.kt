package com.app.activity

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.app.ngn.R
import com.app.util.CVUtils.Companion.featureMatching
import com.app.util.CVUtils.Companion.sift
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CVActivity : AppCompatActivity() {
    private val dir = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera"
    private lateinit var cameraResult : ActivityResultLauncher<Uri>
    private var photoURI : Uri? = null
    private var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_cv)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val image = findViewById<ImageView>(R.id.ac_cv_camera_pic)
        val transformed = findViewById<ImageView>(R.id.ac_cv_pic_transformed)
        cameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it == true) {
                count++;
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoURI)
                if(count>1){
                    val temp = (image.drawable as BitmapDrawable).bitmap
                    transformed.setImageBitmap(featureMatching(bitmap, temp))
                }else{
                    transformed.setImageBitmap(sift(bitmap))
                }
                image.setImageBitmap(bitmap)

            }else{
                Toast.makeText(this@CVActivity, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cv_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cv_menu_camera->{
                val file = createImageFile()
                photoURI = if(file!=null){
                    FileProvider.getUriForFile(this,"com.app.activity.CVActivity.provider", file)
                }else{
                    null
                }

                if(photoURI!=null){
                    cameraResult.launch(photoURI)
                }
            }
            else->{

            }
        }
        return true
    }

    private fun createImageFile() : File?{
        val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val name = formatter.format(Date()).plus("_IMG.jpg")
        if(!File(dir).exists()){
            val success = File(dir).mkdirs()
            if(!success){
                return null
            }
        }
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