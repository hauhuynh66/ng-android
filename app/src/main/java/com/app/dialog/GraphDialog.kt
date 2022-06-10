package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.app.data.ForecastData
import com.app.ngn.R
import com.app.view.GraphView

class GraphDialog(private val data:ForecastData?):DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.dlg_graph, null, false)
        val graph = v.findViewById<GraphView>(R.id.graph)
        graph.data = data

        builder.setView(v)
        builder.setNegativeButton("OK") { di, _ ->
            run {
                di.dismiss()
            }
        }
        return builder.create()
    }
}