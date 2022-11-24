package com.app.data.explorer

import java.io.File
import java.util.concurrent.Callable

class FileInfoTask(private val root : String?) : Callable<List<FileInfo>> {
    override fun call(): List<FileInfo> {
        val list = mutableListOf<FileInfo>()
        if(root == null) return list
        val file = File(root)
        if(file.exists()) {
            file.listFiles()?.forEach { f ->
                list.add(FileInfo.fromFile(f))
            }
        }
        return list
    }
}