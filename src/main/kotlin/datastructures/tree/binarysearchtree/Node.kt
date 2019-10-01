package datastructures.tree.binarysearchtree

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

/*
    fun height(): Int {
        if (this.isLeaf()) {
            return 0
        }
        val leftHeight = if (left != null) left!!.height() else 0
        val rightHeight = if (right != null) right!!.height() else 0
        return maxOf(leftHeight, rightHeight) + 1;
    }
*/

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


    fun doBalance() {
        TODO() //left/right- rotations
    }


}
