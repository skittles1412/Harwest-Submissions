import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;

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
			BAromasSearch solver = new BAromasSearch();
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

	static class BAromasSearch {
		BigInteger sx;
		BigInteger sy;
		BigInteger ax;
		BigInteger ay;
		BigInteger bx;
		BigInteger by;
		BigInteger[] powx;
		BigInteger[] powy;
		int n;

		public BAromasSearch() {
		}

		private Pair<BigInteger, BigInteger> s(int ind) {
			BigInteger tmpx = powx[ind], tmpy = powy[ind];
			return new Pair<>(eval(tmpx, sx, bx, tmpx, ax), eval(tmpy, sy, by, tmpy, ay));
		}

		private BigInteger eval(BigInteger a, BigInteger b, BigInteger c, BigInteger d, BigInteger e) {
			return a.multiply(b).add(c.multiply((BigInteger.ONE.subtract(d)).divide(BigInteger.ONE.subtract(e))));
		}

		private BigInteger dist(Pair<BigInteger, BigInteger> a, BigInteger x, BigInteger y) {
			return a.a.subtract(x).abs().add(a.b.subtract(y).abs());
		}

		private int solve(BigInteger xs, BigInteger xy, BigInteger t, int ind) {
			if(t.longValue()<0) {
				return 0;
			}else if(t.longValue()==0) {
				return 1;
			}
			int ans = 0;
			for(int i = 0; i<=ind; i++) {
				BigInteger dist;
				dist = dist(s(i), xs, xy);
				if(dist.compareTo(t)>0) {
					continue;
				}
				ans = Math.max(ans, ind-i+1);
				BigInteger tt = t.subtract(dist.multiply(BigInteger.TEN));
				if(tt.compareTo(BigInteger.ZERO)>0) {
					int cnt = ind-i+1;
					for(int j = i+1; j<=n; j++) {
						if(dist(s(j), xs, xy).compareTo(tt)>0) {
							cnt += j-ind-1;
							break;
						}
						if(j==n) {
							cnt += n-ind-1;
						}
					}
					ans = Math.max(ans, cnt);
				}
			}
			for(int i = ind; i<=n; i++) {
				BigInteger dist;
				dist = dist(s(i), xs, xy);
				if(dist.compareTo(t)>0) {
					continue;
				}
				ans = Math.max(ans, i-ind+1);
				BigInteger tt = t.subtract(dist.multiply(BigInteger.TEN));
				if(tt.compareTo(BigInteger.ZERO)>0) {
					int cnt = i-ind+1;
					for(int j = ind-1; j>=0; j--) {
						if(dist(s(j), xs, xy).compareTo(tt)>0) {
							cnt += j-ind-1;
							break;
						}
						if(j==0) {
							cnt += ind-1;
						}
					}
					ans = Math.max(ans, cnt);
				}
			}
			return ans;
		}

		public void solve(int kase, InputReader in, Output pw) {
			sx = BigInteger.valueOf(in.nextLong());
			sy = BigInteger.valueOf(in.nextLong());
			ax = BigInteger.valueOf(in.nextLong());
			ay = BigInteger.valueOf(in.nextLong());
			bx = BigInteger.valueOf(in.nextLong());
			by = BigInteger.valueOf(in.nextLong());
			BigInteger xs = BigInteger.valueOf(in.nextLong()), ys = BigInteger.valueOf(in.nextLong()), t = BigInteger.valueOf(in.nextLong());
			n = 55;
			Utilities.Debug.dbg(n);
			powx = new BigInteger[n+1];
			powy = new BigInteger[n+1];
			powx[0] = powy[0] = BigInteger.ONE;
			for(int i = 1; i<=n; i++) {
				powx[i] = powx[i-1].multiply(ax);
				powy[i] = powy[i-1].multiply(ay);
			}
			int ans = 0;
			for(int i = 0; i<=n; i++) {
				var v = s(i);
				ans = Math.max(ans, solve(v.a, v.b, t.subtract(dist(v, xs, ys)), i));
				Utilities.Debug.dbg(i, ans);
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

	static class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
		public T1 a;
		public T2 b;

		public Pair(Pair<T1, T2> p) {
			this(p.a, p.b);
		}

		public Pair(T1 a, T2 b) {
			this.a = a;
			this.b = b;
		}

		public String toString() {
			return a+" "+b;
		}

		public int hashCode() {
			return Objects.hash(a, b);
		}

		public boolean equals(Object o) {
			if(o instanceof Pair) {
				Pair p = (Pair) o;
				return a.equals(p.a)&&b.equals(p.b);
			}
			return false;
		}

		public int compareTo(Pair<T1, T2> p) {
			int cmp = ((Comparable<T1>) a).compareTo(p.a);
			if(cmp==0) {
				return ((Comparable<T2>) b).compareTo(p.b);
			}
			return cmp;
		}

	}

	static interface InputReader {
		long nextLong();

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
}

