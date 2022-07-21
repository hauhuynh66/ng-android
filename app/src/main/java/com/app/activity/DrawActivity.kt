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
import com.app.adapter.DrawAdapter
import com.app.helper.SpanLinearLayoutManager
import com.app.ngn.R
import com.app.util.Utils
import com.app.view.DrawView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream


class DrawActivity : AppCompatActivity() {
    private val path = Environment.getExternalStorageDirectory().absolutePath + "/photo"
    private lateinit var colorAdapter : DrawAdapter
    private lateinit var sizeAdapter: DrawAdapter
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

        colorAdapter = DrawAdapter(this,
            getArray(1),
            0,
            object : DrawAdapter.Listener {
                override fun onClick(value: Int) {
                    draw.changeColor(value)
                    val pos = colorAdapter.data.indexOf(colorAdapter.data.filter { it.value==value }[0])
                    colorAdapter.data.filter { it.value==value }[0].selected = true
                    colorAdapter.notifyItemChanged(pos)
                }
            }
        )

        sizeAdapter = DrawAdapter(this,
            getArray(2),
            1,
            object : DrawAdapter.Listener {
                override fun onClick(value: Int) {
                    draw.changePathWidth(value.toFloat())
                    val pos = colorAdapter.data.indexOf(colorAdapter.data.filter { it.value==value }[0])
                    colorAdapter.data.filter { it.value==value }[0].selected = true
                    colorAdapter.notifyItemChanged(pos)
                }
            }
        )

        val colorList = findViewById<RecyclerView>(R.id.ac_draw_color)
        val sizeList = findViewById<RecyclerView>(R.id.ac_draw_size)

        colorList.layoutManager =
            SpanLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sizeList.layoutManager =
            SpanLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        colorList.adapter = colorAdapter

        sizeList.adapter = sizeAdapter

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

    private fun getArray(mode : Int) : ArrayList<DrawAdapter.DrawUtilData>{
        when(mode){
            1->{
                return arrayListOf(
                    DrawAdapter.DrawUtilData(Color.RED),
                    DrawAdapter.DrawUtilData(Color.BLUE),
                    DrawAdapter.DrawUtilData(Color.GREEN, true),
                    DrawAdapter.DrawUtilData(Color.BLACK),
                    DrawAdapter.DrawUtilData(Color.YELLOW),
                )
            }
            2->{
                return arrayListOf(
                    DrawAdapter.DrawUtilData(6),
                    DrawAdapter.DrawUtilData(12, true),
                    DrawAdapter.DrawUtilData(36),
                    DrawAdapter.DrawUtilData(48),
                    DrawAdapter.DrawUtilData(72)
                )
            }
            else->{
                return arrayListOf()
            }
        }
    }
}