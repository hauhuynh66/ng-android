package com.app.activity.explorer

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.app.fragment.explorer.EXGroupFragment
import com.app.fragment.explorer.EXListFragment
import com.app.ngn.R
import com.general.Permission
import com.general.ViewUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.system.exitProcess

class FileExplorerActivity : AppCompatActivity(){
    private lateinit var tabs : TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requiredPermissions = Permission(listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),this, object : Permission.Callback{
            override fun onDenied(permission: String) {
                super.onDenied(permission)
                exitProcess(0)
            }
        })
        requiredPermissions.checkOrRequest()

        setContentView(R.layout.ac_viewpager)
        val pager = findViewById<ViewPager2>(R.id.pager)
        tabs = findViewById(R.id.tabs)
        pager.adapter = ExplorerFragmentAdapter(supportFragmentManager, lifecycle)
        pager.setPageTransformer(ViewUtils.Companion.ZoomOutPageTransformer())
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

    inner class ExplorerFragmentAdapter(fm: FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm, lc){
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->{
                    EXGroupFragment()
                }
                1->{
                    EXListFragment()
                }
                else->{
                    Fragment()
                }
            }
        }
    }
}