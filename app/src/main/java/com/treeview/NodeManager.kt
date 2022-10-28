package com.treeview

import java.util.*

/**
 * Node Manager
 * List of node to layout
 */
class NodeManager {
    var nodes : LinkedList<Node> = LinkedList()

    fun expandNode(node : Node){
        val pos = nodes.indexOf(node)
        if(pos!=-1 && !node.isExpanded){
            node.isExpanded = true
            nodes.addAll(pos+1, node.children)
        }

        nodes.forEach {
            println(it.value.toString())
        }
    }

    fun collapseNode(node : Node){
        val pos = nodes.indexOf(node)
        if(pos!=-1 && node.isExpanded){
            node.isExpanded = false
            node.children.forEach {
                nodes.remove(it)
            }
        }
    }

    fun expandAll(){

    }

    fun collapseAll(){

    }
}