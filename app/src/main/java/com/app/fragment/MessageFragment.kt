package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.MessageAdapter
import com.app.data.Message
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade

class MessageFragment(var messages : ArrayList<Message>) : Fragment() {
    private lateinit var adapter: MessageAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var list : RecyclerView
    var isActivated = false
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
        list = view.findViewById(R.id.item_list)
        progressBar = view.findViewById(R.id.progress)
        progressBar.visibility = View.VISIBLE
        adapter = MessageAdapter(messages)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun add(message : Message){
        messages.add(message)
        adapter.notifyItemInserted(messages.size - 1)
        list.scrollToPosition(messages.size - 1)
    }

    fun remove(message: Message){
        val pos = messages.indexOf(message)
        messages.remove(message)
        adapter.notifyItemRemoved(pos)
    }

    fun activate(){
        crossfade(list, progressBar, 1000)
        isActivated = true
    }
}