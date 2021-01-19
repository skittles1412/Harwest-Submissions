import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
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
			EAwardsForContestants solver = new EAwardsForContestants();
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

	static class EAwardsForContestants {
		private final long linf = 4_000_000_000_000_000_000L;
		final long mul1 = 5000;
		final long mul2 = mul1*mul1;
		int logn;

		public EAwardsForContestants() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			logn = 31-Integer.numberOfLeadingZeros(n);
			int[][] arr = new int[n][3];
			for(int i = 0; i<n; i++) {
				arr[i][0] = in.nextInt();
				arr[i][1] = i;
			}
			Arrays.sort(arr, Comparator.comparingInt(o -> o[0]));
			long max = -linf;
			int ind = 0;
			for(int i = 1; i<=n/3; i++) {
				long[] dp3 = new long[n];
				Arrays.fill(dp3, -linf);
				for(int j = n-i; j>=1&&j>=n-2*i; j--) {
					dp3[j] = mul2*(arr[j][0]-arr[j-1][0]);
				}
				SparseTable st3 = new SparseTable(dp3);
				long[] dp2 = new long[n];
				Arrays.fill(dp2, -linf);
				for(int j = n-2*i; j>=1&&j>=n-4*i; j--) {
					dp2[j] = mul1*(arr[j][0]-arr[j-1][0])+st3.max(j+i, Math.min(n-1, j+2*i));
				}
				SparseTable st2 = new SparseTable(dp2);
				long[] dp1 = new long[n];
				Arrays.fill(dp1, -linf);
				for(int j = n-3*i; j>=0&&j>=n-6*i; j--) {
					if(j==0) {
						dp1[j] = arr[j][0]+st2.max(j+i, Math.min(n-1, j+2*i));
					}else {
						dp1[j] = arr[j][0]-arr[j-1][0]+st2.max(j+i, Math.min(n-1, j+2*i));
					}
				}
				for(int j = 0; j<n; j++) {
					if(dp1[j]>max) {
						max = dp1[j];
						ind = i;
					}
				}
			}
			int i = ind;
			long[] dp3 = new long[n];
			Arrays.fill(dp3, -linf);
			for(int j = n-i; j>=1&&j>=n-2*i; j--) {
				dp3[j] = mul2*(arr[j][0]-arr[j-1][0]);
			}
			int[] next2 = new int[n];
			long[] dp2 = new long[n];
			Arrays.fill(dp2, -linf);
			for(int j = n-1; j>=1; j--) {
				for(int k = i; k<=2*i&&j+k<n; k++) {
					long val = mul1*(arr[j][0]-arr[j-1][0])+dp3[j+k];
					if(val>dp2[j]) {
						dp2[j] = val;
						next2[j] = j+k;
					}
				}
			}
			int[] next1 = new int[n];
			long[] dp1 = new long[n];
			Arrays.fill(dp1, -linf);
			int prev = 0;
			for(int j = 0; j<n; j++) {
				for(int k = i; k<=2*i&&j+k<n; k++) {
					long val = arr[j][0]-prev+dp2[j+k];
					if(val>dp1[j]) {
						dp1[j] = val;
						next1[j] = j+k;
					}
				}
				prev = arr[j][0];
			}
			for(int j = 0; j<n; j++) {
				if(dp1[j]==max) {
					for(int k = 0; k<j; k++) {
						arr[k][2] = -1;
					}
					for(int k = j; k<next1[j]; k++) {
						arr[k][2] = 3;
					}
					for(int k = j = next1[j]; k<next2[j]; k++) {
						arr[k][2] = 2;
					}
					for(int k = next2[j]; k<n; k++) {
						arr[k][2] = 1;
					}
				}
			}
			Arrays.sort(arr, Comparator.comparingInt(o -> o[1]));
			for(int j = 0; j<n; j++) {
				pw.print(arr[j][2]+" ");
			}
		}

		private class SparseTable {
			public long[][] dmax;

			public SparseTable(long[] arr) {
				int n = arr.length;
				dmax = new long[logn+1][n];
				dmax[0] = arr.clone();
				for(int i = 1; i<=logn; i++) {
					for(int j = 0; j+(1<<i)-1<n; j++) {
						dmax[i][j] = Math.max(dmax[i-1][j], dmax[i-1][j+(1<<i-1)]);
					}
				}
			}

			public long max(int i, int j) {
				if(i==j) {
					return dmax[0][i];
				}
				int k = 63-Long.numberOfLeadingZeros(j-i);
				return Math.max(dmax[k][i], dmax[k][j-(1<<k)+1]);
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

	interface InputReader {
		int nextInt();

	}
}

