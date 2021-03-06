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
			FFruitSequences solver = new FFruitSequences();
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

	static class FFruitSequences {
		RangeFenwickTree ft;

		public FFruitSequences() {
		}

		public int lowerBound(int h, int target) {
			int l = 0;
			while(l<h) {
				int mid = l+h >> 1;
				if(ft.sum(mid, mid)>target) {
					l = mid+1;
				}else {
					h = mid;
				}
			}
			return l;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			String s = in.next(n);
			long ans = 0;
			ft = new RangeFenwickTree(n);
			int cur = 0;
			for(int i = 0; i<n; i++) {
				if(s.charAt(i)=='1') {
					cur++;
				}else {
					cur = 0;
				}
				int ind = lowerBound(i+1, cur-1);
				if(ind<=i) {
					ft.add(ind, i, 1);
				}
				ans += ft.sum(0, i);
//				Utilities.Debug.dbg(ft);
			}
			pw.println(ans);
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

		public void println(long l) {
			println(String.valueOf(l));
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

	static class FenwickTree {
		private int n;
		public long[] sum;
		public long[] arr;

		public FenwickTree(int n) {
			this.n = n;
			sum = new long[n+1];
			arr = new long[n+1];
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
			while(ind<=n) {
				sum[ind] += val;
				ind += ind&-ind;
			}
		}

		public long psum(int x) {
			long ret = 0;
			while(x>0) {
				ret += sum[x];
				x -= x&-x;
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
		}

		public String next(int maxLength) {
			byte[] ret = new byte[maxLength];
			byte c = skip();
			int ind = 0;
			while(c!=-1&&!isSpaceChar(c)) {
				ret[ind++] = c;
				c = read();
			}
			return new String(ret, 0, ind);
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

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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

	static interface InputReader {
		String next();

		int nextInt();

		default String next(int maxLength) {
			return next();
		}

	}

	static class RangeFenwickTree {
		private int n;
		private FenwickTree ft1;
		private FenwickTree ft2;

		public RangeFenwickTree(int n) {
			this.n = n;
			ft1 = new FenwickTree(n+1);
			ft2 = new FenwickTree(n+1);
		}

		public RangeFenwickTree(int[] arr) {
			n = arr.length;
			ft1 = new FenwickTree(n+1);
			ft2 = new FenwickTree(n+1);
			for(int i = 0; i<n; i++) {
				add(i, i, arr[i]);
			}
		}

		public RangeFenwickTree(long[] arr) {
			n = arr.length;
			ft1 = new FenwickTree(n+1);
			ft2 = new FenwickTree(n+1);
			for(int i = 0; i<n; i++) {
				add(i, i, arr[i]);
			}
		}

		public void add(int l, int r, long val) {
			ft1.add(l, val);
			ft1.add(r+1, -val);
			ft2.add(l, val*l);
			ft2.add(r+1, -val*(r+1));
		}

		public long psum(int ind) {
			return ft1.psum(ind)*ind-ft2.psum(ind);
		}

		public long sum(int l, int r) {
			return psum(r+1)-psum(l);
		}

		public String toString() {
			StringBuilder ret = new StringBuilder(n<<2);
			ret.append("{");
			for(int i = 0; i<n; i++) {
				if(i!=0) {
					ret.append(", ");
				}
				ret.append(sum(i, i));
			}
			return ret.append("}").toString();
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
}

