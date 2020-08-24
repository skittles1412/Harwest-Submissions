import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.DataInputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.BufferedOutputStream;
import java.io.Closeable;
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
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			QFlowers solver = new QFlowers();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class QFlowers {
		public QFlowers() {
		}

		public void solve(int kase, FastReader in, Output pw) {
			int n = in.nextInt();
			int[] h = new int[n], a = new int[n];
			for(int i = 0; i<n; i++) {
				h[i] = in.nextInt()-1;
			}
			for(int i = 0; i<n; i++) {
				a[i] = in.nextInt();
			}
			long[] dp = new long[n];
			long ans = -1;
			PointSegmentTree st = new PointSegmentTree(n);
			for(int i = n-1; i>=0; i--) {
				ans = Math.max(ans, dp[i] = a[i]+st.max(h[i], n-1));
				st.set(h[i], dp[i]);
			}
			pw.println(ans);
			Utilities.Debug.dbg(dp);
		}

	}

	static class FastReader {
		final private int BUFFER_SIZE = 1<<16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer;
		private int bytesRead;

		public FastReader(InputStream is) {
			din = new DataInputStream(is);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public int nextInt() {
			int ret = 0;
			byte c = skipToDigit();
			boolean neg = (c=='-');
			if(neg) {
				c = read();
			}
			do {
				ret = ret*10+c-'0';
			} while((c = read())>='0'&&c<='9');
			if(neg) {
				return -ret;
			}
			return ret;
		}

		private boolean isDigit(byte b) {
			return b>='0'&&b<='9';
		}

		private byte skipToDigit() {
			byte ret;
			while(!isDigit(ret = read())&&ret!='-') ;
			return ret;
		}

		private void fillBuffer() {
			try {
				bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			}catch(IOException e) {
				e.printStackTrace();
				throw new InputMismatchException();
			}
			if(bytesRead==-1) {
				buffer[0] = -1;
			}
		}

		private byte read() {
			if(bytesRead==-1) {
				throw new InputMismatchException();
			}else if(bufferPointer==bytesRead) {
				fillBuffer();
			}
			return buffer[bufferPointer++];
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

	static class Utilities {
		public static class Debug {
			public static final boolean LOCAL = System.getProperty("LOCAL")!=null;

			private static <T> String ts(T t) {
				if(t==null) {
					return "null";
				}
				try {
					return ts((Iterable) t);
				}catch(ClassCastException e) {
					if(t instanceof int[]) {
						String s = Arrays.toString((int[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof long[]) {
						String s = Arrays.toString((long[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof char[]) {
						String s = Arrays.toString((char[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof double[]) {
						String s = Arrays.toString((double[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof boolean[]) {
						String s = Arrays.toString((boolean[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}
					try {
						return ts((Object[]) t);
					}catch(ClassCastException e1) {
						return t.toString();
					}
				}
			}

			private static <T> String ts(T[] arr) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: arr) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			private static <T> String ts(Iterable<T> iter) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: iter) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			public static void dbg(Object... o) {
				if(LOCAL) {
					System.err.print("Line #"+Thread.currentThread().getStackTrace()[2].getLineNumber()+": [");
					for(int i = 0; i<o.length; i++) {
						if(i!=0) {
							System.err.print(", ");
						}
						System.err.print(ts(o[i]));
					}
					System.err.println("]");
				}
			}

		}

	}

	static class PointSegmentTree {
		long[] minv;
		long[] maxv;
		long[] sumv;
		long[] arr;
		long _sum;
		long _min;
		long _max;
		long ind;
		long val;
		long x;
		long y;
		int n;

		public PointSegmentTree(int n) {
			this(new long[n]);
		}

		public PointSegmentTree(long[] arr) {
			this.arr = arr.clone();
			n = arr.length;
			minv = new long[n<<2];
			maxv = new long[n<<2];
			sumv = new long[n<<2];
			build(1, 0, n-1);
		}

		private void maintain(int o, int l, int r) {
			if(r>l) {
				int lc = o<<1, rc = (o<<1)+1;
				sumv[o] = sumv[lc]+sumv[rc];
				minv[o] = Math.min(minv[lc], minv[rc]);
				maxv[o] = Math.max(maxv[lc], maxv[rc]);
			}
		}

		private void build(int o, int l, int r) {
			if(l==r) {
				sumv[o] = minv[o] = maxv[o] = arr[l];
				return;
			}
			int mid = r+l >> 1;
			build(o<<1, l, mid);
			build((o<<1)+1, mid+1, r);
			maintain(o, l, r);
		}

		public void set(int ind, long val) {
			this.ind = ind;
			this.val = val;
			arr[ind] = val;
			set(1, 0, n-1);
		}

		private void set(int o, int l, int r) {
			if(l==r) {
				minv[o] = maxv[o] = sumv[o] = val;
				return;
			}
			int mid = l+r >> 1;
			if(ind<=mid) {
				set(o<<1, l, mid);
			}else {
				set((o<<1)+1, mid+1, r);
			}
			maintain(o, l, r);
		}

		private void query(int o, int l, int r) {
			if(x<=l&&y>=r) {
				_sum += sumv[o];
				_min = Math.min(_min, minv[o]);
				_max = Math.max(_max, maxv[o]);
				return;
			}
			int mid = l+r >> 1;
			if(x<=mid) {
				query(o<<1, l, mid);
			}
			if(y>mid) {
				query((o<<1)+1, mid+1, r);
			}
		}

		public long max(int l, int r) {
			query(l, r);
			return _max;
		}

		public void query(int l, int r) {
			_sum = 0;
			_min = Long.MAX_VALUE;
			_max = Long.MIN_VALUE;
			x = l;
			y = r;
			query(1, 0, n-1);
		}

	}
}

