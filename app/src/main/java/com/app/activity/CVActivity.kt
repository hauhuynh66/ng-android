package com.app.activity

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.app.fragment.cv.ORB
import com.app.ngn.R
import com.app.util.Utils

class CVActivity() : AppCompatActivity(){
    private val dir = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera"
    private lateinit var cameraResult : ActivityResultLauncher<Uri>
    private var photoURI : Uri? = null
    private var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_cv)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val mode = intent.extras!!.getString("mode")
        when(mode){
            "Orb"->{
                supportFragmentManager.beginTransaction().replace(R.id.ac_cv_container, ORB()).commit()
            }
        }


        cameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it == true) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoURI)
                val currentFragment = supportFragmentManager.findFragmentById(R.id.ac_cv_container);
                when(mode){
                    "Orb"->{
                        (currentFragment as ORB).process(bitmap)
                    }
                }
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
                val file = Utils.createImageFile(dir)
                photoURI = if(file!=null){
                    FileProvider.getUriForFile(this,"com.app.activity.ngn", file)
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
}