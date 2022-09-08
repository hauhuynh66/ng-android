package com.app.fragment.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
import com.app.listener.NoteDialogListener
import com.app.model.AppDatabase
import com.app.model.Note
import com.app.ngn.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*


class NoteFragment:Fragment(), NoteDialogListener {
    private lateinit var fb:FloatingActionButton
    private lateinit var adapter: NoteAdapter
    private lateinit var data:ArrayList<Note>
    private lateinit var db: AppDatabase
    private lateinit var rootView : View
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
        rootView = view.findViewById(R.id.rootView)
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

        })

        noteList.adapter = adapter
        fb = view.findViewById(R.id.addBtn)
        fb.setOnClickListener {
            NoteDialog(this).show(requireActivity().supportFragmentManager, "NewNote")
        }

        val itemCallback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(ItemTouchHelper.ACTION_STATE_DRAG or ItemTouchHelper.ACTION_STATE_SWIPE,
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.RIGHT)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction==ItemTouchHelper.RIGHT){
                    val pos = viewHolder.adapterPosition
                    val item = adapter.removeItem(pos)
                    deleteNote(item)
                    val snackBar = Snackbar.make(requireContext(), rootView, "Note removed", Snackbar.LENGTH_LONG)
                    snackBar.setAction("Undo") {
                        adapter.restoreItem(item, pos)
                        db.noteRepository().insert(item)
                    }
                    snackBar.show()
                }
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val currentPosition = viewHolder.adapterPosition
                val newPosition = target.adapterPosition
                if(currentPosition < newPosition){
                    for(i in currentPosition until newPosition){
                        Collections.swap(adapter.data, i , i+1)
                    }
                }else{
                    for(i in newPosition until currentPosition){
                        Collections.swap(adapter.data, i+1 , i)
                    }
                }
                adapter.notifyItemMoved(currentPosition, newPosition)
                return true
            }
        }
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

    fun deleteNote(note: Note) : Boolean{
        var success: Boolean
        runBlocking {
            withContext(Dispatchers.IO){
                success = db.noteRepository().delete(note) == 1
            }
        }
        return success
    }

    override fun onCancel(temp: Note) {
        /**/
    }
}