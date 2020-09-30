import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

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
			DSearchlights solver = new DSearchlights();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class DSearchlights {
		private final int iinf = 1_000_000_000;

		public DSearchlights() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt(), mn = (int) 1e6;
			int[] ind, height;
			int[][] arr = in.nextInt(n, 2);
			{
				int[] aind = new int[m+5], aheight = new int[m+5];
				int[][] tmp = in.nextInt(m, 2);
				Arrays.sort(tmp, (o1, o2) -> o1[0]==o2[0] ? o1[1]-o2[1] : o1[0]-o2[0]);
				int prev = -1, a = 0, b = 0;
				aind[a++] = mn<<2;
				aheight[b++] = -1;
				for(int i = m-1; i>=0; i--) {
					if(tmp[i][1]>prev) {
						aind[a++] = tmp[i][0];
						aheight[b++] = prev = tmp[i][1];
					}
				}
				aind[a++] = -1;
				Utilities.reverse(aind, 0, a-1);
				Utilities.reverse(aheight, 0, b-1);
				ind = new int[a];
				height = new int[b];
				System.arraycopy(aind, 0, ind, 0, a);
				System.arraycopy(aheight, 0, height, 0, b);
			}
			int[] cur = new int[n];
			Integer[] up = new Integer[n];
			PriorityQueue<DSearchlights.Pair> pq = new PriorityQueue<>(n);
			int ans = iinf;
			for(int i = 0; i<n; i++) {
				cur[i] = Utilities.lowerBound(ind, arr[i][0])-1;
				up[i] = Math.max(0, height[cur[i]]-arr[i][1]+1);
				pq.add(new DSearchlights.Pair(ind[cur[i]+1]-arr[i][0]+1, i));
			}
			TPointSegmentTree<Integer> st = new TPointSegmentTree<>(up, Integer::max);
			for(int i = 0; i<mn; i++) {
				while(pq.peek().a==i) {
					var v = pq.poll();
					cur[v.b]++;
					st.set(v.b, Math.max(0, height[cur[v.b]]-arr[v.b][1]+1));
					pq.add(new DSearchlights.Pair(ind[cur[v.b]+1]-arr[v.b][0]+1, v.b));
				}
				ans = Math.min(ans, i+st.query(0, n-1));
			}
			pw.println(ans);
		}

		static class Pair implements Comparable<DSearchlights.Pair> {
			int a;
			int b;

			public Pair(int a, int b) {
				this.a = a;
				this.b = b;
			}

			public int compareTo(DSearchlights.Pair p) {
				return a-p.a;
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

	static class TPointSegmentTree<T> {
		public int n;
		public int ind;
		public int ql;
		public int qr;
		public T[] arr;
		public T[] value;
		public BiFunction<T, T, T> operation;

		public TPointSegmentTree(T[] arr, BiFunction<T, T, T> operation) {
			n = arr.length;
			this.arr = arr;
			value = (T[]) new Object[n<<2];
			this.operation = operation;
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
			value[o] = operation.apply(value[lc], value[rc]);
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
			value[o] = operation.apply(value[lc], value[rc]);
		}

		public void set(int ind, T val) {
			this.ind = ind;
			arr[ind] = val;
			set(1, 0, n-1);
		}

		private T query(int o, int l, int r) {
			if(ql<=l&&qr>=r) {
				return value[o];
			}
			int lc = o<<1, rc = lc|1, mid = l+r >> 1;
			T ret = null;
			if(ql<=mid) {
				ret = query(lc, l, mid);
			}
			if(qr>mid) {
				if(ret==null) {
					ret = query(rc, mid+1, r);
				}else {
					ret = operation.apply(ret, query(rc, mid+1, r));
				}
			}
			return ret;
		}

		public T query(int l, int r) {
			ql = l;
			qr = r;
			return query(1, 0, n-1);
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

	static class Utilities {
		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

		public static void reverse(int[] arr, int i, int j) {
			while(i<j) {
				swap(arr, i++, j--);
			}
		}

		public static int lowerBound(int[] arr, int target) {
			int l = 0, h = arr.length;
			while(l<h) {
				int mid = l+h >> 1;
				if(arr[mid]<target) {
					l = mid+1;
				}else {
					h = mid;
				}
			}
			return l;
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
}





