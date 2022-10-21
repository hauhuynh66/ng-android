package com.app.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListAdapter
import com.app.data.LineData
import com.app.data.LineStyle
import com.app.ngn.R
import com.app.viewmodel.Authentication
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private val firebase : Authentication by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val title = toolbar.findViewById<TextView>(R.id.title)
        val profileList = findViewById<RecyclerView>(R.id.info_list)
        val infoList = findViewById<RecyclerView>(R.id.stat_list)
        val infoData = listOf(
            LineData("1", "Line1"),
            LineData("2", "Line2"),
            LineData("3", "Line3"),
            LineData("4", "Line4"),
            LineData("5", "Line5"),
        )

        title.text = getString(R.string.profile_title)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firebase.firebaseAuth.currentUser!!.apply {
            findViewById<TextView>(R.id.email).text = email
            Picasso.get().load(photoUrl).into(findViewById<ImageView>(R.id.photo))
        }

        profileList.apply {
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false){
                override fun generateLayoutParams(
                    c: Context?,
                    attrs: AttributeSet?
                ): RecyclerView.LayoutParams {
                    return spanLayoutSize(super.generateLayoutParams(c, attrs))
                }

                private fun getVerticalSpace(): Int {
                    return height - paddingBottom - paddingTop
                }

                private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
                    layoutParams.height = getVerticalSpace() / itemCount
                    return layoutParams
                }

                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = ListAdapter(context, infoData, LineStyle.Style1)
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