package com.app.task

import com.app.data.HttpResponseData
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

class PostHttpTask(private val url : String, private val postData : JSONObject, private val header : MutableMap<String, String>? = null, private val extra : String? = null) : Callable<HttpResponseData>{
    override fun call(): HttpResponseData {
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

            val responseCode = conn.responseCode.toString()
            val content = StringBuilder()
            val bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))
            while (bufferedReader.lineSequence().iterator().hasNext()){
                content.append(bufferedReader.lineSequence().iterator().next())
            }

            val ex = if(extra != null){
                conn.getHeaderField(extra)
            }else{
                null
            }
            return HttpResponseData(responseCode, content.toString(), ex)
        }catch (e : Exception){
            return HttpResponseData("ERROR", null)
        }
    }
}