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
			DKuroniAndTheCelebration solver = new DKuroniAndTheCelebration();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class DKuroniAndTheCelebration {
		private final int iinf = 1_000_000_000;
		Output pw;
		InputReader in;
		ArrayList<Integer>[] graph;
		int n;
		BitSet valid;
		BitSet path;
		BitSet next;
		int[] from;

		public DKuroniAndTheCelebration() {
		}

		public int[] bfs(int u) {
			int[] dist = new int[n];
			Arrays.fill(dist, iinf);
			ArrayDeque<Pair<Integer, Integer>> q = new ArrayDeque<>(n);
			q.addLast(new Pair<>(u, dist[u] = 0));
			while(!q.isEmpty()) {
				Pair<Integer, Integer> cur = q.removeFirst();
				for(int v: graph[cur.a]) {
					if(dist[v]==iinf&&valid.get(v)) {
						q.addLast(new Pair<>(v, dist[v] = cur.b+1));
						from[v] = cur.a;
					}
				}
			}
			return dist;
		}

		public Pair<Integer, Integer> diameter() {
			int start = valid.nextSetBit(0);
			int[] dist = bfs(start);
			int max = 0, v = start;
			for(int i = 0; i<n; i++) {
				if(dist[i]>max&&valid.get(i)) {
					max = dist[i];
					v = i;
				}
			}
			from = new int[n];
			path = new BitSet(n);
			dist = bfs(v);
			int u = v;
			max = v = 0;
			for(int i = 0; i<n; i++) {
				if(dist[i]>max&&valid.get(i)) {
					max = dist[i];
					v = i;
				}
			}
			for(int i = v; i!=u; i = from[i]) {
				path.set(i);
			}
			path.set(u);
			return new Pair<>(v, u);
		}

		public void solve() {
			if(valid.cardinality()==1) {
				pw.println("!", valid.nextSetBit(0)+1);
				return;
			}
			var q = diameter();
			pw.println("?", q.a+1, q.b+1);
			pw.flush();
			next = new BitSet(n);
			dfs(in.nextInt()-1);
			valid = next;
			solve();
		}

		public void dfs(int u) {
			next.set(u);
			for(int v: graph[u]) {
				if(!path.get(v)&&!next.get(v)&&valid.get(v)) {
					dfs(v);
				}
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			graph = in.nextUndirectedGraph(n, n-1);
			from = new int[n];
			valid = new BitSet(n);
			valid.set(0, n);
			this.in = in;
			this.pw = pw;
			solve();
			pw.flush();
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

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
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

