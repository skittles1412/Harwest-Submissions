import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			FZublicanesAndMumocrates solver = new FZublicanesAndMumocrates();
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

	static class FZublicanesAndMumocrates {
		private final int iinf = 1_000_000_000;
		int[] cnt;
		int[][][] dp;
		ArrayList<Integer>[] graph;

		public FZublicanesAndMumocrates() {
		}

		public void dfs(int u) {
			if(graph[u].isEmpty()) {
				cnt[u] = 1;
			}
			Integer ui = u;
			for(int v: graph[u]) {
				graph[v].remove(ui);
				dfs(v);
				cnt[u] += cnt[v];
			}
		}

		public int dp(int u, int r, int x) {
			if(dp[u][r][x]==-1) {
				dp(u);
			}
			return dp[u][r][x];
		}

		public void dp(int u) {
			if(graph[u].isEmpty()) {
				for(int i = 0; i<2; i++) {
					Arrays.fill(dp[u][i], iinf);
					dp[u][i][i] = 0;
				}
				return;
			}
			int n = graph[u].size();
			int[][][] dp = new int[2][n+1][graph.length+1];
			for(int i = 0; i<2; i++) {
				Arrays.fill(dp[i][n], iinf);
				dp[i][n][0] = 0;
			}
			for(int r = 0; r<2; r++) {
				for(int i = n-1; i>=0; i--) {
					int v = graph[u].get(i);
					dp[r][i] = dp[r][i+1].clone();
					for(int j = 0; j<=cnt[u]; j++) {
						dp[r][i][j]++;
					}
					for(int j = 0; j<=cnt[v]; j++) {
						for(int k = 0; j+k<=cnt[u]; k++) {
							for(int nr = 0; nr<2; nr++) {
								dp[r][i][j+k] = Math.min(dp[r][i][j+k], (r^nr)+dp(v, nr, j)+dp[r][i+1][k]);
							}
						}
					}
				}
			}
			this.dp[u][0] = dp[0][0];
			this.dp[u][1] = dp[1][0];
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			if(n==2) {
				pw.println("1");
				return;
			}
			graph = in.nextUndirectedGraph(n, n-1);
			cnt = new int[n];
			int root = 0;
			while(graph[root].size()==1) {
				root++;
			}
			dfs(root);
			dp = new int[n][2][n+1];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<2; j++) {
					Arrays.fill(dp[i][j], -1);
				}
			}
			pw.println(Math.min(dp(root, 0, cnt[root]/2), dp(root, 1, cnt[root]/2)));
			dp(3);
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
}

