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
			DMissileSilos solver = new DMissileSilos();
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

	static class DMissileSilos {
		private final long linf = 4_000_000_000_000_000_000L;
		int n;
		ArrayList<Pair<Integer, Long>>[] graph;

		public DMissileSilos() {
		}

		public long[] dijkstra(int v) {
			long[] dist = new long[n];
			Arrays.fill(dist, linf);
			PriorityQueue<Pair<Integer, Long>> pq = new PriorityQueue<>(new Pair.CompareByB<>());
			pq.add(new Pair<>(v, 0L));
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
			return dist;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int m = in.nextInt(), c = in.nextInt()-1;
			graph = in.nextUndirectedWeightedLongGraph(n, m);
			long l = in.nextLong();
			long[] dist = dijkstra(c);
			long ans = 0;
			for(int i = 0; i<n; i++) {
				for(var v: graph[i]) {
					if(i<v.a) {
						int a = i, b = v.a;
						if(dist[a]<l&&dist[a]+v.b>l&&v.b+dist[a]+dist[b]>(l<<1)) {
							ans++;
						}
						a = v.a;
						b = i;
						if(dist[a]<l&&dist[a]+v.b>l&&v.b+dist[a]+dist[b]>(l<<1)) {
							ans++;
						}
						if(dist[a]<l&&dist[b]<l&&dist[a]+dist[b]+v.b==(l<<1)) {
							ans++;
						}
					}
				}
			}
			Utilities.Debug.dbg(dist);
			for(int i = 0; i<n; i++) {
				if(dist[i]==l) {
					Utilities.Debug.dbg(i);
					ans++;
				}
			}
			pw.println(ans);
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

		long nextLong();

		default ArrayList<Pair<Integer, Long>>[] nextUndirectedWeightedLongGraph(int n, int m) {
			ArrayList<Pair<Integer, Long>>[] ret = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				ret[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				int u = nextInt()-1, v = nextInt()-1;
				long w = nextLong();
				ret[u].add(new Pair<>(v, w));
				ret[v].add(new Pair<>(u, w));
			}
			return ret;
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

		public static class CompareByB<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements Comparator<Pair<T1, T2>> {
			public int compare(Pair<T1, T2> o1, Pair<T1, T2> o2) {
				int cmp = o1.b.compareTo(o2.b);
				if(cmp==0) {
					return o1.a.compareTo(o2.a);
				}
				return cmp;
			}

		}

	}
}

