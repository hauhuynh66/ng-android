package com.app.activity

import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.EXListAdapter
import com.app.data.FileData
import com.app.ngn.R
import com.app.task.FileDataCallable
import com.app.task.TaskRunner
import com.app.util.Animation.Companion.crossfade

class FileListViewActivity() : AppCompatActivity() {
    private lateinit var data: ArrayList<FileData>
    private lateinit var type: String
    private lateinit var extensionList : ArrayList<String>
    private lateinit var list: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_file_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        type = if(bundle!=null){
            bundle.getString("TYPE")!!
        }else{
            "ALL"
        }
        extensionList = arrayListOf()
        when(type){
            "PICTURE"->{
                extensionList.addAll(listOf("png","jpeg", "jpg"))
            }
            "MUSIC"->{
                extensionList.addAll(listOf("mp3"))
            }
            "VIDEO"->{
                extensionList.addAll(listOf("mp4", "flv"))
            }
            "DOC"->{
                extensionList.addAll(listOf("csv, pdf, txt"))
            }
            else->{

            }
        }
        val progress = findViewById<ProgressBar>(R.id.progress)
        progress.visibility = View.VISIBLE
        list = findViewById(R.id.ac_file_list_list)
        list.visibility = View.INVISIBLE
        val runner = TaskRunner()
        runner.execute(FileDataCallable(extensionList, Environment.getExternalStorageDirectory().absolutePath),
            object : TaskRunner.Callback<ArrayList<FileData>>{
            override fun onComplete(result: ArrayList<FileData>) {
                this@FileListViewActivity.data = result
                list.layoutManager = GridLayoutManager(this@FileListViewActivity, 4)
                list.adapter = EXListAdapter(this@FileListViewActivity, data, isGrid = true)
                crossfade(arrayListOf(list), arrayListOf(progress), duration = 1000)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
            }
        }
        return true
    }
}