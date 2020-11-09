import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Map.Entry;
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
			CTeamBuilding solver = new CTeamBuilding();
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

	static class CTeamBuilding {
		int n;
		int m;
		int k;
		int cmax;
		boolean bipartite;
		int[] compd;
		int[] arr;
		int[] other;
		int[] bcolor;
		boolean[] tvalid;
		ArrayList<Integer> cvisited;
		ArrayList<Integer>[] graph;

		public CTeamBuilding() {
		}

		public void comp(int u, int cur, int next) {
			if(compd[u]!=-1) {
				if(compd[u]!=cur) {
					tvalid[arr[u]] = false;
				}
				return;
			}
			compd[u] = cur;
			cmax = Math.max(cmax, cur);
			for(int v: graph[u]) {
				if(arr[u]==arr[v]) {
					comp(v, next, cur);
				}
			}
		}

		public void dfs(int u, int cur, int next) {
			if(bcolor[u]!=-1) {
				if(bcolor[u]!=cur) {
					bipartite = false;
				}
				return;
			}
			bcolor[u] = cur;
			cvisited.add(u);
			if(other[u]!=-1) {
				dfs(other[u], next, cur);
			}
			for(int v: graph[u]) {
				dfs(v, next, cur);
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			{
				n = in.nextInt();
				m = in.nextInt();
				k = in.nextInt();
				arr = in.nextInt(n, o -> o-1);
				graph = in.nextUndirectedGraph(n, m);
			}
			{
				compd = new int[n];
				Arrays.fill(compd, -1);
				tvalid = new boolean[k];
				Arrays.fill(tvalid, true);
				other = new int[n];
				Arrays.fill(other, -1);
				cmax = 0;
				int curc = 0;
				for(int i = 0; i<n; i++) {
					if(compd[i]==-1) {
						comp(i, curc, curc+1);
						if(cmax>curc) {
							other[curc] = curc+1;
							other[curc+1] = curc;
						}
						curc = cmax+1;
					}
				}
			}
			HashMap<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> edges = new HashMap<>(m*4);
			{
				for(int i = 0; i<n; i++) {
					int iteam = arr[i];
					if(tvalid[iteam]) {
						for(int j: graph[i]) {
							int jteam = arr[j];
							if(tvalid[jteam]&&jteam>iteam) {
								edges.computeIfAbsent(new Pair<>(iteam, jteam), o -> new ArrayList<>()).add(new Pair<>(compd[i], compd[j]));
							}
						}
					}
				}
			}
			long cnt = 0;
			{
				for(int i = 0; i<n; i++) {
					graph[i].clear();
				}
				bcolor = new int[n];
				Arrays.fill(bcolor, -1);
				HashSet<Pair<Integer, Integer>> tvisited = new HashSet<>(m*2);
				cvisited = new ArrayList<>(n);
				for(var v: edges.entrySet()) {
					ArrayList<Integer> curv = new ArrayList<>();
					for(var i: v.getValue()) {
						graph[i.a].add(i.b);
						graph[i.b].add(i.a);
						curv.add(i.a);
						curv.add(i.b);
					}
					bipartite = true;
					for(int i: curv) {
						if(bcolor[i]==-1) {
							dfs(i, 0, 1);
						}
					}
					if(!bipartite) {
						cnt++;
//						Utilities.Debug.dbg(v.getKey());
					}
					for(int i: cvisited) {
						graph[i].clear();
						bcolor[i] = -1;
					}
					cvisited.clear();
				}
			}
			{
				long left = k;
				for(int i = 0; i<k; i++) {
					if(!tvalid[i]) {
						left--;
					}
				}
//				Utilities.Debug.dbg(left, cnt);
				pw.println((left)*(left-1)/2-cnt);
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

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n, IntUnaryOperator operator) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = operator.applyAsInt(nextInt());
			}
			return ret;
		}

		default ArrayList<Integer>[] nextUndirectedGraph(int n, int m) {
			ArrayList<Integer>[] ret = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				ret[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				int u = nextInt()-1, v = nextInt()-1;
				ret[u].add(v);
				ret[v].add(u);
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

