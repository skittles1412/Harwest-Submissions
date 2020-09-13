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
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Objects;

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
			EMinimumSpanningTreeForEachEdge solver = new EMinimumSpanningTreeForEachEdge();
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

	static class EMinimumSpanningTreeForEachEdge {
		int n;
		int time;
		ArrayList<Pair<Integer, Long>>[] graph;
		int[] rank;
		int[] tin;
		int[] tout;
		int[][] up;
		long[][] max;

		public EMinimumSpanningTreeForEachEdge() {
		}

		public void dfs(int u, int r) {
			Utilities.Debug.dbg(u);
			tin[u] = time++;
			rank[u] = r;
			for(var v: graph[u]) {
				graph[v.a].remove(new Pair<>(u, v.b));
				up[v.a][0] = u;
				max[v.a][0] = v.b;
				dfs(v.a, r+1);
			}
			tout[u] = time-1;
		}

		public boolean anc(int u, int v) {
			return tin[u]<=tin[v]&&tout[u]>=tout[v];
		}

		public int lca(int u, int v) {
			if(anc(u, v)) {
				return u;
			}
			for(int i = 18; i>=0; i--) {
				int x = up[u][i];
				if(x!=-1&&!anc(x, v)) {
					u = x;
				}
			}
			return up[u][0];
		}

		public int lift(int u, int dist) {
			int ret = 0;
			for(int i = 0; i<18; i++) {
				if((dist&1<<i)>0) {
					ret = Math.max(ret, (int) max[u][i]);
					u = up[u][i];
				}
			}
			return ret;
		}

		public int dist(int u, int v) {
			int lca = lca(u, v);
			return Math.max(lift(u, rank[u]-rank[lca]), lift(v, rank[v]-rank[lca]));
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int m = in.nextInt();
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			Pair<Pair<Pair<Integer, Integer>, Long>, Integer>[] edges = new Pair[m];
			for(int i = 0; i<m; i++) {
				edges[i] = new Pair<>(new Pair<>(new Pair<>(in.nextInt()-1, in.nextInt()-1), in.nextLong()), i);
			}
			Arrays.sort(edges, Comparator.comparingLong(o -> o.a.b));
			long sum = 0;
			DSU dsu = new DSU(n);
			for(var e: edges) {
				var v = e.a;
				if(!dsu.connected(v.a.a, v.a.b)) {
					sum += v.b;
					graph[v.a.a].add(new Pair<>(v.a.b, v.b));
					graph[v.a.b].add(new Pair<>(v.a.a, v.b));
					dsu.union(v.a.a, v.a.b);
				}
			}
			Utilities.Debug.dbg(graph);
			Arrays.sort(edges, Comparator.comparingLong(o -> o.b));
			up = new int[n][19];
			max = new long[n][19];
			rank = new int[n];
			time = 0;
			tin = new int[n];
			tout = new int[n];
			dfs(0, 0);
			for(int i = 1; i<=18; i++) {
				for(int j = 0; j<n; j++) {
					if(up[j][i-1]!=-1) {
						up[j][i] = up[up[j][i-1]][i-1];
						max[j][i] = Math.max(max[j][i-1], max[up[j][i-1]][i-1]);
					}
				}
			}
			for(var e: edges) {
				var v = e.a;
				pw.println(sum-dist(v.a.a, v.a.b)+v.b);
			}
		}

	}

	static class DSU {
		public int[] rank;
		public int[] parent;
		public int[] size;
		public int n;

		public DSU(int x) {
			n = x;
			rank = new int[n];
			parent = new int[n];
			size = new int[n];
			for(int i = 0; i<n; i++) {
				parent[i] = i;
				size[i] = 1;
			}
		}

		public int find(int i) {
			if(parent[i]==i) {
				return i;
			}else {
				int result = find(parent[i]);
				parent[i] = result;
				return result;
			}
		}

		public void union(int i, int j) {
			int irep = find(i);
			int jrep = find(j);
			if(irep==jrep) {
				return;
			}
			int irank = rank[irep];
			int jrank = rank[jrep];
			if(irank<jrank) {
				parent[irep] = jrep;
				size[jrep] += size[irep];
			}else if(jrank<irank) {
				parent[jrep] = irep;
				size[irep] += size[jrep];
			}else {
				parent[irep] = jrep;
				rank[jrep]++;
				size[jrep] += size[irep];
			}
		}

		public boolean connected(int i, int j) {
			return find(i)==find(j);
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

		public void println(long l) {
			println(String.valueOf(l));
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
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

		public long nextLong() {
			long ret = 0;
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

	static interface InputReader {
		int nextInt();

		long nextLong();

	}
}

