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

fun <V : Comparable<V>> newTree(key: V, vararg keys: V): Node<V> =
    Node(key, BLACK).apply { keys.forEach { this.insert(it) } }


class Node<V : Comparable<V>>(
    override var key: V,
    left: Node<V>? = null,
    right: Node<V>? = null,
    var parent: Node<V>? = null,
    var color: Color = RED
): BinaryNode<V, Node<V>>  {
    constructor(key: V, color: Color) : this(key, null, null, null, color)

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

    val grandParent: Node<V>?
        get() = parent?.parent

    fun insert(value: V) {
        insert(Node(value))
    }

    fun insert(n: Node<V>) {
        //TODO also Top-Down insertion https://www.geeksforgeeks.org/red-black-trees-top-down-insertion/
        if (n.key > key) {
            if (right != null) {
                right!!.insert(n)
                //modify parent if no parent link
            } else {
                right = n
                n.parent = this
                n.color = RED
                n.insertingBalance()
            }
        } else if (n.key < key) {
            if (left != null) {
                left!!.insert(n)
            } else {
                left = n
                n.parent = this
                n.color = RED
                n.insertingBalance()
            }
        }
    }

    private fun setRootBlack() {
        if (parent == null) {
            color = BLACK
        } else {
            parent?.setRootBlack()
        }
    }

    //recoloring if not then rotation
    private fun insertingBalance() {
        var node : Node<V>? = this

        while (node?.parent?.color == RED) {
            val uncle: Node<V>?

            if (node.grandParent?.left == node.parent) {
                uncle = node.grandParent?.right

                if (uncle?.color == RED) { // Case 1
                    uncle.color = BLACK
                    node.parent?.color = BLACK
                    node.grandParent?.color = RED
                    node = node.grandParent
                } else {
                    if (node.parent?.right == node) { // Case 2
                        node = node.parent
                        node?.rotateLeft()
                    }
                    node?.parent?.color = BLACK
                    node?.grandParent?.color = RED
                    node?.grandParent?.rotateRight()
                }

            } else { // Case 4
                uncle = node.grandParent?.left

                if (uncle?.color == RED) {
                    uncle.color = BLACK
                    node.parent?.color = BLACK
                    node.grandParent?.color = RED
                    node = node.grandParent
                } else {
                    if (node.parent?.left == node) {
                        node = node.parent
                        node?.rotateRight()
                    }
                    node?.parent?.color = BLACK
                    node?.grandParent?.color = RED
                    node?.grandParent?.rotateLeft()
                }
            }
        }

        node?.setRootBlack()
    }

}
