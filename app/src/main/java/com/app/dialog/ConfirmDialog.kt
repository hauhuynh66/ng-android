package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.ngn.R

class ConfirmDialog<T>(private val title : String? = null, private val message : String, val listener : Listener<T>, val data : T) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_confirm, null, false)
        view.findViewById<TextView>(R.id.title).apply {
            text = title?:""
        }
        view.findViewById<TextView>(R.id.message).apply {
            text = message
        }

        builder.setView(view)
            .setPositiveButton("Confirm"){
            di, _ -> run {
                listener.onConfirm(data)
                di.dismiss()
            }
        }.setNegativeButton("Cancel"){
            di, _ -> run{
                di.dismiss()
            }
        }

        return builder.create()
    }

    interface Listener<T>{
        fun onConfirm(data : T)

        fun onCancel(){

        }
    }
}