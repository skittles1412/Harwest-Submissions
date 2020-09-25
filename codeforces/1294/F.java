import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
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
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			FThreePathsOnATree solver = new FThreePathsOnATree();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class FThreePathsOnATree {
		int n;
		ArrayList<Integer>[] graph;
		int[] parent;
		int[] rank;

		public FThreePathsOnATree() {
		}

		public int[] bfs(Iterable<Integer> x) {
			int[] dist = new int[n];
			Arrays.fill(dist, -1);
			ArrayDeque<Pair<Integer, Integer>> q = new ArrayDeque<>(n);
			for(int u: x) {
				q.addLast(new Pair<>(u, dist[u] = 0));
			}
			while(!q.isEmpty()) {
				Pair<Integer, Integer> cur = q.removeFirst();
				for(int v: graph[cur.a]) {
					if(dist[v]==-1) {
						q.addLast(new Pair<>(v, dist[v] = cur.b+1));
					}
				}
			}
			return dist;
		}

		public Pair<Integer, Pair<Integer, Integer>> diameter() {
			int[] dist = bfs(Collections.singletonList(0));
			int max = 0, v = 0;
			for(int i = 0; i<n; i++) {
				if(dist[i]>max) {
					max = dist[i];
					v = i;
				}
			}
			dist = bfs(Collections.singletonList(v));
			int u = 0;
			for(int i = 0; i<n; i++) {
				if(dist[i]>max) {
					max = dist[i];
					u = i;
				}
			}
			return new Pair<>(max, new Pair<>(u, v));
		}

		public void dfs(int u, int p, int r) {
			rank[u] = r++;
			parent[u] = p;
			for(int v: graph[u]) {
				if(v!=p) {
					dfs(v, u, r);
				}
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			graph = in.nextUndirectedGraph(n, n-1);
			parent = new int[n];
			rank = new int[n];
			dfs(0, -1, 0);
			var tmp = diameter();
			int a = tmp.b.a, b = tmp.b.b;
			ArrayList<Integer> path = new ArrayList<>();
			while(rank[a]>rank[b]) {
				path.add(a);
				a = parent[a];
			}
			while(rank[b]>rank[a]) {
				path.add(b);
				b = parent[b];
			}
			while(a!=b) {
				path.add(a);
				path.add(b);
				a = parent[a];
				b = parent[b];
			}
			path.add(a);
			int[] dist = bfs(path);
			int ans = tmp.a;
			int max = 0, ind = 0;
			for(int i = 0; i<n; i++) {
				if(dist[i]>=max) {
					max = dist[i];
					ind = i;
				}
			}
			if(max==0) {
				ind = Utilities.mex(Arrays.asList(tmp.b.a, tmp.b.b));
			}
			pw.println(ans+max);
			pw.println(tmp.b.a+1, tmp.b.b+1, ind+1);
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

	static class Utilities {
		public static int mex(Iterable<Integer> iter) {
			BitSet bs = new BitSet();
			for(int i: iter) {
				bs.set(i);
			}
			return bs.nextClearBit(0);
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

	static interface InputReader {
		int nextInt();

		default ArrayList<Integer>[] nextUndirectedGraph(int n, int m) {
			ArrayList<Integer>[] ret = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				ret[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				int u = nextInt()-1, v = nextInt()-1;
				ret[u].add(v);
				ret[v].add(u);
			}
			return ret;
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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
			println();
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
}

