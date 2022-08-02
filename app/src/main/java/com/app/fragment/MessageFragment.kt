package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.MessageAdapter
import com.app.data.Message
import com.app.ngn.R

class MessageFragment(var messages : ArrayList<Message>) : Fragment() {
    private lateinit var adapter: MessageAdapter
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
        val list = view.findViewById<RecyclerView>(R.id.item_list)
        adapter = MessageAdapter(requireContext(), messages)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun add(message : Message){
        messages.add(message)
        adapter.notifyDataSetChanged()
    }

    fun remove(message: Message){
        messages.remove(message)
        adapter.notifyDataSetChanged()
    }
}