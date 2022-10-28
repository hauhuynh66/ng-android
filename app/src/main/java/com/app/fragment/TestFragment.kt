package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.treeview.Node
import com.treeview.NodeManager
import com.treeview.TreeAdapter

/**
 * Test Fragment
 * Fragment for testing new components
 */

class TestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nodeManager = NodeManager()
        val n1 = Node("Test1")
        n1.addChild(Node("Test11"))
        n1.addChild(Node("Test12"))
        val n2 = Node("Test2")
        n2.addChild(Node("Test21"))
        n2.addChild(Node("Test22"))
        nodeManager.nodes.add(n1)
        nodeManager.nodes.add(n2)
        view.findViewById<RecyclerView>(R.id.item_list).apply {
            val treeAdapter = TreeAdapter(nodeManager)
            treeAdapter.setOnNodeClickListener(object : TreeAdapter.OnNodeClickListener{
                override fun onClick(node: Node) {
                    if(!node.isExpanded){
                        nodeManager.expandNode(node)
                        treeAdapter.notifyDataSetChanged()
                    }else{
                        nodeManager.collapseNode(node)
                        treeAdapter.notifyDataSetChanged()
                    }
                }
            })
            layoutManager = LinearLayoutManager(context, VERTICAL,false)
            adapter = treeAdapter
        }
    }

}