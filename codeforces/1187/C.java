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
			CVasyaAndArray solver = new CVasyaAndArray();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CVasyaAndArray {
		public CVasyaAndArray() {
		}

		public void solve(int testNumber, Input in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			int[] b = new int[n];
			int[][] inp = new int[m][3];
			Arrays.fill(b, -1);
			for(int i = 0; i<m; i++) {
				int t = in.nextInt(), l = in.nextInt()-1, r = in.nextInt()-1;
				inp[i] = new int[] {t, l, r};
				for(int j = l; j<r&&t==1; j++) {
					b[j] = 0;
				}
			}
			int[] a = new int[n];
			a[0] = n<<1;
			for(int i = 1; i<n; i++) {
				a[i] = a[i-1]+b[i-1];
			}
			loop:
			for(int[] test: inp) {
				int l = test[1], r = test[2];
				if(test[0]==1) {
					for(int i = l+1; i<=r; i++) {
						if(a[i]<a[i-1]) {
							pw.println("NO");
							return;
						}
					}
				}else {
					for(int i = l+1; i<=r; i++) {
						if(a[i]<a[i-1]) {
							continue loop;
						}
					}
					pw.println("NO");
					return;
				}
			}
			pw.println("YES");
			pw.println(a);
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

		public void print(int i) {
			print(String.valueOf(i));
		}

		public void print(String s) {
			sb.append(s);
			if(autoFlush||sb.length()>BUFFER_SIZE >> 1) {
				flush();
			}
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush||sb.length()>BUFFER_SIZE >> 1) {
				flush();
			}
		}

		public void println() {
			sb.append(LineSeparator);
		}

		public void println(int[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
			}
			println();
		}

		public void flush() {
			try {
				os.write(sb.toString().getBytes());
			}catch(IOException e) {
				e.printStackTrace();
			}
			sb = new StringBuilder(BUFFER_SIZE);
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

