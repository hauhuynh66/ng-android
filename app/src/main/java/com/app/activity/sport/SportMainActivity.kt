package com.app.activity.sport

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.SportAdapter
import com.app.ngn.R

class SportMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_list_2)

        findViewById<TextView>(R.id.title).apply {
            text = "Sport"
        }

        val sports = arrayListOf(
            SportAdapter.Sport("Football"),
            SportAdapter.Sport("Volleyball"),
            SportAdapter.Sport("Baseball"),
            SportAdapter.Sport("Basketball")
        )
        val list = findViewById<RecyclerView>(R.id.item_list)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list.adapter = SportAdapter(this, sports, object : SportAdapter.Callback{
            override fun onClick(sportName: String) {
                val intent = when(sportName){
                    else->{
                        Intent(this@SportMainActivity, FootballMainActivity::class.java)
                    }
                }
                startActivity(intent)
            }
        })
    }
}