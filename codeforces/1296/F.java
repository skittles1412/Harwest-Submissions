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
import java.util.function.IntUnaryOperator;

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
			FBerlandBeauty solver = new FBerlandBeauty();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class FBerlandBeauty {
		private final int iinf = 1_000_000_000;
		ArrayList<Integer>[] graph;
		int n;
		int clock;
		int[] parent;
		int[] weight;
		int[] tin;
		int[] tout;

		public FBerlandBeauty() {
		}

		public void dfs(int u, int p) {
			parent[u] = p;
			tin[u] = clock++;
			for(int v: graph[u]) {
				if(v!=p) {
					dfs(v, u);
				}
			}
			tout[u] = clock-1;
		}

		public boolean anc(int u, int v) {
			return tin[u]<=tin[v]&&tout[u]>=tout[v];
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int[][] arr = in.nextInt(n-1, 2, o -> o-1);
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 0; i<n-1; i++) {
				graph[arr[i][0]].add(arr[i][1]);
				graph[arr[i][1]].add(arr[i][0]);
			}
			parent = new int[n];
			tin = new int[n];
			tout = new int[n];
			dfs(0, -1);
			int m = in.nextInt();
			int[][] queries = in.nextInt(m, 3);
			weight = new int[n];
			Arrays.fill(weight, 1);
			for(int i = 0; i<m; i++) {
				queries[i][0]--;
				queries[i][1]--;
				for(int j = queries[i][0]; !anc(j, queries[i][1]); j = parent[j]) {
					weight[j] = Math.max(weight[j], queries[i][2]);
				}
				for(int j = queries[i][1]; !anc(j, queries[i][0]); j = parent[j]) {
					weight[j] = Math.max(weight[j], queries[i][2]);
				}
			}
			for(int i = 0; i<m; i++) {
				int val = iinf;
				for(int j = queries[i][0]; !anc(j, queries[i][1]); j = parent[j]) {
					val = Math.min(val, weight[j]);
				}
				for(int j = queries[i][1]; !anc(j, queries[i][0]); j = parent[j]) {
					val = Math.min(val, weight[j]);
				}
				if(val!=queries[i][2]) {
					pw.println(-1);
					return;
				}
			}
			for(int i = 0; i<n-1; i++) {
				if(parent[arr[i][1]]==arr[i][0]) {
					Utilities.swap(arr[i], 0, 1);
				}
				pw.print(weight[arr[i][0]]+" ");
			}
			pw.println();
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

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

		default int[][] nextInt(int n, int m) {
			int[][] ret = new int[n][m];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt(m);
			}
			return ret;
		}

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

	static class Utilities {
		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

	}
}

