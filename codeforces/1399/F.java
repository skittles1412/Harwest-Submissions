import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;

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
			FYetAnotherSegmentsSubset solver = new FYetAnotherSegmentsSubset();
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

	static class FYetAnotherSegmentsSubset {
		ArrayList<Integer>[] start;
		int[][] dp;

		public FYetAnotherSegmentsSubset() {
		}

		public int dp(int l, int r) {
			if(dp[l][r]!=-1) {
				return dp[l][r];
			}
			boolean valid = start[l].contains(r);
			if(l==r) {
				return dp[l][r] = valid ? 1 : 0;
			}
			int ans = dp(l+1, r);
			for(int j: start[l]) {
				if(j<r) {
					ans = Math.max(ans, dp(l, j)+dp(j+1, r));
				}
			}
			if(valid) {
				ans++;
			}
			return dp[l][r] = ans;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			start = new ArrayList[2*n];
			for(int i = 0; i<2*n; i++) {
				start[i] = new ArrayList<>();
			}
			{
				int[][] arr = in.nextInt(n, 2);
				int[] tmp = new int[n*2];
				for(int i = 0; i<n; i++) {
					for(int j = 0; j<2; j++) {
						tmp[i*2+j] = arr[i][j];
					}
				}
				Utilities.sort(tmp);
				int ind = 1;
				HashMap<Integer, Integer> hm = new HashMap<>(2*n);
				hm.put(tmp[0], 0);
				for(int i = 1; i<n*2; i++) {
					if(tmp[i]!=tmp[i-1]) {
						hm.put(tmp[i], ind++);
					}
				}
				for(int i = 0; i<n; i++) {
					int s = hm.get(arr[i][0]), e = hm.get(arr[i][1]);
					start[s].add(e);
				}
			}
			System.gc();
			dp = new int[2*n][2*n];
			for(int i = 0; i<2*n; i++) {
				Arrays.fill(dp[i], -1);
			}
			pw.println(dp(0, 2*n-1));
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

	}

	static class Utilities {
		public static void sort(int[] arr) {
			Random rand = new Random();
			int n = arr.length;
			for(int i = 0; i<n; i++) {
				swap(arr, i, rand.nextInt(n));
			}
			Arrays.sort(arr);
		}

		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
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
}

