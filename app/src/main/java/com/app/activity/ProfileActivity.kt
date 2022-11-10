package com.app.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListAdapter
import com.app.data.LineData
import com.app.data.LineDisplayOption
import com.app.data.LineManager
import com.app.ngn.R
import com.app.util.ViewUtils
import com.app.viewmodel.Authentication
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private val firebase : Authentication by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val title = toolbar.findViewById<TextView>(R.id.title)
        val infoList = findViewById<RecyclerView>(R.id.info_list)
        val statList = findViewById<RecyclerView>(R.id.stat_list)

        val infoData = listOf(
            LineData("1", "Line1"),
            LineData("2", "Line2"),
            LineData("3", "Line3"),
            LineData("4", "Line4"),
            LineData("5", "Line5"),
        )

        val lineOption = LineDisplayOption(Color.RED, 10f, Gravity.CENTER)
        val statData = listOf(
            LineData("Name 1", 10, option = lineOption),
            LineData("Name 2", 50, option = lineOption),
            LineData("Name 3", 100, option = lineOption),
            LineData("Name 4", 1, option = lineOption),
        )

        title.text = getString(R.string.profile_title)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firebase.firebaseAuth.currentUser!!.apply {
            findViewById<TextView>(R.id.email).text = email
            Picasso.get().load(photoUrl).into(findViewById<ImageView>(R.id.photo))
        }

        infoList.apply {
            layoutManager = ViewUtils.getFixedVerticalLayoutManager(this@ProfileActivity)
            adapter = ListAdapter(LineManager(infoData, LineManager.LineStyle.One))
        }

        statList.apply {
            layoutManager = ViewUtils.getFixedHorizontalLayoutManager(this@ProfileActivity)
            adapter = ListAdapter(LineManager(statData, LineManager.LineStyle.Two))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
            }
            else->{

            }
        }

        return true
    }
}