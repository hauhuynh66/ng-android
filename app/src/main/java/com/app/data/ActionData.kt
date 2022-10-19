package com.app.data

class ActionData(val resource: Int?, val text : String, val listener: Listener) {
    interface Listener{
        fun onClick()
    }
}