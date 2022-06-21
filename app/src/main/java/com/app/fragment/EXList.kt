package com.app.fragment

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
        this.adapter = EXListAdapter(requireActivity(), data)
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
                    }
                    ret.add(FileData(f.name, Date(date), length()/1024, listener, f.absolutePath))
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