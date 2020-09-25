import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InputMismatchException;
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
			C2PokmonArmyHardVersion solver = new C2PokmonArmyHardVersion();
			int testCount = in.nextInt();
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class C2PokmonArmyHardVersion {
		TPointSegmentTree<C2PokmonArmyHardVersion.Node> st;
		int n;

		public C2PokmonArmyHardVersion() {
		}

		public long query() {
			var v = st.query(0, n-1);
			return Math.max(v.ab, v.bb);
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int q = in.nextInt();
			C2PokmonArmyHardVersion.Node[] arr = new C2PokmonArmyHardVersion.Node[n];
			for(int i = 0; i<n; i++) {
				arr[i] = new C2PokmonArmyHardVersion.Node(in.nextInt());
			}
			st = new TPointSegmentTree<>(arr, (x, y) ->
					new C2PokmonArmyHardVersion.Node(
							Math.min(x.aa-y.bb, x.ba+y.aa),
							Math.max(x.ab-y.ba, x.bb+y.ab),
							Math.min(x.aa-y.ab, x.ba+y.ba),
							Math.max(x.ab-y.aa, x.bb+y.bb)
					));
			pw.println(query());
			while(q-->0) {
				int u = in.nextInt()-1, v = in.nextInt()-1;
				var tmp = arr[u];
				st.set(u, arr[v]);
				st.set(v, tmp);
				pw.println(query());
			}
		}

		static class Node {
			long aa;
			long ab;
			long ba;
			long bb;

			public Node(long aa, long ab, long ba, long bb) {
				this.aa = aa;
				this.ab = ab;
				this.ba = ba;
				this.bb = bb;
			}

			public Node(long x) {
				this(x, x, 0, 0);
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

	static interface InputReader {
		int nextInt();

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
}


