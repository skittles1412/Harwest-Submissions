import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
			FDestroyIt solver = new FDestroyIt();
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

	static class FDestroyIt {
		private final long linf = 4_000_000_000_000_000_000L;

		public FDestroyIt() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			long[][] arr = new long[n][4], doub = new long[n][4];
			for(int i = 0; i<n; i++) {
				long thr = -linf, two = -linf;
				ArrayList<Long> one = new ArrayList<>();
				for(int j = 0; j<3; j++) {
					one.add(-linf/2);
				}
				{
					int x = in.nextInt();
					for(int j = 0; j<x; j++) {
						int type = in.nextInt();
						if(type==1) {
							one.add((long) in.nextInt());
						}else if(type==2) {
							two = Math.max(two, in.nextInt());
						}else {
							thr = Math.max(thr, in.nextInt());
						}
					}
				}
				one.sort(Comparator.comparingLong(o -> -o));
				arr[i][1] = Math.max(one.get(0), Math.max(two, thr));
				arr[i][2] = Math.max(two+one.get(0), one.get(0)+one.get(1));
				arr[i][3] = one.get(0)+one.get(1)+one.get(2);
				doub[i][1] = 2*arr[i][1];
				doub[i][2] = Math.max(Math.max(two, one.get(0))+two+one.get(0), 2*one.get(0)+one.get(1));
				doub[i][3] = arr[i][3]+one.get(0);
				for(int j = 1; j<=3; j++) {
					arr[i][j] = Math.max(arr[i][j], -linf);
					doub[i][j] = Math.max(doub[i][j], -linf);
				}
			}
			long[][] dp = new long[n+1][10];
			for(int i = n-1; i>=0; i--) {
				for(int j = 0; j<10; j++) {
					for(int k = 0; k<=3; k++) {
						if(j+k>=10) {
							dp[i][j] = Math.max(dp[i][j], doub[i][k]+dp[i+1][j+k-10]);
						}else {
							dp[i][j] = Math.max(dp[i][j], arr[i][k]+dp[i+1][j+k]);
						}
					}
				}
			}
			pw.println(dp[0][0]);
		}

	}

	static interface InputReader {
		int nextInt();

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
}

