package com.treeview.binary

import java.util.*

/**
 * Node class
 * level :
 * parent : parent node
 * children : child nodes
 * isExpanded : node display status
 * layoutId : node layout resource
 */
class BNode(val value : Any) {
    var left : BNode? = null
    var right : BNode? = null
    private var depth : Int = 0
    val uuid : String = UUID.randomUUID().toString()

    fun attachLeft(bNode : BNode){
        this.left = bNode
        bNode.depth += 1
    }

    fun attachRight(bNode : BNode){
        this.right = bNode
        bNode.depth += 1
    }
}