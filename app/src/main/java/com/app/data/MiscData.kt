package com.app.data

class MiscData(val resource: Int?, val text : String, val listener: Listener) {
    public interface Listener{
        fun onClick()
    }
}