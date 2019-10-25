package datastructures.util

import datastructures.tree.binarysearchtree.Node

fun <V:Comparable<V>> printNode(v: Node<V>, depth: Int) {
    for (i in 1..depth) {
        print('-')
    }
    println(v.key)
}