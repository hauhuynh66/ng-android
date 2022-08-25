package com.app.activity.ex

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ExplorerListAdapter
import com.app.data.FileData
import com.app.ngn.R
import com.app.task.FileDataCallable
import com.app.task.TaskRunner
import com.app.util.Animation.Companion.crossfade
import kotlin.collections.ArrayList

class FileListActivity() : AppCompatActivity() {
    private lateinit var data: ArrayList<FileData>
    private var type: Int = 1
    private lateinit var list: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_file_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        type = bundle?.getInt("TYPE") ?: 1

        val progress = findViewById<ProgressBar>(R.id.progress)
        progress.visibility = View.VISIBLE
        list = findViewById(R.id.ac_file_list_list)
        list.visibility = View.INVISIBLE
        val runner = TaskRunner()

        val listener = object : ExplorerListAdapter.Listener{
            override fun onCheck(path: String) {

            }

            override fun onUnCheck(path: String) {

            }

            override fun onClick(path: String, isChecked: Boolean, position: Int) {

            }

            override fun onLongClick(path: String) {

            }
        }

        runner.execute(FileDataCallable(contentResolver, type),
            object : TaskRunner.Callback<ArrayList<FileData>>{
            override fun onComplete(result: ArrayList<FileData>) {
                this@FileListActivity.data = result
                list.layoutManager = GridLayoutManager(this@FileListActivity, 3)
                list.adapter = ExplorerListAdapter(this@FileListActivity, data, listener = listener, isGrid = true)
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