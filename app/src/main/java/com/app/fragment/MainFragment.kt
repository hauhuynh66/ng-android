package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.CardAdapter
import com.app.data.chart.SingleValueData
import com.app.ngn.R

class MainFragment : Fragment() {
    private lateinit var summaryList : RecyclerView
    private lateinit var statList : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val images = arrayListOf(
            R.drawable.image_background_1,
            R.drawable.heh,
            R.drawable.goldengate,
            R.drawable.mt_rushmore,
            R.drawable.bryce_canyon
        )

        summaryList = view.findViewById(R.id.statistic_list1)
        statList = view.findViewById(R.id.statistic_list2)

        val carousel = view.findViewById<Carousel>(R.id.carousel)

        carousel.setAdapter(object : Carousel.Adapter{
            override fun count(): Int {
                return images.size
            }

            override fun populate(view: View?, index: Int) {
                (view as ImageView).setImageResource(images[index])
            }

            override fun onNewItem(index: Int) {

            }
        })

        summaryList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val sumData = arrayListOf(
            CardAdapter.CardData(arrayListOf<SingleValueData>(), "Summary 1", "Subtile 1", "PIE"),
            CardAdapter.CardData(arrayListOf<SingleValueData>(), "Summary 2", "Subtile 2", "BAR"),
            CardAdapter.CardData(arrayListOf<SingleValueData>(), "Summary 3", "Subtile 3", "LINE"),
            CardAdapter.CardData(20, "Summary 4", "Subtile 4", "GAUGE")
        )
        summaryList.adapter = CardAdapter(requireContext(), sumData, LinearLayoutManager.VERTICAL)

        statList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val statData = arrayListOf(
            CardAdapter.CardData(arrayListOf<SingleValueData>(), "Summary 1", "Subtile 1", "PIE"),
            CardAdapter.CardData(arrayListOf<SingleValueData>(), "Summary 2", "Subtile 2", "BAR"),
            CardAdapter.CardData(arrayListOf<SingleValueData>(), "Summary 3", "Subtile 3", "LINE"),
            CardAdapter.CardData(20, "Summary 4", "Subtile 4", "GAUGE")
        )
        statList.adapter = CardAdapter(requireContext(), statData, LinearLayoutManager.HORIZONTAL)

    }
}