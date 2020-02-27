package datastructures.util

import datastructures.tree.BinaryNode
import datastructures.tree.redblacktree.Node

fun  <V:Comparable<V>, N: BinaryNode<V, N>> printNode(v: BinaryNode<V, N>, depth: Int) {
    for (i in 1..depth) {
        print('-')
    }
    val msg = when (v) {
        is Node -> "${v.key}${v.color.name.first().toLowerCase()}(${v.parent?.key})"
        else -> "${v.key}"
    }
    println(msg)
}
