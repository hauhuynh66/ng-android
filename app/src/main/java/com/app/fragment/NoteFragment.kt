package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.NoteAdapter
import com.app.data.NoteData
import com.app.dialog.NoteDialog
import com.app.helper.OneColumnListHelperCallback
import com.app.listener.NoteDialogListener
import com.app.listener.NoteItemListener
import com.app.ngn.R
import com.app.util.Generator.Companion.generateString
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class NoteFragment:Fragment(), NoteDialogListener {
    private lateinit var fb:FloatingActionButton
    private lateinit var data:ArrayList<NoteData>
    private lateinit var adapter:NoteAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        data = getPseudoNotes(10)
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val noteList = view.findViewById<RecyclerView>(R.id.noteList)
        noteList.layoutManager = layoutManager
        adapter = NoteAdapter(this.requireActivity(), data, object: NoteItemListener{
            override fun onItemClick(note: NoteData) {

            }
        })
        noteList.adapter = adapter
        fb = view.findViewById(R.id.addBtn)
        fb.setOnClickListener {
            NoteDialog(this, null).show(requireActivity().supportFragmentManager, "NewNote")
        }
        val itemCallback = OneColumnListHelperCallback(adapter)
        val itemHelper = ItemTouchHelper(itemCallback)
        itemHelper.attachToRecyclerView(noteList)
    }

    private fun getPseudoNotes(length:Int):ArrayList<NoteData>{
        data = arrayListOf()
        for(i in 0 until length){
            data.add(NoteData(generateString(10), generateString(25), Date()))
        }
        return data
    }

    override fun onAdd(note: NoteData) {
        /**/
        this.data.add(note)
        adapter.notifyItemInserted(this.data.size - 1)
    }

    override fun onCancel(temp: NoteData) {
        /**/

    }
}