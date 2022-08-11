package com.app.data

abstract class Device {
    protected var value : Long = 0
    abstract fun interact(vararg values : Any)
    abstract fun update()
}