package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.BitmapAdapter
import com.app.ngn.R
import java.io.File

class ImageDialog(private val callback: Callback): DialogFragment() {
    private val filePath:String = Environment.getExternalStorageDirectory().absolutePath + "/photo"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.dlg_image, null, false)
        val list = v.findViewById<RecyclerView>(R.id.dlg_image_image_list)
        val layoutInflater = GridLayoutManager(requireContext(), 2)
        val adapter = BitmapAdapter(requireContext(), getPaths(File(filePath)))
        list.layoutManager = layoutInflater
        list.adapter = adapter
        builder.setView(v)
        builder.setPositiveButton("Confirm", DialogInterface.OnClickListener{ di, _ ->run{
                callback.onConfirm(adapter.selected)
                di.dismiss()
            }
        })

        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ di, _ ->run{
                di.dismiss()
            }
        })
        return builder.create()
    }

    private fun getPaths(dir: File) : ArrayList<String?>{
        if(!dir.isDirectory){
            return arrayListOf()
        }
        val paths = arrayListOf<String?>()
        for (file in dir.listFiles()){
            if("png" == file.extension){
                paths.add(file.absolutePath)
            }
        }
        return paths
    }

    interface Callback{
        fun onConfirm(path : String?)
    }
}