package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ExpandableCardAdapter
import com.app.adapter.ItemAdapter
import com.app.data.ItemData
import com.app.data.SearchCardData
import com.app.ngn.R
import com.app.util.Generator.Companion.generateString
import kotlin.random.Random

class ItemFragment:Fragment() {
    private lateinit var itemList: ArrayList<ItemData>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemList = generatePseudoItem(10)
        val adapter = ItemAdapter(requireActivity(), itemList)
        val layoutManager = GridLayoutManager(requireContext(), 2)

        val list = view.findViewById<RecyclerView>(R.id.fg_item_list)
        list.layoutManager = layoutManager
        list.adapter = adapter

        val filter = view.findViewById<RecyclerView>(R.id.fg_item_filter_group)
        filter.layoutManager = LinearLayoutManager(requireContext())
        val words = arrayListOf<String>()
        for(i:Int in 0 until 15){
            words.add(generateString(6))
        }
        filter.adapter = ExpandableCardAdapter(requireActivity(), SearchCardData("Search",words))
    }

    private fun generatePseudoItem(n:Int):ArrayList<ItemData>{
        val list = arrayListOf<ItemData>()
        for (i in 0..n){
            list.add(ItemData(generateString(10),"https://picsum.photos/id/".plus(Random.nextInt(20)+1).plus("/200/300"),Random.nextLong(1000)+1000))
        }
        return list
    }
}