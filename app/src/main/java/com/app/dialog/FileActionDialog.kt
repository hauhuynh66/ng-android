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

class FileActionDialog(val paths : ArrayList<String>, val listener : Listener) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fg_modal_file_action, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val property = view.findViewById<TextView>(R.id.fg_modal_file_action_show_properties)
        val rename = view.findViewById<TextView>(R.id.fg_modal_file_action_rename)
        val del = view.findViewById<TextView>(R.id.fg_modal_file_action_delete)

        del.setOnClickListener{
            ConfirmDialog("Do you want to delete?", object : ConfirmDialog.Listener{
                override fun onConfirm(data: Any?) {
                    val retName = File(data.toString()).name
                    val success = File(data.toString()).delete()
                    if(success){
                        listener.onDelete(retName)
                    }
                }
            }, paths).show(requireActivity().supportFragmentManager, "DELETE")
        }

        when{
            paths.size > 1->{
                property.visibility = View.GONE
                rename.visibility = View.GONE
            }
            paths.size == 1 ->{
                property.setOnClickListener {
                    DetailDialog(paths[0]).show(requireActivity().supportFragmentManager, "DETAIL")
                }

                rename.setOnClickListener {
                    EditTextDialog(File(paths[0]).name, object : EditTextDialog.Listener{
                        override fun onConfirm(value: String) {
                            val dirPath = paths[0].substringBeforeLast("/")
                            val to = File(dirPath, value)
                            if(!to.exists()){
                                val success = File(paths[0]).renameTo(to)
                                if(success){
                                    listener.onRename(File(paths[0]).name, value)
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

    interface Listener{
        fun onRename(oldName: String, newName : String){

        }
        fun onDelete(name : String)
    }
}