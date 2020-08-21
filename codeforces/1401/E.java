import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.TreeSet;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.Closeable;
import java.io.InputStream;

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
			EDivideSquare solver = new EDivideSquare();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class EDivideSquare {
		public EDivideSquare() {
		}

		public void solve(int kase, FastReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt(), id = 0;
			EDivideSquare.Event[] events = new EDivideSquare.Event[(n<<1)+m+6];
			EDivideSquare.xseg[] xs = new EDivideSquare.xseg[n+2];
			EDivideSquare.yseg[] ys = new EDivideSquare.yseg[m+2];
			for(int i = 0; i<n; i++) {
				xs[i] = new EDivideSquare.xseg(in.nextInt(), in.nextInt(), in.nextInt());
			}
			for(int i = 0; i<m; i++) {
				ys[i] = new EDivideSquare.yseg(in.nextInt(), in.nextInt(), in.nextInt());
			}
			int mil = (int) 1e6;
			xs[n] = new EDivideSquare.xseg(0, 0, mil);
			xs[n+1] = new EDivideSquare.xseg(mil, 0, mil);
			ys[m] = new EDivideSquare.yseg(0, 0, mil);
			ys[m+1] = new EDivideSquare.yseg(mil, 0, mil);
			int ind = 0;
			for(EDivideSquare.xseg x: xs) {
				events[id++] = new EDivideSquare.Event(x.l, ind, true);
				events[id++] = new EDivideSquare.Event(x.r, ind++, false);
			}
			ind = 0;
			for(EDivideSquare.yseg y: ys) {
				events[id++] = new EDivideSquare.Event(y.x, ind++);
			}
			Arrays.sort(events);
			FenwickTree found = new FenwickTree(mil+5);
			TreeSet<EDivideSquare.xseg> left = new TreeSet<>();
			long ans = 0;
			Utilities.Debug.dbg(events);
			for(EDivideSquare.Event e: events) {
				if(e.xseg) {
					EDivideSquare.xseg cur = xs[e.id];
					if(e.start) {
						left.add(cur);
					}else {
						if(!left.remove(cur)) {
							found.set(cur.y, 0);
						}
					}
				}else {
					EDivideSquare.yseg cur = ys[e.id];
					ans += Math.max(found.sum(cur.l, cur.r)-1, 0);
					Utilities.Debug.dbg(ans);
					if(cur.l==0) {
						ArrayList<EDivideSquare.xseg> toRemove = new ArrayList<>();
						for(EDivideSquare.xseg x: left) {
							if(x.y>cur.r) {
								break;
							}
							found.set(x.y, 1);
							toRemove.add(x);
						}
						for(EDivideSquare.xseg x: toRemove) {
							left.remove(x);
						}
					}else {
						ArrayList<EDivideSquare.xseg> toRemove = new ArrayList<>();
						for(EDivideSquare.xseg x: left.descendingSet()) {
							if(x.y<cur.l) {
								break;
							}
							found.set(x.y, 1);
							toRemove.add(x);
						}
						for(EDivideSquare.xseg x: toRemove) {
							left.remove(x);
						}
					}
				}
			}
			pw.println(ans);
		}

		static class xseg implements Comparable<EDivideSquare.xseg> {
			int y;
			int l;
			int r;

			public xseg(int y, int l, int r) {
				this.y = y;
				this.l = l;
				this.r = r;
			}

			public int compareTo(EDivideSquare.xseg s) {
				return y-s.y;
			}

		}

		static class yseg {
			int x;
			int l;
			int r;

			public yseg(int x, int l, int r) {
				this.x = x;
				this.l = l;
				this.r = r;
			}

		}

		static class Event implements Comparable<EDivideSquare.Event> {
			int x;
			int id;
			int value;
			boolean start;
			boolean xseg;

			public Event(int x, int id, boolean start) {
				this.x = x;
				this.id = id;
				this.start = start;
				xseg = true;
				if(start) {
					value = 0;
				}else {
					value = 2;
				}
			}

			public Event(int x, int id) {
				this.x = x;
				this.id = id;
				start = xseg = false;
				value = 1;
			}

			public int compareTo(EDivideSquare.Event e) {
				if(x!=e.x) {
					return x-e.x;
				}
				return value-e.value;
			}

			public String toString() {
				return "Event{"+
						"x="+x+
						", id="+id+
						", start="+start+
						", xseg="+xseg+
						'}';
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

	}
}


