package com.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.FCMActivity
import com.app.activity.cv.CVActivity
import com.app.activity.fex.FileExplorerActivity
import com.app.adapter.MiscAdapter
import com.app.data.MiscData
import com.app.ngn.R

class MiscFragment : Fragment() {
    private lateinit var data: ArrayList<MiscData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_misc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arrayListOf()
        data.add(MiscData(null, object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), FCMActivity::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(null, object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), FileExplorerActivity::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(null, object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), CVActivity::class.java)
                startActivity(intent)
            }
        }))

        val layoutManager = GridLayoutManager(requireContext(), 2)
        val list = view.findViewById<RecyclerView>(R.id.fg_misc_list)
        val adapter = MiscAdapter(requireActivity(), data)
        list.layoutManager = layoutManager
        list.adapter = adapter
    }
}