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
			FControversialRounds solver = new FControversialRounds();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class FControversialRounds {
		int[] cnt;
		int[] next;
		int n;
		int cur;

		public FControversialRounds() {
		}

		public int count(int ind) {
			if(ind==n) {
				return 0;
			}else if(cnt[ind]>=cur) {
				return 1+count(ind+cur);
			}
			return count(next(ind));
		}

		public int next(int x) {
			if(x==n) {
				return n;
			}else if(cnt[x]>=cur) {
				return x;
			}
			return next[x] = next(next[x]);
		}

		public void solve(int kase, Input in, Output pw) {
			n = in.nextInt();
			String s = in.next();
			int[][] most = new int[n+1][2];
			cnt = new int[n];
			next = new int[n];
			for(int i = n-1; i>=0; i--) {
				char c = s.charAt(i);
				if(c=='0'||c=='?') {
					most[i][0] = 1+most[i+1][0];
				}
				if(c=='1'||c=='?') {
					most[i][1] = 1+most[i+1][1];
				}
				cnt[i] = Math.max(most[i][0], most[i][1]);
			}
			for(int i = 0; i<n; i++) {
				next[i] = i+1;
			}
			for(cur = 1; cur<=n; cur++) {
				pw.print(count(0)+" ");
			}
			pw.println();
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

		public void print(String s) {
			sb.append(s);
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


