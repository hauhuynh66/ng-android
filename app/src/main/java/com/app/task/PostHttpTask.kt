package com.app.task

import com.app.data.HttpResponse
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

class PostHttpTask(private val url : String,
                   private val postData : JSONObject,
                   private val header : MutableMap<String, String>? = null,
                   private val extra : String? = null) : Callable<HttpResponse>{
    override fun call(): HttpResponse? {
        try {
            val url = URL(url)
            val conn = url.openConnection() as HttpURLConnection
            val data = postData.toString().toByteArray(Charsets.UTF_8)

            conn.requestMethod = "POST"
            conn.setRequestProperty("Accept", "application/json")

            header?.forEach{ (key, value) ->
                run{
                    conn.setRequestProperty(key, value)
                }
            }

            conn.doOutput = true

            conn.outputStream.write(data)


            return HttpResponse(conn.responseCode, conn.headerFields, IOUtils.toByteArray(conn.inputStream))
        }catch (e : Exception){
            return HttpResponse(HttpURLConnection.HTTP_BAD_REQUEST)
        }
    }
}