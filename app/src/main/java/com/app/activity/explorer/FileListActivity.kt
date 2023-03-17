package com.app.activity.explorer

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.general.ViewUtils

class FileListActivity : AppCompatActivity() {
    private var type: Int = 1
    private lateinit var list: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        ViewUtils.configTitle(toolbar, true)

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        type = bundle?.getInt("display") ?: 0

        val progress = findViewById<ProgressBar>(R.id.progress)

        progress.visibility = View.VISIBLE
        list = findViewById(R.id.list)
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