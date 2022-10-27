package com.app.data

class ActionData(val resource: Int?, val text : String, val callback: Callback) {
    interface Callback {
        fun onClick()
    }
}