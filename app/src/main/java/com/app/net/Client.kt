package com.app.net

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class Client(private val port : Int, private val ip : String) {
    private var socket: Socket
    private var outStream : OutputStream
    private var inputStream: InputStream
    init {
        try {
            socket = Socket(ip, port)
            outStream = socket.getOutputStream()
            inputStream = socket.getInputStream()
        }catch (e : Exception){
            throw e
        }
    }

    fun get(){

    }

    fun write(){

    }
}