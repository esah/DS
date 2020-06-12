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
//@see https://www.cs.usfca.edu/~galles/visualization/RedBlack.html
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

    val grandParent
        get() = parent?.parent
    val uncle
        get() = if (parent == grandParent?.left) grandParent?.right else grandParent?.left
    val brother
        get() = if (isRight) parent?.left else parent?.right
    val root
        get() = parent == null
    val orphan
        get() = parent != null && !isRight && !isLeft
    val isRight : Boolean
        get() = parent?.right == this
    val isLeft : Boolean
        get() = parent?.left == this


    fun swapColors(n: Node<V>) {
        val tmpColor = this.color
        this.color = n.color
        n.color = tmpColor
    }

    override fun swapWith(n: Node<V>) {
        super.swapWith(n)
        swapColors(n)
    }

    fun insert(value: V) {
        insert(Node(value))
    }

    //TODO also see Top-Down insertion https://www.geeksforgeeks.org/red-black-trees-top-down-insertion/
    fun insert(n: Node<V>) {
        if (n.key > key) {
            if (right != null) {
                right!!.insert(n)
                //modify parent if no parent link
            } else {
                right = n
                n.parent = this
                n.color = RED
                n.removeDoubleReds()
            }
        } else if (n.key < key) {
            if (left != null) {
                left!!.insert(n)
            } else {
                left = n
                n.parent = this
                n.color = RED
                n.removeDoubleReds()
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

    private fun removeDoubleReds() {
        var node : Node<V>? = this

        while (node?.parent?.color == RED) {

            val uncle = node.uncle
            if (uncle?.color == RED) { // Case 1
                uncle.color = BLACK
                node.parent?.color = BLACK
                node.grandParent?.color = RED
                node = node.grandParent
                continue
            }

            if (node.parent!!.isLeft) {
                if (node.isRight) { // Left Right
                    node.parent?.rotateLeft()
                }
                // Left Left
                node.grandParent?.rotateRight()
                node = node.parent
                node?.color = BLACK
                node?.right?.color = RED

            } else {
                if (node.isLeft) { // Right Left
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

    fun delete(v: V) = scan(v, null)?.first?.let { delete(it) }

    fun delete(n: Node<V>) {
        var originColor = n.color
        //no child
        if (n.isLeaf()) { //?
            if (n.isLeft) {
                n.parent?.left = null
            } else {
                n.parent?.right = null
            }
            if (originColor == BLACK) removeDoubleBlacks(n)
            return
        }
        //single child
        if (n.left == null || n.right == null) {
            val aChild = n.right ?: n.left
            aChild?.let {
                n.swapWith(it)
                n.left = it.left
                n.right = it.right
            }
            when (originColor) {
                BLACK -> removeDoubleBlacks(n)
                RED -> n.color = BLACK
            }
            return
        }
        //double child
        val successor = n.right?.successor()!!
        originColor = successor.color
        n.swapWith(successor)
        if (successor.parent == n) {
            n.right = successor.right
        } else if (successor.right != null) {
            successor.parent?.left = successor.right
        }
        when (originColor) {
            BLACK -> removeDoubleBlacks(n)
            RED -> n.color == BLACK
        }
    }

    private fun removeDoubleBlacks(n: Node<V>) {
        if (!n.orphan && n.color == RED) { //red-black
            n.color = BLACK
            return
        }
        if (n.root) {
            return
        }
        val parent = n.parent!!
        val s = n.brother
        if (s == null) {
            removeDoubleBlacks(parent)
            return
        }

        if (s.color == RED) {
            //rotation the sibling up & recolour the old sibling
            if (s.isLeft) {
                parent.rotateRight()
            } else if (s.isRight) {
                parent.rotateLeft()
            }
            parent.color = RED
            s.color = BLACK
            return
        }

        //double-black case
        assert(s.color == BLACK)

        if (s.left?.color == BLACK && s.right?.color == BLACK) {
            n.color = RED
            s.color = RED
            if (n.parent?.color == BLACK) {
                removeDoubleBlacks(parent)
            } else {
                n.parent?.color = BLACK
            }
        } else { // some child is RED
            //rotate sibling s & red child r (or both children are red)
            if (s.isLeft) {
                if (s.left?.color == RED) {
                    parent.rotateRight()
                    parent.left!!.color = BLACK
                } else if (s.right?.color == RED) {
                    s.rotateLeft()
                    s.color == BLACK
                    parent.rotateRight()
                }
            } else if (s.isRight) {
                if (s.right?.color == RED) {
                    parent.rotateLeft()
                    parent.right!!.color = BLACK
                } else if (s.left?.color == RED) {
                    s.rotateRight()
                    s.color == BLACK
                    parent.rotateLeft()
                }
            }
            return
        }

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
