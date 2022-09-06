package com.app.activity.ex

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.app.adapter.ExplorerFragmentAdapter
import com.app.fragment.ex.EXListFragment
import com.app.ngn.R
import com.app.util.Check.Companion.checkPermissions
import com.app.util.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.system.exitProcess

class FileExplorerActivity : AppCompatActivity(), EXListFragment.Listener{
    private lateinit var tabs : TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContentView(R.layout.ac_viewpager)
        val pager = findViewById<ViewPager2>(R.id.pager)
        tabs = findViewById(R.id.tabs)
        pager.adapter = ExplorerFragmentAdapter(supportFragmentManager, lifecycle)
        pager.setPageTransformer(Utils.Companion.ZoomOutPageTransformer())
        TabLayoutMediator(tabs, pager){
            tab, pos -> run{
                when(pos){
                    0->{
                        tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_access_time)
                    }
                    1->{
                        tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_folder_open)
                    }
                    else->{

                    }
                }
            }
        }.attach()
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

    override fun onMultipleChanged(isMultiple: Boolean) {
        if(isMultiple){
            tabs.visibility = View.GONE
        }else{
            tabs.visibility = View.VISIBLE
        }
    }
}