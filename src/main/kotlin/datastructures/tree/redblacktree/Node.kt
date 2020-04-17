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
    constructor(key: V, color: Color, parent: Node<V>?) : this(key, null, null, parent, color)
    constructor(key: V, color: Color) : this(key, color, null)

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


    override fun swapWith(n: Node<V>) {
        super.swapWith(n)
        val tmpColor = this.color
        this.color = n.color
        n.color = tmpColor
    }

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
                    continue
                }

                if (node.parent?.right == node) { // Left Right
                    node.parent?.rotateLeft()
                }
                // Left Left
                node.grandParent?.rotateRight()
                node = node.parent
                node?.color = BLACK
                node?.right?.color = RED

            } else {
                uncle = node.grandParent?.left

                if (uncle?.color == RED) { // Case 1
                    uncle.color = BLACK
                    node.parent?.color = BLACK
                    node.grandParent?.color = RED
                    node = node.grandParent
                    continue
                }

                if (node.parent?.left == node) { // Right Left
                    node.parent?.rotateRight()
                }
                // Right Right
                node.grandParent?.rotateLeft()
                node = node.parent
                node?.color = BLACK
                node?.left?.color = RED
            }
        }

        node?.setRootBlack()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node<*>

        if (key != other.key) return false
        if (parent != other.parent) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + color.hashCode()
        return result
    }

    override fun toString(): String {
        return "Node(key=$key, parent=$parent, color=$color)"
    }


}
