package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListAdapter
import com.app.data.LineData
import com.app.data.LineManager
import com.app.ngn.R
import java.io.File

class DetailDialog(val path : String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val v = (requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_list, null, false)
        val list = v.findViewById<RecyclerView>(R.id.dlg_ex_detail_list)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = ListAdapter(LineManager(getDetails(path), LineManager.LineStyle.One))
        builder.setView(v).setPositiveButton("OK"){
            di, _ -> run{
                di.dismiss()
            }
        }
        return builder.create()
    }

    private fun getDetails(path: String) : ArrayList<LineData>{
        val ret = arrayListOf<LineData>()
        val file = File(path)
        if(!file.exists()){
            return arrayListOf()
        }

        ret.add(LineData("Name", file.name))
        ret.add(LineData("Extension", file.extension))

        return ret
    }
}