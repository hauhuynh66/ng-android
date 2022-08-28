package com.app.activity.sport

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.fragment.sport.FootballFixtureFragment
import com.app.fragment.sport.FootballMatchDetailFragment
import com.app.fragment.sport.FootballTeamDetailFragment
import com.app.ngn.R
import com.app.util.Formatter.Companion.formatDate
import com.app.viewmodel.Football
import java.util.*

class FootballMainActivity : AppCompatActivity() {
    private val model : Football by viewModels()
    private lateinit var titleHolder : ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)
        model.init(this)
        titleHolder = findViewById(R.id.title_holder)

        model.state.observe(this){
            val fragment = when(model.state.value!!){
                Football.State.TeamDetails->{
                    FootballTeamDetailFragment()
                }
                Football.State.MatchDetails->{
                    FootballMatchDetailFragment()
                }
                else->{
                    FootballFixtureFragment()
                }
            }
            changeTitleDisplay(titleHolder, model.state.value!!)
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            println(model.currentDate.value.toString())
        }
    }
    private fun changeTitleDisplay(titleBar : View, state : Football.State){
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
    }

    override fun onBackPressed() {
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
    }
}