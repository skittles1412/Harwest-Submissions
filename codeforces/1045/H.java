import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			HSelfExploration solver = new HSelfExploration();
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

	static class HSelfExploration {
		private final int mod = (int) (1e9+7);
		final Utilities.BinomCoefficient bc = new Utilities.BinomCoefficient((int) 1e6, mod);
		int n;
		String sa;
		String sb;

		public HSelfExploration() {
		}

		public long solve(int ind, int[][] arr) {
			ind = n-1-ind;
			int zero = arr[0][0]+arr[1][0], ones = ind-zero, groups = arr[1][0];
			if(zero==0||groups>0) {
				if(arr[1][0]-1==arr[0][1]) {
					return (sandb(zero-groups, groups)*sandb(ones-groups+1, groups))%mod;
				}else if(arr[1][0]==arr[0][1]) {
					return (sandb(zero-groups, groups)*sandb(ones-groups, groups+1))%mod;
				}
			}
			return 0;
		}

		public long solve0(int ind, int[][] arr) {
			long ans = 0;
			int[][] cur = new int[2][2];
			int prev = 1;
			for(int i = ind; i<n; i++) {
				loop:
				if(sb.charAt(i)=='1') {
					int[][] narr = Utilities.deepClone(arr);
					narr[prev][0]--;
					for(int j = 0; j<2; j++) {
						for(int k = 0; k<2; k++) {
							if((narr[j][k] -= cur[j][k])<0) {
								break loop;
							}
						}
					}
					{
						int tmp = narr[0][0];
						narr[0][0] = narr[1][1];
						narr[1][1] = tmp;
					}
					{
						int tmp = narr[0][1];
						narr[0][1] = narr[1][0];
						narr[1][0] = tmp;
					}
					ans += solve(i, narr);
				}
				cur[prev][prev = (sb.charAt(i)-'0')]++;
			}
			if(Arrays.deepEquals(arr, cur)) {
				ans++;
			}
			return ans%mod;
		}

		public long sandb(int n, int k) {//stars and bars n elements into k groups
			return Math.min(n, k)==0 ? 1 : bc.choose(n+k-1, n);
		}

		public void solve(int kase, InputReader in, Output pw) {
			sa = in.next();
			sb = in.next();
			int[][] arr = in.nextInt(2, 2);
			n = 1;
			for(int i = 0; i<2; i++) {
				for(int j = 0; j<2; j++) {
					n += arr[i][j];
				}
			}
			if(sa.length()>n||sb.length()<n) {
				pw.println("0");
				return;
			}
			if(sa.length()<n) {
				sa = "1"+"0".repeat(n-1);
			}
			if(sb.length()>n) {
				sb = "1".repeat(n);
			}
			long ans = 0;
			boolean diff = false;
			int[][] cur = new int[2][2];
			for(int i = 0; i<n; i++) {
				loop:
				if(sa.charAt(i)=='0') {
					if(diff) {
						int[][] narr = Utilities.deepClone(arr);
						narr[sa.charAt(i-1)-'0'][1]--;
						for(int j = 0; j<2; j++) {
							for(int k = 0; k<2; k++) {
								if((narr[j][k] -= cur[j][k])<0) {
									break loop;
								}
							}
						}
						ans += solve(i, narr);
					}else if(sb.charAt(i)=='1') {
						int[][] narr = Utilities.deepClone(arr);
						if(i>0) {
							narr[sa.charAt(i-1)-'0'][1]--;
						}
						for(int j = 0; j<2; j++) {
							for(int k = 0; k<2; k++) {
								if((narr[j][k] -= cur[j][k])<0) {
									break loop;
								}
							}
						}
						ans += solve0(i+1, narr);
					}
				}
				diff |= sa.charAt(i)!=sb.charAt(i);
				if(i>0) {
					cur[sa.charAt(i-1)-'0'][sa.charAt(i)-'0']++;
				}
			}
			if(Arrays.deepEquals(arr, cur)) {
				ans++;
			}
			pw.println(ans%mod);
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
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

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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

	static interface InputReader {
		String next();

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
		public static int[][] deepClone(int[][] arr) {
			int[][] ret = new int[arr.length][];
			for(int i = 0; i<arr.length; i++) {
				ret[i] = arr[i].clone();
			}
			return ret;
		}

		public static long[] factorial(int n, long mod) {
			long[] ret = new long[n+1];
			ret[0] = 1;
			for(int i = 1; i<=n; i++) {
				ret[i] = (i*ret[i-1])%mod;
			}
			return ret;
		}

		public static long[] inverseFactorial(int n, long mod) {
			long[] ret = Utilities.math.modInverseArr(n, mod);
			ret[0] = 1;
			Arrays.parallelPrefix(ret, (a, b) -> a*b%mod);
			return ret;
		}

		public static class BinomCoefficient {
			private final long mod;
			private final long[] fact;
			private final long[] invFact;

			public BinomCoefficient(int n, long mod) {
				this.mod = mod;
				fact = factorial(n, mod);
				invFact = inverseFactorial(n, mod);
			}

			public long choose(int n, int k) {
				return fact[n]*invFact[k]%mod*invFact[n-k]%mod;
			}

		}

		public static class math {
			public static long[] modInverseArr(int n, long mod) {
				if(n==0) {
					return new long[] {0};
				}
				long[] ret = new long[n+1];
				ret[1] = 1;
				for(int i = 2; i<=n; i++) {
					ret[i] = (-(mod/i)*ret[(int) (mod%i)])%mod;
					ret[i] += mod;
				}
				return ret;
			}

		}

	}
}

