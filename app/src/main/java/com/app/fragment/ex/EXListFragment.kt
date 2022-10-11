package com.app.fragment.ex

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ExplorerListAdapter
import com.app.data.FileDisplay
import com.app.dialog.FileActionDialog
import com.app.listener.EXListListener
import com.app.ngn.R
import com.app.util.Animation.Companion.crossfade

class EXListFragment : Fragment(), EXListListener {
    private lateinit var data: MutableList<FileDisplay>
    private val rootPath : String = Environment.getExternalStorageDirectory().absolutePath
    private var path : String = rootPath
    private lateinit var adapter : ExplorerListAdapter
    private lateinit var list : RecyclerView
    private lateinit var pathView : TextView
    private lateinit var pathGroup: ConstraintLayout
    private lateinit var bottomBar: ConstraintLayout
    private lateinit var topBar: ConstraintLayout
    private lateinit var num: TextView
    private lateinit var listener : Listener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_ex_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as Listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = view.findViewById(R.id.fg_ex_list_list)
        pathView = view.findViewById(R.id.fg_ex_list_input)
        bottomBar = view.findViewById(R.id.fg_ex_list_bottom_bar)
        topBar = view.findViewById(R.id.fg_ex_list_top_bar)
        num = view.findViewById(R.id.fg_ex_list_num_selected)
        pathGroup = view.findViewById(R.id.fg_ex_list_input_group)
        val prev = view.findViewById<ImageButton>(R.id.previous)
        val dismissBottomBar = view.findViewById<ImageButton>(R.id.dismiss)
        val more = view.findViewById<Button>(R.id.more)
        val check = view.findViewById<ImageButton>(R.id.check_all)
        val changeLayout = view.findViewById<ImageButton>(R.id.fg_ex_list_changeLayout)

        val linearCallback = object : ExplorerListAdapter.Callback{
            override fun onCheck(position: Int) {

            }

            override fun onUnCheck(position: Int) {

            }

            override fun onClick(position : Int) {
                if(adapter.mode == ExplorerListAdapter.Mode.Display){
                    adapter.apply {
                        val action = getAction(position)
                        if(action[0] == "next"){
                            onPathChanged(action[1])
                        }
                    }
                }else{
                    adapter.check(position)
                }
            }

            override fun onLongClick(position: Int) {
                adapter.apply {
                    changeMode(ExplorerListAdapter.Mode.Select)
                    this.check(position)
                    crossfade(arrayListOf(bottomBar, topBar), arrayListOf(pathGroup))
                }
            }
        }

        adapter = ExplorerListAdapter(requireActivity(), rootPath, isGrid = false, linearCallback)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
        pathView.text = path

        changeLayout.setOnClickListener{
            /*if(grid){
                changeLayout.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_menu))
            }else{
                changeLayout.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_apps))
            }*/
        }

        dismissBottomBar.setOnClickListener {
            crossfade(arrayListOf(pathGroup),arrayListOf(bottomBar, topBar))
            adapter.changeMode(ExplorerListAdapter.Mode.Display)
        }

        more.setOnClickListener {
            val dialog = FileActionDialog(arrayListOf(), object : FileActionDialog.Listener{
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

            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, "TAG")
        }

        prev.setOnClickListener{
            if(pathView.text.toString() != rootPath || pathView.text.toString().length > rootPath.length){
                this.path = this.path.substringBeforeLast("/")
                onPathChanged(path)
            }else{
                pathView.text = rootPath
            }
        }

        check.setOnClickListener {

        }

    }

    override fun onPathChanged(path : String) {
        this.path = path
        pathView.text = path
        adapter.redisplay(path)
    }

    override fun onSelectionChange() {

    }

    interface Listener{
        fun onMultipleChanged(isMultiple : Boolean)
    }
}