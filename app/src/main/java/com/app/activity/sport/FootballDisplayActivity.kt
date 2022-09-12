package com.app.activity.sport

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.data.FootballTeam
import com.app.ngn.R
import com.app.viewmodel.Football

class FootballDisplayActivity : AppCompatActivity() {
    private val model : Football by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_holder)
        val bundle = intent.extras
        when(bundle?.getString("type")){
            "td"->{
                val team : FootballTeam? = bundle.getParcelable("team")
                model.selectedClub.value = team
            }
        }
    }


}