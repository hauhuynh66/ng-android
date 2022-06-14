package com.app.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner
import com.app.util.AnimateUtils.Companion.crossfade
import java.io.File
import java.io.FileOutputStream

class CVActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var filePath:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_cv)
        filePath = Environment.getExternalStorageDirectory().absolutePath + "/photo"
        val src = findViewById<ImageView>(R.id.ac_cv_src)
        val out = findViewById<ImageView>(R.id.ac_cv_out)
        val task = TaskRunner()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        progressBar = findViewById(R.id.progress)
        mainLayout = findViewById(R.id.main_layout)
        mainLayout.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        task.execute(ImageCallable("https://picsum.photos/id/237/200/300"), object: TaskRunner.Callback<Bitmap?>{
            override fun onComplete(result: Bitmap?) {
                src.setImageBitmap(result)
                crossfade(mainLayout, progressBar, 1000)
                val dir = File(filePath)
                if(!dir.exists()){
                    dir.mkdir()
                }
                val image = File(dir, "dog.png")
                println(image.absolutePath)
                if(!image.exists()){
                    val fOut = FileOutputStream(image)
                    result!!.apply {
                        this.compress(Bitmap.CompressFormat.PNG, 90,fOut)
                        fOut.flush()
                        fOut.close()
                    }
                }else{
                    println("existed")
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
            }
        }
        return true
    }

}