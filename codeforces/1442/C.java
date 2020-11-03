import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
			CGraphTranspositions solver = new CGraphTranspositions();
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

	static class CGraphTranspositions {
		private final int mod = 998244353;

		public CGraphTranspositions() {
		}

		public boolean good(ArrayList<CGraphTranspositions.State> al, CGraphTranspositions.State s) {
			for(CGraphTranspositions.State x: al) {
				if(x.pow<s.pow&&x.compareTo(s)<=0) {
					return false;
				}
			}
			return true;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			ArrayList<Integer>[][] graph = new ArrayList[2][n];
			graph[0] = in.nextDirectedGraph(n, m);
			for(int i = 0; i<n; i++) {
				graph[1][i] = new ArrayList<>();
			}
			for(int i = 0; i<n; i++) {
				for(int j: graph[0][i]) {
					graph[1][j].add(i);
				}
			}
			ArrayList<CGraphTranspositions.State>[][] visited = new ArrayList[n][2];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<2; j++) {
					visited[i][j] = new ArrayList<>();
				}
			}
			HashSet<Pair<Integer, Integer>> hs = new HashSet<>(n*4);
			PriorityQueue<CGraphTranspositions.State> pq = new PriorityQueue<>(n*4);
			pq.add(new CGraphTranspositions.State(0, 0, 0));
			while(!pq.isEmpty()) {
				CGraphTranspositions.State cur = pq.poll();
				var p = cur.getPair();
				if(hs.contains(p)||!good(visited[cur.ind][cur.pow&1], cur)) {
					continue;
				}
////				Utilities.Debug.dbg(cur);
				hs.add(p);
				visited[cur.ind][cur.pow&1].add(cur);
				if(cur.ind==n-1) {
////					Utilities.Debug.dbg(cur);
					pw.println((Utilities.math.pow(2, cur.pow, mod)+cur.time+mod-1)%mod);
					return;
				}
				CGraphTranspositions.State s = cur.trans();
				if(!hs.contains(s.getPair())&&good(visited[s.ind][s.pow&1], s)) {
					pq.add(s);
				}
				for(int i: graph[cur.pow&1][cur.ind]) {
					s = cur.move(i);
					if(!hs.contains(s.getPair())&&good(visited[s.ind][s.pow&1], s)) {
						pq.add(s);
					}
				}
			}
		}

		static class State implements Comparable<CGraphTranspositions.State> {
			int ind;
			int pow;
			int time;

			public State(int ind, int pow, int time) {
				this.ind = ind;
				this.pow = pow;
				this.time = time;
			}

			public CGraphTranspositions.State move(int next) {
				return new CGraphTranspositions.State(next, pow, time+1);
			}

			public CGraphTranspositions.State trans() {
				return new CGraphTranspositions.State(ind, pow+1, time);
			}

			public Pair<Integer, Integer> getPair() {
				return new Pair<>(ind, pow);
			}

			public int compareTo(CGraphTranspositions.State s) {
				if(s.pow==pow) {
					return Integer.compare(time, s.time);
				}else if(Math.max(s.pow, pow)<=60) {
					return Long.compare((1L<<pow)+time, (1L<<s.pow)+s.time);
				}
				return Integer.compare(pow, s.pow);
			}

			public String toString() {
				return "State{"+
						"ind="+ind+
						", pow="+pow+
						", time="+time+
						'}';
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

		public static class math {
			public static long pow(long base, long exp, long mod) {
				long ans = 1, cur = base;
				while(exp>0) {
					if((exp&1)>0) {
						ans = (ans*cur)%mod;
					}
					cur = (cur*cur)%mod;
					exp >>= 1;
				}
				return ans;
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

		default ArrayList<Integer>[] nextDirectedGraph(int n, int m) {
			ArrayList<Integer>[] ret = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				ret[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				ret[nextInt()-1].add(nextInt()-1);
			}
			return ret;
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


