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
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			FTwoBracketSequences solver = new FTwoBracketSequences();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class FTwoBracketSequences {
		private final int iinf = 1_000_000_000;

		public FTwoBracketSequences() {
		}

		private int get(char c) {
			return c=='(' ? 1 : 0;
		}

		public void solve(int kase, InputReader in, Output pw) {
			String sa = in.next(200), sb = in.next(200);
			int n = sa.length(), m = sb.length();
			int[][] a = new int[n+1][2], b = new int[m+1][2];
			for(int i = 0; i<n; i++) {
				a[i][get(sa.charAt(i))] = 1;
			}
			for(int i = 0; i<m; i++) {
				b[i][get(sb.charAt(i))] = 1;
			}
			int[][][] dp = new int[n+1][m+1][201], next = new int[n+1][m+1][201];
			for(int i = 0; i<=200; i++) {
				dp[n][m][i] = i;
			}
			for(int i = n; i>=0; i--) {
				for(int j = m-(i==n ? 1 : 0); j>=0; j--) {
					for(int k = 1; k<200; k++) {
						dp[i][j][k] = iinf;
						if(a[i][0]>0||b[j][0]>0) {
							dp[i][j][k] = dp[i+a[i][0]][j+b[j][0]][k-1];
							next[i][j][k] = -1;
						}
						if((a[i][1]>0||b[j][1]>0)&&dp[i+a[i][1]][j+b[j][1]][k+1]<dp[i][j][k]) {
							dp[i][j][k] = dp[i+a[i][1]][j+b[j][1]][k+1];
							next[i][j][k] = 1;
						}
						dp[i][j][k]++;
					}
					next[i][j][0] = 1;
					dp[i][j][0] = 1+dp[i+a[i][1]][j+b[j][1]][1];
					next[i][j][200] = -1;
					dp[i][j][200] = 1+dp[i+a[i][0]][j+b[j][0]][199];
				}
			}
			int i = 0, j = 0, k = 0;
			while(i<n||j<m) {
				pw.print(next[i][j][k]==1 ? "(" : ")");
				int x = Math.max(next[i][j][k], 0);
				k += next[i][j][k];
				i += a[i][x];
				j += b[j][x];
			}
			pw.println(")".repeat(k));
		}

	}

	static interface InputReader {
		String next();

		default String next(int maxLength) {
			return next();
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

		public String next(int maxLength) {
			byte[] ret = new byte[maxLength];
			byte c = skip();
			int ind = 0;
			while(c!=-1&&!isSpaceChar(c)) {
				ret[ind++] = c;
				c = read();
			}
			return new String(ret, 0, ind);
		}

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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

