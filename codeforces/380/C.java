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
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			CSerejaAndBrackets solver = new CSerejaAndBrackets();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}
	static class CSerejaAndBrackets {
		public CSerejaAndBrackets() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			CSerejaAndBrackets.SegmentTree st = new CSerejaAndBrackets.SegmentTree(in.next(1000005));
			int m = in.nextInt();
			for(int i = 0; i<m; i++) {
				pw.println(st.query(in.nextInt()-1, in.nextInt()-1));
			}
		}

		static class Value {
			int t;
			int s;
			int e;

			public Value(int t, int s, int e) {
				this.t = t;
				this.s = s;
				this.e = e;
			}

		}

		static class SegmentTree {
			CSerejaAndBrackets.Value[] v;
			int[] arr;
			CSerejaAndBrackets.Value q;
			int n;
			int ql;
			int qr;

			public SegmentTree(String s) {
				n = s.length();
				arr = new int[n];
				v = new CSerejaAndBrackets.Value[n<<2];
				for(int i = 0; i<n; i++) {
					if(s.charAt(i)=='(') {
						arr[i] = 1;
					}else {
						arr[i] = -1;
					}
				}
				build(1, 0, n-1);
			}

			private CSerejaAndBrackets.Value merge(CSerejaAndBrackets.Value l, CSerejaAndBrackets.Value r) {
				if(l==null) {
					return r;
				}else if(r==null) {
					return l;
				}
				int min = Math.min(l.s, r.e);
				return new CSerejaAndBrackets.Value(l.t+r.t+(min<<1), l.s+r.s-min, l.e+r.e-min);
			}

			private void build(int o, int l, int r) {
				if(l==r) {
					if(arr[l]==-1) {
						v[o] = new CSerejaAndBrackets.Value(0, 0, 1);
					}else {
						v[o] = new CSerejaAndBrackets.Value(0, 1, 0);
					}
				}else {
					int mid = l+r >> 1;
					build(o<<1, l, mid);
					build((o<<1)+1, mid+1, r);
					v[o] = merge(v[o<<1], v[(o<<1)+1]);
				}
			}

			private void query(int o, int l, int r) {
				if(ql<=l&&qr>=r) {
					q = v[o];
					return;
				}
				int mid = l+r >> 1;
				CSerejaAndBrackets.Value a = null, b = null;
				if(ql<=mid) {
					query(o<<1, l, mid);
					a = q;
				}
				if(qr>mid) {
					query((o<<1)+1, mid+1, r);
					b = q;
				}
				q = merge(a, b);
			}

			public int query(int l, int r) {
				ql = l;
				qr = r;
				query(1, 0, n-1);
				return q.t;
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

		public void println(int i) {
			println(String.valueOf(i));
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

	static interface InputReader {
		String next();

		int nextInt();

		default String next(int maxLength) {
			return next();
		}

	}
}


