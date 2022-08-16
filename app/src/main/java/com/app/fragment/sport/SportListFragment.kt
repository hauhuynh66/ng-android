package com.app.fragment.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.TextListAdapter
import com.app.ngn.R
import com.app.viewmodel.Sport

class SportListFragment : Fragment() {
    private val stateModel : Sport by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arrayListOf(
            "Football",
            "Basketball",
            "Formula 1"
        )
        val list = view.findViewById<RecyclerView>(R.id.item_list)
        list.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = TextListAdapter(requireContext(), data, object : TextListAdapter.Listener{
                override fun onClick(data: String) {
                    stateModel.arg.value = data
                    stateModel.state.value = 1
                }

                override fun onLongClick(data: String) {

                }
            })
        }
    }
}