import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
			ZFrog3 solver = new ZFrog3();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}

	static class ZFrog3 {
		public ZFrog3() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			long c = in.nextLong();
			LiChaoTree lct = new LiChaoTree((int) 1e6, false);
			long[] arr = in.nextLong(n);
			long[] dp = new long[n];
			for(int i = n-2; i>=0; i--) {
				lct.add(-arr[i+1]<<1, arr[i+1]*arr[i+1]+dp[i+1]);
				dp[i] = c+arr[i]*arr[i]+lct.query((int) arr[i]);
			}
			pw.println(dp[0]);
		}

	}

	static interface InputReader {
		int nextInt();

		long nextLong();

		default long[] nextLong(int n) {
			long[] ret = new long[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextLong();
			}
			return ret;
		}

	}

	static class LiChaoTree {
		private int n;
		private LiChaoTree.Line[] nodes;
		private LiChaoTree.Line toAdd;
		private boolean max;

		public LiChaoTree(int n, boolean max) {
			this.n = n+1;
			nodes = new LiChaoTree.Line[n<<2];
			this.max = max;
			Arrays.fill(nodes, new LiChaoTree.Line(0, max ? Long.MIN_VALUE : Long.MAX_VALUE));
		}

		private void add(int o, int l, int r) {
			int mid = l+r >> 1;
			boolean left, cur;
			if(max) {
				left = toAdd.eval(l)>nodes[o].eval(l);
				cur = toAdd.eval(mid)>nodes[o].eval(mid);
			}else {
				left = toAdd.eval(l)<nodes[o].eval(l);
				cur = toAdd.eval(mid)<nodes[o].eval(mid);
			}
			if(cur) {
				LiChaoTree.Line tmp = nodes[o];
				nodes[o] = toAdd;
				toAdd = tmp;
			}
			if(l!=r) {
				if(left==cur) {
					add((o<<1)|1, mid+1, r);
				}else {
					add(o<<1, l, mid);
				}
			}
		}

		private long query(int o, int l, int r, int x) {
			long ret = nodes[o].eval(x);
			int mid = l+r >> 1;
			if(l!=r) {
				long query;
				if(x<=mid) {
					query = query(o<<1, l, mid, x);
				}else {
					query = query((o<<1)|1, mid+1, r, x);
				}
				if(max) {
					ret = Math.max(ret, query);
				}else {
					ret = Math.min(ret, query);
				}
			}
			return ret;
		}

		public void add(long m, long b) {
			toAdd = new LiChaoTree.Line(m, b);
			add(1, 0, n);
		}

		public long query(int x) {
			return query(1, 0, n, x);
		}

		static class Line {
			long m;
			long b;

			public Line(long m, long b) {
				this.m = m;
				this.b = b;
			}

			public long eval(long x) {
				return m*x+b;
			}

			public String toString() {
				return "y = "+m+"x + "+b;
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
}