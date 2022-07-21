package com.app.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.app.adapter.ExplorerFragmentAdapter
import com.app.ngn.R
import com.app.util.Check.Companion.checkPermissions
import com.app.util.Utils
import com.google.android.material.tabs.TabLayout
import kotlin.system.exitProcess

class FileExplorerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContentView(R.layout.ac_flx)
        val pager = findViewById<ViewPager>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = ExplorerFragmentAdapter(supportFragmentManager, tabs)
        pager.setPageTransformer(true, Utils.Companion.ZoomOutPageTransformer())
        tabs.setupWithViewPager(pager)
    }

    private fun checkPermissions(){
        val requiredPermissions =
            arrayListOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permission -> run {
                when{
                    permission.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false) -> {

                    }
                    permission.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false) -> {

                    }
                    else->{
                        exitProcess(0)
                    }
                }
            }
        }

        if(!checkPermissions(this, requiredPermissions)){
            launcher.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }
}