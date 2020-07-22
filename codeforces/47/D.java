import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.HashSet;
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
			DSafe solver = new DSafe();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DSafe {
		public DSafe() {
		}

		public void solve(int testNumber, Input in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			HashSet<Long> prev = null, cur = null;
			for(int i = 0; i<m; i++) {
				cur = new HashSet<>();
				long l = Long.parseLong(in.next(), 2);
				int nums = in.nextInt();
				ArrayList<int[]> ways = Utilities.generate(0, n-1, nums);
				for(int[] x: ways) {
					long xor = (1L<<n)-1L;
					for(int j: x) {
						xor &= ~(1L<<(long) j);
					}
					long cl = l^(xor);
					if(prev==null) {
						cur.add(cl);
					}else if(prev.contains(cl)) {
						cur.add(cl);
					}
				}
				prev = cur;
			}
			pw.println(cur.size());
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

	static class Utilities {
		private static ArrayList<int[]> generate_ans;
		private static int upper_bound;
		private static int length;

		public static ArrayList<int[]> generate(int l, int r, int n) {
			generate_ans = new ArrayList<>();
			upper_bound = r;
			length = n;
			generate(l, 0, new int[n]);
			return generate_ans;
		}

		private static void generate(int cur, int ind, int[] soFar) {
			if(ind==length) {
				generate_ans.add(soFar);
				return;
			}
			for(int i = cur; i<=upper_bound-length+ind+1; i++) {
				int[] soFarc = soFar.clone();
				soFarc[ind] = i;
				generate(i+1, ind+1, soFarc);
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

