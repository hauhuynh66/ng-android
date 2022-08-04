package com.app.fragment.mp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.AudioListAdapter
import com.app.data.AudioData
import com.app.ngn.R
import com.app.util.Resolver.Companion.getAudioList

class AudioListFragment(val i : Int) : Fragment() {
    private lateinit var data : ArrayList<AudioData>
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
        data = when(i){
            0->{
                getAudioList(requireContext().contentResolver)
            }
            1->{
                arrayListOf()
            }
            2->{
                arrayListOf()
            }
            3->{
                arrayListOf()
            }
            else->{
                arrayListOf()
            }
        }
        val list = view.findViewById<RecyclerView>(R.id.item_list)
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        list.adapter = AudioListAdapter(requireContext(), data, i)
    }
}