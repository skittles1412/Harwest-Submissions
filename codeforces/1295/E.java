import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.function.IntUnaryOperator;

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
			EPermutationSeparation solver = new EPermutationSeparation();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class EPermutationSeparation {
		public EPermutationSeparation() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n, (IntUnaryOperator) o -> o-1), cost = new int[n];
			for(int i = 0; i<n; i++) {
				cost[arr[i]] = in.nextInt();
			}
			long[] dp = new long[n+1];
			dp[0] = cost[arr[0]];
			for(int i = 1; i<=n; i++) {
				dp[i] = dp[i-1];
				if(arr[0]==i-1) {
					dp[i] -= cost[i-1];
				}else {
					dp[i] += cost[i-1];
				}
			}
			Utilities.Debug.dbg(dp);
			SegmentTree st = new SegmentTree(dp);
			long ans = st.min(0, n);
			for(int i = 1; i<n-1; i++) {
				int cur = arr[i];
				Utilities.Debug.dbg(cur, cost[cur]);
				if(cur>0) {
					st.add(0, cur, cost[cur]);
				}
				st.add(cur+1, n, -cost[cur]);
				ans = Math.min(ans, st.min(0, n));
			}
			pw.println(ans);
		}

	}

	static class Utilities {
		public static class Debug {
			public static final boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

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

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n, IntUnaryOperator operator) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = operator.applyAsInt(nextInt());
			}
			return ret;
		}

	}

	static class SegmentTree {
		public long[] arr;
		public long[] sumv;
		public long[] minv;
		public long[] maxv;
		public long[] addv;
		public long[] setv;
		public long _sum;
		public long _min;
		public long _max;
		public long y1;
		public long y2;
		public long v;
		public int n;
		public boolean[] setc;
		public boolean add;

		public SegmentTree(int n) {
			this(new long[n]);
		}

		public SegmentTree(long[] arr) {
			this.arr = arr.clone();
			n = arr.length;
			sumv = new long[n<<2];
			minv = new long[n<<2];
			maxv = new long[n<<2];
			addv = new long[n<<2];
			setv = new long[n<<2];
			setc = new boolean[n<<2];
			for(int i = 0; i<n; i++) {
				set(i, arr[i]);
			}
		}

		public void query(int o, int l, int r, long add) {
			if(setc[o]) {
				long v = setv[o]+add+addv[o];
				_sum += v*(Math.min(r, y2)-Math.max(l, y1)+1);
				_min = Math.min(_min, v);
				_max = Math.max(_max, v);
			}else if(y1<=l&&y2>=r) {
				_sum += sumv[o]+add*(r-l+1);
				_min = Math.min(_min, minv[o]+add);
				_max = Math.max(_max, maxv[o]+add);
			}else {
				int m = (l+r)/2;
				if(y1<=m) {
					query(o<<1, l, m, add+addv[o]);
				}
				if(y2>m) {
					query((o<<1)+1, m+1, r, add+addv[o]);
				}
			}
		}

		public void update(int o, int l, int r) {
			int lc = o<<1, rc = (o<<1)+1;
			if(y1<=l&&y2>=r) {
				if(add) {
					addv[o] += v;
				}else {
					setv[o] = v;
					setc[o] = true;
					addv[o] = 0;
				}
			}else {
				pushdown(o);
				int m = (l+r)/2;
				if(y1<=m) {
					update(lc, l, m);
				}else {
					maintain(lc, l, m);
				}
				if(y2>m) {
					update(rc, m+1, r);
				}else {
					maintain(rc, m+1, r);
				}
			}
			maintain(o, l, r);
		}

		private void maintain(int o, int l, int r) {
			int lc = o<<1, rc = (o<<1)+1;
			if(r>l) {
				sumv[o] = sumv[lc]+sumv[rc];
				minv[o] = Math.min(minv[lc], minv[rc]);
				maxv[o] = Math.max(maxv[lc], maxv[rc]);
			}
			if(setc[o]) {
				minv[o] = maxv[o] = setv[o];
				sumv[o] = setv[o]*(r-l+1);
			}
			if(addv[o]!=0) {
				minv[o] += addv[o];
				maxv[o] += addv[o];
				sumv[o] += addv[o]*(r-l+1);
			}
		}

		private void pushdown(int o) {
			int lc = o<<1, rc = (o<<1)+1;
			if(setc[o]) {
				setv[lc] = setv[rc] = setv[o];
				addv[lc] = addv[rc] = 0;
				setc[o] = false;
				setc[lc] = setc[rc] = true;
			}
			if(addv[o]!=0) {
				addv[lc] += addv[o];
				addv[rc] += addv[o];
				addv[o] = 0;
			}
		}

		public void add(int l, int r, long v) {
			y1 = l;
			y2 = r;
			this.v = v;
			add = true;
			update(1, 0, n-1);
		}

		public void set(int l, int r, long v) {
			y1 = l;
			y2 = r;
			this.v = v;
			add = false;
			update(1, 0, n-1);
		}

		public void set(int p, long v) {
			set(p, p, v);
		}

		public void query(int l, int r) {
			y1 = l;
			y2 = r;
			_sum = 0;
			_max = Long.MIN_VALUE;
			_min = Long.MAX_VALUE;
			query(1, 0, n-1, 0);
		}

		public long min(int l, int r) {
			query(l, r);
			return _min;
		}

	}

	static class FastReader implements InputReader {
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
}


