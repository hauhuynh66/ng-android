package com.app.data

class MiscData(val resource: Int?, val listener: Listener) {
    public interface Listener{
        fun onClick()
    }
}