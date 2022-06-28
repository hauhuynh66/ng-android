package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.app.ngn.R

class EditTextDialog(private val originalText:String?, val listener: Listener) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.dlg_edit, null, false)
        val text = v.findViewById<EditText>(R.id.dlg_edit_text)
        if(originalText!=null){
            text.setText(originalText)
        }
        builder.setView(v)
        text.requestFocus()
        builder.setPositiveButton("Confirm"){
            di, _ -> run{
                listener.onConfirm(text.text.toString())
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
    interface Listener{
        fun onConfirm(value:String)
        fun onDismiss(){

        }
    }
}