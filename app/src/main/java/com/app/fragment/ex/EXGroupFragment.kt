package com.app.fragment.ex

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.ex.FileListActivity
import com.app.adapter.ActionAdapter
import com.app.data.ActionData
import com.app.helper.SpanLinearLayoutManager
import com.app.ngn.R

class EXGroupFragment : Fragment() {
    private lateinit var data : ArrayList<ActionData>
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
        for( i in 0 until 4){
            val listener = object : ActionData.Listener{
                override fun onClick() {
                    val intent = Intent(requireContext(), FileListActivity::class.java)
                    intent.putExtra("display", i)
                    startActivity(intent)
                }
            }

            data.add(ActionData(null, i.toString(), listener))
        }
        val iconList = view.findViewById<RecyclerView>(R.id.fg_ex_group1_list)
        val layoutManager = SpanLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = ActionAdapter(requireActivity(), data, 60)
        iconList.layoutManager = layoutManager
        iconList.adapter = adapter
    }
}