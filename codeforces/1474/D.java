import java.io.*;
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
			DCleaning solver = new DCleaning();
			int testCount = in.nextInt();
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<26);
		thread.start();
		thread.join();
	}

	static class DCleaning {
		private final int iinf = 1_000_000_000;

		public DCleaning() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n);
			long min = 0, sum = 0;
			long[] psum = new long[n];
			sum += psum[0] = arr[0];
			for(int i = 1; i<n; i++) {
				min = Math.min(min, psum[i] = arr[i]-psum[i-1]);
				sum += arr[i];
			}
			if(min>=0&&psum[n-1]==0) {
				pw.println("YES");
				return;
			}else if(Utilities.odd(sum)) {
				pw.println("NO");
				return;
			}
			long[] odda = new long[n], evena = new long[n];
			for(int i = 0; i<n; i++) {
				if(Utilities.odd(i)) {
					odda[i] = psum[i];
					evena[i] = 3L*iinf;
				}else {
					evena[i] = psum[i];
					odda[i] = 3L*iinf;
				}
			}
			LazySegmentTree odd = new LazySegmentTree(odda), even = new LazySegmentTree(evena);
			for(int i = 0; i<n-1; i++) {
				if(Utilities.odd(i)) {
					odd.add(i, n-1, -arr[i]+arr[i+1]);
					even.add(i, n-1, arr[i]-arr[i+1]);
					even.add(i, n-1, arr[i]-arr[i+1]);
					odd.add(i+1, n-1, -arr[i]+arr[i+1]);
				}else {
					even.add(i, n-1, -arr[i]+arr[i+1]);
					odd.add(i, n-1, arr[i]-arr[i+1]);
					odd.add(i, n-1, arr[i]-arr[i+1]);
					even.add(i+1, n-1, -arr[i]+arr[i+1]);
				}
				if(Math.min(odd.min(0, n-1), even.min(0, n-1))>=0&&(Utilities.odd(n-1) ? odd.sum(n-1, n-1)==0 : even.sum(n-1, n-1)==0)) {
					pw.println("YES");
					return;
				}
				if(Utilities.odd(i)) {
					odd.add(i, n-1, -(-arr[i]+arr[i+1]));
					even.add(i, n-1, -(arr[i]-arr[i+1]));
					even.add(i, n-1, -(arr[i]-arr[i+1]));
					odd.add(i+1, n-1, -(-arr[i]+arr[i+1]));
				}else {
					even.add(i, n-1, -(-arr[i]+arr[i+1]));
					odd.add(i, n-1, -(arr[i]-arr[i+1]));
					odd.add(i, n-1, -(arr[i]-arr[i+1]));
					even.add(i+1, n-1, -(-arr[i]+arr[i+1]));
				}
			}
			pw.println("NO");
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
		private final DataInputStream din;
		private final byte[] buffer;
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

	static class LazySegmentTree {
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

		public LazySegmentTree(int n) {
			arr = new long[n];
			this.n = n;
			sumv = new long[n*4];
			minv = new long[n*4];
			maxv = new long[n*4];
			addv = new long[n*4];
			setv = new long[n*4];
			setc = new boolean[n*4];
			build0(1, 0, n-1);
		}

		public LazySegmentTree(long[] arr) {
			this.arr = arr.clone();
			n = arr.length;
			sumv = new long[n*4];
			minv = new long[n*4];
			maxv = new long[n*4];
			addv = new long[n*4];
			setv = new long[n*4];
			setc = new boolean[n*4];
			build(1, 0, n-1);
		}

		public void build0(int o, int l, int r) {
			if(l==r) {
				setc[o] = true;
			}else {
				int m = (l+r)/2;
				build0(o*2, l, m);
				build0(o*2+1, m+1, r);
			}
		}

		public void build(int o, int l, int r) {
			if(l==r) {
				setc[o] = true;
				setv[o] = minv[o] = maxv[o] = sumv[o] = arr[l];
			}else {
				int lc = o*2, rc = o*2+1;
				int m = (l+r)/2;
				build(lc, l, m);
				build(rc, m+1, r);
				sumv[o] = sumv[lc]+sumv[rc];
				minv[o] = Math.min(minv[lc], minv[rc]);
				maxv[o] = Math.max(maxv[lc], maxv[rc]);
			}
		}

		public void maintain(int o, int l, int r) {
			int lc = o*2, rc = o*2+1;
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

		public void pushdown(int o) {
			int lc = o*2, rc = o*2+1;
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
					query(o*2, l, m, add+addv[o]);
				}
				if(y2>m) {
					query(o*2+1, m+1, r, add+addv[o]);
				}
			}
		}

		public void update(int o, int l, int r) {
			int lc = o*2, rc = o*2+1;
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

		public void add(int l, int r, long v) {
			y1 = l;
			y2 = r;
			this.v = v;
			add = true;
			update(1, 0, n-1);
		}

		public void query(int l, int r) {
			y1 = l;
			y2 = r;
			_sum = 0;
			_max = Long.MIN_VALUE;
			_min = Long.MAX_VALUE;
			query(1, 0, n-1, 0);
		}

		public long sum(int l, int r) {
			query(l, r);
			return _sum;
		}

		public long min(int l, int r) {
			query(l, r);
			return _min;
		}

		public String toString() {
			StringBuilder ret = new StringBuilder(n*4);
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
		public static boolean odd(int x) {
			return (x&1)>0;
		}

		public static boolean odd(long x) {
			return (x&1)>0;
		}

	}

	interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

	}
}

