package com.app.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.app.ngn.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FileActionDialog(val path: String) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fg_modal_file_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val property = view.findViewById<TextView>(R.id.fg_modal_file_action_show_properties)
        property.setOnClickListener {
            EX
        }
    }
}