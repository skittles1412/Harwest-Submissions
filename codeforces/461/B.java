import java.io.*;
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
			long startTime = System.currentTimeMillis();
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			BApplemanAndTree solver = new BApplemanAndTree();
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

	static class BApplemanAndTree {
		private final int mod = (int) (1e9+7);
		int n;
		int[] arr;
		long[][] memo;
		ArrayList<Integer>[] graph;

		public BApplemanAndTree() {
		}

		public long dp(int u, int x) {
			if(memo[u][x]!=-1) {
				return memo[u][x];
			}
			long ans = 0;
			if(x==0) {
				ans = dp(u, 1);
				if(arr[u]==0) {
					long cur = 1;
					for(int v: graph[u]) {
						cur = (cur*dp(v, 0))%mod;
					}
					ans += cur;
				}
			}else {
				if(arr[u]==1) {
					long cur = 1;
					for(int v: graph[u]) {
						cur = (cur*dp(v, 0))%mod;
					}
					ans = cur;
				}else {
					long tot = 1;
					for(int v: graph[u]) {
						tot = (tot*dp(v, 0))%mod;
					}
					for(int v: graph[u]) {
						ans += ((tot*Utilities.math.pow(dp(v, 0), mod-2, mod))%mod*dp(v, 1))%mod;
					}
					ans %= mod;
				}
			}
			return memo[u][x] = ans;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 1; i<n; i++) {
				graph[in.nextInt()].add(i);
			}
			arr = in.nextInt(n);
			memo = new long[n][2];
			for(int i = 0; i<n; i++) {
				memo[i][0] = memo[i][1] = -1;
			}
			pw.println(dp(0, 1));
		}

	}

	static class Utilities {
		public static class math {
			public static long pow(long base, long exp, long mod) {
				long ans = 1, cur = base;
				while(exp>0) {
					if((exp&1)>0) {
						ans = (ans*cur)%mod;
					}
					cur = (cur*cur)%mod;
					exp >>= 1;
				}
				return ans;
			}

		}

	}

	interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
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

		public void println(long l) {
			println(String.valueOf(l));
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

