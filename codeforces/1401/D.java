import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.util.Random;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Objects;
import java.io.Closeable;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			DMaximumDistributedTree solver = new DMaximumDistributedTree();
			int testCount = Integer.parseInt(in.next());
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DMaximumDistributedTree {
		private final int mod = (int) (1e9+7);
		static DMaximumDistributedTree.Node[] nodes;
		static ArrayList<Integer>[] graph;
		static PriorityQueue<DMaximumDistributedTree.Edge> edges;
		static ArrayList<Pair<DMaximumDistributedTree.Node, DMaximumDistributedTree.Node>> toadd;
		static long tot;

		public DMaximumDistributedTree() {
		}

		public void solve(int kase, FastReader in, Output pw) {
			int n = in.nextInt();
			graph = new ArrayList[n];
			nodes = new DMaximumDistributedTree.Node[n];
			edges = new PriorityQueue<>();
			toadd = new ArrayList<>();
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
				nodes[i] = new DMaximumDistributedTree.Node(i);
			}
			for(int i = 0; i<n-1; i++) {
				int u = in.nextInt()-1, v = in.nextInt()-1;
				graph[u].add(v);
				graph[v].add(u);
			}
			nodes[0].dfs();
			int m = in.nextInt();
			long[] factors = new long[m];
			for(int i = 0; i<m; i++) {
				factors[i] = in.nextInt();
			}
			Utilities.rsort(factors);
			long[] weights = new long[n-1];
			if(n-1>=m) {
				System.arraycopy(factors, 0, weights, 0, m);
				for(int i = m; i<n-1; i++) {
					weights[i] = 1;
				}
			}else {
				weights[0] = 1;
				for(int i = 0; i<=m-n+1; i++) {
					weights[0] *= factors[i];
					weights[0] %= mod;
				}
				System.arraycopy(factors, m-n+2, weights, 1, n-2);
			}
			tot = nodes[0].count;
			for(var v: toadd) {
				edges.add(new DMaximumDistributedTree.Edge(v.a, v.b));
			}
			Utilities.Debug.dbg(edges);
			long ans = 0;
			for(int i = 0; i<n-1; i++) {
				DMaximumDistributedTree.Edge e = edges.remove();
				ans = (ans+e.value*weights[i])%mod;
			}
			pw.println(ans);
		}

		static class Node {
			DMaximumDistributedTree.Node parent;
			ArrayList<DMaximumDistributedTree.Node> children;
			long count;
			int ind;
			boolean visited;

			public Node(int ind) {
				this.ind = ind;
				parent = null;
				children = new ArrayList<>();
				count = 1;
				visited = false;
			}

			public void dfs() {
				visited = true;
				for(int i: graph[ind]) {
					DMaximumDistributedTree.Node n = nodes[i];
					if(!n.visited) {
						children.add(n);
						n.parent = this;
						n.dfs();
						count += n.count;
						toadd.add(new Pair<>(n, this));
					}
				}
			}

			public String toString() {
				return "Node{"+
						"count="+count+
						", ind="+ind+
						'}';
			}

		}

		static class Edge implements Comparable<DMaximumDistributedTree.Edge> {
			DMaximumDistributedTree.Node child;
			DMaximumDistributedTree.Node parent;
			long value;

			public Edge(DMaximumDistributedTree.Node child, DMaximumDistributedTree.Node parent) {
				this.child = child;
				this.parent = parent;
				value = (tot-child.count)*child.count;
			}

			public int compareTo(DMaximumDistributedTree.Edge e) {
				return Long.compare(e.value, value);
			}

			public String toString() {
				return "Edge{"+
						"child="+child+
						", parent="+parent+
						", value="+value+
						'}';
			}

		}

	}

	static class FastReader {
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
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

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public boolean autoFlush;
		public String LineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			autoFlush = false;
			LineSeparator = System.lineSeparator();
		}

		public void println(long l) {
			println(String.valueOf(l));
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println() {
			sb.append(LineSeparator);
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

	static class Utilities {
		public static void rsort(long[] arr) {
			Random rand = new Random();
			int n = arr.length;
			for(int i = 0; i<n; i++) {
				swap(arr, i, rand.nextInt(n));
			}
			Arrays.sort(arr);
			reverse(arr, 0, n-1);
		}

		public static void swap(long[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

		public static void reverse(long[] arr, int i, int j) {
			while(i<j) {
				swap(arr, i++, j--);
			}
		}

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

	static class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
		public T1 a;
		public T2 b;

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
}


