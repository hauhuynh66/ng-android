package com.app.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.app.ngn.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File

class FileActionDialog(val path: String, val listener : Listener) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fg_modal_file_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val property = view.findViewById<TextView>(R.id.fg_modal_file_action_show_properties)
        property.setOnClickListener {
            DetailDialog(path).show(requireActivity().supportFragmentManager, "DETAIL")
        }

        val rename = view.findViewById<TextView>(R.id.fg_modal_file_action_rename)
        rename.setOnClickListener {
            EditTextDialog(File(path).name, object : EditTextDialog.Listener{
                override fun onConfirm(value: String) {
                    val dirPath = path.substringBeforeLast("/")
                    val to = File(dirPath, value)
                    if(!to.exists()){
                        val success = File(path).renameTo(to)
                        if(success){
                            listener.onRename(File(path).name, value)
                        }
                    }else{
                        Toast.makeText(requireContext(),"File with entered name already existed" ,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ).show(requireActivity().supportFragmentManager, "EDIT")
        }

        val del = view.findViewById<TextView>(R.id.fg_modal_file_action_delete)
        del.setOnClickListener{
            ConfirmDialog("Do you want to delete?", object : ConfirmDialog.Listener{
                override fun <T> onConfirm(pr: T) {
                    val retName = File(pr.toString()).name
                    val success = File(pr.toString()).delete()
                    if(success){
                        listener.onDelete(retName)
                    }
                }
            }, path).show(requireActivity().supportFragmentManager, "DELETE")
        }
    }

    interface Listener{
        fun onRename(oldName: String, newName : String)
        fun onDelete(name : String)
    }
}