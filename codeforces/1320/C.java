import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			CWorldOfDarkraftBattleForAzathoth solver = new CWorldOfDarkraftBattleForAzathoth();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class CWorldOfDarkraftBattleForAzathoth {
		private final int iinf = 1_000_000_000;
		private final long linf = 4_000_000_000_000_000_000L;

		public CWorldOfDarkraftBattleForAzathoth() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt(), p = in.nextInt(), mn = (int) 1e6;
			SegmentTree st = new SegmentTree(mn+1);
			int[][] arr = in.nextInt(n, 2);
			{
				int[][] tmp = in.nextInt(m, 2);
				Arrays.sort(tmp, Comparator.comparingInt(o -> -o[0]));
				int min = iinf;
				st.add(tmp[0][0], mn, -linf);
				for(int i = 0; i<m; i++) {
					st.add(i==m-1 ? 0 : tmp[i+1][0], tmp[i][0]-1, -(min = Math.min(min, tmp[i][1])));
				}
			}
			Arrays.sort(arr, Comparator.comparingInt(o -> o[0]));
			int[][] mon = in.nextInt(p, 3);
			Arrays.sort(mon, Comparator.comparingInt(o -> o[0]));
			long ans = -linf;
			int j = 0;
			for(int i = 0; i<n; i++) {
				while(j<p&&mon[j][0]<arr[i][0]) {
					st.add(mon[j][1], mn, mon[j++][2]);
				}
				ans = Math.max(ans, st.max(0, mn)-arr[i][1]);
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

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

		default int[][] nextInt(int n, int m) {
			int[][] ret = new int[n][m];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt(m);
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
			arr = new long[n];
			this.n = n;
			sumv = new long[n<<2];
			minv = new long[n<<2];
			maxv = new long[n<<2];
			addv = new long[n<<2];
			setv = new long[n<<2];
			setc = new boolean[n<<2];
			build0(1, 0, n-1);
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
			build(1, 0, n-1);
		}

		private void build0(int o, int l, int r) {
			if(l==r) {
				setc[o] = true;
			}else {
				int m = l+r >> 1;
				build0(o<<1, l, m);
				build0(o<<1|1, m+1, r);
			}
		}

		private void build(int o, int l, int r) {
			if(l==r) {
				setc[o] = true;
				setv[o] = minv[o] = maxv[o] = sumv[o] = arr[l];
			}else {
				int lc = o<<1, rc = o<<1|1;
				int m = l+r >> 1;
				build(lc, l, m);
				build(rc, m+1, r);
				sumv[o] = sumv[lc]+sumv[rc];
				minv[o] = Math.min(minv[lc], minv[rc]);
				maxv[o] = Math.max(maxv[lc], maxv[rc]);
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
				int m = (l+r) >> 1;
				if(y1<=m) {
					query(o<<1, l, m, add+addv[o]);
				}
				if(y2>m) {
					query(o<<1|1, m+1, r, add+addv[o]);
				}
			}
		}

		public void update(int o, int l, int r) {
			int lc = o<<1, rc = o<<1|1;
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
				int m = (l+r) >> 1;
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
			int lc = o<<1, rc = o<<1|1;
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
			int lc = o<<1, rc = o<<1|1;
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

		public void query(int l, int r) {
			y1 = l;
			y2 = r;
			_sum = 0;
			_max = Long.MIN_VALUE;
			_min = Long.MAX_VALUE;
			query(1, 0, n-1, 0);
		}

		public long max(int l, int r) {
			query(l, r);
			return _max;
		}

	}
}

