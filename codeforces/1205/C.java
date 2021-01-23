import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.StringTokenizer;

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
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			CPalindromicPaths solver = new CPalindromicPaths();
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

	static class CPalindromicPaths {
		int n;
		Output pw;
		InputReader in;

		public CPalindromicPaths() {
		}

		public int query(int a, int b, int c, int d) {
			pw.println("?", a+1, b+1, c+1, d+1);
			pw.flush();
			return in.nextInt();
		}

		public void solve(int kase, InputReader in, Output pw) {
			this.in = in;
			this.pw = pw;
			n = in.nextInt();
			Solver solver0 = new Solver(0, 0, 1, 0, 1, 0, n-1, n-1, 0);
			solver0.dfs(0, 0);
			solver0.dfs(0, 1);
			Solver solver1 = solver0.inverse();
			for(int a = 0; a<n; a++) {
				for(int b = 0; b<n; b++) {
					for(int c = a; c<n; c++) {
						for(int d = b; d<n; d++) {
							if(a+b+3==c+d&&solver0.path(a, b, c, d)^solver1.path(a, b, c, d)) {
								boolean ans = query(a, b, c, d)==1;
								if(ans==solver0.path(a, b, c, d)) {
									solver0.output();
								}else {
									solver1.output();
								}
								return;
							}
						}
					}
				}
			}
		}

		private class Solver {
			int[][] arr;

			public Solver(int... set) {
				arr = new int[n][n];
				for(int i = 0; i<n; i++) {
					Arrays.fill(arr[i], -1);
				}
				for(int i = 0; i<set.length; i += 3) {
					arr[set[i]][set[i+1]] = set[i+2];
				}
			}

			public Solver(int[][] arr) {
				this.arr = arr;
			}

			public void dfs(int x, int y) {
				for(int i = -2; i<=0; i++) {
					for(int j = -2; j<=0; j++) {
						if(i+j==-2) {
							int cx = x+i, cy = y+j;
							if(cx>=0&&cy>=0&&cx<n&&cy<n&&arr[cx][cy]==-1) {
								arr[cx][cy] = arr[x][y]^1^query(Math.min(cx, x), Math.min(cy, y), Math.max(cx, x), Math.max(cy, y));
								dfs(cx, cy);
							}
						}
					}
				}
				for(int i = 0; i<=2; i++) {
					for(int j = 0; j<=2; j++) {
						if(i+j==2) {
							int cx = x+i, cy = y+j;
							if(cx>=0&&cy>=0&&cx<n&&cy<n&&arr[cx][cy]==-1) {
								arr[cx][cy] = arr[x][y]^1^query(Math.min(cx, x), Math.min(cy, y), Math.max(cx, x), Math.max(cy, y));
								dfs(cx, cy);
							}
						}
					}
				}
			}

			public Solver inverse() {
				int[][] ret = new int[n][n];
				for(int i = 0; i<n; i++) {
					for(int j = 0; j<n; j++) {
						ret[i][j] = arr[i][j]^((i+j)&1);
					}
				}
				return new Solver(ret);
			}

			public boolean path(int a, int b, int c, int d) {
				if(arr[a][b]!=arr[c][d]) {
					return false;
				}
				int diff = c-a;
				if(diff==3) {
					return arr[a+1][b]==arr[a+2][b];
				}else if(diff==2) {
					return arr[a+1][b]==arr[a+2][b]||arr[a][b+1]==arr[a+1][b+1];
				}else if(diff==1) {
					return arr[a][b+1]==arr[a][b+2]||arr[a+1][b]==arr[a+1][b+1];
				}
				return arr[a][b+1]==arr[a][b+2];
			}

			public void output() {
				pw.println("!");
				for(int i = 0; i<n; i++) {
					for(int j = 0; j<n; j++) {
						pw.print(arr[i][j]);
					}
					pw.println();
				}
				pw.flush();
			}

		}

	}

	static class Input implements InputReader {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = new StringTokenizer("");
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

	static interface InputReader {
		int nextInt();

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

		public void print(int i) {
			print(String.valueOf(i));
		}

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
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

