package com.app.activity.sport

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.data.FootballResult
import com.app.data.FootballTeam
import com.app.fragment.sport.FootballMatchDetailFragment
import com.app.fragment.sport.FootballTeamDetailFragment
import com.app.ngn.R
import com.app.viewmodel.FootballDisplay

class FootballDisplayActivity : AppCompatActivity() {
    private val model : FootballDisplay by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)
        val bundle = intent.extras

        when(bundle?.getString("type")){
            "team"->{
                model.team = bundle.getParcelable("team")
                supportFragmentManager.beginTransaction().replace(R.id.container, FootballTeamDetailFragment()).commit()
            }
            "match"->{
                model.matchOverview = bundle.getParcelable("match")
                supportFragmentManager.beginTransaction().replace(R.id.container, FootballMatchDetailFragment()).commit()
            }
        }
    }


}