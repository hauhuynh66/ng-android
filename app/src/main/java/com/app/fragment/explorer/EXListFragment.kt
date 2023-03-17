package com.app.fragment.explorer

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ExplorerListAdapter
import com.app.data.explorer.FileInfo
import com.app.data.explorer.FileInfoTask
import com.app.ngn.R
import com.app.task.TaskRunner
import com.general.Animation.Companion.crossfade
import com.general.FileUtils
import com.google.android.material.snackbar.Snackbar
import java.io.File

class EXListFragment : Fragment(), ExplorerListAdapter.OnItemClickListener,
    ExplorerListAdapter.OnItemLongClickListener {
    private val rootPath: String = Environment.getExternalStorageDirectory().absolutePath
    private var currentPath: String = rootPath
    private lateinit var adapter: ExplorerListAdapter
    private lateinit var list: RecyclerView
    private lateinit var path: TextView
    private lateinit var bottomBar: ConstraintLayout
    private lateinit var progress: ProgressBar
    private val taskRunner = TaskRunner()

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
        path = view.findViewById(R.id.path_view)
        bottomBar = view.findViewById(R.id.actions)
        progress = view.findViewById(R.id.progress)

        adapter = ExplorerListAdapter(null, isGrid = false)
        adapter.setOnItemClickListener(this)
        adapter.setOnItemLongClickListener(this)

        crossfade(progress, list, 500L)
        taskRunner.execute(FileInfoTask(rootPath), object : TaskRunner.Callback<List<FileInfo>> {
            override fun onComplete(result: List<FileInfo>) {
                adapter.setData(result)
                crossfade(list, progress, 500L)
            }
        })

        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        path.text = currentPath

        view.findViewById<ImageButton>(R.id.change_layout).apply {
            setOnClickListener {

            }
        }

        view.findViewById<ImageButton>(R.id.dismiss).setOnClickListener {
            crossfade(null, bottomBar)
            adapter.changeMode(ExplorerListAdapter.Mode.Display)
        }

        view.findViewById<ImageButton>(R.id.action_ex).setOnClickListener { it ->
            val select = adapter.getSelected().map {
                it.info.absolutePath
            }
            val menu = configMenu(requireContext(), it, select)

            menu.show()
        }

        view.findViewById<ImageButton>(R.id.previous).setOnClickListener {
            if (currentPath != rootPath) {
                val parent = File(currentPath).parent
                if (parent != null) {
                    onPathChange(parent)
                }
            } else {
                Snackbar
                    .make(view, "Cant go back further", Snackbar.LENGTH_SHORT)
                    .setAction("OK") {}
                    .show()
            }
        }

        view.findViewById<ImageButton>(R.id.check_all).setOnClickListener {
            adapter.flip()
        }
    }

    private fun configMenu(context: Context, view: View, select: List<String>): PopupMenu {
        val menu = PopupMenu(context, view)
        menu.menuInflater.inflate(R.menu.file_menu, menu.menu)
        val renameItem = menu.menu[2]
        if (select.size > 1) {
            renameItem.isEnabled = false
        }
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.file_menu_delete -> {
                    var message = "${select.size} file(s) deleted"
                    if (!handleDelete(select)) {
                        message = "Something went wrong"
                    }
                    onPathChange(currentPath)
                    Snackbar.make(this.requireView(), message, Snackbar.LENGTH_SHORT)
                        .setAction("OK") {}
                        .show()
                }
                else -> {

                }
            }
            true
        }
        return menu
    }

    private fun handleDelete(list: List<String>): Boolean {
        return FileUtils.deleteFiles(list) == list.size
    }

    fun handleMove(list: List<String>) {

    }

    fun handleRename(newPath: String) {

    }

    override fun onClick(fileInfo: FileInfo) {
        if (fileInfo.extension == null) {
            onPathChange(fileInfo.absolutePath)
        }
    }

    override fun onLongClick(fileInfo: FileInfo) {
        adapter.changeMode(ExplorerListAdapter.Mode.Select, fileInfo)
        crossfade(bottomBar)
    }

    private fun onPathChange(newPath: String) {
        crossfade(progress, list, 500L)
        taskRunner.execute(FileInfoTask(newPath), object : TaskRunner.Callback<List<FileInfo>> {
            override fun onComplete(result: List<FileInfo>) {
                adapter.setData(result)
                currentPath = newPath
                path.text = currentPath
                crossfade(list, progress, 500L)
            }
        })
    }
}