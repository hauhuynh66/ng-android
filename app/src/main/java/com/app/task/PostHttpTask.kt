package com.app.task

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

class PostHttpTask(
    private val url: URL,
    private val json : JSONObject,
    private val requestContent : RequestContent,
    private val headerName : String? = null
) : Callable<String>{
    enum class RequestContent{
        Body,
        Header
    }
    override fun call(): String {
        val jsonString = json.toString()
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true

        try {
            val input = jsonString.toByteArray(Charsets.UTF_8)
            conn.outputStream.write(input)

            return if(requestContent == RequestContent.Header){
                conn.getHeaderField(headerName)?:"No value"
            }else{
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val ret = StringBuilder()
                reader.useLines {
                    ret.append(it)
                }
                ret.toString()
            }
        }finally {

        }
    }
}