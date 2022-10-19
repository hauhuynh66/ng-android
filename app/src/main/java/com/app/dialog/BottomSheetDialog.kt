package com.app.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.BottomSheetAdapter
import com.app.data.BottomSheetActionData
import com.app.ngn.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(
    val data: List<BottomSheetActionData>) : BottomSheetDialogFragment() {

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
        list.adapter = BottomSheetAdapter(requireContext(), data)
        list.layoutManager = LinearLayoutManager(requireContext())
    }
}