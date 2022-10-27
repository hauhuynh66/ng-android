package com.treeview

import java.util.*

class Node(val value : Any) {
    var level : Int = 0
    var parent : Node? = null
    val children : LinkedList<Node> = LinkedList<Node>()
    val isExpanded : Boolean = false
    val layoutId : Int = 0

    fun addChild(node: Node){
        children.add(node)
        level+=1
        node.parent = this
        updateChildDepth(node)
    }

    fun updateChildDepth(childNode: Node){
        childNode.children.forEach{
            it.level+=1
        }
    }
}