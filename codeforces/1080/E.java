import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
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
			ESonyaAndMatrixBeauty solver = new ESonyaAndMatrixBeauty();
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

	static class ESonyaAndMatrixBeauty {
		public ESonyaAndMatrixBeauty() {
		}

		public int solve(long[] arr) {
			int n = arr.length;
			int ans = 0;
			//https://cp-algorithms.com/string/manacher.html
			int[] odd = new int[n], even = new int[n];
			for(int i = 0, l = 0, r = -1; i<n; i++) {
				int k = (i>r) ? 1 : Math.min(odd[l+r-i], r-i+1);
				while(0<=i-k&&i+k<n&&arr[i-k]==arr[i+k]) {
					k++;
				}
				ans += odd[i] = k--;
				if(i+k>r) {
					l = i-k;
					r = i+k;
				}
			}
			for(int i = 0, l = 0, r = -1; i<n; i++) {
				int k = (i>r) ? 0 : Math.min(even[l+r-i+1], r-i+1);
				while(0<=i-k-1&&i+k<n&&arr[i-k-1]==arr[i+k]) {
					k++;
				}
				ans += even[i] = k--;
				if(i+k>r) {
					l = i-k-1;
					r = i+k;
				}
			}
			return ans;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			int[][] arr = in.nextIntChar(n, o -> o-'a');
			long ans = 0;
			for(int i = 0; i<m; i++) {
				ESonyaAndMatrixBeauty.Subarray[] cur = new ESonyaAndMatrixBeauty.Subarray[n];
				for(int j = 0; j<n; j++) {
					cur[j] = new ESonyaAndMatrixBeauty.Subarray();
				}
				for(int j = i; j<m; j++) {
					for(int k = 0; k<n; k++) {
						cur[k].add(arr[k][j]);
					}
					for(int x = 0; x<n; ) {
						for(; x<n&&!cur[x].palin(); x++) ;
						if(x==n) {
							break;
						}
						int l = x;
						for(; x<n&&cur[x].palin(); x++) ;
						int r = x-1;
						long[] hash = new long[r-l+1];
						for(int k = 0; k<hash.length; k++) {
							hash[k] = cur[k+l].hash;
						}
						ans += solve(hash);
					}
				}
			}
			pw.println(ans);
		}

		static class Subarray {
			static final long[] hashv;
			int mask;
			int cardinality;
			long hash;

			static {
				hashv = new long[26];
				for(int i = 0; i<26; i++) {
					hashv[i] = BigInteger.probablePrime(63, new Random()).longValue();
				}
			}

			public Subarray() {
				hash = mask = cardinality = 0;
			}

			public void add(int x) {
				cardinality = Integer.bitCount(mask ^= 1<<x);
				hash += hashv[x];
			}

			public boolean palin() {
				return cardinality<=1;
			}

		}

	}

	static interface InputReader {
		String next();

		int nextInt();

		default int[] nextIntChar(InputReader.CharToIntFunction f) {
			String s = next();
			int[] ret = new int[s.length()];
			for(int i = 0; i<s.length(); i++) {
				ret[i] = f.apply(s.charAt(i));
			}
			return ret;
		}

		default int[][] nextIntChar(int n, InputReader.CharToIntFunction f) {
			int[][] ret = new int[n][];
			for(int i = 0; i<n; i++) {
				ret[i] = nextIntChar(f);
			}
			return ret;
		}

		interface CharToIntFunction {
			int apply(char c);

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
}

