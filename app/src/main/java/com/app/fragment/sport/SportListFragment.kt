package com.app.fragment.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.sport.SportMainActivity
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
        val data : ArrayList<String> = resources.getStringArray(R.array.sports).toCollection(ArrayList())
        val list = view.findViewById<RecyclerView>(R.id.item_list)
        list.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = TextListAdapter(requireContext(), data, object : TextListAdapter.Listener{
                override fun onClick(data: String) {
                    val value = when(data){
                        "Football"->{
                            SportMainActivity.SportStates.FootballResultList
                        }
                        else->{
                            SportMainActivity.SportStates.SportList
                        }
                    }
                    stateModel.state.value = value
                }

                override fun onLongClick(data: String) {

                }
            })
        }
    }
}