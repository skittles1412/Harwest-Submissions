import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InputMismatchException;
import java.util.function.Function;

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
			EKeyboardPurchase solver = new EKeyboardPurchase();
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

	static class EKeyboardPurchase {
		private final int iinf = 1_000_000_000;

		public EKeyboardPurchase() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			int[] arr = in.nextIntChar(o -> o-'a');
			int[][] sum = new int[m][1<<m];
			{
				int[][] cnt = new int[m][m];
				for(int i = 0; i<n-1; i++) {
					int a = arr[i], b = arr[i+1];
					cnt[a][b]++;
					cnt[b][a]++;
				}
				for(int i = 0; i<m; i++) {
					for(int j = 1; j<1<<m; j++) {
						int x = j&-j;
						sum[i][j] = sum[i][j^x]+cnt[i][31-Integer.numberOfLeadingZeros(x)];
					}
				}
			}
			int[] mbit = new int[1<<m];
			for(int i = 1; i<1<<m; i++) {
				mbit[i] = Integer.numberOfTrailingZeros(i&-i);
			}
			int[] dp = new int[1<<m];
			for(int i = 1; i<1<<m; i++) {
				int ans = iinf, ci = i;
				for(int j = mbit[ci]; ci>0; ci -= (1<<j), j = mbit[ci]) {
					int cur = dp[i^(1<<j)]+sum[j][i^(1<<j)];
					int x = ((1<<m)-1)^i;
					int cm = i;
					for(int k = mbit[cm]; cm>0; cm -= (1<<k), k = mbit[cm]) {
						cur += sum[k][x];
					}
					cur -= sum[j][x];
					ans = Math.min(ans, cur);
				}
				dp[i] = ans;
			}
//		dbg(dp);
			pw.println(dp[dp.length-1]);
		}

	}

	static interface InputReader {
		String next();

		int nextInt();

		default int[] nextIntChar(Function<Character, Integer> f) {
			String s = next();
			int[] ret = new int[s.length()];
			for(int i = 0; i<s.length(); i++) {
				ret[i] = f.apply(s.charAt(i));
			}
			return ret;
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
}

