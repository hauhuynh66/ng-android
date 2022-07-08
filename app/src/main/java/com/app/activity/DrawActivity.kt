package com.app.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.DrawUtilAdapter
import com.app.helper.SpanLinearLayoutManager
import com.app.ngn.R
import com.app.util.Utils
import com.app.view.DrawView
import java.io.File
import java.io.FileOutputStream

class DrawActivity : AppCompatActivity() {
    private val path = Environment.getExternalStorageDirectory().absolutePath + "/photo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_draw)

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

        val btn = findViewById<Button>(R.id.fg_test_clear)
        val draw = findViewById<DrawView>(R.id.fg_test_draw)
        btn.setOnClickListener {
            draw.reset()
        }

        val colorList = findViewById<RecyclerView>(R.id.fg_test_color)
        val sizeList = findViewById<RecyclerView>(R.id.fg_test_size)
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

        val save = findViewById<Button>(R.id.fg_test_save)
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

    private fun getArray(mode : Int) : ArrayList<DrawUtilAdapter.DrawUtilData>{
        when(mode){
            1->{
                return arrayListOf(
                    DrawUtilAdapter.DrawUtilData(Color.RED),
                    DrawUtilAdapter.DrawUtilData(Color.BLUE),
                    DrawUtilAdapter.DrawUtilData(Color.GREEN, true),
                    DrawUtilAdapter.DrawUtilData(Color.BLACK),
                    DrawUtilAdapter.DrawUtilData(Color.WHITE),
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