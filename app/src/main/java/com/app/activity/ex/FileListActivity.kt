package com.app.activity.ex

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ExplorerListAdapter
import com.app.ngn.R
import com.app.task.TaskRunner

class FileListActivity() : AppCompatActivity() {
    private var type: Int = 1
    private lateinit var list: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_file_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        type = bundle?.getInt("type") ?: 1

        val progress = findViewById<ProgressBar>(R.id.progress)
        progress.visibility = View.VISIBLE
        list = findViewById(R.id.ac_file_list_list)
        list.visibility = View.INVISIBLE
        val runner = TaskRunner()

        val callback = object : ExplorerListAdapter.Callback{
            override fun onCheck(position: Int) {

            }

            override fun onUnCheck(position: Int) {

            }

            override fun onClick(position: Int) {

            }

            override fun onLongClick(position: Int) {

            }
        }
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