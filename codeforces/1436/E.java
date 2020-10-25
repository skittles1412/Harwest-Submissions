import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
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
			long startTime = System.currentTimeMillis();
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			EComplicatedComputations solver = new EComplicatedComputations();
			solver.solve(1, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class EComplicatedComputations {
		int n;
		int[] arr;
		EComplicatedComputations.SegmentTree st;

		public EComplicatedComputations() {
		}

		public int bSearch(int x) {
			int o = 1, l = 0, r = n;
			while(l<r) {
				int mid = l+r >> 1, lc = o<<1, rc = lc|1;
				if(st.value[lc]>=x) {
					o = rc;
					l = mid+1;
				}else {
					o = lc;
					r = mid;
				}
			}
			return l;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			arr = in.nextInt(n, o -> o-1);
			BitSet ans = new BitSet(n+2);
			ArrayList<Integer>[] query = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				query[i] = new ArrayList<>();
			}
			int[] prev = new int[n];
			prev[arr[0]] = 1;
			for(int i = 1; i<n; i++) {
				query[i-1].add(prev[arr[i]]);
				prev[arr[i]] = i+1;
			}
			for(int i = 0; i<n; i++) {
				query[n-1].add(prev[i]);
			}
			query[n-1].add(0);
			st = new EComplicatedComputations.SegmentTree(n+1);
			for(int i = 0; i<n; i++) {
				st.set(arr[i], i);
				for(int j: query[i]) {
					if(j<=i) {
						ans.set(bSearch(j));
					}
				}
			}
//			Utilities.Debug.dbg(ans);
//			Utilities.Debug.dbg(bSearch(0));
			pw.println(ans.nextClearBit(0)+1);
		}

		static class SegmentTree {
			public int n;
			public int ind;
			public int[] arr;
			public int[] value;

			public SegmentTree(int n) {
				this.n = n;
				arr = new int[n];
				Arrays.fill(arr, -1);
				value = new int[n<<2];
				build(1, 0, n-1);
			}

			private void build(int o, int l, int r) {
				if(l==r) {
					value[o] = arr[l];
					return;
				}
				int lc = o<<1, rc = lc|1, mid = l+r >> 1;
				build(lc, l, mid);
				build(rc, mid+1, r);
				value[o] = Math.min(value[lc], value[rc]);
			}

			private void set(int o, int l, int r) {
				if(l==r) {
					value[o] = arr[l];
					return;
				}
				int lc = o<<1, rc = lc|1, mid = l+r >> 1;
				if(ind<=mid) {
					set(lc, l, mid);
				}else {
					set(rc, mid+1, r);
				}
				value[o] = Math.min(value[lc], value[rc]);
			}

			public void set(int ind, int val) {
				this.ind = ind;
				arr[ind] = val;
				set(1, 0, n-1);
			}

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

		public void println(int i) {
			println(String.valueOf(i));
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

