package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.app.ngn.R

class EditDialog<T>(private val data: T, private val listener: Listener<T>) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val v = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_text_edit, null, false)
        val text = v.findViewById<EditText>(R.id.edit_text)

        builder.setView(v)
        text.requestFocus()
        builder.setPositiveButton("Confirm"){
            di, _ -> run{
                //listener.onConfirm()
                di.dismiss()
            }
        }
        builder.setNegativeButton("No"){
            di, i -> run{
                listener.onDismiss()
                di.dismiss()
            }
        }
        return builder.create()
    }

    interface Listener<T>{
        fun onConfirm(value : T)
        fun onDismiss(){

        }
    }
}