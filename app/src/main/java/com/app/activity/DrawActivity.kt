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
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ColorAdapter
import com.app.adapter.DrawAdapter
import com.app.adapter.ValueAdapter
import com.app.ngn.R
import com.general.FileUtils
import com.general.ViewUtils
import com.custom.DrawView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream


class DrawActivity : AppCompatActivity() {
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
                Intent(Intent.ACTION_PICK)
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            lc.launch(chooserIntent)
        }

        changeBg.setOnLongClickListener {
            if(draw.bg!=null){
                draw.clearBackground()
            }
            true
        }

        btn.setOnClickListener {
            draw.prev()
        }

        val colorList = findViewById<RecyclerView>(R.id.ac_draw_color)
        val sizeList = findViewById<RecyclerView>(R.id.ac_draw_size)

        colorList.layoutManager = ViewUtils.getFixedHorizontalLayoutManager(this)
        sizeList.layoutManager = ViewUtils.getFixedHorizontalLayoutManager(this)

        val colorAdapter = ColorAdapter(
            listOf(
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.CYAN
            )
        )
        colorAdapter.onItemSelectedListener = object : DrawAdapter.OnItemSelectedListener<Int> {
            override fun onClick(value: Int) {
                draw.changeColor(value.toString().toInt())
            }
        }

        val sizeAdapter = ValueAdapter(
            listOf(
                12,
                24,
                36,
                72
            )
        )

        sizeAdapter.onItemSelectedListener = object : DrawAdapter.OnItemSelectedListener<Number> {
            override fun onClick(value: Number) {
                draw.changePathWidth(value.toString().toFloat())
            }
        }

        colorList.adapter = colorAdapter
        sizeList.adapter = sizeAdapter

        val save = findViewById<Button>(R.id.ac_draw_save)
        save.setOnClickListener {
            val intent = Intent()
            val file = FileUtils.createImageFile(path)
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
}