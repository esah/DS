package datastructures.skiplist

import org.junit.Assert
import org.junit.Test

class SkipListTest {

    @Test
    fun test() {

        val list = SkipList()
        list.add(100, 5, 3, 400, 25, 1, 55, 78, 2)
        println(list)

        Assert.assertEquals(25, list.find(25)?.value)
        Assert.assertNull(list.find(50))
    }

}