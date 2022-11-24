package com.app.task

import com.app.data.HttpResponse
import org.apache.commons.io.IOUtils
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

class GetHttpTask(private val url : String, private val params : Map<String, Any>? = null, private val header : MutableMap<String, String>? = null) : Callable<HttpResponse> {
    override fun call(): HttpResponse? {
        return try {
            val url = if (params!=null){
                URL(buildUrl(url, params))
            }else{
                URL(url)
            }

            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            header?.forEach{ (key, value) ->
                run{
                    conn.setRequestProperty(key, value)
                }
            }

            HttpResponse(conn.responseCode, conn.headerFields, IOUtils.toByteArray(conn.inputStream))
        }catch (e : Exception){
            null
        }
    }

    private fun buildUrl(baseUrl : String, params : Map<String, Any>) : String {
        val ret = StringBuilder(baseUrl)
        ret.append("?")
        params.onEachIndexed { index, entry ->
            ret.append(entry.key)
            ret.append("=")
            ret.append(entry.value)
            if(index!=params.size-1){
                ret.append("&")
            }
        }

        return ret.toString()
    }
}