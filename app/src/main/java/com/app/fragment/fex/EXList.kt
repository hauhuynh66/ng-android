package com.app.fragment.fex

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.EXListAdapter
import com.app.data.FileData
import com.app.dialog.FileActionDialog
import com.app.listener.EXPathChangeListener
import com.app.ngn.R
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

class EXList : Fragment(), EXPathChangeListener {
    private lateinit var data: ArrayList<FileData>
    private val rootPath : String = Environment.getExternalStorageDirectory().absolutePath
    private var path : String = rootPath
    private lateinit var adapter : EXListAdapter
    private lateinit var list : RecyclerView
    private lateinit var pathView : EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_ex_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = view.findViewById(R.id.fg_ex_list_list)
        val layoutManager = LinearLayoutManager(requireContext())
        data = getFileList(path)
        this.adapter = EXListAdapter(requireActivity(), data, false)
        list.layoutManager = layoutManager
        list.adapter = adapter
        pathView = view.findViewById<EditText>(R.id.fg_ex_list_input)
        val prev = view.findViewById<ImageButton>(R.id.fg_ex_list_input_button)
        pathView.setText(path)
        prev.setOnClickListener{
            if(pathView.text.toString() == rootPath){

            }else{
                path = path.substringBeforeLast("/")
                onChanged(path)
            }
        }
    }

    private fun getFileList(path : String) : ArrayList<FileData>{
        val ret = arrayListOf<FileData>()
        val file = File(path)
        if(file.isDirectory){
            for(f:File? in file.listFiles()){
                if(file.exists()){
                    f!!.apply {
                        val attrs = Files.readAttributes(file.toPath(),BasicFileAttributes::class.java)
                        val date = attrs.creationTime().toMillis()
                        val listener = object : FileData.Listener{
                            override fun onClick( path: String) {
                                if(File(path).isDirectory){
                                    this@EXList.path = path
                                    onChanged(path)
                                }
                            }

                            override fun onLongClick(path: String) {
                                val dialog = FileActionDialog(path, object : FileActionDialog.Listener{
                                    override fun onRename(oldName: String, newName: String) {
                                        val renamed = data.filter{
                                            it.name == oldName
                                        }[0]
                                        val m =
                                            FileData(newName, renamed.createDate, renamed.size, renamed.listener, renamed.path, renamed.type)
                                        val pos = data.indexOf(renamed)
                                        data.remove(renamed)
                                        data.add(m)
                                        adapter.notifyItemRemoved(pos)
                                        adapter.notifyItemInserted(data.size-1)
                                    }

                                    override fun onDelete(name: String) {
                                        val deleted = data.filter {
                                            it.name == name
                                        }[0]
                                        val pos = data.indexOf(deleted)
                                        data.remove(deleted)
                                        adapter.notifyItemRemoved(pos)

                                    }
                                })
                                dialog.show(requireActivity().supportFragmentManager, "TAG")
                            }
                        }
                        if(f.isDirectory){
                            ret.add(FileData(f.name, Date(date), null, listener, f.absolutePath, "DIR"))
                        }else{
                            ret.add(FileData(f.name, Date(date), f.length()/1024, listener, f.absolutePath, f.extension))
                        }
                    }
                }
            }
        }
        return ret
    }

    override fun onChanged(path : String) {
        data.clear()
        data.addAll(getFileList(path))
        adapter.notifyDataSetChanged()
        pathView.setText(path)
    }
}