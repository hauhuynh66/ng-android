package com.app.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.DrawUtilAdapter
import com.app.helper.SpanLinearLayoutManager
import com.app.ngn.R
import com.app.util.Utils
import com.app.view.DrawView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream


class Draw : AppCompatActivity() {
    private val requestImage = 1
    private val path = Environment.getExternalStorageDirectory().absolutePath + "/photo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_draw)

        val draw = findViewById<DrawView>(R.id.ac_draw_draw)
        val lc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.data!=null){
                val uri = it.data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                draw.changeBackground(bitmap)
            }
        }

        if (!File(path).exists()) {
            val success = File(path).mkdirs()
            if (!success) {
                finish()
            }
        } else {
            if (!File(path).isDirectory) {
                finish()
            }
        }

        val btn = findViewById<Button>(R.id.ac_draw_clear)
        val changeBg = findViewById<FloatingActionButton>(R.id.ac_draw_change_picture)
        changeBg.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            lc.launch(chooserIntent)
        }

        btn.setOnClickListener {
            draw.prev()
        }

        val colorList = findViewById<RecyclerView>(R.id.ac_draw_color)
        val sizeList = findViewById<RecyclerView>(R.id.ac_draw_size)
        colorList.layoutManager =
            SpanLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sizeList.layoutManager =
            SpanLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        colorList.adapter = DrawUtilAdapter(this,
            getArray(1),
            0,
            object : DrawUtilAdapter.Listener {
                override fun onClick(value: Int) {
                    draw.changeColor(value)
                }
            }
        )

        sizeList.adapter = DrawUtilAdapter(this,
            getArray(2),
            1,
            object : DrawUtilAdapter.Listener {
                override fun onClick(value: Int) {
                    draw.changePathWidth(value.toFloat())
                }
            }
        )

        val save = findViewById<Button>(R.id.ac_draw_save)
        save.setOnClickListener {
            val intent = Intent()
            val file = Utils.createImageFile(path)
            val fos = FileOutputStream(file)
            val bmp = draw.mBitmap
            bmp!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            file!!.apply {
                intent.putExtra("bmp", path)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            requestImage->{
                val uri = data!!.data
                println(uri)
            }
            else->{

            }
        }
    }

    private fun getArray(mode : Int) : ArrayList<DrawUtilAdapter.DrawUtilData>{
        when(mode){
            1->{
                return arrayListOf(
                    DrawUtilAdapter.DrawUtilData(Color.RED),
                    DrawUtilAdapter.DrawUtilData(Color.BLUE),
                    DrawUtilAdapter.DrawUtilData(Color.GREEN, true),
                    DrawUtilAdapter.DrawUtilData(Color.BLACK),
                    DrawUtilAdapter.DrawUtilData(Color.YELLOW),
                )
            }
            2->{
                return arrayListOf(
                    DrawUtilAdapter.DrawUtilData(6),
                    DrawUtilAdapter.DrawUtilData(12, true),
                    DrawUtilAdapter.DrawUtilData(36),
                    DrawUtilAdapter.DrawUtilData(48),
                    DrawUtilAdapter.DrawUtilData(72)
                )
            }
            else->{
                return arrayListOf()
            }
        }
    }
}