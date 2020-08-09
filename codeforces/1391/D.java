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
			D505 solver = new D505();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class D505 {
		public D505() {
		}

		public int solve(boolean[][] arr) {
			int n = arr.length, m = arr[0].length, x = Math.min(n, m);
			if(x==2) {
				int ans = 0;
				for(int i = 0; i<m-1; i++) {
					int sum = 0;
					for(int a = 0; a<2; a++) {
						for(int b = i; b<i+2; b++) {
							if(arr[a][b]) {
								sum++;
							}
						}
					}
					if((sum&1)==0) {
						ans++;
						arr[0][i+1] ^= true;
					}
				}
				return ans;
			}else {
				int ans = 0;
				for(int i = 0; i<m-1; i++) {
					int sum1 = 0, sum2 = 0;
					for(int a = 0; a<2; a++) {
						for(int b = i; b<i+2; b++) {
							if(arr[a][b]) {
								sum1++;
							}
						}
					}
					for(int a = 1; a<3; a++) {
						for(int b = i; b<i+2; b++) {
							if(arr[a][b]) {
								sum2++;
							}
						}
					}
					boolean o1 = (sum1&1)==0, o2 = (sum2&1)==0;
					if(o1&&o2) {
						ans++;
						arr[1][i+1] ^= true;
					}else if(o1) {
						ans++;
						arr[0][i+1] ^= true;
					}else if(o2) {
						ans++;
						arr[2][i+1] ^= true;
					}
				}
				return ans;
			}
		}

		public boolean[][] clone(boolean[][] arr) {
			int n = arr.length, m = arr[0].length;
			boolean[][] ret = new boolean[n][m];
			for(int i = 0; i<n; i++) {
				ret[i] = arr[i].clone();
			}
			return ret;
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt(), m = in.nextInt(), x = Math.min(n, m);
			boolean swapped = false;
			if(x>=4) {
				pw.println(-1);
				return;
			}else if(n>m) {
				n ^= m;
				m ^= n;
				n ^= m;
				swapped = true;
			}
			boolean[][] arr = new boolean[n][m];
			if(swapped) {
				for(int i = 0; i<m; i++) {
					String s = in.next();
					for(int j = 0; j<n; j++) {
						arr[j][i] = s.charAt(j)=='1';
					}
				}
			}else {
				for(int i = 0; i<n; i++) {
					String s = in.next();
					for(int j = 0; j<m; j++) {
						arr[i][j] = s.charAt(j)=='1';
					}
				}
			}
			if(x==1) {
				pw.println(0);
				return;
			}else if(x==2) {
				int ans = solve(clone(arr));
				arr[0][0] ^= true;
				pw.println(Math.min(ans, 1+solve(arr)));
			}else {
				int ans = solve(clone(arr));
				arr[0][0] ^= true;
				ans = Math.min(ans, 1+solve(clone(arr)));
				arr[0][0] ^= true;
				arr[1][0] ^= true;
				ans = Math.min(ans, 1+solve(clone(arr)));
				arr[1][0] ^= true;
				arr[2][0] ^= true;
				ans = Math.min(ans, 1+solve(clone(arr)));
				arr[2][0] ^= true;
				pw.println(ans);
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


