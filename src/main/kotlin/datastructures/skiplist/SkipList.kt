package datastructures.skiplist

import java.util.*

const val MAX_HEIGHT = 10

/**
 * @see java.util.concurrent.ConcurrentSkipListSet
 */
class Node (var value: Int?, var forward: Array<Node?>) {
    constructor(value: Int?) : this(value, Array(MAX_HEIGHT, { null }))

    override fun toString(): String {
        return if (value == null) "x" else value.toString()
    }

}

class SkipList(var head: Node) {
    constructor() : this(Node(null))

    fun scan(value: Int, links : Array<Node?>? = null): Node {
        var current : Node = head
        for (level in MAX_HEIGHT - 1 downTo 0) {
            while (current.forward[level] != null
                && (current.forward[level]!!.value == null
                || current.forward[level]!!.value!! <= value)) {
                current = current.forward[level]!!
            }
            links?.let {
                it[level] = current
            }
        }
        return current
    }

    fun find(value: Int): Node? {
        val prev = scan(value)
        return if (prev.value == value) prev else null
    }


    fun add(vararg values: Int) {
        values.forEach { add(it) }
    }

    fun add(value: Int): Node {
        val newNode = Node(value)
        val prevLinks = Array<Node?>(MAX_HEIGHT, { null})
        scan(value, prevLinks)
        val newHeight = randomLevel()

        for (i in 0..newHeight) {
            newNode.forward[i] = prevLinks[i]?.forward!![i]
            prevLinks[i]?.forward!![i] = newNode
        }
        return newNode
    }

    private fun randomLevel() = Random(System.nanoTime()).nextInt(MAX_HEIGHT)

    override fun toString() : String {
        val result = StringBuilder()
        for (level in MAX_HEIGHT - 1 downTo 0) {
            result.append("$level: ")
            var node: Node? = head
            result.append("$node -> ")
            while (node != null) {
                val next = node.forward[level]
                result.append(if (next != null) "$next -> " else "x")
                node = next
            }
            result.append("\n")
        }
        return result.toString()
    }

}