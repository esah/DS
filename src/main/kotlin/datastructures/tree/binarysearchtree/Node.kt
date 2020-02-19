package datastructures.tree.binarysearchtree

import datastructures.tree.BinaryNode
import datastructures.util.log2
import datastructures.util.twoPow


class Node<V : Comparable<V>>(
    override var key: V,
    override var left: Node<V>? = null,
    override var right: Node<V>? = null
) : BinaryNode<V, Node<V>> {

    fun find(value: V): Node<V>? = scan(value, null)?.first

    fun scan(value: V, parent: Node<V>?): Pair<Node<V>, Node<V>?>? = when {
        key > value -> left?.scan(value, this)
        key < value -> right?.scan(value, this)
        else -> Pair(this, parent)
    }

    fun insert(value: V) {
        insertNonRecursive(value)
    }

    fun insertRecursive(value: V) {
        if (key > value) {
            if (left == null) {
                left = Node(value)
            } else {
                left!!.insert(value)
            }
        }
        if (key < value) {
            if (right == null) {
                right = Node(value)
            } else {
                right!!.insert(value)
            }
        }
    }

    fun insertNonRecursive(value: V) {
        var node: Node<V> = this
        while (true) {
            if (node.key > value) {
                if (node.left == null) {
                    node.left = Node(value)
                    break
                }
                node = node.left as Node<V>
            }
            if (node.key <= value) {
                if (node.right == null) {
                    node.right = Node(value)
                    break
                }
                node = node.right as Node<V>
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
    * Time complexity: log(n)
     */
    fun min(): Node<V> {
        return when (left) {
            null -> this
            else -> left!!.min()
        }
    }


    fun getN(): Int {
        var result = 0
        visit { n, d -> result++ }
        return result
    }

    fun getBalancedHeight() = log2(getN())

    fun getPerfectN(n: Int) = twoPow(log2(n + 1)) - 1

    // Full portion of a complete tree
    fun getPerfectN_2(n: Int): Int {
        var result = 1
        while (result <= n) {
            // Drive one step PAST FULL
            result = result + result + 1   // next 2^k - 1
        }
        return result / 2
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

    /**
     * 1 Make a sorted linked list by right-rotations
     * 2 Rotate every second node
     * https://stackoverflow.com/questions/14001676/balancing-a-bst
     */
    fun doBalance() {
        this.createBackbone()
        this.rotateEverySecondNode()
    }

    /*                1
     *      5           2
     *    2   7    ->     3
     *  1   3               5
     *                        7
     */
    fun createBackbone() {
        // visit { n, i -> while (n.left != null) n.rotateRight() }
        var current: Node<V>? = this
        while (current != null) {
            while (current.left != null) current.rotateRight()
            current = current.right
        }
    }

     // O(n)
     fun rotateEverySecondNode() {
         val n = getN()
         //to generate not just a balanced tree, but also a complete tree, one in which the bottommost tree level is filled from left to right.
         //The only change is to do a partial pass down the backbone/vine such that the remaining structure has a vine length given (for some integer k) by 2k–1
         val perfectN = getPerfectN(n)
         val leftovers = n - perfectN
         rotateLeftTimes(leftovers)

         var times = perfectN
         while (times > 1) {
             times /= 2
             rotateLeftTimes(times)
         }
     }

}
