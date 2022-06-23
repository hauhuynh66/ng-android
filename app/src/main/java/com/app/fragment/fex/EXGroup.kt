package com.app.fragment.fex

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.fex.FileListViewActivity
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
            val intent = Intent(requireContext(), FileListViewActivity::class.java)
            when(i){
                0->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", "PICTURE")
                            startActivity(intent)
                        }
                    }))
                }
                1->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", "VIDEO")
                            startActivity(intent)
                        }
                    }))
                }
                2->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", "MUSIC")
                            startActivity(intent)
                        }
                    }))
                }
                3->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {
                            intent.putExtra("TYPE", "DOC")
                            startActivity(intent)
                        }
                    }))
                }
                else->{
                    data.add(MiscData(null, object : MiscData.Listener{
                        override fun onClick() {

                        }
                    }))
                }
            }
        }
        val iconList = view.findViewById<RecyclerView>(R.id.fg_ex_group1_list)
        val layoutManager = GridLayoutManager(requireContext(), 4)
        val adapter = MiscAdapter(requireActivity(), data)
        iconList.layoutManager = layoutManager
        iconList.adapter = adapter
    }
}