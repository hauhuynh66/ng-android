package com.app.data

import android.graphics.Bitmap

class MiscData(val bitmap:Bitmap?, val listener: Listener) {
    public interface Listener{
        fun onClick()
    }
}