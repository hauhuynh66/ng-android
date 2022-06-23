package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDialog(private val message : String, val listener : Listener, val pr : Any?) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message).setPositiveButton("Confirm"){
            di, _ -> run {
                listener.onConfirm(pr)
                di.dismiss()
            }
        }.setNegativeButton("Cancel"){
            di, _ -> run{
                di.dismiss()
            }
        }
        return builder.create()
    }

    interface Listener{
        fun <T> onConfirm(pr : T)
    }
}