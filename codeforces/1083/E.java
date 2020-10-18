import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
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
			ETheFairNutAndRectangles solver = new ETheFairNutAndRectangles();
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

	static class ETheFairNutAndRectangles {
		ArrayDeque<ETheFairNutAndRectangles.Line> lines = new ArrayDeque<>();

		public ETheFairNutAndRectangles() {
		}

		public void addLine(long x, long y) {
			ETheFairNutAndRectangles.Line l = new ETheFairNutAndRectangles.Line(x, y);
			if(lines.size()<=1) {
				lines.addLast(l);
			}else {
				ETheFairNutAndRectangles.Line cur = lines.removeLast(), next = lines.removeLast();
				while(next!=null&&cur.xinter(next)>=l.xinter(next)) {
					cur = next;
					next = lines.pollLast();
				}
				if(next!=null) {
					lines.addLast(next);
				}
				lines.addLast(cur);
				lines.addLast(l);
			}
		}

		public long query(long x) {
			if(lines.size()<=1) {
				return lines.getFirst().eval(x);
			}else {
				ETheFairNutAndRectangles.Line cur = lines.removeFirst(), next = lines.removeFirst();
				while(next!=null&&cur.eval(x)<next.eval(x)) {
					cur = next;
					next = lines.pollFirst();
				}
				if(next!=null) {
					lines.addFirst(next);
				}
				lines.addFirst(cur);
				return cur.eval(x);
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			long[][] arr = in.nextLong(n, 3);
			Arrays.sort(arr, Comparator.comparingLong(o -> o[0]));
			addLine(0, 0);
			long[] dp = new long[n];
			long ans = 0;
			for(int i = n-1; i>=0; i--) {
				ans = Math.max(ans, dp[i] = arr[i][0]*arr[i][1]-arr[i][2]+query(-arr[i][0]));
				addLine(arr[i][1], dp[i]);
			}
			pw.println(ans);
////			Utilities.Debug.dbg(dp);
		}

		static class Line {
			long m;
			long b;

			public Line(long m, long b) {
				this.m = m;
				this.b = b;
			}

			public double xinter(ETheFairNutAndRectangles.Line l) {
				return ((double) b-l.b)/(l.m-m);
			}

			public long eval(long x) {
				return m*x+b;
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

		public long nextLong() {
			long ret = 0;
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

	static interface InputReader {
		int nextInt();

		long nextLong();

		default long[] nextLong(int n) {
			long[] ret = new long[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextLong();
			}
			return ret;
		}

		default long[][] nextLong(int n, int m) {
			long[][] ret = new long[n][m];
			for(int i = 0; i<n; i++) {
				ret[i] = nextLong(m);
			}
			return ret;
		}

	}
}


