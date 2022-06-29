package com.app.fragment.fex

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.EXListAdapter
import com.app.data.FileData
import com.app.dialog.FileActionDialog
import com.app.listener.EXPathChangeListener
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
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
    private lateinit var pathGroup: LinearLayoutCompat
    private lateinit var bottomBar: ConstraintLayout
    private var isMultiple = false
    private var selected: ArrayList<Any> = arrayListOf()
    private lateinit var num: TextView

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
        pathView = view.findViewById(R.id.fg_ex_list_input)
        bottomBar = view.findViewById(R.id.fg_ex_list_bottom_bar)
        num = view.findViewById(R.id.fg_ex_list_num_selected)
        pathGroup = view.findViewById(R.id.fg_ex_list_input_group)
        val prev = view.findViewById<ImageButton>(R.id.fg_ex_list_input_button)
        val dismissBottomBar = view.findViewById<Button>(R.id.fg_ex_list_back)
        val more = view.findViewById<Button>(R.id.fg_ex_list_more)

        data = getFileList(path)
        adapter = EXListAdapter(requireActivity(), data, isGrid = false)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        pathView.setText(path)

        dismissBottomBar.setOnClickListener {
            isMultiple = false
            selected = arrayListOf()
            crossfade(pathGroup, bottomBar, 200)
        }

        more.setOnClickListener {
            val dialog = FileActionDialog(selected, object : FileActionDialog.Listener{
                override fun onRename(oldName: String, newName: String) {
                    val renamed = data.filter{
                        it.name == oldName
                    }[0]
                    val m =
                        FileData(newName, renamed.createDate, renamed.size, renamed.listener,
                            renamed.path.replace(oldName, newName), renamed.type)
                    val pos = data.indexOf(renamed)
                    data.remove(renamed)
                    data.add(m)
                    adapter.notifyItemRemoved(pos)
                    adapter.notifyItemInserted(data.size-1)
                    dismissBottomBar.performClick()
                }

                override fun onDelete(name: String) {
                    val deleted = data.filter {
                        it.name == name
                    }[0]
                    val pos = data.indexOf(deleted)
                    data.remove(deleted)
                    adapter.notifyItemRemoved(pos)
                    dismissBottomBar.performClick()
                }
            })
            dialog.show(requireActivity().supportFragmentManager, "TAG")
        }

        prev.setOnClickListener{
            if(pathView.text.toString() != rootPath){
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
                                if(!isMultiple){
                                    if(File(path).isDirectory){
                                        this@EXList.path = path
                                        onChanged(path)
                                    }
                                }else{
                                    if(!selected.contains(path)){
                                        selected.add(path)
                                    }else{
                                        selected.remove(path)
                                    }
                                    num.text = selected.size.toString().plus(" selected")
                                }
                            }

                            override fun onLongClick(path: String) {
                                if(!isMultiple){
                                    selected.add(path)
                                    num.text = selected.size.toString().plus(" selected")
                                    isMultiple = true
                                    crossfade(bottomBar, pathGroup, 200)
                                }
                                /*val dialog = FileActionDialog(path, object : FileActionDialog.Listener{
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
                                dialog.show(requireActivity().supportFragmentManager, "TAG")*/

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