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

        rotateLeft(n, n.right!!)

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

        rotateRight(n, n.left!!)

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
    fun balance() {
        val n = Node(1)
        for (i in arrayOf(2, 3, 5, 6, 7, 8)) {
            n.insert(i)
        }
        Assert.assertEquals(6, n.getHeight())
        n.doBalance()
       Assert.assertTrue(n.isBalanced())
       Assert.assertEquals(2, n.getHeight())

    }

}