package ru.emkn.kotlin

import java.io.File
import java.util.ArrayDeque

interface Visitor {
    fun visit(node: Node): Int
}

sealed class Node {
    fun accept(visitor: Visitor) = visitor.visit(this)

    class Num(val value: Int) : Node()
    class Sum(val left: Node, val right: Node) : Node()
    class Mul(val left: Node, val right: Node) : Node()
}

class PrintVisitor : Visitor {
    val sb = StringBuilder()

    override fun visit(node: Node): Int {
        when (node) {
            is Node.Num -> sb.append(node.value)
            is Node.Sum -> stringify("+", node.left, node.right)
            is Node.Mul -> stringify("*", node.left, node.right)
        }
        return 0
    }

    private fun stringify(name : String, left: Node, right: Node) {
        sb.append('(')
        left.accept(this); sb.append(name); right.accept(this)
        sb.append(')')
    }
}

class CalculateVisitor : Visitor {
    var result: Int = 0
    override fun visit(node: Node): Int {
        result = when (node) {
            is Node.Mul -> node.left.accept(this) * node.right.accept(this)
            is Node.Sum -> node.left.accept(this) + node.right.accept(this)
            is Node.Num -> node.value
        }
        return result
    }
}

fun main(args: Array<String>) {
    val (printer, calculator) = pair(args)
    println("${printer.sb} = ${calculator.result}")
}

private fun pair(args: Array<String>): Pair<PrintVisitor, CalculateVisitor> {
    val tree = initializeNodes(args[1])
    val printer = PrintVisitor()
    val calculator = CalculateVisitor()
    tree.accept(printer)
    tree.accept(calculator)
    return Pair(printer, calculator)
}

fun initializeNodes(s: String): Node {
    val numLine = File(s).readLines()[0]
    val stack = ArrayDeque<Node>()
    numLine.split(" ").forEach {
        val node = when (it) {
            "*" -> Node.Mul(stack.pop(), stack.pop())
            "+" -> Node.Sum(stack.pop(), stack.pop())
            else -> Node.Num(it.toInt())
        }
        stack.push(node)
    }
    return stack.peekFirst()
}
