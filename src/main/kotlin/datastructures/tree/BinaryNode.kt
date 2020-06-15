package datastructures.tree

interface BinaryNode<V: Comparable<V>, N: BinaryNode<V, N>> {
    var key: V
    var left: N?
    var right: N?

    val isLeaf
        get() = left == null && right == null

    fun rotateLeft() {
        if (right != null) {
            rotateLeft(this as N, right!!)
        }
    }

    fun rotateLeftTimes(count: Int) {
        var current: BinaryNode<V, N>? = this
        for (i in 0 until count) {
            if (current != null) {
                current.rotateLeft()
                current = current.right
            }
        }
    }

    fun rotateRight() {
        if (left != null) {
            rotateRight(this as N, left!!)
        }
    }

    fun visit(callback: (BinaryNode<V, N>, Int) -> Unit) = visit(0, callback)
    /**
     * Pre-order depth first traversal
     *
     * Another depth first traversals:
     *  Inorder:    Left - Root  - Right
     *  Pre-order:  Root - Left  - Right
     *  Post-order: Left - Right - Root
     *  O(n)
     */
    fun visit(depth: Int, callback: (BinaryNode<V, N>, Int) -> Unit) {
        callback(this, depth)
        left?.visit(depth + 1, callback)
        right?.visit(depth + 1, callback)
    }

    fun inOrderVisit(callback: (BinaryNode<V, N>, Int) -> Unit) = inOrderVisit(0, callback)
    fun inOrderVisit(depth: Int, callback: (BinaryNode<V, N>, Int) -> Unit) {
        left?.visit(depth + 1, callback)
        callback(this, depth)
        right?.visit(depth + 1, callback)
    }

    fun visitBreadthFirst(callback: (BinaryNode<V, N>, Int) -> Unit) {
        for (level in 0..getHeight()) {
            visitBreadthFirst(level, 0, callback)
        }
    }

    /**
     * Breadth First Level Order Traversal = Level Order Traversal
     * O(n)
     */
    private fun visitBreadthFirst(level: Int, depth: Int, callback: (BinaryNode<V, N>, Int) -> Unit) {
        if (level == 0) {
            callback(this, depth)
        } else if (level > 0) {
            left?.visitBreadthFirst(level - 1, depth + 1, callback)
            right?.visitBreadthFirst(level - 1, depth + 1, callback)
        }
    }

    fun getHeight(): Int {
        return height(this as N) - 1
    }

    /*
    * Time complexity: O(n)
     */
    fun getHeight2(): Int {
        var height = 0
        visit { n, d ->
            if (n.isLeaf) {
                height = maxOf(height, d)
            }
        }
        return height
    }

    /**
     * O(log n) for balanced tree
     */
    fun getHeightForBalancedTree(): Int {
        return balHeight(this as N) - 1
    }

    fun find(value: V): N? = scan(value, null)?.first

    fun scan(value: V, parent: N?): Pair<N, N?>? = when {
        key > value -> left?.scan(value, this as N)
        key < value -> right?.scan(value, this as N)
        else -> Pair(this as N, parent)
    }

    fun findNear(value: V): N = scanNear(value, null).first

    fun scanNear(value: V, parent: N?): Pair<N, N?> = when {
        key > value -> left?.scan(value, this as N) ?: this as N to parent
        key < value -> right?.scan(value, this as N) ?: this as N to parent
        else -> Pair(this as N, parent)
    }

    fun swapWith(n: N) {
        val tmpKey = key
        key = n.key
        n.key = tmpKey
    }

    fun predecessor(): N? = left?.max()
    fun successor(): N? = right?.min()

    fun max(): N {
        val p = maxParent()
        return p.right ?: p
    }

    fun maxParent(parent: N? = null): N {
        return when (right) {
            null -> parent ?: this as N
            else -> right!!.maxParent(this as N)
        }
    }

    /*
    * Time complexity: log(n) for balanced tree
     */
    fun min(): N {
        return when (left) {
            null -> this as N
            else -> left!!.min()
        }
    }
}

fun <V : Comparable<V>, N : BinaryNode<V, N>> height(n: N?): Int =
    if (n == null) 0 else 1 + maxOf(height(n.left), height(n.right))

fun <V : Comparable<V>, N : BinaryNode<V, N>> balHeight(n: N?): Int =
    if (n == null) 0 else 1 + balHeight(n.left)

/*
      \              \
      Par    -->     Ch
     /  \           /  \
    Ch   Z         X   Par
   /  \                /  \
  X    Y              Y    Z
*/
private fun <V:Comparable<V>, N: BinaryNode<V, N>> rotateRight(par: N, ch:N) {
    par.swapWith(ch)
    val tmpParRight = par.right // Z
    par.left = ch.left // X
    par.right = ch // New Par

    ch.left = ch.right // Y
    ch.right = tmpParRight // Z
}

/*
      \              \
      Par    -->     Ch
     /  \           /  \
    Z   Ch        Par   Y
       /  \      /  \
      X    Y    Z    X
*/
private fun <V:Comparable<V>, N: BinaryNode<V, N>> rotateLeft(par: N, ch: N) {
    par.swapWith(ch)

    val tmpParLeft = par.left // Z
    par.left = ch // New Par
    par.right = ch.right // Y

    val tmpChLeft = ch.left // X
    ch.left = tmpParLeft  // Z
    ch.right = tmpChLeft // X

}
