package com.app.activity.sport

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.app.adapter.FootballFragmentAdapter
import com.app.dialog.OptionBottomSheet
import com.app.ngn.R
import com.app.util.Utils
import com.app.viewmodel.Football
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FootballMainActivity : AppCompatActivity() {
    private val model : Football by viewModels()
    private lateinit var titleHolder : Toolbar
    private lateinit var title : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_viewpager)
        model.init(this)
        titleHolder = findViewById(R.id.toolbar)
        title = findViewById(R.id.title)
        title.text = "Premier League"
        val listener = object : OptionBottomSheet.Listener{
            override fun onClick(option: String?) {
                when(option){
                    else->{
                        model.league.value = 39
                    }
                }
            }
        }

        titleHolder.visibility = View.VISIBLE
        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = FootballFragmentAdapter(supportFragmentManager, lifecycle)
        pager.setPageTransformer(Utils.Companion.ZoomOutPageTransformer())
        TabLayoutMediator(tabs, pager){ tab, pos ->
            run{
                when(pos){
                    0->{
                        tab.text = "Fixture"
                    }
                    1->{
                        tab.text = "Table"
                    }
                    2->{
                        tab.text = "Stats"
                    }
                    3->{
                        tab.text = "..."
                    }
                }
            }
        }.attach()

        title.apply {
            setOnClickListener {
                val optionData = arrayListOf(
                    OptionBottomSheet.Data("Premier League"),
                    OptionBottomSheet.Data("Champion League"),
                    OptionBottomSheet.Data("La Liga"),
                    OptionBottomSheet.Data("Serie A")
                )
                OptionBottomSheet(optionData, listener).show(supportFragmentManager, "LEAGUE-SEL")
            }
        }
    }
}