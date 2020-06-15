package datastructures.tree.binarysearchtree

import datastructures.tree.BinaryNode
import datastructures.util.log2
import datastructures.util.twoPow


class Node<V : Comparable<V>>(
    override var key: V,
    override var left: Node<V>? = null,
    override var right: Node<V>? = null
) : BinaryNode<V, Node<V>> {

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

    fun predecessorAndSuccessor(v: V): Pair<Node<V>, Node<V>> {
        var p: Node<V>? = null
        var s: Node<V>? = null
        inOrderVisit { n, _ ->
            if (n.key < v) {
                if (p == null || p!!.key < n.key) {
                    p = n as Node<V>
                }
            }
            if (n.key > v) {
                if (s == null || s!!.key > n.key) {
                    s = n as Node<V>
                }
            }
        }
        return Pair(p!!, s!!)
    }

    fun delete(value: V) {
        val (n, parent) = scan(value, null) ?: return

        if (n.isLeaf) {
            removeLeaf(parent, n)
            return
        }
        if (n.left != null && n.right != null) {
            removeTwoChildNode(n)
            return
        }
        removeSingleChildNode(n)
    }

    private fun removeTwoChildNode(node: Node<V>) {
        //           10
        //        5      20
        //     2    7   15 25
        //    1 3  6
        //       4

        val leftNode = node.left!!

        leftNode.right?.let {
            val maxParent = leftNode.maxParent() //3
            val max = maxParent.right //4
            max?.let {
                maxParent.right = it.left
                node.key = it.key
            }

        } ?: run {
            node.key = leftNode.key
            node.left = leftNode.left
        }
    }

    private fun removeSingleChildNode(node: Node<V>) {
        val aLeaf = node.left ?: node.right!!
        node.key = aLeaf.key
        node.left = aLeaf.left
        node.right = aLeaf.right
    }

    private fun removeLeaf(parent: Node<V>?, leaf: Node<V>) {
        if (parent == null) {
            throw IllegalStateException("Can not remove the root node without child nodes")
        }
        if (parent.left == leaf) {
            parent.left = null
        }
        if (parent.right == leaf) {
            parent.right = null
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

    override fun toString(): String {
        return "N($key)"
    }

}
