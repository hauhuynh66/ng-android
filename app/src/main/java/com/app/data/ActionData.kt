package com.app.data

class ActionData(val resource: Int?, val text : String,  val callback: Callback? = object : Callback{}) {
    interface Callback {
        fun onClick(){

        }
    }
}