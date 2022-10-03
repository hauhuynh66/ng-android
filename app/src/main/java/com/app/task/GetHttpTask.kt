package com.app.task

import com.app.data.HttpResponseData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

class GetHttpTask(private val url : String, private val header : MutableMap<String, String>? = null) : Callable<HttpResponseData> {
    override fun call(): HttpResponseData {
        try {
            val url = URL(url)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            header?.forEach{ (key, value) ->
                run{
                    conn.setRequestProperty(key, value)
                }
            }

            val bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))
            val responseCode = conn.responseCode
            val content = StringBuilder()
            val iterator = bufferedReader.lineSequence().iterator()
            while (iterator.hasNext()){
                content.append(iterator.next())
            }

            return HttpResponseData(responseCode, content.toString())
        }catch (e : Exception){
            return HttpResponseData(-999, null)
        }
    }
}