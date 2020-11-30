import java.io.*;
import java.util.ArrayList;
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
			long startTime = System.currentTimeMillis();
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			CKarenAndSupermarket solver = new CKarenAndSupermarket();
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

	static class CKarenAndSupermarket {
		private final long linf = 4_000_000_000_000_000_000L;
		int n;
		int[] size;
		long[] c;
		long[] d;
		long[][] f;
		long[][] g;
		ArrayList<Integer>[] graph;

		public CKarenAndSupermarket() {
		}

		public void dp(int u) {
			size[u] = 1;
			for(int v: graph[u]) {
				dp(v);
				size[u] += size[v];
			}
			int m = graph[u].size(), csize = 1;
			long[][] ftmp = new long[2][size[u]+1];
			long[][] gtmp = new long[2][size[u]+1];
			Arrays.fill(ftmp[0], linf);
			Arrays.fill(gtmp[0], linf);
			ftmp[0][1] = c[u]-d[u];
			gtmp[0][0] = 0;
			gtmp[0][1] = c[u];
			int cur = 1, prev = 0;
			for(int i = 0; i<m; i++, cur = prev, prev ^= 1) {
				int v = graph[u].get(i);
				Arrays.fill(ftmp[cur], linf);
				Arrays.fill(gtmp[cur], linf);
				for(int j = 0; j<=csize; j++) {
					for(int k = 0; k<=size[v]; k++) {
						ftmp[cur][j+k] = Math.min(ftmp[cur][j+k], ftmp[prev][j]+Math.min(f[v][k], g[v][k]));
						gtmp[cur][j+k] = Math.min(gtmp[cur][j+k], gtmp[prev][j]+g[v][k]);
					}
				}
				csize += size[v];
			}
			for(int i = 0; i<=csize; i++) {
				f[u][i] = ftmp[prev][i];
				g[u][i] = gtmp[prev][i];
			}
			f[u][0] = 0;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			long b = in.nextInt();
			c = new long[n];
			d = new long[n];
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 0; i<n; i++) {
				c[i] = in.nextInt();
				d[i] = in.nextInt();
				if(i>0) {
					graph[in.nextInt()-1].add(i);
				}
			}
			size = new int[n];
			f = new long[n][n+1];
			g = new long[n][n+1];
			for(int i = 0; i<n; i++) {
				Arrays.fill(f[i], linf);
				Arrays.fill(g[i], linf);
			}
			dp(0);
//			Utilities.Debug.dbg(f);
//			Utilities.Debug.dbg(g);
			for(int i = 0; i<=n; i++) {
				if(Math.min(f[0][i], g[0][i])>b) {
					pw.println(i-1);
					return;
				}
			}
			pw.println(n);
		}

	}

	interface InputReader {
		int nextInt();

	}

	static class FastReader implements InputReader {
		final private int BUFFER_SIZE = 1<<16;
		private final DataInputStream din;
		private final byte[] buffer;
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

	static class Utilities {
		public static class Debug {
			public static final boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

			private static <T> String ts(T t) {
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

