import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
			FVickysDeliveryService solver = new FVickysDeliveryService();
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

	static class FVickysDeliveryService {
		int n;
		int m;
		int c;
		int q;
		int id;
		FVickysDeliveryService.DSU dsu;
		HashMap<Integer, Integer>[] colors;

		public FVickysDeliveryService() {
		}

		public void add(int u, int v, int x) {
			dsu.union(colors[u].computeIfAbsent(x, (a) -> id++), v);
			dsu.union(colors[v].computeIfAbsent(x, (a) -> id++), u);
			dsu.addExtra(u, v);
			dsu.addExtra(v, u);
		}

		public boolean query(int u, int v) {
			return dsu.connected(u, v)||dsu.hasExtra(u, v);
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			m = in.nextInt();
			c = in.nextInt();
			q = in.nextInt();
			dsu = new FVickysDeliveryService.DSU(2*(n+m+c+q));//can't be bothered to find the exact number
			colors = new HashMap[n];
			for(int i = 0; i<n; i++) {
				colors[i] = new HashMap<>();
			}
			id = 2*n;
			for(int i = 0; i<m; i++) {
				add(in.nextInt()-1, in.nextInt()-1, in.nextInt());
			}
			while(q-->0) {
				if(in.next().equals("+")) {
					add(in.nextInt()-1, in.nextInt()-1, in.nextInt());
				}else {
					pw.println(query(in.nextInt()-1, in.nextInt()-1) ? "Yes" : "No");
				}
			}
		}

		static class DSU {
			int[] p;
			HashSet<Integer>[] e;

			public DSU(int n) {
				p = new int[n];
				e = new HashSet[n];
				for(int i = 0; i<n; i++) {
					p[i] = i;
				}
			}

			public int find(int u) {
				return p[u]==u ? u : (p[u] = find(p[u]));
			}

			public void union(int u, int v) {
//				Utilities.Debug.dbg("Union", u, v);
				int ur = find(u), vr = find(v);
				if(ur!=vr) {
					if(e[ur]!=null) {
						if(e[vr]!=null) {
							if(e[ur].size()>e[vr].size()) {
								int tmp = ur;
								ur = vr;
								vr = tmp;
							}
							e[vr].addAll(e[ur]);
						}else {
							e[vr] = e[ur];
						}
					}
					p[ur] = vr;
				}
			}

			public void addExtra(int u, int v) {
//				Utilities.Debug.dbg("Add", u, v);
				u = find(u);
				if(e[u]==null) {
					e[u] = new HashSet<>();
				}
				e[u].add(v);
			}

			public boolean connected(int u, int v) {
				return find(u)==find(v);
			}

			public boolean hasExtra(int u, int v) {
				return e[find(u)]!=null&&e[find(u)].contains(v);
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

	static interface InputReader {
		String next();

		int nextInt();

	}

	static class Utilities {
		public static class Debug {
			public static boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

			public static <T> String ts(T t) {
				if(t==null) {
					return "null";
				}
				try {
					return ts((Iterable) t);
				}catch(ClassCastException e) {
					if(t instanceof int[]) {
						String s = Arrays.toString((int[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof long[]) {
						String s = Arrays.toString((long[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof char[]) {
						String s = Arrays.toString((char[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof double[]) {
						String s = Arrays.toString((double[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof boolean[]) {
						String s = Arrays.toString((boolean[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}
					try {
						return ts((Object[]) t);
					}catch(ClassCastException e1) {
						return t.toString();
					}
				}
			}

			private static <T> String ts(T[] arr) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: arr) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			private static <T> String ts(Iterable<T> iter) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: iter) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			public static void dbg(Object... o) {
				if(LOCAL) {
					System.err.print("Line #"+Thread.currentThread().getStackTrace()[2].getLineNumber()+": [");
					for(int i = 0; i<o.length; i++) {
						if(i!=0) {
							System.err.print(", ");
						}
						System.err.print(ts(o[i]));
					}
					System.err.println("]");
				}
			}

		}

	}
}

