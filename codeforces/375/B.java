import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
			BMaximumSubmatrix2 solver = new BMaximumSubmatrix2();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BMaximumSubmatrix2 {
		int n;
		int m;
		int[][] cnt;
		int[][] memo;
		int[][] d;
		int[] ends;
		int[] sum;

		public BMaximumSubmatrix2() {
		}

		public int dp(int x, int y) {
			if(memo[x][y]!=-1) {
				return memo[x][y];
			}else if(x==y) {
				return memo[x][y] = sum[x];
			}
			return memo[x][y] = dp(x, y-1)-d(x, y-1);
		}

		public int d(int x, int y) {
			if(d[x][y]!=-1) {
				return d[x][y];
			}else if(x==y) {
				return ends[y];
			}
			return d[x][y] = d(x+1, y)-cnt[x+1][y];
		}

		public void solve(int kase, Input in, Output pw) {
			n = in.nextInt();
			m = in.nextInt();
			cnt = new int[m][m];
			sum = new int[m+1];
			ends = new int[m];
			for(int i = 0; i<n; i++) {
				String s = in.next();
				for(int j = 0; j<m; ) {
					for(; j<m&&s.charAt(j)=='0'; j++) ;
					if(j!=m) {
						int start = j;
						for(; j<m&&s.charAt(j)=='1'; j++) ;
						cnt[start][j-1]++;
						ends[j-1]++;
						sum[start]++;
						sum[j]--;
					}
				}
			}
			for(int i = 1; i<m; i++) {
				sum[i] += sum[i-1];
			}
			memo = new int[m][m];
			d = new int[m][m];
			for(int i = 0; i<m; i++) {
				Arrays.fill(memo[i], -1);
				Arrays.fill(d[i], -1);
			}
			int ans = 0;
			for(int i = 0; i<m; i++) {
				for(int j = i; j<m; j++) {
					ans = Math.max(ans, (j-i+1)*dp(i, j));
				}
			}
			pw.println(ans);
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

		public void println(int i) {
			println(String.valueOf(i));
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
}


