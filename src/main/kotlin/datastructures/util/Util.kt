package datastructures.util

import datastructures.tree.BinaryNode

fun  <V:Comparable<V>, N: BinaryNode<V, N>> printNode(v: BinaryNode<V, N>, depth: Int) {
    for (i in 1..depth) {
        print('-')
    }
    println(v.key)
}

fun <V:Comparable<V>> printNode(v: datastructures.tree.redblacktree.Node <V>, depth: Int) {
    for (i in 1..depth) {
        print('-')
    }
    println("$v.key $v.color")
}
