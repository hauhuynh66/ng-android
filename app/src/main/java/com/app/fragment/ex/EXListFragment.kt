package com.app.fragment.ex

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ExplorerListAdapter
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.app.util.FileUtils
import com.google.android.material.snackbar.Snackbar

class EXListFragment : Fragment() {
    private val rootPath : String = Environment.getExternalStorageDirectory().absolutePath
    private var currentPath : String = rootPath
    private lateinit var adapter : ExplorerListAdapter
    private lateinit var list : RecyclerView
    private lateinit var pathView : TextView
    private lateinit var bottomBar: ConstraintLayout
    private lateinit var selectedCount: TextView

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
        list = view.findViewById(R.id.list)
        pathView = view.findViewById(R.id.path_view)
        bottomBar = view.findViewById(R.id.actions)
        selectedCount = view.findViewById(R.id.count)

        adapter = ExplorerListAdapter(rootPath, isGrid = false)
        adapter.setOnItemClickListener(object : ExplorerListAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                adapter.apply {
                    val (action,path) = getAction(position)
                    if(action == "next"){
                        setPath(path)
                        currentPath = path
                        pathView.text = currentPath
                    }
                }
            }
        })

        adapter.setOnItemLongClickListener(object : ExplorerListAdapter.OnItemLongClickListener{
            override fun onLongClick(position: Int) {
                adapter.apply {
                    changeMode(ExplorerListAdapter.Mode.Select)
                    this.select(position)
                    crossfade(arrayListOf(bottomBar))
                }
            }
        })

        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        pathView.text = currentPath

        view.findViewById<ImageButton>(R.id.change_layout).apply {
            setOnClickListener{

            }
        }

        view.findViewById<ImageButton>(R.id.dismiss).setOnClickListener {
            crossfade(null, arrayListOf(bottomBar))
            adapter.changeMode(ExplorerListAdapter.Mode.Display)
        }

        view.findViewById<ImageButton>(R.id.action_ex).setOnClickListener { it ->
            val select  = adapter.getSelected().map {
                it.absolutePath
            }
            val menu = configMenu(requireContext(), it, select)

            menu.show()
        }

        view.findViewById<ImageButton>(R.id.previous).setOnClickListener{
            if(currentPath != rootPath){
                currentPath = adapter.back(currentPath)
                pathView.text = currentPath
            }else{
                Snackbar
                    .make(view, "Cant go back further", Snackbar.LENGTH_SHORT)
                    .setAction("OK"){}
                    .show()
            }
        }

        view.findViewById<ImageButton>(R.id.check_all).setOnClickListener {
            adapter.flip()
        }
    }

    fun configMenu(context : Context, view : View, select : List<String>) : PopupMenu{
        val menu = PopupMenu(context, view)
        menu.menuInflater.inflate(R.menu.file_menu, menu.menu)
        val renameItem = menu.menu[2]
        if(select.size > 1) {
            renameItem.isEnabled = false
        }
        menu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.file_menu_delete->{
                    var message = "${select.size} file(s) deleted"
                    if(!handleDelete(select)){
                        message = "Something went wrong"
                    }
                    Snackbar.make(this.requireView(),message, Snackbar.LENGTH_SHORT)
                        .setAction("OK"){}
                        .show()
                }
                else->{

                }
            }
            adapter.refresh()
            true
        }

        return menu
    }

    fun handleDelete(list : List<String>) : Boolean{
        val success = FileUtils.deleteFiles(list)
        return success == list.size
    }

    fun handleMove(list : List<String>){

    }

    fun handleRename(newPath : String){

    }
}