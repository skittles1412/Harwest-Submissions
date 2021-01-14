import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
			DProgram solver = new DProgram();
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

	static class DProgram {
		public DProgram() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			String s = in.next(n);
			DProgram.Node[] arr = new DProgram.Node[n];
			for(int i = 0; i<n; i++) {
				arr[i] = new DProgram.Node(s.charAt(i)=='+' ? 1 : -1);
			}
			TPointSegmentTree<DProgram.Node> st = new TPointSegmentTree<>(arr, DProgram.Node::merge);
			while(m-->0) {
				int l = in.nextInt()-1, r = in.nextInt()-1;
				if(l==0) {
					if(r==n-1) {
						pw.println("1");
					}else {
						pw.println(st.query(r+1, n).value());
					}
				}else if(r==n-1) {
					pw.println(st.query(0, l-1).value());
				}else {
					pw.println(st.query(0, l-1).merge(st.query(r+1, n)).value());
				}
			}
		}

		static class Node {
			int min;
			int max;
			int end;

			public Node(int x) {
				min = Math.min(0, x);
				max = Math.max(0, x);
				end = x;
			}

			public Node(int min, int max, int end) {
				this.min = min;
				this.max = max;
				this.end = end;
			}

			public DProgram.Node merge(DProgram.Node n) {
				return new DProgram.Node(Math.min(min, end+n.min), Math.max(max, end+n.max), end+n.end);
			}

			public int value() {
				return max-min+1;
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

	static class TPointSegmentTree<T> {
		public int n;
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

		public TPointSegmentTree(int n, T initialValue, BinaryOperator<T> operation) {
			this.n = n;
			arr = (T[]) new Object[n];
			value = (T[]) new Object[n<<2];
			Arrays.fill(arr, initialValue);
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
}

