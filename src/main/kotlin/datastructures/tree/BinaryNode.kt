package datastructures.tree

interface BinaryNode<V: Comparable<V>, N: BinaryNode<V, N>> {
    var key: V
    var left: N?
    var right: N?

    fun isLeaf(): Boolean = left == null && right == null

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


    fun visit(callback: (BinaryNode<V, N>, Int) -> Unit) {
        visit(0, callback)
    }

    /**
     * Pre-order depth first traversal
     */
    fun visit(depth: Int, callback: (BinaryNode<V, N>, Int) -> Unit) {
        callback(this, depth)
        left?.visit(depth + 1, callback)
        right?.visit(depth + 1, callback)
    }

    fun visitBreadthFirst(callback: (BinaryNode<V, N>, Int) -> Unit) {
        for (level in 0..getHeight()) {
            visitBreadthFirst(level, 0, callback)
        }
    }

    /**
     * Breadth First Level Order Traversal
     */
    private fun visitBreadthFirst(level: Int, depth: Int, callback: (BinaryNode<V, N>, Int) -> Unit) {
        if (level == 0) {
            callback(this, depth)
        } else if (level > 0) {
            left?.visitBreadthFirst(level - 1, depth + 1, callback)
            right?.visitBreadthFirst(level - 1, depth + 1, callback)
        }
    }

        /*
    * Time complexity: O(n)
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

    fun find(value: V): N? = scan(value, null)?.first

    fun scan(value: V, parent: N?): Pair<N, N?>? = when {
        key > value -> left?.scan(value, this as N)
        key < value -> right?.scan(value, this as N)
        else -> Pair(this as N, parent)
    }


}

/*
      \              \
      Par    -->     Ch
     /  \           /  \
    Ch   Z         X   Par
   /  \                /  \
  X    Y              Y    Z
*/
private fun <V:Comparable<V>, N: BinaryNode<V, N>> rotateRight(par: N, ch:N) {
    swapKey(par, ch)
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
    swapKey(par, ch)

    val tmpParLeft = par.left // Z
    par.left = ch // New Par
    par.right = ch.right // Y

    val tmpChLeft = ch.left // X
    ch.left = tmpParLeft  // Z
    ch.right = tmpChLeft // X

}

private fun <V:Comparable<V>, N: BinaryNode<V, N>> swapKey(n1: N, n2: N) {
    val tmpKey = n1.key
    n1.key = n2.key
    n2.key = tmpKey
}
