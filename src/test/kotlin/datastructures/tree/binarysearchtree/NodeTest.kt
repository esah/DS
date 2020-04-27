package datastructures.tree.binarysearchtree

import datastructures.util.printNode
import org.junit.Assert
import org.junit.Test

class NodeTest {


    @Test
    fun delete() {
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
        var n = Node(1)
        for (i in arrayOf(2, 3, 4, 5, 6, 7)) {
            n.insert(i)
        }
        Assert.assertEquals(6, n.getHeight())
        Assert.assertFalse(n.isBalanced())

        n = Node(4)
        for (i in arrayOf(2, 6, 1, 3, 5, 7)) {
            n.insert(i)
        }

        Assert.assertEquals(2, n.getHeight())
        Assert.assertTrue(n.isBalanced())
    }

    @Test
    fun getN() {
        val n = Node(1)
        for (i in arrayOf(2, 3, 7, 5, 6, 4)) {
            n.insert(i)
        }
        Assert.assertEquals(7, n.getN())
    }

    /*
          \             \
          3    -->      5
         /  \          /  \
        1    5        3    7
           /  \      /  \
          4    7    1    4
    */
    @Test
    fun rotateLeft() {
        val n = Node(3)
        for (i in arrayOf(1, 5, 4, 7)) {
            n.insert(i)
        }
        println("Original: ")
        n.visit(::printNode)

        n.rotateLeft()


        println("Rotated: ")
        n.visit(::printNode)
    }

/*
      \              \
       5     -->     2
     /  \           /  \
    2    7         1    5
   /  \                /  \
  1    3              3    7
*/

    @Test
    fun rotateRight() {
        val n = Node(5)
        for (i in arrayOf(2, 7, 1, 3)) {
            n.insert(i)
        }
        println("Original: ")
        n.visit(::printNode)

        n.rotateRight()

        println("Rotated: ")
        n.visit(::printNode)
    }

    /*                  1
     *        5           2
     *    2        7  ->    3
     *  1   3    6  8         5
     *                         6
     *                           7
     *                            8
     */
    @Test
    fun backbone() {
        val n = Node(5)
        for (i in arrayOf(2, 7, 1, 3, 6, 8)) {
            n.insert(i)
        }
        println("Original: ")
        n.visit(::printNode)

        Assert.assertEquals(2, n.getHeight())

        n.createBackbone()

        Assert.assertEquals(6, n.getHeight())

        println("Backbone: ")
        n.visit(::printNode)

    }

    @Test
    fun perfectN() {
        val node = Node(0)
        Assert.assertEquals(1, node.getPerfectN(1))
        Assert.assertEquals(1, node.getPerfectN(2))
        Assert.assertEquals(3, node.getPerfectN(3))
        Assert.assertEquals(3, node.getPerfectN(6))
        Assert.assertEquals(7, node.getPerfectN(7))
        Assert.assertEquals(7, node.getPerfectN(8))
        Assert.assertEquals(7, node.getPerfectN(14))
        Assert.assertEquals(15, node.getPerfectN(15))
        Assert.assertEquals(15, node.getPerfectN(16))

        Assert.assertEquals(1, node.getPerfectN_2(1))
        Assert.assertEquals(1, node.getPerfectN_2(2))
        Assert.assertEquals(3, node.getPerfectN_2(3))
        Assert.assertEquals(3, node.getPerfectN_2(4))
        Assert.assertEquals(3, node.getPerfectN_2(5))
        Assert.assertEquals(3, node.getPerfectN_2(6))
        Assert.assertEquals(7, node.getPerfectN_2(7))
        Assert.assertEquals(7, node.getPerfectN_2(8))
        Assert.assertEquals(7, node.getPerfectN_2(9))
        Assert.assertEquals(7, node.getPerfectN_2(10))
        Assert.assertEquals(7, node.getPerfectN_2(11))
        Assert.assertEquals(7, node.getPerfectN_2(14))
        Assert.assertEquals(15, node.getPerfectN_2(15))
        Assert.assertEquals(15, node.getPerfectN_2(16))
    }

    private fun rotateEverySecondNodeN(count : Int) {
        val n = Node(1)
        for (i in 2..count) {
            n.insert(i)
        }
        Assert.assertEquals("BST $count",count - 1, n.getHeight())
        n.visit(::printNode)
        n.rotateEverySecondNode()
        n.visit(::printNode)
        Assert.assertTrue("BST $count", n.isBalanced())
        Assert.assertEquals("BST $count", n.getBalancedHeight(), n.getHeight())
    }

    @Test
    fun rotateEverySecondNode() {
        for (count in 3..16) {
            rotateEverySecondNodeN(count)
        }
    }

    @Test
    fun balance() {
        val n = Node(1)
        for (i in arrayOf(9, 3, 8, 6, 50, 7, 5, 2, 22)) {
            n.insert(i)
        }
        Assert.assertEquals(5, n.getHeight())
        n.doBalance()
        n.visit(::printNode)
        Assert.assertTrue(n.isBalanced())
        Assert.assertEquals(3, n.getHeight())
        Assert.assertEquals(3, n.getHeightForBalancedTree())
    }

}
