package com.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.data.media.Image
import com.app.ngn.R

class ImageGallery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_list)
        //TestUtils.ipsumImage()
        val list = Image.getExternalImages(this.contentResolver)
    }
}