package com.app.fragment.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.app.activity.DrawActivity
import com.app.adapter.NoteAdapter
import com.app.dialog.NoteDialog
import com.app.helper.OneColumnListHelperCallback
import com.app.listener.NoteDialogListener
import com.app.model.AppDatabase
import com.app.model.Note
import com.app.ngn.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*

class NoteFragment:Fragment(), NoteDialogListener {
    private lateinit var fb:FloatingActionButton
    private lateinit var adapter: NoteAdapter
    private lateinit var data:ArrayList<Note>
    private lateinit var db: AppDatabase
    private val drawLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        run{
            if(result.resultCode== Activity.RESULT_OK) {
                val path = result.data!!.extras!!.getString("bmp")

                val note = Note(
                    title = System.currentTimeMillis().toString(),
                    content = null,
                    displayDate = Date(),
                    extra = path
                )

                var id : Long

                runBlocking {
                    withContext(Dispatchers.IO){
                        id = db.noteRepository().insert(note)
                    }
                }

                note.id = id.toInt()
                data.add(note)
                adapter.notifyItemInserted(data.size - 1)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FloatingActionButton>(R.id.fg_note_draw).setOnClickListener {
            val intent = Intent(requireContext(), DrawActivity::class.java)
            drawLauncher.launch(intent)
        }
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val noteList = view.findViewById<RecyclerView>(R.id.noteList)
        noteList.layoutManager = layoutManager

        db = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()

        runBlocking {
            withContext(Dispatchers.IO){
                val d = db.noteRepository().getAll()
                data = d.toCollection(ArrayList())
            }
        }

        adapter = NoteAdapter(requireActivity(), data, object : NoteAdapter.Callback{
            override fun onItemClick(note: Note) {

            }

            override fun onDelete(note: Note) {
                var success = false
                runBlocking {
                    withContext(Dispatchers.IO){
                        success = db.noteRepository().delete(note) == 1
                    }
                }
                if(success){
                    val pos = data.indexOf(note)
                    data.remove(note)
                    adapter.notifyItemRemoved(pos)
                }

            }
        })

        noteList.adapter = adapter
        fb = view.findViewById(R.id.addBtn)
        fb.setOnClickListener {
            NoteDialog(this).show(requireActivity().supportFragmentManager, "NewNote")
        }
        val itemCallback = OneColumnListHelperCallback(adapter)
        val itemHelper = ItemTouchHelper(itemCallback)
        itemHelper.attachToRecyclerView(noteList)
    }

    override fun onAdd(note: Note) {
        /**/
        try{
            var success: Long?
            runBlocking {
                withContext(Dispatchers.IO){
                    success = db.noteRepository().insert(note)
                }
            }
            if(success!=null){
                this.data.add(note)
                adapter.notifyItemInserted(this.data.size - 1)
            }

        }catch (e : Exception){
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCancel(temp: Note) {
        /**/
    }
}