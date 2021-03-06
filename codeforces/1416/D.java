import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			DGraphAndQueries solver = new DGraphAndQueries();
			solver.solve(1, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class DGraphAndQueries {
		int[] arr;
		int[] parent;
		int[] tin;
		int[] tout;
		int n;
		int ind;
		int clock;
		BitSet visited;
		Pair<Integer, Integer>[] tour;
		ArrayList<Integer>[] graph;

		public DGraphAndQueries() {
		}

		public int find(int u) {
			return parent[u]==u ? u : (parent[u] = find(parent[u]));
		}

		public void merge(Pair<Integer, Integer> p) {
			int u = find(p.a), v = find(p.b);
			if(u==v) {
				return;
			}
			int next = ind++;
			parent[u] = parent[v] = next;
			graph[next] = new ArrayList<>(Arrays.asList(u, v));
		}

		public void dfs(int u) {
			tour[clock] = new Pair<>(arr[u], tin[u] = clock++);
			if(u>=n) {
				for(int v: graph[u]) {
					dfs(v);
				}
			}
			tout[u] = clock-1;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int m = in.nextInt(), q = in.nextInt();
			arr = new int[n+q];
			Arrays.fill(arr, -1);
			parent = new int[n+q];
			graph = new ArrayList[n+q];
			ind = n;
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			for(int i = 0; i<n+q; i++) {
				parent[i] = i;
			}
			Pair<Integer, Integer>[] edges = new Pair[m];
			for(int i = 0; i<m; i++) {
				edges[i] = new Pair<>(in.nextInt()-1, in.nextInt()-1);
			}
			BitSet marked = new BitSet(m);
			int[][] queries = new int[q][2];
			for(int i = 0; i<q; i++) {
				queries[i] = new int[] {in.nextInt(), in.nextInt()-1};
				if(queries[i][0]==2) {
					marked.set(queries[i][1]);
				}
			}
			for(int i = 0; i<m; i++) {
				if(!marked.get(i)) {
					merge(edges[i]);
				}
			}
			for(int i = q-1; i>=0; i--) {
				if(queries[i][0]==2) {
					merge(edges[queries[i][1]]);
				}else {
					queries[i][1] = find(queries[i][1]);
				}
			}
			Utilities.Debug.dbg(ind);
			Utilities.Debug.dbg(graph);
			tour = new Pair[ind];
			tin = new int[ind];
			tout = new int[ind];
			visited = new BitSet(ind);
			clock = 0;
			for(int i = 0; i<n; i++) {
				int u = find(i);
				if(!visited.get(u)) {
					dfs(u);
					visited.set(u);
				}
			}
			Utilities.Debug.dbg(tour);
			TPointSegmentTree<Pair<Integer, Integer>> st = new TPointSegmentTree<>(tour, (o1, o2) -> o1.a>=o2.a ? o1 : o2);
			for(int[] query: queries) {
				if(query[0]==1) {
					int u = query[1];
					var v = st.query(tin[u], tout[u]);
					Utilities.Debug.dbg(u, v);
					pw.println(v.a);
					st.set(v.b, new Pair<>(0, v.b));
				}
			}
		}

	}

	static class TPointSegmentTree<T> {
		public int n;
		public int ind;
		public int ql;
		public int qr;
		public T[] arr;
		public T[] value;
		public BiFunction<T, T, T> operation;

		public TPointSegmentTree(T[] arr, BiFunction<T, T, T> operation) {
			n = arr.length;
			this.arr = arr;
			value = (T[]) new Object[n<<2];
			this.operation = operation;
			build(1, 0, n-1);
		}

		private void build(int o, int l, int r) {
			if(l==r) {
				value[o] = arr[l];
				return;
			}
			int lc = o<<1, rc = lc|1, mid = l+r >> 1;
			build(lc, l, mid);
			build(rc, mid+1, r);
			value[o] = operation.apply(value[lc], value[rc]);
		}

		private void set(int o, int l, int r) {
			if(l==r) {
				value[o] = arr[l];
				return;
			}
			int lc = o<<1, rc = lc|1, mid = l+r >> 1;
			if(ind<=mid) {
				set(lc, l, mid);
			}else {
				set(rc, mid+1, r);
			}
			value[o] = operation.apply(value[lc], value[rc]);
		}

		public void set(int ind, T val) {
			this.ind = ind;
			arr[ind] = val;
			set(1, 0, n-1);
		}

		private T query(int o, int l, int r) {
			if(ql<=l&&qr>=r) {
				return value[o];
			}
			int lc = o<<1, rc = lc|1, mid = l+r >> 1;
			T ret = null;
			if(ql<=mid) {
				ret = query(lc, l, mid);
			}
			if(qr>mid) {
				if(ret==null) {
					ret = query(rc, mid+1, r);
				}else {
					ret = operation.apply(ret, query(rc, mid+1, r));
				}
			}
			return ret;
		}

		public T query(int l, int r) {
			ql = l;
			qr = r;
			return query(1, 0, n-1);
		}

	}

	static class Utilities {
		public static class Debug {
			public static final boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

			private static <T> String ts(T t) {
				if(t==null) {
					return "null";
				}
				try {
					return ts((Iterable) t);
				}catch(ClassCastException e) {
					if(t instanceof int[]) {
						String s = Arrays.toString((int[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof long[]) {
						String s = Arrays.toString((long[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof char[]) {
						String s = Arrays.toString((char[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof double[]) {
						String s = Arrays.toString((double[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof boolean[]) {
						String s = Arrays.toString((boolean[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}
					try {
						return ts((Object[]) t);
					}catch(ClassCastException e1) {
						return t.toString();
					}
				}
			}

			private static <T> String ts(T[] arr) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: arr) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			private static <T> String ts(Iterable<T> iter) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: iter) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			public static void dbg(Object... o) {
				if(LOCAL) {
					System.err.print("Line #"+Thread.currentThread().getStackTrace()[2].getLineNumber()+": [");
					for(int i = 0; i<o.length; i++) {
						if(i!=0) {
							System.err.print(", ");
						}
						System.err.print(ts(o[i]));
					}
					System.err.println("]");
				}
			}

		}

	}

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public String lineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			lineSeparator = System.lineSeparator();
		}

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(String s) {
			sb.append(s);
			println();
		}

		public void println() {
			sb.append(lineSeparator);
		}

		private void flushToBuffer() {
			try {
				os.write(sb.toString().getBytes());
			}catch(IOException e) {
				e.printStackTrace();
			}
			sb = new StringBuilder(BUFFER_SIZE);
		}

		public void flush() {
			try {
				flushToBuffer();
				os.flush();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public void close() {
			flush();
			try {
				os.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

	}

	static interface InputReader {
		int nextInt();

	}

	static class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
		public T1 a;
		public T2 b;

		public Pair(Pair<T1, T2> p) {
			this(p.a, p.b);
		}

		public Pair(T1 a, T2 b) {
			this.a = a;
			this.b = b;
		}

		public String toString() {
			return a+" "+b;
		}

		public int hashCode() {
			return Objects.hash(a, b);
		}

		public boolean equals(Object o) {
			if(o instanceof Pair) {
				Pair p = (Pair) o;
				return a.equals(p.a)&&b.equals(p.b);
			}
			return false;
		}

		public int compareTo(Pair<T1, T2> p) {
			int cmp = ((Comparable<T1>) a).compareTo(p.a);
			if(cmp==0) {
				return ((Comparable<T2>) b).compareTo(p.b);
			}
			return cmp;
		}

	}

	static class FastReader implements InputReader {
		final private int BUFFER_SIZE = 1<<16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer;
		private int bytesRead;

		public FastReader(InputStream is) {
			din = new DataInputStream(is);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public int nextInt() {
			int ret = 0;
			byte c = skipToDigit();
			boolean neg = (c=='-');
			if(neg) {
				c = read();
			}
			do {
				ret = ret*10+c-'0';
			} while((c = read())>='0'&&c<='9');
			if(neg) {
				return -ret;
			}
			return ret;
		}

		private boolean isDigit(byte b) {
			return b>='0'&&b<='9';
		}

		private byte skipToDigit() {
			byte ret;
			while(!isDigit(ret = read())&&ret!='-') ;
			return ret;
		}

		private void fillBuffer() {
			try {
				bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			}catch(IOException e) {
				e.printStackTrace();
				throw new InputMismatchException();
			}
			if(bytesRead==-1) {
				buffer[0] = -1;
			}
		}

		private byte read() {
			if(bytesRead==-1) {
				throw new InputMismatchException();
			}else if(bufferPointer==bytesRead) {
				fillBuffer();
			}
			return buffer[bufferPointer++];
		}

	}
}

