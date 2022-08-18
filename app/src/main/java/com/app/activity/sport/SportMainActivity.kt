package com.app.activity.sport

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.fragment.sport.FootballResultFragment
import com.app.fragment.sport.FootballTeamDetailFragment
import com.app.fragment.sport.SportListFragment
import com.app.ngn.R
import com.app.viewmodel.Sport

class SportMainActivity : AppCompatActivity() {
    enum class SportStates{
        SportList,
        FootballResultList,
        FootballTeamDetail,
        FootballMathDetail
    }
    private val stateModel : Sport by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)

        findViewById<TextView>(R.id.title).apply {
            text = "Sport"
        }

        stateModel.state.observe(this){
            when(it){
                SportStates.FootballResultList->{
                    supportFragmentManager.beginTransaction().replace(R.id.container, FootballResultFragment()).commit()
                }
                SportStates.FootballTeamDetail->{
                    supportFragmentManager.beginTransaction().replace(R.id.container, FootballTeamDetailFragment()).commit()
                }
                else->{
                    supportFragmentManager.beginTransaction().replace(R.id.container, SportListFragment()).commit()
                }
            }
        }
    }

    override fun onBackPressed() {
        when(stateModel.state.value){
            SportStates.SportList->{
                super.onBackPressed()
            }
            SportStates.FootballTeamDetail, SportStates.FootballMathDetail->{
                stateModel.state.value = SportStates.FootballResultList
            }
            SportStates.FootballResultList->{
                stateModel.state.value = SportStates.SportList
            }
        }
    }
}