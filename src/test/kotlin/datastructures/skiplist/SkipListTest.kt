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

        list.add(7, 500, 4, 1024, 64, 128)
        println(list)
        Assert.assertEquals(1024, list.find(1024)?.value)
    }

}