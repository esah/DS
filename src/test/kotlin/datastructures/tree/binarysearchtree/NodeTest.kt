package datastructures.tree.binarysearchtree

import org.junit.Assert
import org.junit.Test

class NodeTest {

    fun printNode(v: Node<Int>, depth: Int) {
        for (i in 1..depth) {
            print('-')
        }
        println(v.key)
    }

    @Test
    fun test() {
        val n = Node(10)
        for (i in arrayOf(1, 5, 7, 21, 50, 11, 51, 9, 4, 3)) {
            n.insert(i)
        }
        n.visit(::printNode)

        n.delete(10)
        Assert.assertNull(n.find(10))
        n.visit(::printNode)

        n.delete(50)
        Assert.assertNull(n.find(50))
        n.visit(::printNode)
    }


    @Test
    fun max() {
        val n = Node(10)
        for (i in arrayOf(1, 5, 7, 21, 50, 11, 51, 9, 4, 3)) {
            n.insert(i)
        }
        Assert.assertEquals(51, n.max().key)
    }

    @Test
    fun height() {
        val n = Node(10)
        for (i in arrayOf(20, 30, 40, 50, 60, 70)) {
            n.insert(i)
        }
        n.visit(::printNode)
        Assert.assertEquals(6, n.getHeight())

        Assert.assertFalse(n.isBalanced())

        n.doBalance()

        Assert.assertEquals(3, n.getHeight())
        Assert.assertTrue(n.isBalanced())
    }

}