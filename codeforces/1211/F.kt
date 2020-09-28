import java.io.*
import java.util.*

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
fun main(args: Array<String>) {
    val thread = Thread(null, TaskAdapter(), "", 1 shl 28)
    thread.start()
    thread.join()
}

internal class TaskAdapter : Runnable {
    override fun run() {
        val startTime = System.currentTimeMillis()
        val inputStream = System.`in`
        val outputStream: OutputStream = System.out
        val `in` = FastReader(inputStream)
        val out = Output(outputStream)
        val solver = FKotlinkotlinkotlinkotlin()
        solver.solve(1, `in`, out)
        out.close()
        System.err.println((System.currentTimeMillis() - startTime).toString() + "ms")
    }
}

internal class FKotlinkotlinkotlinkotlin {
    lateinit var graph: Array<ArrayDeque<Int>>
    var clock = 0
    var path: ArrayDeque<Int>? = null
    fun dfs(u: Int) {
        while (!graph[u].isEmpty()) {
            dfs(graph[u].pollLast())
        }
        path!!.addFirst(u)
    }

    fun solve(kase: Int, `in`: InputReader, pw: Output) {
        val n = `in`.nextInt()
        val kotlin = "kotlin"
        graph = Array(6) { ArrayDeque() }
        val edges: Array<Array<ArrayDeque<Int>>> = Array(6) { Array(6) { ArrayDeque() } }
        for (i in 0 until n) {
            val s = `in`.next()
            val u = kotlin.indexOf(s[0])
            val v = (kotlin.indexOf(s[s.length - 1]) + 1) % 6
            graph[u].addLast(v)
            edges[u][v].addFirst(i)
        }
        path = ArrayDeque(n)
        clock = n
        dfs(0)
        Utilities.Debug.dbg(path)
        var prev = path!!.pollFirst()
        for (i in 1..n) {
            val next = path!!.pollFirst()
            pw.print((edges[prev][next].pollFirst() + 1).toString() + " ")
            prev = next
        }
        pw.println()
    }
}

internal interface InputReader {
    operator fun next(): String
    fun nextInt(): Int
}

internal class Output @JvmOverloads constructor(os: OutputStream?, var BUFFER_SIZE: Int = 1 shl 16) : Closeable, Flushable {
    var sb: StringBuilder
    var os: OutputStream
    var lineSeparator: String
    fun print(s: String?) {
        sb.append(s)
        if (sb.length > BUFFER_SIZE shr 1) {
            flushToBuffer()
        }
    }

    fun println() {
        sb.append(lineSeparator)
    }

    private fun flushToBuffer() {
        try {
            os.write(sb.toString().toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        sb = StringBuilder(BUFFER_SIZE)
    }

    override fun flush() {
        try {
            flushToBuffer()
            os.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun close() {
        flush()
        try {
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        sb = StringBuilder(BUFFER_SIZE)
        this.os = BufferedOutputStream(os, 1 shl 17)
        lineSeparator = System.lineSeparator()
    }
}

internal class FastReader(`is`: InputStream?) : InputReader {
    private val BUFFER_SIZE = 1 shl 16
    private val din: DataInputStream
    private val buffer: ByteArray
    private var bufferPointer: Int
    private var bytesRead: Int
    override fun next(): String {
        val ret = StringBuilder(64)
        var c = skip()
        while (c.toInt() != -1 && !isSpaceChar(c)) {
            ret.appendCodePoint(c.toInt())
            c = read()
        }
        return ret.toString()
    }

    override fun nextInt(): Int {
        var ret = 0
        var c = skipToDigit()
        val neg = c == '-'.toByte()
        if (neg) {
            c = read()
        }
        do {
            ret = ret * 10 + c - '0'.toInt()
        } while (read().also { c = it } >= '0'.toByte() && c <= '9'.toByte())
        return if (neg) {
            -ret
        } else ret
    }

    private fun isSpaceChar(b: Byte): Boolean {
        return b == ' '.toByte() || b == '\r'.toByte() || b == '\n'.toByte() || b == '\t'.toByte()
    }

    private fun skip(): Byte {
        var ret: Byte
        while (isSpaceChar(read().also { ret = it }));
        return ret
    }

    private fun isDigit(b: Byte): Boolean {
        return b >= '0'.toByte() && b <= '9'.toByte()
    }

    private fun skipToDigit(): Byte {
        var ret: Byte
        while (!isDigit(read().also { ret = it }) && ret != '-'.toByte());
        return ret
    }

    private fun fillBuffer() {
        try {
            bytesRead = din.read(buffer, 0.also { bufferPointer = it }, BUFFER_SIZE)
        } catch (e: IOException) {
            e.printStackTrace()
            throw InputMismatchException()
        }
        if (bytesRead == -1) {
            buffer[0] = -1
        }
    }

    private fun read(): Byte {
        if (bytesRead == -1) {
            throw InputMismatchException()
        } else if (bufferPointer == bytesRead) {
            fillBuffer()
        }
        return buffer[bufferPointer++]
    }

    init {
        din = DataInputStream(`is`)
        buffer = ByteArray(BUFFER_SIZE)
        bytesRead = 0
        bufferPointer = bytesRead
    }
}

internal class Utilities {
    object Debug {
        val LOCAL = System.getProperty("ONLINE_JUDGE") == null
        private fun <T> ts(t: T?): String {
            return if (t == null) {
                "null"
            } else try {
                ts(t as Iterable<*>)
            } catch (e: ClassCastException) {
                if (t is IntArray) {
                    val s = Arrays.toString(t as IntArray?)
                    return "{" + s.substring(1, s.length - 1) + "}"
                } else if (t is LongArray) {
                    val s = Arrays.toString(t as LongArray?)
                    return "{" + s.substring(1, s.length - 1) + "}"
                } else if (t is CharArray) {
                    val s = Arrays.toString(t as CharArray?)
                    return "{" + s.substring(1, s.length - 1) + "}"
                } else if (t is DoubleArray) {
                    val s = Arrays.toString(t as DoubleArray?)
                    return "{" + s.substring(1, s.length - 1) + "}"
                } else if (t is BooleanArray) {
                    val s = Arrays.toString(t as BooleanArray?)
                    return "{" + s.substring(1, s.length - 1) + "}"
                }
                try {
                    ts(t as Array<Any>)
                } catch (e1: ClassCastException) {
                    t.toString()
                }
            }
        }

        private fun <T> ts(arr: Array<T>): String {
            val ret = StringBuilder()
            ret.append("{")
            var first = true
            for (t in arr) {
                if (!first) {
                    ret.append(", ")
                }
                first = false
                ret.append(ts(t))
            }
            ret.append("}")
            return ret.toString()
        }

        private fun <T> ts(iter: Iterable<T>): String {
            val ret = StringBuilder()
            ret.append("{")
            var first = true
            for (t in iter) {
                if (!first) {
                    ret.append(", ")
                }
                first = false
                ret.append(ts(t))
            }
            ret.append("}")
            return ret.toString()
        }

        fun dbg(vararg o: Any?) {
            if (LOCAL) {
                System.err.print("Line #" + Thread.currentThread().stackTrace[2].lineNumber + ": [")
                for (i in 0 until o.size) {
                    if (i != 0) {
                        System.err.print(", ")
                    }
                    System.err.print(ts<Any?>(o[i]))
                }
                System.err.println("]")
            }
        }
    }
}
