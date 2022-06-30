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
import com.app.listener.EXListListener
import com.app.listener.EXPathChangeListener
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

class EXList(val listener : Listener) : Fragment(), EXPathChangeListener, EXListListener {
    private lateinit var data: ArrayList<FileData>
    private val rootPath : String = Environment.getExternalStorageDirectory().absolutePath
    private var path : String = rootPath
    private lateinit var adapter : EXListAdapter
    private lateinit var list : RecyclerView
    private lateinit var pathView : EditText
    private lateinit var pathGroup: LinearLayoutCompat
    private lateinit var bottomBar: ConstraintLayout
    private lateinit var topBar: ConstraintLayout
    private var isMultiple = false
    private var selected: ArrayList<Any> = arrayListOf()
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
                    listener.onMultipleChanged(isMultiple)
                    crossfade(arrayListOf(bottomBar, topBar), arrayListOf(pathGroup), 0)
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
            }
        }

        adapter = EXListAdapter(requireActivity(), data, isGrid = false, itemListener)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        pathView.setText(path)

        dismissBottomBar.setOnClickListener {
            isMultiple = false
            listener.onMultipleChanged(isMultiple)
            selected = arrayListOf()
            crossfade(arrayListOf(pathGroup),arrayListOf(bottomBar, topBar), duration = 0)
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

        check.setOnClickListener {
            if(!isCheckAll){
                for ((i, fileData) in adapter.data.withIndex()){
                    if(!selected.contains(fileData.path)){
                        selected.add(fileData.path)
                    }
                    fileData.checked = true
                    adapter.notifyItemChanged(i)
                }
            }else{
                for ((i, fileData) in adapter.data.withIndex()){
                    fileData.checked = false
                    adapter.notifyItemChanged(i)
                }
                selected.clear()
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
                                    onSelectionChange()
                                    isCheckAll = adapter.data.size == selected.size
                                }
                            }

                            override fun onLongClick(path: String) {
                                if(!isMultiple){
                                    selected.add(path)
                                    onSelectionChange()
                                    isMultiple = true
                                    listener.onMultipleChanged(isMultiple)
                                    crossfade(arrayListOf(bottomBar, topBar), arrayListOf(pathGroup), duration = 0)
                                    isCheckAll = adapter.data.size == 1
                                }
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

    override fun onSelectionChange() {
        num.text = selected.size.toString().plus(" selected")
    }

    interface Listener{
        fun onMultipleChanged(isMultiple : Boolean)
    }
}