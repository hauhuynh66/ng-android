package com.app.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.app.ngn.R
import com.app.util.CVOperation
import com.app.util.FileOperation
import java.io.File

/**
 * CV Activity
 *
 */
class CVActivity : AppCompatActivity(){
    private val dir = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera"
    private lateinit var cameraResult : ActivityResultLauncher<Uri>
    private lateinit var fileSelectResult : ActivityResultLauncher<Intent>
    private lateinit var source : ImageView
    private lateinit var result : ImageView
    private var photoURI : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_cv)

        source = findViewById(R.id.src)
        result = findViewById(R.id.res)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.title).apply {
            text = getString(R.string.cv_title)
        }

        cameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it == true) {
                val bitmap = CVOperation.getBitmap(photoURI!!, contentResolver, Bitmap.Config.ARGB_8888)
                source.setImageBitmap(bitmap)
                result.setImageBitmap(CVOperation.faceDetect(bitmap!!))
            }else{
                val file = File(photoURI?.path)
                file.delete()
                Toast.makeText(this@CVActivity, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        fileSelectResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.data?.data !=null){
                val bitmap = CVOperation.getBitmap(it.data?.data!!, contentResolver, Bitmap.Config.ARGB_8888)
                source.setImageBitmap(bitmap)
                result.setImageBitmap(CVOperation.faceDetect(bitmap!!))
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
                val file = FileOperation.createImageFile(dir)

                photoURI = if(file!=null){
                    FileProvider.getUriForFile(this,"com.app.activity.ngn", file)
                }else{
                    null
                }

                if(photoURI!=null){
                    cameraResult.launch(photoURI)
                }
            }
            R.id.cv_menu_open->{
                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"

                val pickIntent =
                    Intent(Intent.ACTION_PICK)
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")

                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

                fileSelectResult.launch(chooserIntent)
            }
            android.R.id.home->{
                super.onBackPressed()
            }
            else->{

            }
        }
        return true
    }
}