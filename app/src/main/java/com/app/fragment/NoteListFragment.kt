package com.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activity.note.NoteActivity
import com.app.ngn.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NoteListFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FloatingActionButton>(R.id.addBtn).apply {
            setOnClickListener {
                val intent = Intent(requireActivity(), NoteActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fg_note, container, false)
    }
}