package com.general

import android.os.Environment
import com.app.task.DownloadImageTask
import com.app.task.TaskRunner
import java.net.URL
import java.util.*

class TestUtils {
    companion object{
        fun ipsumImage(){
            val taskRunner = TaskRunner()
            val random = Random()
            for(i in 0..10){
                val prefix = "https://picsum.photos/id/"
                val id = random.nextInt(300)
                val url = "${prefix}${id}/200/300"
                val path = Environment.getExternalStorageDirectory().absolutePath + "/photo/${
                    Generator.generateString(
                        10
                    )
                }.jpg"
                taskRunner.execute(DownloadImageTask(URL(url), path), object : TaskRunner.Callback<Boolean>{
                    override fun onComplete(result: Boolean) {
                        println(result)
                    }
                })
            }
        }
    }
}