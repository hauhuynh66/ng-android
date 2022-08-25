package com.app.activity.sport

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.fragment.sport.FootballResultFragment
import com.app.fragment.sport.FootballTeamDetailFragment
import com.app.ngn.R
import com.app.viewmodel.Football

class FootballMainActivity : AppCompatActivity() {
    private val model : Football by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)
        model.init(this)

        findViewById<TextView>(R.id.title).apply {
            text = "Football"
        }

        model.state.observe(this){
            val fragment = when(model.state.value!!){
                Football.State.TeamDetails->{
                    FootballTeamDetailFragment()
                }
                else->{
                    FootballResultFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }
    }
}