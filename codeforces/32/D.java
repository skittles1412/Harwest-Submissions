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
			DConstellation solver = new DConstellation();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DConstellation {
		public DConstellation() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt(), m = in.nextInt(), k = in.nextInt();
			boolean[][] arr = new boolean[n][m];
			for(int i = 0; i<n; i++) {
				String s = in.next();
				for(int j = 0; j<m; j++) {
					arr[i][j] = s.charAt(j)=='*';
				}
			}
			for(int r = 1; r<=150; r++) {
				for(int i = 0; i<n; i++) {
					if(i-r >= 0&&i+r<n) {
						for(int j = 0; j<m; j++) {
							if(j-r >= 0&&j+r<m&&arr[i][j]&&arr[i-r][j]&&arr[i+r][j]&&arr[i][j-r]&&arr[i][j+r]&&--k==0) {
								pw.println(new DConstellation.cross(i, j, r));
								return;
							}
						}
					}
				}
			}
			pw.println(-1);
		}

		static class cross implements Comparable<DConstellation.cross> {
			int x;
			int y;
			int r;

			public cross(int x, int y, int r) {
				this.x = x;
				this.y = y;
				this.r = r;
			}

			public int compareTo(DConstellation.cross c) {
				if(c.r==r) {
					if(c.x==x) {
						return y-c.y;
					}
					return x-c.x;
				}
				return r-c.r;
			}

			public String toString() {
				StringBuilder ret = new StringBuilder();
				x++;
				y++;
				ret.append(x).append(" ").append(y).append("\n")
						.append(x-r).append(" ").append(y).append("\n")
						.append(x+r).append(" ").append(y).append("\n")
						.append(x).append(" ").append(y-r).append("\n")
						.append(x).append(" ").append(y+r);
				x--;
				y--;
				return ret.toString();
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

