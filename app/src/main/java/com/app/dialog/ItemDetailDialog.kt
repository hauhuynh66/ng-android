package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.data.ItemData
import com.app.ngn.R

class ItemDetailDialog(val data : ItemData, val listener : Listener) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dlg_item_detail, null, false)
        val itemName = view.findViewById<TextView>(R.id.dlg_item_detail_title)
        val seekBar = view.findViewById<SeekBar>(R.id.dlg_item_detail_count)

        builder.setView(view)

        itemName.text = data.name

        builder.setPositiveButton("Add"){ di, _ ->
            listener.onConfirm(data, seekBar.progress)
            di.dismiss()
        }
        builder.setNegativeButton("Cancel"){ di, _ ->
            listener.onCancel()
            di.dismiss()
        }

        return builder.create()
    }

    interface Listener{
        fun onConfirm(data : ItemData, count : Int)
        fun onCancel()
    }
}