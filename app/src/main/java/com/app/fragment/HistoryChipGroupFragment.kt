package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.model.AppDatabase
import com.app.ngn.R
import kotlinx.coroutines.runBlocking

class HistoryChipGroupFragment(activity : String) : Fragment(){
    private lateinit var db : AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_chips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()
        view.findViewById<RecyclerView>(R.id.item_list).apply {

        }
    }
}