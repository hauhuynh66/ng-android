package com.app.activity.sport

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.fragment.sport.FootballResultFragment
import com.app.fragment.sport.SportListFragment
import com.app.ngn.R
import com.app.viewmodel.Sport

class SportMainActivity : AppCompatActivity() {
    private val stateModel : Sport by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)

        findViewById<TextView>(R.id.title).apply {
            text = "Sport"
        }

        stateModel.state.observe(this){
            println(stateModel.arg.value)
            when(it){
                1->{
                    supportFragmentManager.beginTransaction().replace(R.id.container, FootballResultFragment()).commit()
                }
                else->{
                    supportFragmentManager.beginTransaction().replace(R.id.container, SportListFragment()).commit()
                }
            }
        }
    }
}