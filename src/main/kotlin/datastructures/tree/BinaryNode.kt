package datastructures.tree

interface BinaryNode<V: Comparable<V>, N: BinaryNode<V, N>> {
    var key: V
    var left: N?
    var right: N?

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

    val tmpParRight = par.right
    par.left = ch.left
    par.right = ch

    ch.left = ch.right
    ch.right = tmpParRight
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

    val tmpParLeft = par.left
    par.left = ch
    par.right = ch.right

    val tmpChLeft = ch.left
    ch.left = tmpParLeft
    ch.right = tmpChLeft

}

private fun <V:Comparable<V>, N: BinaryNode<V, N>> swapKey(n1: N, n2: N) {
    val tmpKey = n1.key
    n1.key = n2.key
    n2.key = tmpKey
}