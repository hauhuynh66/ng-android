package com.app.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.app.data.ConfirmDialogData
import com.app.ngn.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.util.*

class FileActionDialog(private val paths : List<String>, val listener : Listener) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dlg_file_action, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val property = view.findViewById<TextView>(R.id.fg_modal_file_action_show_properties)
        val rename = view.findViewById<TextView>(R.id.fg_modal_file_action_rename)
        val del = view.findViewById<TextView>(R.id.fg_modal_file_action_delete)

        del.setOnClickListener{
            ConfirmDialog("Do you want to delete?", object : ConfirmDialog.Listener{
                override fun onConfirm(data: ConfirmDialogData) {
                    for(s:Any in data.data){
                        val retName = File(s.toString()).absolutePath
                        val success = File(s.toString()).delete()
                        if(success){
                            listener.onDelete(retName)
                        }
                    }
                    dismiss()
                }
            }, ConfirmDialogData(ArrayList(paths))).show(requireActivity().supportFragmentManager, "DELETE")
        }

        when{
            paths.size > 1->{
                property.visibility = View.GONE
                rename.visibility = View.GONE
            }
            paths.size == 1 ->{
                paths[0].apply {
                    property.setOnClickListener {
                        DetailDialog(this).show(requireActivity().supportFragmentManager, "DETAIL")
                        dismiss()
                    }

                    rename.setOnClickListener {
                        EditTextDialog(File(this).name, object : EditTextDialog.Listener{
                            override fun onConfirm(value: String) {
                                val dirPath = this@apply.substringBeforeLast("/")
                                val to = File(dirPath, value)
                                if(!to.exists()){
                                    val success = File(this@apply).renameTo(to)
                                    if(success){
                                        listener.onRename(File(this@apply).name, value)
                                        dismiss()
                                    }
                                }else{
                                    Toast.makeText(requireContext(),"File with entered name already existed" ,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        ).show(requireActivity().supportFragmentManager, "EDIT")
                    }
                }

            }
        }
    }

    interface Listener{
        fun onRename(oldName: String, newName : String){

        }

        fun onDelete(path : String)
    }
}