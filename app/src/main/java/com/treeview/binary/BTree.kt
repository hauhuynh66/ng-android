package com.treeview.binary

class BTree(private val root : BNode?) {
    enum class TraverseType{
        PreOrder,
        PostOrder,
        InOrder
    }

    fun traverse(type : TraverseType){
        when(type){
            TraverseType.InOrder->{
                inOrder(this.root)
            }
            TraverseType.PostOrder->{
                postOrder(this.root)
            }
            TraverseType.PreOrder->{
                preOrder(this.root)
            }
        }
    }

    private fun inOrder(root : BNode?){
        if (root==null){
            return
        }
        inOrder(root.left)

        println("${root.value}   ")

        inOrder(root.right)
    }

    private fun preOrder(root : BNode?){
        if (root == null){
            return
        }

        println("${root.value}   ")

        preOrder(root.left)
        preOrder(root.right)
    }

    private fun postOrder(root : BNode?){
        if(root == null){
            return
        }

        postOrder(root.left)
        postOrder(root.right)

        println("${root.value}   ")
    }

}