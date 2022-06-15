package com.app.activity

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.dialog.FileDownloadDialog
import com.app.dialog.ImageDialog
import com.app.ngn.R
import com.app.util.Check.Companion.checkPermissions

class CVActivity : AppCompatActivity() {
    private lateinit var src:ImageView
    private lateinit var out:ImageView
    private lateinit var path:String
    private lateinit var mainLayout: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_cv)
        src = findViewById(R.id.ac_cv_src)
        out = findViewById(R.id.ac_cv_out)
        val testBtn = findViewById<Button>(R.id.test)
        testBtn.setOnClickListener {
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val permissions = arrayListOf<String>()
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(checkPermissions(applicationContext, permissions)){

        }else{
            requestPermission()
        }

        mainLayout = findViewById(R.id.main_layout)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
            }
            R.id.cv_menu_choose->{
                ImageDialog(object : ImageDialog.Callback{
                    override fun onConfirm(path: String?) {
                        this@CVActivity.path = path!!
                        src.setImageBitmap(BitmapFactory.decodeFile(path))
                    }
                }).show(supportFragmentManager, "IMAGE DIALOG")
            }
            R.id.cv_menu_download->{
                FileDownloadDialog().show(supportFragmentManager, "DOWNLOAD DIALOG")
            }
        }
        return true
    }

    private fun requestPermission(){
        val permission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permission ->
            run {
                when {
                    permission.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false) -> {

                    }
                    else -> {
                        super.finish()
                    }
                }
            }
        }
        permission.launch(arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cv_menu, menu)
        return true
    }

}