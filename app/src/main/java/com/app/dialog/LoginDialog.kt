package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.app.ngn.R

class LoginDialog(val callback : Callback) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_login, null, false)

        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
        return builder.create()
    }

    interface Callback{
        fun onSuccess(){

        }
        fun onFailure(){

        }
        fun onCancel(){

        }
    }
}