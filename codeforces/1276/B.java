import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

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
			BTwoFairs solver = new BTwoFairs();
			int testCount = in.nextInt();
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class BTwoFairs {
		int n;
		int m;
		int a;
		int b;
		int[][] edges;

		public BTwoFairs() {
		}

		public int[] component(int ignore) {
			DSU dsu = new DSU(n);
			for(int[] x: edges) {
				if(x[0]!=ignore&&x[1]!=ignore) {
					dsu.union(x[0], x[1]);
				}
			}
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = dsu.find(i);
			}
			return ret;
		}

		public void solve(int kase, InputReader in, Output pw) {
//			Utilities.Debug.dbg(kase);
			n = in.nextInt();
			m = in.nextInt();
			a = in.nextInt()-1;
			b = in.nextInt()-1;
			edges = in.nextInt(m, 2, o -> o-1);
			int[] x = component(a), y = component(b);
			HashMap<Pair<Integer, Integer>, Integer> both = new HashMap<>(n*4);
			HashMap<Integer, Integer> ahm = new HashMap<>(n*4), bhm = new HashMap<>(n*4);
			for(int i = 0; i<n; i++) {
				if(i!=a&&i!=b) {
					Utilities.add(both, new Pair<>(x[i], y[i]));
					Utilities.add(ahm, x[i]);
					Utilities.add(bhm, y[i]);
				}
			}
			long ans = (long) (n-2)*(n-3)/2;
			for(long i: ahm.values()) {
				ans -= i*(i-1)/2;
			}
			for(long i: bhm.values()) {
				ans -= i*(i-1)/2;
			}
			for(long i: both.values()) {
				ans += i*(i-1)/2;
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

	static class DSU {
		public int[] parent;
		public int[] size;
		public int n;

		public DSU(int x) {
			n = x;
			parent = new int[n];
			size = new int[n];
			for(int i = 0; i<n; i++) {
				parent[i] = i;
				size[i] = 1;
			}
		}

		public int find(int i) {
			if(parent[i]==i) {
				return i;
			}else {
				int result = find(parent[i]);
				parent[i] = result;
				return result;
			}
		}

		public boolean union(int i, int j) {
			int irep = find(i);
			int jrep = find(j);
			if(irep==jrep) {
				return false;
			}
			int irank = size[irep];
			int jrank = size[jrep];
			if(irank<=jrank) {
				parent[irep] = jrep;
				size[jrep] += size[irep];
			}else {
				parent[jrep] = irep;
				size[irep] += size[jrep];
			}
			return true;
		}

	}

	static class Utilities {
		public static <K, V> V getOrDefault(Map<? extends K, ? extends V> map, K key, V value) {
			V ret = map.get(key);
			if(ret==null) {
				return value;
			}
			return ret;
		}

		public static <T> void add(Map<? super T, Integer> map, T key) {
			map.put(key, getOrDefault(map, key, 0)+1);
		}

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

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n, IntUnaryOperator operator) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = operator.applyAsInt(nextInt());
			}
			return ret;
		}

		default int[][] nextInt(int n, int m, IntUnaryOperator operator) {
			int[][] ret = new int[n][m];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt(m, operator);
			}
			return ret;
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
}

