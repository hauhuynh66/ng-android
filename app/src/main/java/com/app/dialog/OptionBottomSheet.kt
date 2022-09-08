package com.app.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.BottomSheetAdapter
import com.app.ngn.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OptionBottomSheet(
    val data: ArrayList<Data>, val listener: Listener?) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dlg_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = view.findViewById<RecyclerView>(R.id.dlg_bottom_sheet_list)
        list.adapter = BottomSheetAdapter(requireContext(), data, listener)
        list.layoutManager = LinearLayoutManager(requireContext())
    }

    class Data(val display :String, val enable : Boolean = true, val option: String? = null)

    interface Listener{
        fun onClick(option : String?)
    }
}