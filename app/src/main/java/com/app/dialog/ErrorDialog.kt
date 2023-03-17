package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.ngn.R

class ErrorDialog(val message: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val v = (requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_error, null, false)

        builder.setPositiveButton("OK") { di, _ ->
            run {
                di.dismiss();
            }
        }

        val message = v.findViewById<TextView>(R.id.error_message)
        message.text = this.message

        return builder.create()
    }
}