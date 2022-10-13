package com.app.fragment.ex

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ExplorerListAdapter
import com.app.data.FileDisplay
import com.app.dialog.FileActionDialog
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade
import com.google.android.material.snackbar.Snackbar

class EXListFragment : Fragment() {
    private lateinit var data: MutableList<FileDisplay>
    private val rootPath : String = Environment.getExternalStorageDirectory().absolutePath
    private var currentPath : String = rootPath
    private lateinit var adapter : ExplorerListAdapter
    private lateinit var list : RecyclerView
    private lateinit var pathView : TextView
    private lateinit var pathGroup: ConstraintLayout
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
        pathGroup = view.findViewById(R.id.path_group)
        val dismiss = view.findViewById<ImageButton>(R.id.dismiss)

        val callback = object : ExplorerListAdapter.Callback{
            override fun onClick(position : Int) {
                if(adapter.mode == ExplorerListAdapter.Mode.Display){
                    adapter.apply {
                        val action = getAction(position)
                        if(action[0] == "next"){
                            setPath(action[1])
                            currentPath = action[1]
                            pathView.text = currentPath
                        }
                    }
                }
            }

            override fun onLongClick(position: Int) {
                adapter.apply {
                    changeMode(ExplorerListAdapter.Mode.Select)
                    this.select(position)
                    crossfade(arrayListOf(bottomBar), arrayListOf(pathGroup))
                }
            }
        }

        adapter = ExplorerListAdapter(requireActivity(), rootPath, isGrid = false, callback)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        pathView.text = currentPath

        view.findViewById<ImageButton>(R.id.change_layout).apply {
            setOnClickListener{

            }
        }

        view.findViewById<ImageButton>(R.id.dismiss).setOnClickListener {
            crossfade(arrayListOf(pathGroup),arrayListOf(bottomBar))
            adapter.changeMode(ExplorerListAdapter.Mode.Display)
        }

        view.findViewById<ImageButton>(R.id.action_ex).setOnClickListener {
            val select = adapter.getSelected().map {
                it.path
            }
            val dialog = FileActionDialog(select, object : FileActionDialog.Listener{
                override fun onRename(oldName: String, newName: String) {
                    val renamed = data.filter{
                        it.name == oldName
                    }[0]
                    val m =
                        FileDisplay(newName, renamed.createDate, renamed.size,
                            renamed.path.replace(oldName, newName), renamed.type)
                    val pos = data.indexOf(renamed)
                    data.remove(renamed)
                    data.add(m)
                    adapter.notifyItemRemoved(pos)
                    adapter.notifyItemInserted(data.size-1)
                    dismiss.performClick()
                }

                override fun onDelete(path: String) {
                    val deleted = adapter.data.filter {
                        it.path == path
                    }[0]
                    val pos = data.indexOf(deleted)
                    adapter.data.remove(deleted)
                    adapter.notifyItemRemoved(pos)
                    dismiss.performClick()
                }
            })

            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, "TAG")
        }

        view.findViewById<ImageButton>(R.id.previous).setOnClickListener{
            if(currentPath != rootPath){
                currentPath = adapter.back(currentPath)
                pathView.text = currentPath
            }else{
                Snackbar
                    .make(view, "Cant go back further", Snackbar.LENGTH_SHORT)
                    .setAction("OK"){
                        //
                    }
                    .show()
            }
        }

        view.findViewById<ImageButton>(R.id.check_all).setOnClickListener {
            adapter.flip()
        }

    }
}