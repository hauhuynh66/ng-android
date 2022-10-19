package com.app.data
import java.net.HttpURLConnection
import java.nio.charset.Charset

class HttpResponse(val responseCode : Int, private val headers : Map<String, List<String>>? = null, private val content: ByteArray = ByteArray(0)){
    fun get(charset: Charset = Charsets.UTF_8) : String {
        return content.toString(charset)
    }

    fun getHeader(headerName : String) : List<String>?{
        return headers?.get(headerName)
    }

    fun ok() : Boolean{
        return responseCode == HttpURLConnection.HTTP_OK
    }
}