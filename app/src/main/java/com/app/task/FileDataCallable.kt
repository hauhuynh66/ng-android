package com.app.task

import android.os.Environment
import com.app.data.FileData
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.concurrent.Callable

class FileDataCallable(private val extensionList:ArrayList<String>, val root: String) : Callable<ArrayList<FileData>> {
    override fun call(): ArrayList<FileData> {
        val ret = arrayListOf<FileData>()
        val root = Environment.getExternalStorageDirectory().absolutePath
        File(root).walk().forEach {
            for(extension: String in extensionList){
                if(it.extension == extension){
                    val attrs = Files.readAttributes(it.toPath(), BasicFileAttributes::class.java)
                    val date = attrs.creationTime().toMillis()
                    ret.add(FileData(it.name, Date(date), null, it.absolutePath, "FILE"))
                }
            }
        }
        return ret
    }
}