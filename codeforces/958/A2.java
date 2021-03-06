import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Flushable;

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
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			A2DeathStarsMedium solver = new A2DeathStarsMedium();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class A2DeathStarsMedium {
		public A2DeathStarsMedium() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			char[][] a = new char[n][m], b = new char[m][n];
			for(int i = 0; i<n; i++) {
				a[i] = in.next().toCharArray();
			}
			for(int i = 0; i<m; i++) {
				b[i] = in.next().toCharArray();
			}
			int[] ah = new int[n];
			for(int i = 0; i<n; i++) {
				long p = 31, mod = 1000000009, hash = 0, pow = 1;
				for(int j = 0; j<m; j++) {
					hash = (hash+(a[i][j]-'a'+1)*pow)%mod;
					pow = (pow*p)%mod;
				}
				ah[i] = (int) hash;
			}
			int[][] bh = new int[m][n-m+1];
			for(int i = 0; i<m; i++) {
				for(int j = 0; j<n-m+1; j++) {
					long p = 31, mod = 1000000009, hash = 0, pow = 1;
					for(int k = 0; k<m; k++) {
						hash = (hash+(b[i][j+k]-'a'+1)*pow)%mod;
						pow = (pow*p)%mod;
					}
					bh[i][j] = (int) hash;
				}
			}
			for(int i = 0; i<n-m+1; i++) {
				loop:
				for(int j = 0; j<n-m+1; j++) {
					for(int k = 0; k<m; k++) {
						if(ah[i+k]!=bh[k][j]) {
							continue loop;
						}
					}
					pw.println(i+1, j+1);
					return;
				}
			}
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

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
			println();
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

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = null;
		}

		public boolean hasNext() {
			try {
				while(st==null||!st.hasMoreTokens()) {
					String s = br.readLine();
					if(s==null) {
						return false;
					}
					st = new StringTokenizer(s);
				}
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		public String next() {
			if(!hasNext()) {
				throw new InputMismatchException();
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

