package com.app.fragment.fex

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.app.listener.EXListListener
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.stream.Collectors

class EXList(val listener : Listener) : Fragment(), EXListListener {
    private lateinit var data: ArrayList<FileData>
    private val rootPath : String = Environment.getExternalStorageDirectory().absolutePath
    private var path : String = rootPath
    private lateinit var adapter : EXListAdapter
    private lateinit var list : RecyclerView
    private lateinit var pathView : TextView
    private lateinit var pathGroup: LinearLayoutCompat
    private lateinit var bottomBar: ConstraintLayout
    private lateinit var topBar: ConstraintLayout
    private var isMultiple = false
    private var selected: MutableList<String> = mutableListOf()
    private lateinit var num: TextView
    private var isCheckAll = false

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
        topBar = view.findViewById(R.id.fg_ex_list_top_bar)
        num = view.findViewById(R.id.fg_ex_list_num_selected)
        pathGroup = view.findViewById(R.id.fg_ex_list_input_group)
        val prev = view.findViewById<ImageButton>(R.id.fg_ex_list_input_button)
        val dismissBottomBar = view.findViewById<Button>(R.id.fg_ex_list_back)
        val more = view.findViewById<Button>(R.id.fg_ex_list_more)
        val check = view.findViewById<Button>(R.id.fg_ex_list_check)

        data = getFileList(path)
        val itemListener = object : EXListAdapter.Listener{
            override fun onCheck(path : String) {
                if(!isMultiple){
                    isMultiple = true
                    crossfade(arrayListOf(bottomBar, topBar), arrayListOf(pathGroup))
                    listener.onMultipleChanged(isMultiple)
                }
                if(!selected.contains(path)){
                    selected.add(path)
                }
                isCheckAll = selected.size == adapter.data.size
                onSelectionChange()
            }

            override fun onUnCheck(path : String) {
                if(selected.contains(path)){
                    selected.remove(path)
                }
                onSelectionChange()
                isCheckAll = false
            }

            override fun onClick(path: String, isChecked: Boolean, position : Int) {
                if(!isMultiple){
                    if(adapter.data[position].type =="DIR"){
                        adapter.data = getFileList(path)
                        adapter.notifyDataSetChanged()
                        pathView.text = path
                        onPathChanged(path)
                    }
                }else{
                    if(!isChecked){
                        if(!selected.contains(path)){
                            selected.add(path)
                        }
                    }else{
                        if(selected.contains(path)){
                            selected.remove(path)
                        }
                    }
                    adapter.data[position].checked = !isChecked
                    adapter.notifyItemChanged(position)
                    onSelectionChange()
                }
            }

            override fun onLongClick(path: String) {
                if(!isMultiple){
                    isMultiple = true
                    crossfade(arrayListOf(bottomBar, topBar), arrayListOf(pathGroup))
                    listener.onMultipleChanged(isMultiple)

                    selected.add(path)
                    isCheckAll = adapter.data.size == 1
                    onSelectionChange()
                }
            }
        }

        adapter = EXListAdapter(requireActivity(), data, isGrid = false, itemListener)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        pathView.setText(path)

        dismissBottomBar.setOnClickListener {
            isMultiple = false
            listener.onMultipleChanged(isMultiple)
            selected.clear()
            for (fileData in adapter.data){
                fileData.checked = false
            }
            adapter.notifyDataSetChanged()
            crossfade(arrayListOf(pathGroup),arrayListOf(bottomBar, topBar), duration = 0)
        }

        more.setOnClickListener {
            val dialog = FileActionDialog(selected, object : FileActionDialog.Listener{
                override fun onRename(oldName: String, newName: String) {
                    val renamed = data.filter{
                        it.name == oldName
                    }[0]
                    val m =
                        FileData(newName, renamed.createDate, renamed.size,
                            renamed.path.replace(oldName, newName), renamed.type)
                    val pos = data.indexOf(renamed)
                    data.remove(renamed)
                    data.add(m)
                    adapter.notifyItemRemoved(pos)
                    adapter.notifyItemInserted(data.size-1)
                    dismissBottomBar.performClick()
                }

                override fun onDelete(path: String) {
                    val deleted = adapter.data.filter {
                        it.path == path
                    }[0]
                    val pos = data.indexOf(deleted)
                    adapter.data.remove(deleted)
                    adapter.notifyItemRemoved(pos)
                    dismissBottomBar.performClick()
                }
            })
            dialog.show(requireActivity().supportFragmentManager, "TAG")
        }

        prev.setOnClickListener{
            if(pathView.text.toString() != rootPath || pathView.text.toString().length > rootPath.length){
                this.path = this.path.substringBeforeLast("/")
                onPathChanged(path)
            }else{
                pathView.setText(rootPath)
            }
        }

        check.setOnClickListener {
            if(!isCheckAll){
                for (fileData in adapter.data){
                    fileData.checked = true
                }
                selected.clear()
                selected = adapter.data.stream().map { it.path }.collect(Collectors.toList())
                adapter.notifyDataSetChanged()
            }else{
                for (fileData in adapter.data){
                    fileData.checked = false
                }
                selected.clear()
                adapter.notifyDataSetChanged()
            }
            isCheckAll = !isCheckAll
            onSelectionChange()
        }

    }

    private fun getFileList(path : String) : ArrayList<FileData>{
        val ret = arrayListOf<FileData>()
        val file = File(path)
        if(file.isDirectory&&file.listFiles()!=null){
            for(f:File? in file.listFiles()){
                if(file.exists()){
                    f!!.apply {
                        val attrs = Files.readAttributes(file.toPath(),BasicFileAttributes::class.java)
                        val date = attrs.creationTime().toMillis()
                        if(f.isDirectory){
                            ret.add(FileData(f.name, Date(date), null, f.absolutePath, "DIR"))
                        }else{
                            ret.add(FileData(f.name, Date(date), f.length()/1024, f.absolutePath, f.extension))
                        }
                    }
                }
            }
        }
        return ret
    }

    override fun onPathChanged(path : String) {
        this.path = path
        pathView.setText(path)
        adapter.data.clear()
        adapter.data.addAll(getFileList(this.path))
        adapter.notifyDataSetChanged()
    }

    override fun onSelectionChange() {
        num.text = selected.size.toString().plus(" selected")
    }

    interface Listener{
        fun onMultipleChanged(isMultiple : Boolean)
    }
}