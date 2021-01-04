import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
//		Double[] arr = new Double[n];
//		for(int i = 0; i<n; i++) {
//			arr[i] = (double) in.nextInt();
//		}
//		TLazySegmentTree<Double, Lazy> st = new TLazySegmentTree<>(arr, Double::sum, Lazy::combine, Lazy::applyToSegment);
			EEyesClosed.SegmentTree st = new EEyesClosed.SegmentTree(in.nextDouble(n));
			while(q-->0) {
				if(in.nextInt()==1) {
					int a = in.nextInt()-1, b = in.nextInt()-1, c = in.nextInt()-1, d = in.nextInt()-1;
					double x = st.query(a, b), y = st.query(c, d), al = b-a+1, bl = d-c+1;
//				st.update(a, b, new Lazy((al-1)/al, y/bl/al));
//				st.update(c, d, new Lazy((bl-1)/bl, x/al/bl));
					st.mul(a, b, (al-1)/al);
					st.mul(c, d, (bl-1)/bl);
					st.add(a, b, y/bl/al);
					st.add(c, d, x/al/bl);
				}else {
					pw.printf("%.4f\n", st.query(in.nextInt()-1, in.nextInt()-1));
				}
			}
		}

		static class SegmentTree {
			int n;
			int ql;
			int qr;
			double v;
			boolean qadd;
			double[] arr;
			double[] add;
			double[] mul;
			double[] sum;

			public SegmentTree(double[] arr) {
				n = arr.length;
				this.arr = arr;
				add = new double[4*n];
				mul = new double[4*n];
				sum = new double[4*n];
				build(1, 0, n-1);
			}

			public void maintain(int o, int l, int r) {
				if(l<r) {
					sum[o] = sum[o*2]+sum[o*2+1];
				}else {
					sum[o] = arr[l];
				}
				sum[o] = sum[o]*mul[o]+(r-l+1)*add[o];
			}

			public void pushdown(int o, int l, int r) {
				if(l<r) {
					for(int i = o*2; i<=o*2+1; i++) {
						mul[i] *= mul[o];
						add[i] *= mul[o];
						add[i] += add[o];
					}
					mul[o] = 1;
					add[o] = 0;
				}
			}

			public void build(int o, int l, int r) {
				mul[o] = 1;
				if(l==r) {
					sum[o] = arr[l];
				}else {
					int mid = (l+r)/2, lc = o*2, rc = o*2+1;
					build(lc, l, mid);
					build(rc, mid+1, r);
					maintain(o, l, r);
				}
			}

			public void update(int o, int l, int r) {
				if(l>=ql&&r<=qr) {
					if(qadd) {
						add[o] += v;
					}else {
						mul[o] *= v;
						add[o] *= v;
					}
				}else {
					pushdown(o, l, r);
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

			public void query(int o, int l, int r, double cadd, double cmul) {
				if(l>=ql&&r<=qr) {
					v += sum[o]*cmul+(r-l+1)*cadd;
				}else {
					cadd += cmul*add[o];
					cmul *= mul[o];
					int mid = (l+r)/2;
					if(ql<=mid) {
						query(o*2, l, mid, cadd, cmul);
					}
					if(qr>mid) {
						query(o*2+1, mid+1, r, cadd, cmul);
					}
					maintain(o, l, r);
				}
			}

			public void add(int l, int r, double v) {
				ql = l;
				qr = r;
				this.v = v;
				qadd = true;
				update(1, 0, n-1);
			}

			public void mul(int l, int r, double v) {
				ql = l;
				qr = r;
				this.v = v;
				qadd = false;
				update(1, 0, n-1);
			}

			public double query(int l, int r) {
				ql = l;
				qr = r;
				v = 0;
				query(1, 0, n-1, 0, 1);
				return v;
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

		public double nextDouble() {
			double ret = 0, div = 1;
			byte c = skipToDigit();
			boolean neg = (c=='-');
			if(neg) {
				c = read();
			}
			do {
				ret = ret*10+c-'0';
			} while((c = read())>='0'&&c<='9');
			if(c=='.') {
				while((c = read())>='0'&&c<='9') {
					ret += (c-'0')/(div *= 10);
				}
			}
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

		double nextDouble();

		default double[] nextDouble(int n) {
			double[] ret = new double[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextDouble();
			}
			return ret;
		}

	}
}

