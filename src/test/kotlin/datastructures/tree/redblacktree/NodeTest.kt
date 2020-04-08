package datastructures.tree.redblacktree

import datastructures.util.printNode
import org.junit.Assert
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

        Assert.assertTrue(root.parent == null)
        Assert.assertTrue(root.key == 5)
        Assert.assertEquals(Color.BLACK, root.color)

        val one = root.find(1)!!
        Assert.assertEquals(Color.RED, one.color)
        Assert.assertTrue(one.parent === root)

        val ten = root.find(10)!!
        Assert.assertEquals(Color.RED, ten.color)
        Assert.assertTrue(ten.parent === root)

    }

    @Test
    fun newTree() {
        val root = newTree(10, 1, 5, 7, 21, 50, 11, 51, 9, 4, 3)
        root.visit(::printNode) //todo FIX color
    }

    @Test
    fun rotateWithParentLinkKept() {
        val n = newTree(10, 5, 15)

        Assert.assertEquals(n, Node(10, Color.BLACK))
        Assert.assertEquals(n.left, Node(5, Color.RED, n))
        Assert.assertEquals(n.right, Node(15, Color.RED, n))
        n.rotateRight()
        Assert.assertEquals(n, Node(5, Color.BLACK)) //TODO color changed!
        Assert.assertEquals(n.left, null)
        Assert.assertEquals(n.right, Node(10, Color.RED, n))
        Assert.assertEquals(n.right?.right, Node(15, Color.RED, n.right))
        n.rotateLeft()
        Assert.assertEquals(n, Node(10, Color.BLACK))
        Assert.assertEquals(n.left, Node(5, Color.RED, n))
        Assert.assertEquals(n.right, Node(15, Color.RED, n))
        n.rotateLeft()
        Assert.assertEquals(n, Node(15, Color.BLACK))
        Assert.assertEquals(n.left, Node(10, Color.RED, n))
        Assert.assertEquals(n.left?.left, Node(5, Color.RED, n.left))

    }
}
