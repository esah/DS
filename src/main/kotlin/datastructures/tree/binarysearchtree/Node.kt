package datastructures.tree.binarysearchtree

import datastructures.util.printNode

class Node<V: Comparable<V>>(
    var key: V,
    var left: Node<V>? = null,
    var right: Node<V>? = null
) {

    fun isLeaf(): Boolean = left == null && right == null


    fun find(value: V): Node<V>? = scan(value, null)?.first

    fun scan(value: V, parent: Node<V>?): Pair<Node<V>, Node<V>?>? = when {
        key > value -> left?.scan(value, this)
        key < value -> right?.scan(value, this)
        else -> Pair(this, parent)
    }

    fun insert(value: V) {
        if (key > value) {
            if (left == null) {
                left = Node(value)
            } else {
                left?.insert(value)
            }
        }
        if (key < value) {
            if (right == null) {
                right = Node(value)
            } else {
                right?.insert(value)
            }
        }
    }

    fun delete(value: V) {
        val (found, parent) = scan(value, null) ?: return

        if (found.isLeaf()) {
            removeLeaf(parent, found)
            return
        }

        if (found.left != null && found.right != null) {
            removeTwoChildNode(found)
            return
        }

        removeSingleChildNode(found)
    }

    private fun removeTwoChildNode(node: Node<V>) {
        //           10
        //        5      20
        //     2    7   15 25
        //    1 3  6

        val leftNode = node.left!!

        leftNode.right?.let {
            val maxParent = getParentOfMax(leftNode)
            val max = maxParent.right
            max?.let {
                maxParent.right = it.left
                node.key = it.key
            }

        } ?: run {
            node.key = leftNode.key
            node.left = leftNode.left
        }
    }
    /*
         Opa            Opa
          \              \
          Par    -->     Ch
         /  \           /  \
        Ch   Z         X   Par
       /  \                /  \
      X    Y              Y    Z
    */
    private fun rotateRight(opa: Node<V>?, parent: Node<V>, leftChild: Node<V>): Node<V>? {
        var result: Node<V>? = parent
        if (opa != null) {
            opa.right = leftChild
        } else {
            result = opa
        }
        parent.left = leftChild.right
        leftChild.right = parent

        return result
    }

    /*
         Opa            Opa
          \              \
          Par    -->     Ch
         /  \           /  \
        Z   Ch        Par   Y
           /  \      /  \
          X    Y    Z    X
    */
    private fun rotateLeft(opa: Node<V>?, parent: Node<V>, rightChild: Node<V>) {
        opa?.let {
            if (it.left == parent) {
                it.left = rightChild
            }
            if (it.right == parent) {
                it.right = rightChild
            }
        }
        parent.right = rightChild.left
        rightChild.left = parent
    }

    private fun getParentOfMax(n: Node<V>): Node<V> {
        if (n.right == null || n.right!!.right == null) {
            return n
        }
        return getParentOfMax(n.right!!)
    }

    private fun removeSingleChildNode(node: Node<V>) {
        val aLeaf = node.left ?: node.right!!
        node.key = aLeaf.key
        node.left = aLeaf.left
        node.right = aLeaf.right
    }

    private fun removeLeaf(node: Node<V>?, leaf: Node<V>) {
        if (node == null) {
            throw IllegalStateException("Can not remove the root node without child nodes")
        }
        if (node.left == leaf) {
            node.left = null
        }
        if (node.right == leaf) {
            node.right = null
        }
    }

    fun max(): Node<V> {
        return when (right) {
            null -> this
            else -> right!!.max()
        }
    }

    fun min(): Node<V> {
        return when (left) {
            null -> this
            else -> left!!.min()
        }
    }

    fun getHeight(): Int {
        var height = 0
        visit { n, d ->
            if (n.isLeaf()) {
                height = maxOf(height, d)
            }
        }
        return height
    }

    fun isBalanced(): Boolean {
        var maxHeight = 0
        var minHeight = Int.MAX_VALUE
        visit { n, d ->
            if (n.left == null || n.right == null) {
                minHeight = minOf(minHeight, d)
                maxHeight = maxOf(maxHeight, d)
            }
        }
        return maxHeight - minHeight <= 1
    }

    fun visit(callback : (Node<V>, Int) -> Unit) {
        visit(0, callback)
    }

    fun visit(depth: Int, callback : (Node<V>, Int) -> Unit) {
        callback(this, depth)

        left?.visit(depth + 1, callback)
        right?.visit(depth + 1, callback)
    }


    /**
     * 1 Make a sorted linked list by right-rotations
     * 2 Rotate every second node
     */
    fun doBalance() {
        this.createBackbone()
        this.rotateEverySecondNode()
    }

    /*
     *      5           2
     *    2   7    ->     3
     *      3               5
     *                        7
     */
    fun createBackbone() {
        var opa : Node<V>? = null
        var parent : Node<V>? = min()
        var leftChild: Node<V>?

        while (parent != null) {
            leftChild = parent.left
            if (leftChild != null) {
                rotateRight(opa, parent, leftChild)
                parent = leftChild
            } else {
                opa = parent;
                parent = parent.right
            }
            //visit(::printNode)
        }
    }

    private fun rotateEverySecondNode() {

    }


}
