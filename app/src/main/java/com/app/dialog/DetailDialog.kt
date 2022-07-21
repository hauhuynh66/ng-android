package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.PropertyAdapter
import com.app.ngn.R
import java.io.File

class DetailDialog(val path : String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val v = (requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_ex_detail, null, false)
        val list = v.findViewById<RecyclerView>(R.id.dlg_ex_detail_list)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = PropertyAdapter(requireActivity(), getDetails(path)!!)
        builder.setView(v).setPositiveButton("OK"){
            di, _ -> run{
                di.dismiss()
            }
        }
        return builder.create()
    }

    private fun getDetails(path: String) : ArrayList<PropertyAdapter.DetailData>?{
        val ret = arrayListOf<PropertyAdapter.DetailData>()
        val file = File(path)
        if(!file.exists()){
            return null
        }

        ret.add(PropertyAdapter.DetailData("Name", file.name))
        ret.add(PropertyAdapter.DetailData("Extension", file.extension))

        return ret
    }
}