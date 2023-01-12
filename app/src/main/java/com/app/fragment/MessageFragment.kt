package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.CustomListAdapter
import com.app.data.Message
import com.app.data.MessageManager
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.app.viewmodel.ListHolderModel

class MessageFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var list : RecyclerView
    private val listModel : ListHolderModel<Message> by activityViewModels()
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
        val messageManager = MessageManager(listModel.data)
        list.adapter = CustomListAdapter(messageManager)
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun add(message : Message){
        listModel.data.add(message)
        list.adapter?.notifyItemInserted(listModel.data.size - 1)
        list.scrollToPosition(listModel.data.size - 1)
    }

    fun remove(message: Message){
        val pos = listModel.data.indexOf(message)
        println(pos)
        listModel.data.remove(message)
        list.adapter?.notifyItemRemoved(pos)
        list.scrollToPosition(pos)
    }

    fun activate(){
        crossfade(list, progressBar, 1000)
        isActivated = true
    }
}