import java.io.*
import java.util.*
import java.util.function.BiFunction
import kotlin.collections.ArrayList

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
        val solver = DGraphAndQueries()
        solver.solve(1, `in`, out)
        out.close()
        System.err.println((System.currentTimeMillis() - startTime).toString() + "ms")
    }
}

internal class DGraphAndQueries {
    lateinit var arr: IntArray
    lateinit var parent: IntArray
    lateinit var tin: IntArray
    lateinit var tout: IntArray
    var n = 0
    var ind = 0
    var clock = 0
    lateinit var visited: BitSet
    lateinit var tour: Array<Pair<Int, Int>>
    lateinit var graph: Array<ArrayList<Int>>
    fun find(u: Int): Int {
        return if (parent[u] == u) u else find(parent[u]).also { parent[u] = it }
    }

    fun merge(p: Pair<Int, Int>) {
        val u = find(p.a)
        val v = find(p.b)
        if (u == v) {
            return
        }
        val next = ind++
        parent[v] = next
        parent[u] = parent[v]
        graph[next] = ArrayList(Arrays.asList(u, v))
    }

    fun dfs(u: Int) {
        tour[clock] = Pair(arr[u], clock++.also { tin[u] = it })
        if (u >= n) {
            for (v in graph[u]) {
                dfs(v)
            }
        }
        tout[u] = clock - 1
    }

    fun solve(kase: Int, `in`: InputReader, pw: Output) {
        n = `in`.nextInt()
        val m = `in`.nextInt()
        val q = `in`.nextInt()
        arr = IntArray(n + q)
        Arrays.fill(arr, -1)
        parent = IntArray(n + q)
        graph = Array(n + q) { ArrayList<Int>() }
        ind = n
        for (i in 0 until n) {
            arr[i] = `in`.nextInt()
        }
        for (i in 0 until n + q) {
            parent[i] = i
        }
        val edges: Array<Pair<Int, Int>> = Array(m) { Pair(0, 0) }
        for (i in 0 until m) {
            edges[i] = Pair(`in`.nextInt() - 1, `in`.nextInt() - 1)
        }
        val marked = BitSet(m)
        val queries = Array(q) { IntArray(2) }
        for (i in 0 until q) {
            queries[i] = intArrayOf(`in`.nextInt(), `in`.nextInt() - 1)
            if (queries[i][0] == 2) {
                marked.set(queries[i][1])
            }
        }
        for (i in 0 until m) {
            if (!marked[i]) {
                merge(edges[i])
            }
        }
        for (i in q - 1 downTo 0) {
            if (queries[i][0] == 2) {
                merge(edges[queries[i][1]])
            } else {
                queries[i][1] = find(queries[i][1])
            }
        }
//            Utilities.Debug.dbg(ind)
//            Utilities.Debug.dbg(*graph)
        tour = Array(ind) { Pair(0, 0) }
        tin = IntArray(ind)
        tout = IntArray(ind)
        visited = BitSet(ind)
        clock = 0
        for (i in 0 until n) {
            val u = find(i)
            if (!visited[u]) {
                dfs(u)
                visited.set(u)
            }
        }
//            Utilities.Debug.dbg(*tour)
        val st = TPointSegmentTree(tour, BiFunction { t, u -> if (t.a >= u.a) t else u })
        for (query in queries) {
            if (query[0] == 1) {
                val u = query[1]
                val v = st.query(tin[u], tout[u])!!
                Utilities.Debug.dbg(u, v)
                pw.println(v.a)
                st[v.b] = Pair(0, v.b)
            }
        }
    }
}

internal class TPointSegmentTree<T>(arr: Array<T>, operation: BiFunction<T, T, T>) {
    var n: Int
    var ind = 0
    var ql = 0
    var qr = 0
    var arr: Array<T>
    var value: Array<T?>
    var operation: BiFunction<T, T, T>
    private fun build(o: Int, l: Int, r: Int) {
        if (l == r) {
            value[o] = arr[l]
            return
        }
        val lc = o shl 1
        val rc = lc or 1
        val mid = l + r shr 1
        build(lc, l, mid)
        build(rc, mid + 1, r)
        value[o] = value[lc]?.let { value[rc]?.let { it1 -> operation.apply(it, it1) } }
    }

    private operator fun set(o: Int, l: Int, r: Int) {
        if (l == r) {
            value[o] = arr[l]
            return
        }
        val lc = o shl 1
        val rc = lc or 1
        val mid = l + r shr 1
        if (ind <= mid) {
            set(lc, l, mid)
        } else {
            set(rc, mid + 1, r)
        }
        value[o] = value[lc]?.let { value[rc]?.let { it1 -> operation.apply(it, it1) } }
    }

    operator fun set(ind: Int, `val`: T) {
        this.ind = ind
        arr[ind] = `val`
        set(1, 0, n - 1)
    }

    private fun query(o: Int, l: Int, r: Int): T? {
        if (ql <= l && qr >= r) {
            return value[o]
        }
        val lc = o shl 1
        val rc = lc or 1
        val mid = l + r shr 1
        var ret: T? = null
        if (ql <= mid) {
            ret = query(lc, l, mid)
        }
        if (qr > mid) {
            ret = if (ret == null) {
                query(rc, mid + 1, r)
            } else {
                query(rc, mid + 1, r)?.let { operation.apply(ret!!, it) }
            }
        }
        return ret
    }

    fun query(l: Int, r: Int): T? {
        ql = l
        qr = r
        return query(1, 0, n - 1)
    }

    init {
        n = arr.size
        this.arr = arr
        value = arrayOfNulls<Any>(n shl 2) as Array<T?>
        this.operation = operation
        build(1, 0, n - 1)
    }
}

internal class Utilities {
    object Debug {
        val LOCAL = System.getProperty("ONLINE_JUDGE") == null
        private fun <T> ts(t: T?): String {
            return if (t == null) {
                "null"
            } else try {
                ts<Any>(t as Iterable<*>)
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

internal class Output @JvmOverloads constructor(os: OutputStream?, var BUFFER_SIZE: Int = 1 shl 16) : Closeable, Flushable {
    var sb: StringBuilder
    var os: OutputStream
    var lineSeparator: String
    fun println(i: Int) {
        println(i.toString())
    }

    fun println(s: String?) {
        sb.append(s)
        println()
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

internal interface InputReader {
    fun nextInt(): Int
}

internal class Pair<T1, T2>(var a: T1, var b: T2) : Comparable<Pair<T1, T2>> {
    constructor(p: Pair<T1, T2>) : this(p.a, p.b) {}

    override fun toString(): String {
        return a.toString() + " " + b
    }

    override fun hashCode(): Int {
        return Objects.hash(a, b)
    }

    override fun equals(o: Any?): Boolean {
        if (o is Pair<*, *>) {
            val p = o
            return a == p.a && b == p.b
        }
        return false
    }

    override fun compareTo(p: Pair<T1, T2>): Int {
        val cmp = (a as Comparable<T1>).compareTo(p.a)
        return if (cmp == 0) {
            (b as Comparable<T2>).compareTo(p.b)
        } else cmp
    }
}

internal class FastReader(`is`: InputStream?) : InputReader {
    private val BUFFER_SIZE = 1 shl 16
    private val din: DataInputStream
    private val buffer: ByteArray
    private var bufferPointer: Int
    private var bytesRead: Int
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
