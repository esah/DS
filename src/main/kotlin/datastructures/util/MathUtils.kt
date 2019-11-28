package datastructures.util

//MSB
fun log2(n: Int): Int {
    var n = n
    var index = 0
    while (n > 1) {
        n = n shr 1  //n >> 1 = / 2
        index++
    }
    return index
}

//2^x
fun twoPow(x: Int): Int {
    return 1 shl x // 1 << x = 2^x
}