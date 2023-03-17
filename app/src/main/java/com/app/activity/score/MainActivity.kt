package com.app.activity.score

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.data.score.ScoreContextHolder
import com.app.data.score.ScoreFlag
import com.app.fragment.score.FootballFixtureFragment
import com.app.fragment.score.FootballMatchDetailFragment
import com.app.fragment.score.FootballStandingFragment
import com.app.fragment.score.FootballTeamDetailFragment
import com.app.ngn.R

class MainActivity : AppCompatActivity() {
    private val data : ScoreContextHolder by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, FootballFixtureFragment(), "fixture")
            .commit()

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount < 1)
        {
            super.onBackPressed()
        }else
        {
            supportFragmentManager.popBackStack()
        }
    }

    fun changeToolbar()
    {

    }

    fun navigate(flag : ScoreFlag)
    {
        val fragment : Fragment
        val tag : String

        this.data.flag = flag
        when(flag)
        {
            ScoreFlag.Team ->{
                fragment = FootballTeamDetailFragment()
                tag = "team"
            }
            ScoreFlag.Match ->{
                fragment = FootballMatchDetailFragment()
                tag = "match"
            }
            ScoreFlag.Standing ->{
                fragment = FootballStandingFragment()
                tag = "standing"
            }
            else->{
                return
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag)
            .setCustomAnimations(
                R.anim.flip_left_in,
                R.anim.flip_right_out,
                R.anim.flip_right_in,
                R.anim.flip_left_out
            )
            .addToBackStack(tag)
            .commit()
    }
}