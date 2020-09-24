package ru.emkn.kotlin

import kotlin.time.measureTime

class FibIterator(private val n: Int) : Iterator<Int> {
    private var f1 = 0
    private var f2 = 1
    private var f3 = 1

    private var index = 0


    override fun hasNext(): Boolean {
        return index < n
    }

    override fun next(): Int {
        val ans = f1
        f3 = f1 + f2
        f1 = f2
        f2 = f3

        index++

        return ans
    }

}

class FibIterable(private val n: Int) : Iterable<Int> {
    override fun iterator(): Iterator<Int> = FibIterator(n)
}

class FibCollection(private val n: Int, override val size: Int) : Collection<Int> {
    override fun contains(element: Int): Boolean {
        val iter =  FibIterator(n).asSequence().toList()
        for(i in iter) {
            if(element == iter[i])
                return true
        }
        return false
    }

    override fun containsAll(elements: Collection<Int>): Boolean {
        val iter =  FibIterator(n).asSequence().toList()
        var counterContains = 0
        for (element in elements) {
            for(i in iter) {
                if (element == iter[i])
                    counterContains++
            }
        }
        return counterContains == elements.size
    }

    override fun isEmpty(): Boolean {
        return n == 0
    }

    override fun iterator(): Iterator<Int> = FibIterator(n)
}

//class FibList(private val n: Int, override val size: Int) : List<Int> {
//
//}

fun main(args: Array<String>) {
    val f = FibIterable(2)
    for (i in f) println(i)
    //println("3rd number of Fibonacci is: ${FibIterable(3)}")
}