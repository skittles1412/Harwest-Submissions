import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;

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
			E1TreesAndQueries solver = new E1TreesAndQueries();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class E1TreesAndQueries {
		ArrayList<Integer>[] graph;
		int time = 0;
		int[] tin;
		int[] tout;
		int[] rank;
		int[][] up;

		public E1TreesAndQueries() {
		}

		public void dfs(int u, int r) {
			tin[u] = time++;
			rank[u] = r;
			for(int v: graph[u]) {
				graph[v].remove((Integer) u);
				up[0][v] = u;
				dfs(v, r+1);
			}
			tout[u] = time-1;
		}

		public boolean ancestor(int u, int v) {
			return u==-1||(tin[u]<=tin[v]&&tout[u]>=tout[v]);
		}

		public int lca(int u, int v) {
			if(ancestor(u, v)) {
				return u;
			}
			for(int i = 17; i>=0; i--) {
				if(!ancestor(up[i][u], v)) {
					u = up[i][u];
				}
			}
			return up[0][u];
		}

		public int dist(int u, int v) {
			return rank[u]+rank[v]-(rank[lca(u, v)]<<1);
		}

		public boolean valid(int dist, int length) {
			return dist<=length&&(dist&1)==(length&1);
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			graph = in.nextUndirectedGraph(n, n-1);
			up = new int[18][n];
			tin = new int[n];
			tout = new int[n];
			rank = new int[n];
			up[0][0] = -1;
			dfs(0, 0);
			for(int i = 1; i<18; i++) {
				for(int j = 0; j<n; j++) {
					if(up[i-1][j]!=-1) {
						up[i][j] = up[i-1][up[i-1][j]];
					}
				}
			}
			int q = in.nextInt();
			while(q-->0) {
				int x = in.nextInt()-1, y = in.nextInt()-1, a = in.nextInt()-1, b = in.nextInt()-1, k = in.nextInt();
				if(valid(dist(a, b), k)||valid(dist(a, x)+dist(b, y)+1, k)||valid(dist(a, y)+dist(b, x)+1, k)) {
					pw.println("YES");
				}else {
					pw.println("NO");
				}
			}
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


