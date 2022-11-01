package com.treeview.single

import java.util.*

/**
 * Node Manager
 * List of node to layout
 */
class NodeManager(var nodes : LinkedList<Node>) {
    fun expandNode(node : Node){
        val pos = nodes.indexOf(node)
        if(pos!=-1 && !node.isExpanded){
            node.isExpanded = true
            nodes.addAll(pos+1, node.children)
            node.children.forEach {
                expand(it)
            }
        }
    }

    fun collapseNode(node : Node){
        val pos = nodes.indexOf(node)
        if(pos!=-1 && node.isExpanded){
            node.isExpanded = false
            nodes.removeAll(node.children)
            node.children.forEach {
                collapse(it)
            }
        }
    }

    fun expandAll(){
        nodes.forEach {
            expandNode(it)
        }
    }

    fun collapseAll(){
        nodes.forEach {
            collapseNode(it)
        }
    }

    private fun expand(node : Node){
        val pos = nodes.indexOf(node)
        if(pos!=-1 && node.isExpanded){
            nodes.addAll(pos + 1, node.children)
            node.children.forEach{
                expand(it)
            }
        }
    }

    private fun collapse(node: Node){
        node.children.forEach {
            if(nodes.contains(it)){
                nodes.remove(it)
                collapse(it)
            }
        }
    }
}