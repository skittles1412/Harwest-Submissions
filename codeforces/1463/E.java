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
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.function.IntUnaryOperator;

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
			EPlanOfLectures solver = new EPlanOfLectures();
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

	static class EPlanOfLectures {
		public EPlanOfLectures() {
		}

		public void dfs(int u, boolean[] visited, ArrayList<Integer>[] graph, ArrayList<Integer> ans) {
			if(visited[u]) {
				return;
			}
			visited[u] = true;
			for(int v: graph[u]) {
				dfs(v, visited, graph, ans);
			}
			ans.add(u);
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), k = in.nextInt();
			int[] arr = in.nextInt(n, o -> o-1);
			ArrayList<Integer>[] graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 0; i<n; i++) {
				if(arr[i]!=-1) {
					graph[arr[i]].add(i);
				}
			}
			ArrayList<Integer>[] adj = new ArrayList[n], comp = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				adj[i] = new ArrayList<>();
				comp[i] = new ArrayList<>();
			}
			DSU dsu = new DSU(n);
			boolean[] valid = new boolean[n];
			Arrays.fill(valid, true);
			for(int i = 0; i<k; i++) {
				int u = in.nextInt()-1, v = in.nextInt()-1;
				adj[u].add(v);
				dsu.union(u, v);
				valid[v] = false;
			}
			int[] conv = new int[n];
			for(int i = 0; i<n; i++) {
				comp[conv[i] = dsu.find(i)].add(i);
			}
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<graph[i].size(); j++) {
					graph[i].set(j, conv[graph[i].get(j)]);
				}
				if(conv[i]!=i) {
					graph[conv[i]].addAll(graph[i]);
				}
			}
//			Utilities.Debug.dbg(conv, graph);
			ArrayList<Integer> sort = new ArrayList<>();
			boolean[] visited = new boolean[n];
			for(int i = 0; i<n; i++) {
				if(conv[i]==i) {
					dfs(i, visited, graph, sort);
				}
			}
			Collections.reverse(sort);
			visited = new boolean[n];
			ArrayList<Integer> ans = new ArrayList<>();
//			Utilities.Debug.dbg(sort);
			loop:
			for(int i: sort) {
				for(int j: comp[i]) {
					if(valid[j]) {
						ArrayList<Integer> tmp = new ArrayList<>();
						dfs(j, visited, adj, tmp);
						Collections.reverse(tmp);
						ans.addAll(tmp);
						continue loop;
					}
				}
				pw.println("0");
				return;
			}
			int[] ind = new int[n];
			for(int i = 0; i<n; i++) {
				ind[ans.get(i)] = i;
			}
			for(int i = 0; i<n; i++) {
				if(arr[i]!=-1&&ind[arr[i]]>ind[i]) {
					pw.println("0");
					return;
				}
			}
			for(int i = 0; i<n; i++) {
				for(int j: adj[i]) {
					if(ind[i]+1!=ind[j]) {
						pw.println("0");
						return;
					}
				}
			}
			for(int i: ans) {
				pw.print((i+1)+" ");
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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
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

	static class DSU {
		public int[] parent;
		public int[] size;
		public int n;

		public DSU(int n) {
			this.n = n;
			parent = new int[this.n];
			size = new int[this.n];
			for(int i = 0; i<this.n; i++) {
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

		public boolean union(int i, int j) {
			int irep = find(i);
			int jrep = find(j);
			if(irep==jrep) {
				return false;
			}
			int irank = size[irep];
			int jrank = size[jrep];
			if(irank<=jrank) {
				parent[irep] = jrep;
				size[jrep] += size[irep];
			}else {
				parent[jrep] = irep;
				size[irep] += size[jrep];
			}
			return true;
		}

	}

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n, IntUnaryOperator operator) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = operator.applyAsInt(nextInt());
			}
			return ret;
		}

	}

	static class Utilities {
		public static class Debug {
			public static boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

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
}

