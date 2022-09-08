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

        /*model.state.observe(this){
            val fragment = when(model.state.value!!){
                Football.State.TeamDetails->{
                    FootballTeamDetailFragment()
                }
                Football.State.MatchDetails->{
                    FootballMatchDetailFragment()
                }
                Football.State.Table->{
                    FootballStandingFragment()
                }
                else->{
                    FootballFixtureFragment()
                }
            }
            changeTitleDisplay(titleHolder, model.state.value!!)
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            println(model.currentDate.value.toString())
        }*/
    }
    /*private fun changeTitleDisplay(titleBar : View, state : Football.State){
        val title = titleBar.findViewById<TextView>(R.id.title)
        val left = titleBar.findViewById<Button>(R.id.calendar_previous)
        val right = titleBar.findViewById<Button>(R.id.calendar_next)

        left.setOnClickListener {
            model.currentDate.value = Date(model.currentDate.value!!.time - 3600*24*1000)
        }

        right.setOnClickListener {
            model.currentDate.value = Date(model.currentDate.value!!.time + 3600*24*1000)
        }

        when(state){
            Football.State.Fixtures->{
                model.currentDate.observe(this){
                    title.apply {
                        text = formatDate(model.currentDate.value!!, "yyyy-MM-dd")
                    }
                }

                val calendar = GregorianCalendar()
                calendar.time = model.currentDate.value!!
                title.setOnClickListener {
                    val dpListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                        calendar.set(Calendar.YEAR, y)
                        calendar.set(Calendar.MONTH, m)
                        calendar.set(Calendar.DATE, d)
                        model.currentDate.value = calendar.time
                    }

                    DatePickerDialog(this@FootballMainActivity,
                        dpListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)
                    ).show()
                }

                left.visibility = View.VISIBLE
                right.visibility = View.VISIBLE
            }
            else->{
                title.text = ""
                left.visibility = View.GONE
                right.visibility = View.GONE
            }
        }
    }*/

    /*override fun onBackPressed() {
        when(model.state.value){
            Football.State.Fixtures->{
                super.onBackPressed()
            }
            Football.State.MatchDetails->{
                model.state.value = Football.State.Fixtures
            }
            Football.State.TeamDetails->{
                model.state.value = Football.State.Fixtures
            }
            Football.State.PlayerDetails->{
                model.state.value = Football.State.TeamDetails
            }
            else->{
                model.state.value = Football.State.Fixtures
            }
        }
    }*/
}