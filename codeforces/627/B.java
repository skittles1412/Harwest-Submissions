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
			BFactoryRepairs solver = new BFactoryRepairs();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BFactoryRepairs {
		public BFactoryRepairs() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt(), k = in.nextInt(), a = in.nextInt(), b = in.nextInt(), q = in.nextInt();
			FenwickTree aft = new FenwickTree(n+1), bft = new FenwickTree(n+1);
			while(q-->0) {
				int type = in.nextInt();
				if(type==1) {
					int ind = in.nextInt()-1, add = in.nextInt();
					aft.add(ind, add);
					bft.add(ind, add);
					if(aft.get(ind)>a) {
						aft.set(ind, a);
					}
					if(bft.get(ind)>b) {
						bft.set(ind, b);
					}
				}else {
					int d = in.nextInt()-1;
					pw.println(bft.sum(0, d-1)+aft.sum(d+k, n));
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

	static class FenwickTree {
		public int n;
		public long[] sum;
		public long[] arr;

		public FenwickTree(int n) {
			this(new long[n]);
		}

		public FenwickTree(int[] arr) {
			n = arr.length;
			sum = new long[n+1];
			this.arr = new long[n+1];
			for(int i = 0; i<n; i++) {
				add(i, arr[i]);
			}
		}

		public FenwickTree(long[] arr) {
			n = arr.length;
			sum = new long[n+1];
			this.arr = new long[n+1];
			for(int i = 0; i<n; i++) {
				add(i, arr[i]);
			}
		}

		public void add(int ind, long val) {
			ind++;
			arr[ind] += val;
			while(ind<=n) {
				sum[ind] += val;
				ind += ind&-ind;
			}
		}

		public long psum(int x) {
			x++;
			long ans = 0;
			while(x>0) {
				ans += sum[x];
				x -= x&-x;
			}
			return ans;
		}

		public long sum(int l, int r) {
			return psum(r)-psum(l-1);
		}

		public void set(int ind, long val) {
			add(ind, val-arr[ind+1]);
		}

		public long get(int ind) {
			return sum(ind, ind);
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

