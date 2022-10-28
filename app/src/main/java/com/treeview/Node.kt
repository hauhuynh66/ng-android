package com.treeview

import java.util.*

/**
 * Node class
 * level :
 * parent : parent node
 * children : child nodes
 * isExpanded : node display status
 * layoutId : node layout resource
 */
class Node(val value : Any) {
    var level : Int = 0
    var parent : Node? = null
    val children : LinkedList<Node> = LinkedList<Node>()
    var isExpanded : Boolean = false

    fun addChild(node: Node){
        this.children.add(node)
        node.level+=1
        node.parent = this
        updateChildDepth(node)
    }

    private fun updateChildDepth(childNode: Node){
        childNode.children.forEach{
            it.level+=1
        }
    }
}