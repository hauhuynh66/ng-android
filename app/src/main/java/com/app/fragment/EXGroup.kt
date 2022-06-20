package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.MiscAdapter
import com.app.data.MiscData
import com.app.ngn.R

class EXGroup : Fragment() {
    private lateinit var data : ArrayList<MiscData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_ex_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arrayListOf()
        for( i in 0 until 8){
            data.add(MiscData(null, object : MiscData.Listener{
                override fun onClick() {
                    Toast.makeText(requireContext(), i, Toast.LENGTH_SHORT).show()
                }
            }))
        }
        val iconList = view.findViewById<RecyclerView>(R.id.fg_ex_icon_list)
        val layoutManager = GridLayoutManager(requireContext(), 5)
        val adapter = MiscAdapter(requireActivity(), data)
        iconList.layoutManager = layoutManager
        iconList.adapter = adapter
    }
}