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
			DRarityAndNewDress solver = new DRarityAndNewDress();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DRarityAndNewDress {
		public DRarityAndNewDress() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			char[][] arr = new char[n][m];
			for(int i = 0; i<n; i++) {
				arr[i] = in.next().toCharArray();
			}
			int[][] up = new int[n][m], down = new int[n][m], left = new int[n][m], right = new int[n][m];
			for(int i = 1; i<n; i++) {
				for(int j = 0; j<m; j++) {
					up[i][j] = arr[i][j]==arr[i-1][j] ? 1+up[i-1][j] : 0;
				}
			}
			for(int i = n-2; i>=0; i--) {
				for(int j = 0; j<m; j++) {
					down[i][j] = arr[i][j]==arr[i+1][j] ? 1+down[i+1][j] : 0;
				}
			}
			for(int i = 0; i<n; i++) {
				for(int j = 1; j<m; j++) {
					left[i][j] = arr[i][j]==arr[i][j-1] ? Math.min(left[i][j-1]+1, Math.min(up[i][j], down[i][j])) : 0;
				}
			}
			for(int i = 0; i<n; i++) {
				for(int j = m-2; j>0; j--) {
					right[i][j] = arr[i][j]==arr[i][j+1] ? Math.min(right[i][j+1]+1, Math.min(up[i][j], down[i][j])) : 0;
				}
			}
			long ans = 0;
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<m; j++) {
					ans += 1+Math.min(left[i][j], right[i][j]);
				}
			}
			pw.println(ans);
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

		public void println(long l) {
			println(String.valueOf(l));
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



