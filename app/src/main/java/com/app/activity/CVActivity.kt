package com.app.activity

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.app.fragment.cv.ORBFragment
import com.app.ngn.R
import com.app.util.Utils

class CVActivity : AppCompatActivity(){
    private val dir = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera"
    private lateinit var cameraResult : ActivityResultLauncher<Uri>
    private var photoURI : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)
        val toolbarHolder = findViewById<LinearLayoutCompat>(R.id.toolbar_holder)
        val toolbar = toolbarHolder.findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<TextView>(R.id.title).apply {
            text = "Computer Vision"
        }

        val mode = intent.extras!!.getString("mode")
        when(mode){
            "ORB"->{
                supportFragmentManager.beginTransaction().replace(R.id.container, ORBFragment()).commit()
            }
        }


        cameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it == true) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoURI)
                val currentFragment = supportFragmentManager.findFragmentById(R.id.container);
                when(mode){
                    "ORB"->{
                        (currentFragment as ORBFragment).process(bitmap)
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