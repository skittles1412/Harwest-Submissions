import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			ELongPermutation solver = new ELongPermutation();
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

	static class ELongPermutation {
		long[] fact;
		long[] psum;

		public ELongPermutation() {
			fact = new long[16];
			fact[0] = 1;
			for(int i = 1; i<=15; i++) {
				fact[i] = fact[i-1]*i;
			}
			int mn = (int) 2e5;
			psum = new long[mn+1];
			for(int i = 1; i<=mn; i++) {
				psum[i] = psum[i-1]+i;
			}
		}

		public int[] solve(int n, long k) {
			if(n==1) {
				return new int[] {1};
			}
			int start = (int) (k/fact[n-1]+1);
			int[] ret = new int[n];
			ret[0] = start;
			System.arraycopy(solve(n-1, k%fact[n-1]), 0, ret, 1, n-1);
			for(int i = 1; i<n; i++) {
				if(ret[i]>=start) {
					ret[i]++;
				}
			}
			return ret;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), q = in.nextInt();
			int m = Math.min(16, n), start = n-m;
			int[] arr = solve(m, 0);
			long sum = 0;
			while(q-->0) {
				int type = in.nextInt();
				if(type==1) {
					int l = in.nextInt()-1, r = in.nextInt()-1;
					long ans = 0;
					if(r>=start) {
						int al = Math.max(0, l-start), ar = r-start;
						for(int i = al; i<=ar; i++) {
							ans += arr[i]+start;
						}
					}
					if(l<start) {
						int al = l, ar = Math.min(start-1, r);
						ans += psum[ar+1]-psum[al];
					}
					pw.println(ans);
				}else {
					arr = solve(m, sum += in.nextInt());
				}
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

	}
}
