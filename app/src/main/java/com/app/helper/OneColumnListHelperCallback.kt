package com.app.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.NoteAdapter
import java.util.*

class OneColumnListHelperCallback(private val adapter: NoteAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
            ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.START or ItemTouchHelper.END)
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

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }
}