package datastructures.tree.redblacktree

import datastructures.util.printNode
import org.junit.Test

class NodeTest {

    @Test
    fun insert() {
        val root = newTree(10)
        for (i in arrayOf(1, 5)) {
            println("Insert $i")
            root.insert(i)
            root.visit(::printNode)
        }
        //todo find & assert
    }

    @Test
    fun newTree() {
        val root = newTree(10, 1, 5, 7, 21, 50, 11, 51, 9, 4, 3)
        root.visit(::printNode)
    }

    @Test
    fun rotate() {
        val n = newTree(10, 5, 15)
        n.visit(::printNode)
        println("---")
        n.rotateRight()
        n.visit(::printNode)
        println("---")
        n.rotateLeft()
        n.visit(::printNode)
        println("---")
        n.rotateLeft()
        n.visit(::printNode)
    }
}
