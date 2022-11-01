package com.treeview.single

import java.util.*

/**
 * Node class
 * level :
 * parent : parent node
 * children : child nodes
 * isExpanded : node display status
 * layoutId : node layout resource
 */
open class Node(val value : Any) {
    var level : Int = 0
    var parent : Node? = null
    val children : LinkedList<Node> = LinkedList<Node>()
    var isExpanded : Boolean = false
    private val uuid : String = UUID.randomUUID().toString()

    fun addChild(node: Node){
        this.children.add(node)
        node.level = this.level + 1
        node.parent = this
        updateLevel(node)
    }

    private fun updateLevel(node: Node){
        node.children.forEach{
            it.level += 1
            updateLevel(it)
        }
    }

    override fun equals(other: Any?): Boolean {
        return (other as Node).uuid == uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}