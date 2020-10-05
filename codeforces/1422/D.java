import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.AbstractCollection;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Stream;

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
			DReturningHome solver = new DReturningHome();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class DReturningHome {
		private final long linf = 4_000_000_000_000_000_000L;

		public DReturningHome() {
		}

		public long dist(long x1, long y1, long x2, long y2) {
			return Math.min(Math.abs(x1-x2), Math.abs(y1-y2));
		}

		public void solve(int kase, InputReader in, Output pw) {
			in.nextInt();
			int n = in.nextInt();
			ArrayList<Pair<Integer, Long>>[] graph = Stream.generate(ArrayList::new).limit(n+2).toArray(ArrayList[]::new);
			long sx = in.nextInt(), sy = in.nextInt(), fx = in.nextInt(), fy = in.nextInt();
			graph[0].add(new Pair<>(n+1, Math.abs(fx-sx)+Math.abs(fy-sy)));
			int[][] arr = new int[n][3];
			for(int i = 0; i<n; i++) {
				arr[i] = new int[] {in.nextInt(), in.nextInt(), i+1};
				graph[i+1].add(new Pair<>(n+1, Math.abs(fx-arr[i][0])+Math.abs(fy-arr[i][1])));
				graph[0].add(new Pair<>(i+1, dist(arr[i][0], arr[i][1], sx, sy)));
			}
			Arrays.sort(arr, Comparator.comparingInt(o -> o[0]));
			for(int i = 0; i<n-1; i++) {
				long w = dist(arr[i][0], arr[i][1], arr[i+1][0], arr[i+1][1]);
				graph[arr[i][2]].add(new Pair<>(arr[i+1][2], w));
				graph[arr[i+1][2]].add(new Pair<>(arr[i][2], w));
			}
			Arrays.sort(arr, Comparator.comparingInt(o -> o[1]));
			for(int i = 0; i<n-1; i++) {
				long w = dist(arr[i][0], arr[i][1], arr[i+1][0], arr[i+1][1]);
				graph[arr[i][2]].add(new Pair<>(arr[i+1][2], w));
				graph[arr[i+1][2]].add(new Pair<>(arr[i][2], w));
			}
////			Utilities.Debug.dbg(graph);
			long[] dist = new long[n+2];
			Arrays.fill(dist, linf);
			PriorityQueue<Pair<Integer, Long>> pq = new PriorityQueue<>(Comparator.comparingLong(o -> o.b));
			pq.add(new Pair<>(0, 0L));
			while(!pq.isEmpty()) {
				Pair<Integer, Long> p = pq.remove();
				long d = p.b;
				int cur = p.a;
				if(dist[cur]==linf) {
					dist[cur] = d;
					for(Pair<Integer, Long> i: graph[cur]) {
						if(dist[i.a]==linf) {
							pq.add(new Pair<>(i.a, d+i.b));
						}
					}
				}
			}
////			Utilities.Debug.dbg(dist);
			pw.println(dist[n+1]);
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


