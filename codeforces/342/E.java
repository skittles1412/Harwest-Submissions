import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;

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
			EXeniaAndTree solver = new EXeniaAndTree();
			solver.solve(1, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<26);
		thread.start();
		thread.join();
	}

	static class EXeniaAndTree {
		private final int iinf = 1_000_000_000;
		final int maxn = 17;
		int n;
		int clock;
		int[] size;
		int[] rank;
		int[] tin;
		int[] tout;
		int[] parent;
		int[] ans;
		int[][] lift;
		boolean[] marked;
		ArrayList<Integer>[] graph;
		ArrayList<Integer>[] centroid;

		public EXeniaAndTree() {
		}

		public void pdfs(int u, int p) {
			tin[u] = clock++;
			size[u] = 1;
			for(int v: graph[u]) {
				if(v!=p) {
					lift[0][v] = u;
					rank[v] = rank[u]+1;
					pdfs(v, u);
					size[u] += size[v];
				}
			}
			tout[u] = clock-1;
		}

		public boolean anc(int u, int v) {
			return tin[u]<=tin[v]&&tout[u]>=tout[v];
		}

		public int lca(int u, int v) {
			if(anc(u, v)) {
				return u;
			}
			for(int i = maxn-1; i>=0; i--) {
				if(lift[i][u]!=-1&&!anc(lift[i][u], v)) {
					u = lift[i][u];
				}
			}
			return lift[0][u];
		}

		public int dist(int u, int v) {
			int lca = lca(u, v);
			return rank[u]-rank[lca]+rank[v]-rank[lca];
		}

		public int decomp(int u) {
			int min = size[u]/2;
			for(int v: graph[u]) {
				if(!marked[v]&&size[v]>min) {
					size[u] -= size[v];
					size[v] += size[u];
					return decomp(v);
				}
			}
			marked[u] = true;
			for(int v: graph[u]) {
				if(!marked[v]) {
					int cur = decomp(v);
					parent[cur] = u;
					centroid[u].add(cur);
				}
			}
			return u;
		}

		public void update(int u) {
			for(int i = u; i!=-1; i = parent[i]) {
				ans[i] = Math.min(ans[i], dist(i, u));
			}
		}

		public int query(int u) {
			int ret = iinf;
			for(int i = u; i!=-1; i = parent[i]) {
				ret = Math.min(ret, dist(i, u)+ans[i]);
			}
			return ret;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int q = in.nextInt();
			graph = in.nextUndirectedGraph(n, n-1);
			clock = 0;
			tin = new int[n];
			tout = new int[n];
			size = new int[n];
			rank = new int[n];
			lift = new int[maxn][n];
			pdfs(0, -1);
			lift[0][0] = -1;
			for(int i = 1; i<maxn; i++) {
				for(int j = 0; j<n; j++) {
					lift[i][j] = lift[i-1][j]==-1 ? -1 : lift[i-1][lift[i-1][j]];
				}
			}
			centroid = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				centroid[i] = new ArrayList<>();
			}
			parent = new int[n];
			marked = new boolean[n];
			parent[decomp(0)] = -1;
			ans = new int[n];
			Arrays.fill(ans, iinf);
			update(0);
			while(q-->0) {
				if(in.nextInt()==1) {
					update(in.nextInt()-1);
				}else {
					pw.println(query(in.nextInt()-1));
				}
			}
		}

	}

	interface InputReader {
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
		private final DataInputStream din;
		private final byte[] buffer;
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
}

