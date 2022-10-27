package com.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.ProfileActivity
import com.app.adapter.CardAdapter
import com.app.ngn.R
import com.app.viewmodel.Authentication
import com.squareup.picasso.Picasso


class MainFragment : Fragment() {
    private val firebase : Authentication by activityViewModels()
    private lateinit var displayName : TextView
    private lateinit var displayImage : ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        firebase.firebaseAuth.currentUser!!.apply {
            this@MainFragment.displayName.text = "Welcome, $displayName"
            if(photoUrl!=null){
                Picasso.get().load(photoUrl!!).into(displayImage)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sumData = arrayListOf(
            CardAdapter.CardData(arrayListOf(50, 60, 20, 10, 100), "Summary 1", "Subtile 1", "PIE"),
            CardAdapter.CardData(arrayListOf(50, 60, 20, 10, 100), "Summary 2", "Subtile 2", "BAR"),
            CardAdapter.CardData(arrayListOf(50, 60, 20, 10, 100), "Summary 3", "Subtile 3", "LINE"),
            CardAdapter.CardData(arrayListOf(80), "Summary 4", "Subtile 4", "GAUGE")
        )
        view.findViewById<RecyclerView>(R.id.list1).apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = CardAdapter(sumData, LinearLayoutManager.VERTICAL)
        }

        val statData = arrayListOf(
            CardAdapter.CardData(arrayListOf(50, 60, 20, 10, 100), type = "PIE"),
            CardAdapter.CardData(arrayListOf(50, 60, 20, 10, 100), type = "BAR"),
            CardAdapter.CardData(arrayListOf(50, 60, 20, 10, 100), type = "LINE"),
            CardAdapter.CardData(arrayListOf(80), type = "GAUGE")
        )

        view.findViewById<RecyclerView>(R.id.list2).apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CardAdapter(statData, LinearLayoutManager.HORIZONTAL)
        }

        displayName = view.findViewById(R.id.text1)
        displayImage = view.findViewById(R.id.profile_icon)

        view.findViewById<ConstraintLayout>(R.id.header).setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}