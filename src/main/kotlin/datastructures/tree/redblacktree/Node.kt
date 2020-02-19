package datastructures.tree.redblacktree

import datastructures.tree.BinaryNode
import datastructures.tree.redblacktree.Color.BLACK
import datastructures.tree.redblacktree.Color.RED

enum class Color { RED, BLACK }

/*
1 Red/Black Property
2 Root Property - always BLACK
3 Red Property - A RED node can't link to a RED node
4 Depth Property - Every path root->leaf has the same BLACK nodes (black-depth)
*/
//AVL: more rotations during modification
class Node<V : Comparable<V>>(
    override var key: V,
    left: Node<V>?,
    right: Node<V>?,
    var parent: Node<V>? = null,
    var color: Color = RED
): BinaryNode<V, Node<V>>  {
    override var right = right
        set(v) {
            field = v
            field?.parent = this
        }
    override var left = left
        set(v) {
            field = v
            field?.parent = this
        }


    fun insert(value: V) {
        insert(Node(value, null, null))
    }

    fun insert(n: Node<V>) {
        if (n.key > key) {
            if (right != null) {
                right!!.insert(n)
                //modify parent if no parent link
            } else {
                right = n
                n.parent = this
                n.color = RED
                n.insertBalance()
            }
        } else if (n.key < key) {
            if (left != null) {
                left!!.insert(n)
            } else {
                left = n
                n.parent = this
                n.color = RED
                n.insertBalance()
            }
        }
    }

    //recoloring if not then rotation
    private fun insertBalance() {
        if (parent == null) {
            color = BLACK
            return
        }
        var node = this
        while (node.parent != null && node.parent?.parent != null && node.parent!!.color == RED) {
            val parent = node.parent!!
            val grandParent = node.parent!!.parent!!
            val uncle: Node<V>?

            if (grandParent.left == node.parent) {
                uncle = grandParent.right
                if (uncle?.color == RED) { // Case 1
                    uncle.color = BLACK
                    parent.color = BLACK
                    grandParent.color = RED
                    node = grandParent
                } else {
                    if (parent.right == node) { // Case 2
                        node = parent
                        node.rotateLeft()
                    }
                    //Case 3
                    node.parent?.color = BLACK
                    node.parent?.parent?.color = RED
                    node.parent?.parent?.rotateRight()
                }

            } else { // Case 4
                uncle = grandParent.left

                if (uncle?.color == RED) {
                    uncle.color = BLACK
                    parent.color = BLACK
                    grandParent.color = RED
                    node = grandParent
                } else {
                    if (parent.left == node) {
                        node = parent
                        node.rotateRight()
                    }
                    node.parent?.color = BLACK
                    node.parent?.parent?.color = RED
                    node.parent?.parent?.rotateLeft()
                }
            }
        }
        //set root BLACK
        if (node.parent == null) {
            node.color = BLACK
        } else if (node.parent?.parent == null) {
            node.parent?.color = BLACK
        }
    }

}
