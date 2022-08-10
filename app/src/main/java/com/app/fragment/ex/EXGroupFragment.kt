package com.app.fragment.ex

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.ex.FileListActivity
import com.app.adapter.MiscAdapter
import com.app.data.MiscData
import com.app.helper.SpanLinearLayoutManager
import com.app.ngn.R

class EXGroupFragment : Fragment() {
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
            val intent = Intent(requireContext(), FileListActivity::class.java)
            when(i){
                0->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", 1)
                            startActivity(intent)
                        }
                    }))
                }
                1->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", 2)
                            startActivity(intent)
                        }
                    }))
                }
                2->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", 3)
                            startActivity(intent)
                        }
                    }))
                }
                3->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", 4)
                            startActivity(intent)
                        }
                    }))
                }
            }
        }
        val iconList = view.findViewById<RecyclerView>(R.id.fg_ex_group1_list)
        val layoutManager = SpanLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = MiscAdapter(requireActivity(), data)
        iconList.layoutManager = layoutManager
        iconList.adapter = adapter
    }
}