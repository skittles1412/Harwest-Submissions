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
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			BTheLeastRoundWay solver = new BTheLeastRoundWay();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}
	static class BTheLeastRoundWay {
		private final int iinf = 1_000_000_000;
		int n;
		int[][] arr2;
		int[][] arr5;
		int[][] memo2;
		int[][] memo5;
		StringBuilder sb;

		public BTheLeastRoundWay() {
		}

		public int dp2(int x, int y) {
			if(memo2[x][y]!=-1) {
				return memo2[x][y];
			}
			int ans = iinf;
			if(x<n-1) {
				ans = Math.min(ans, dp2(x+1, y));
			}
			if(y<n-1) {
				ans = Math.min(ans, dp2(x, y+1));
			}
			return memo2[x][y] = arr2[x][y]+ans;
		}

		public int dp5(int x, int y) {
			if(memo5[x][y]!=-1) {
				return memo5[x][y];
			}
			int ans = iinf;
			if(x<n-1) {
				ans = Math.min(ans, dp5(x+1, y));
			}
			if(y<n-1) {
				ans = Math.min(ans, dp5(x, y+1));
			}
			return memo5[x][y] = arr5[x][y]+ans;
		}

		public void print2(int x, int y) {
			if(x==n-1&&y==n-1) {
				return;
			}
			int cur = memo2[x][y]-arr2[x][y];
			if(x<n-1&&memo2[x+1][y]==cur) {
				sb.append("D");
				print2(x+1, y);
			}else {
				sb.append("R");
				print2(x, y+1);
			}
		}

		public void print5(int x, int y) {
			if(x==n-1&&y==n-1) {
				return;
			}
			int cur = memo5[x][y]-arr5[x][y];
			if(x<n-1&&memo5[x+1][y]==cur) {
				sb.append("D");
				print5(x+1, y);
			}else {
				sb.append("R");
				print5(x, y+1);
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int[][] arr = in.nextInt(n, n);
			boolean zero = false;
			int x = 0, y = 0;
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<n; j++) {
					if(arr[i][j]==0) {
						zero = true;
						x = i;
						y = j;
						arr[i][j] = 10;
					}
				}
			}
			arr2 = new int[n][n];
			arr5 = new int[n][n];
			memo2 = new int[n][n];
			memo5 = new int[n][n];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<n; j++) {
					arr2[i][j] = Integer.numberOfTrailingZeros(arr[i][j]);
					while(arr[i][j]%5==0) {
						arr5[i][j]++;
						arr[i][j] /= 5;
					}
				}
			}
			for(int i = 0; i<n; i++) {
				Arrays.fill(memo2[i], -1);
				Arrays.fill(memo5[i], -1);
			}
			memo2[n-1][n-1] = arr2[n-1][n-1];
			memo5[n-1][n-1] = arr5[n-1][n-1];
			int m2 = dp2(0, 0), m5 = dp5(0, 0);
			if(zero&&m2>1&&m5>1) {
				pw.println(1);
				for(int i = 0; i<x; i++) {
					pw.print("D");
				}
				for(int i = 0; i<y; i++) {
					pw.print("R");
				}
				for(int i = x; i<n-1; i++) {
					pw.print("D");
				}
				for(int i = y; i<n-1; i++) {
					pw.print("R");
				}
				pw.println();
			}else if(m2<m5) {
				pw.println(m2);
				sb = new StringBuilder(n<<1);
				print2(0, 0);
				pw.println(sb);
			}else {
				pw.println(m5);
				sb = new StringBuilder(n<<1);
				print5(0, 0);
				pw.println(sb);
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

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public boolean autoFlush;
		public String LineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			autoFlush = false;
			LineSeparator = System.lineSeparator();
		}

		public void print(String s) {
			sb.append(s);
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
			println();
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println() {
			sb.append(LineSeparator);
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
}


