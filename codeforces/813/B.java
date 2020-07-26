import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.TreeSet;
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
			BTheGoldenAge solver = new BTheGoldenAge();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BTheGoldenAge {
		public BTheGoldenAge() {
		}

		public void solve(int kase, Input in, Output pw) {
			TreeSet<Long> ts = new TreeSet<>();
			long x = in.nextLong(), y = in.nextLong(), l = in.nextLong(), r = in.nextLong();
			long xpow = 1;
			while(xpow<=1e18/x) {
				xpow *= x;
				long ypow = 1;
				while(ypow<=1e18/y) {
					ypow *= y;
					if(xpow+ypow >= l&&xpow+ypow<=r) {
						ts.add(xpow+ypow);
					}
					if(1+ypow >= l&&1+ypow<=r) {
						ts.add(1+ypow);
					}
					if(1+xpow >= l&&1+xpow<=r) {
						ts.add(1+xpow);
					}
				}
			}
			if(2 >= l&&2<=r) {
				ts.add(2L);
			}
			long ans = 0;
			if(ts.isEmpty()) {
				pw.println(r-l+1);
				return;
			}
			ts.add(l-1);
			for(long i: ts) {
				Long next = ts.higher(i);
				if(next==null) {
					next = r+1;
				}
				ans = Math.max(ans, next-i-1);
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

		public long nextLong() {
			return Long.parseLong(next());
		}

	}
}

