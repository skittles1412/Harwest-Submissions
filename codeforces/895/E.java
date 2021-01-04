import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InputMismatchException;
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
			EEyesClosed solver = new EEyesClosed();
			solver.solve(1, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<26);
		thread.start();
		thread.join();
	}

	static class EEyesClosed {
		public EEyesClosed() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), q = in.nextInt();
			Double[] arr = new Double[n];
			for(int i = 0; i<n; i++) {
				arr[i] = (double) in.nextInt();
			}
			TLazySegmentTree<Double, EEyesClosed.Lazy> st = new TLazySegmentTree<>(arr, Double::sum, EEyesClosed.Lazy::combine, EEyesClosed.Lazy::applyToSegment);
//		SegmentTree st = new SegmentTree(in.nextDouble(n));
			while(q-->0) {
				if(in.nextInt()==1) {
					int a = in.nextInt()-1, b = in.nextInt()-1, c = in.nextInt()-1, d = in.nextInt()-1;
					double x = st.query(a, b), y = st.query(c, d), al = b-a+1, bl = d-c+1;
					st.update(a, b, new EEyesClosed.Lazy((al-1)/al, y/bl/al));
					st.update(c, d, new EEyesClosed.Lazy((bl-1)/bl, x/al/bl));
//				st.mul(a, b, (al-1)/al);
//				st.mul(c, d, (bl-1)/bl);
//				st.add(a, b, y/bl/al);
//				st.add(c, d, x/al/bl);
				}else {
					pw.printf("%.4f\n", st.query(in.nextInt()-1, in.nextInt()-1));
				}
			}
		}

		static class Lazy {
			double mul;
			double add;

			public Lazy(double mul, double add) {
				this.mul = mul;
				this.add = add;
			}

			public EEyesClosed.Lazy combine(EEyesClosed.Lazy l) {
				return new EEyesClosed.Lazy(mul*l.mul, add*l.mul+l.add);
			}

			public double applyToSegment(double d, int length) {
				return d*mul+add*length;
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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void printf(String s, Object... o) {
			print(String.format(s, o));
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

	static class TLazySegmentTree<T, U> {
		public int ql;
		public int qr;
		public T ans;
		public U clazy;
		public T[] values;
		public U[] lazy;
		public final int n;
		public final T[] arr;
		public final BinaryOperator<T> operation;
		public final BinaryOperator<U> lazyCombiner;
		public final TLazySegmentTree.LazyOperation<T, U> lazyOperation;

		public TLazySegmentTree(T[] arr, BinaryOperator<T> operation, BinaryOperator<U> lazyCombiner, TLazySegmentTree.LazyOperation<T, U> lazyOperation) {
			n = arr.length;
			this.arr = arr;
			this.operation = operation;
			this.lazyCombiner = lazyCombiner;
			this.lazyOperation = lazyOperation;
			values = (T[]) new Object[n*4];
			lazy = (U[]) new Object[n*4];
			build(1, 0, n-1);
		}

		public U combine(U lazy1, U lazy2) {
			if(lazy1==null) {
				return lazy2;
			}else if(lazy2==null) {
				return lazy1;
			}
			return lazyCombiner.apply(lazy1, lazy2);
		}

		public T applyToSegment(U lazy, T value, int length) {
			if(lazy==null) {
				return value;
			}
			return lazyOperation.applyToSegment(lazy, value, length);
		}

		public void maintain(int o, int l, int r) {
			if(l<r) {
				values[o] = operation.apply(values[o*2], values[o*2+1]);
			}else {
				values[o] = arr[l];
			}
			values[o] = applyToSegment(lazy[o], values[o], r-l+1);
		}

		public void pushdown(int o) {
			lazy[o*2] = combine(lazy[o*2], lazy[o]);
			lazy[o*2+1] = combine(lazy[o*2+1], lazy[o]);
			lazy[o] = null;
		}

		public void build(int o, int l, int r) {
			if(l==r) {
				values[o] = arr[l];
			}else {
				int mid = (l+r)/2, lc = o*2, rc = o*2+1;
				build(lc, l, mid);
				build(rc, mid+1, r);
				values[o] = operation.apply(values[lc], values[rc]);
			}
		}

		public void update(int o, int l, int r) {
			if(l>=ql&&r<=qr) {
				lazy[o] = combine(lazy[o], clazy);
			}else {
				pushdown(o);
				int mid = (l+r)/2, lc = o*2, rc = o*2+1;
				if(ql<=mid) {
					update(lc, l, mid);
				}else {
					maintain(lc, l, mid);
				}
				if(qr>mid) {
					update(rc, mid+1, r);
				}else {
					maintain(rc, mid+1, r);
				}
			}
			maintain(o, l, r);
		}

		public void query(int o, int l, int r, U clazy) {
			if(l>=ql&&r<=qr) {
				if(ans==null) {
					ans = applyToSegment(clazy, values[o], r-l+1);
				}else {
					ans = operation.apply(ans, applyToSegment(clazy, values[o], r-l+1));
				}
			}else {
				clazy = combine(lazy[o], clazy);
				int mid = (l+r)/2, lc = o*2, rc = o*2+1;
				if(ql<=mid) {
					query(lc, l, mid, clazy);
				}
				if(qr>mid) {
					query(rc, mid+1, r, clazy);
				}
			}
		}

		public void update(int l, int r, U lazy) {
			ql = l;
			qr = r;
			clazy = lazy;
			update(1, 0, n-1);
		}

		public T query(int l, int r) {
			ql = l;
			qr = r;
			ans = null;
			query(1, 0, n-1, null);
			return ans;
		}

		public interface LazyOperation<T, U> {
			T applyToSegment(U lazy, T value, int length);

		}

	}

	static interface InputReader {
		int nextInt();

	}
}

