package com.app.task

import com.app.data.FileDisplay
import java.util.concurrent.Callable

class FileTask : Callable<ArrayList<FileDisplay>> {
    override fun call(): ArrayList<FileDisplay> {
        return arrayListOf()
    }
}