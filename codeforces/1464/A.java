import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
			APeacefulRooks solver = new APeacefulRooks();
			int testCount = in.nextInt();
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class APeacefulRooks {
		int n;
		int m;
		int[][] arr;
		boolean cycle;
		boolean[] visited;
		ArrayList<Integer>[] graph;

		public APeacefulRooks() {
		}

		public void dfs(int u, int p) {
			if(visited[u]) {
				cycle = true;
				return;
			}
			visited[u] = true;
			int cnt = 0;
			for(int v: graph[u]) {
				if(v!=p||cnt++>=1) {
					dfs(v, u);
				}
			}
		}

		public int solve() {
			visited = new boolean[m];
			graph = new ArrayList[m];
			for(int i = 0; i<m; i++) {
				graph[i] = new ArrayList<>();
			}
			int[] yx = new int[n];
			Arrays.fill(yx, -1);
			for(int i = 0; i<m; i++) {
				yx[arr[i][1]] = i;
			}
			int ans = 0;
			for(int i = 0; i<m; i++) {
				int x = arr[i][0];
				if(yx[x]!=-1&&yx[x]!=i) {
					int u = yx[x];
					graph[u].add(i);
					graph[i].add(u);
				}
				if(yx[x]!=i) {
					ans++;
				}
			}
			for(int i = 0; i<m; i++) {
				if(!visited[i]) {
					cycle = false;
					dfs(i, -1);
					if(cycle) {
						ans++;
					}
				}
			}
			return ans;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			m = in.nextInt();
			arr = in.nextInt(m, 2, o -> o-1);
			int ans = solve();
			for(int i = 0; i<m; i++) {
				Utilities.swap(arr[i], 0, 1);
			}
			pw.println(Math.min(ans, solve()));
		}

	}

	static class Utilities {
		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

	}

	interface InputReader {
		int nextInt();

		default int[] nextInt(int n, IntUnaryOperator operator) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = operator.applyAsInt(nextInt());
			}
			return ret;
		}

		default int[][] nextInt(int n, int m, IntUnaryOperator operator) {
			int[][] ret = new int[n][m];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt(m, operator);
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
}

