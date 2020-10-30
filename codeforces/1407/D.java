import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

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
			DDiscreteCentrifugalJumps solver = new DDiscreteCentrifugalJumps();
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

	static class DDiscreteCentrifugalJumps {
		public DDiscreteCentrifugalJumps() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n);
			{
				int[] tmp = arr.clone();
				Utilities.sort(tmp);
				HashMap<Integer, Integer> hm = new HashMap<>(n*3);
				int ind = 0;
				hm.put(tmp[0], ind++);
				for(int i = 1; i<n; i++) {
					if(tmp[i]!=tmp[i-1]) {
						hm.put(tmp[i], ind++);
					}
				}
				for(int i = 0; i<n; i++) {
					arr[i] = hm.get(arr[i]);
				}
			}
			TPointSegmentTree<Integer> minst, maxst, last;
			{
				Integer[] tmp = new Integer[n];
				Arrays.fill(tmp, n-1);
				minst = new TPointSegmentTree<>(tmp, Integer::min);
				maxst = new TPointSegmentTree<>(tmp, Integer::min);
				last = new TPointSegmentTree<>(tmp, Integer::min);
			}
			TreeSet<Pair<Integer, Integer>> minv = new TreeSet<>(), maxv = new TreeSet<>();
			int[] dp = new int[n];
			dp[n-1] = 0;
			for(int i = n-1; i>=0; i--) {
				if(i<n-1) {
					int mine = last.query(arr[i], n-1), maxe = last.query(0, arr[i]);
					dp[i] = 1+Math.min(minst.query(i, mine), maxst.query(i, maxe));
					minv.add(new Pair<>(arr[i+1], i+1));
					maxv.add(new Pair<>(arr[i+1], i+1));
				}
				last.set(arr[i], i);
				minst.set(i, dp[i]);
				maxst.set(i, dp[i]);
				while(!minv.isEmpty()&&minv.first().a<=arr[i]) {
					minst.set(minv.pollFirst().b, n-1);
				}
				while(!maxv.isEmpty()&&maxv.last().a>=arr[i]) {
					maxst.set(maxv.pollLast().b, n-1);
				}
			}
			pw.println(dp[0]);
		}

	}

	static class TPointSegmentTree<T> {
		public int n;
		public int ind;
		public int ql;
		public int qr;
		public T[] arr;
		public T[] value;
		public BinaryOperator<T> operation;

		public TPointSegmentTree(T[] arr, BinaryOperator<T> operation) {
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

	static class Utilities {
		public static void sort(int[] arr) {
			Random rand = new Random();
			int n = arr.length;
			for(int i = 0; i<n; i++) {
				swap(arr, i, rand.nextInt(n));
			}
			Arrays.sort(arr);
		}

		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
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

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

	}
}

