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
			AEnlargeGCD solver = new AEnlargeGCD();
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

	static class AEnlargeGCD {
		public AEnlargeGCD() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n);
			int gcd = arr[0];
			for(int i = 1; i<n; i++) {
				gcd = Utilities.gcd(gcd, arr[i]);
			}
			boolean eq = true;
			for(int i = 0; i<n; i++) {
				eq &= arr[i]==gcd;
				arr[i] /= gcd;
			}
			if(eq) {
				pw.println("-1");
				return;
			}
			Utilities.PrimeFactorizer pf = new Utilities.PrimeFactorizer((int) 2e7);
			int ans = 0;
			int[] cnt = new int[(int) 2e7];
			for(int i = 0; i<n; i++) {
				var v = pf.shortFactor(arr[i]);
				for(int j: v) {
					ans = Math.max(ans, ++cnt[j]);
				}
			}
			pw.println(n-ans);
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

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
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

	}

	static class Utilities {
		public static int gcd(int a, int b) {
			return b==0 ? a : gcd(b, a%b);
		}

		public static class PrimeFactorizer {
			int[] minDiv;

			public PrimeFactorizer(int n) {
				minDiv = new int[n+1];
				boolean[] prime = new boolean[n+1];
				Arrays.fill(prime, true);
				for(int i = 2; i<=n; i++) {
					if(prime[i]) {
						minDiv[i] = i;
						if((long) i*i<=n) {
							for(int j = i*i; j<=n; j += i) {
								if(prime[j]) {
									prime[j] = false;
									minDiv[j] = i;
								}
							}
						}
					}
				}
			}

			public ArrayList<Integer> shortFactor(int x) {
				ArrayList<Integer> ans = new ArrayList<>();
				while(x>1) {
					int div = minDiv[x];
					while(minDiv[x /= div]==div) ;
					ans.add(div);
				}
				return ans;
			}

		}

	}
}

