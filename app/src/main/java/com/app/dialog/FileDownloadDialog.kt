package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner
import com.app.util.Generator.Companion.generateString
import java.io.File
import java.io.FileOutputStream

class FileDownloadDialog : DialogFragment() {
    private val root = Environment.getExternalStorageDirectory().absolutePath + "/photo"
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.dlg_file_download, null, false)
        val url = v.findViewById<EditText>(R.id.dlg_file_download_group1_url)
        builder.setView(v)
        builder.setPositiveButton("Try") { di, _ ->
            run {
                val task = TaskRunner()
                task.execute(
                    ImageCallable(url.text!!.toString()),
                    object : TaskRunner.Callback<Bitmap?> {
                        override fun onComplete(result: Bitmap?) {
                            if(saveBitmap(result)) {
                                di.dismiss()
                            }else{
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }
        }
        builder.setNegativeButton("Close") { di, _ ->
            run {
                di.dismiss()
            }
        }
        return builder.create()
    }

    fun saveBitmap(bitmap: Bitmap?): Boolean {
        val now = generateString(10).plus(".png")
        val file = File(root, now)
        return if(file.exists()){
            false
        }else{
            try{
                val fOut = FileOutputStream(file)
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, fOut)
                fOut.flush()
                fOut.close()
                true
            }catch (e:Exception){
                false
            }
        }
    }
}