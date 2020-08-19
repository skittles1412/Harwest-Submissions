import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractList;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.AbstractCollection;
import java.util.StringTokenizer;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.io.Closeable;
import java.io.BufferedReader;
import java.util.Comparator;
import java.io.InputStream;

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
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			E2WeightsDivisionHardVersion solver = new E2WeightsDivisionHardVersion();
			int testCount = Integer.parseInt(in.next());
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class E2WeightsDivisionHardVersion {
		private final int INF = 1_000_000_000;
		static ArrayList<Pair<Integer, Pair<Integer, Long>>>[] graph;
		static E2WeightsDivisionHardVersion.Node[] nodes;
		static PriorityQueue<E2WeightsDivisionHardVersion.Edge> edges;
		static PriorityQueue<E2WeightsDivisionHardVersion.Edge> edges2;

		public E2WeightsDivisionHardVersion() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt();
			long s = in.nextLong();
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 0; i<n-1; i++) {
				int u = in.nextInt()-1, v = in.nextInt()-1;
				long w = in.nextInt();
				int c = in.nextInt();
				graph[u].add(new Pair<>(v, new Pair<>(c, w)));
				graph[v].add(new Pair<>(u, new Pair<>(c, w)));
			}
			ArrayList<Long> e1 = new ArrayList<>(), e2 = new ArrayList<>();
			nodes = new E2WeightsDivisionHardVersion.Node[n];
			for(int i = 0; i<n; i++) {
				nodes[i] = new E2WeightsDivisionHardVersion.Node(i);
			}
			edges = new PriorityQueue<>(Comparator.comparingLong(e -> -e.getChange()));
			edges2 = new PriorityQueue<>(Comparator.comparingLong(e -> -e.getChange()));
			nodes[0].dfs();
			long sum = 0;
			e1.add(0L);
			e2.add(0L);
			while(!edges.isEmpty()) {
				E2WeightsDivisionHardVersion.Edge e = edges.poll();
				if(e.getChange()==0) {
					break;
				}
				sum += e.halfen();
				edges.add(e);
				e1.add(sum);
			}
			edges = edges2;
			sum = 0;
			while(!edges.isEmpty()) {
				E2WeightsDivisionHardVersion.Edge e = edges.poll();
				if(e.getChange()==0) {
					break;
				}
				sum += e.halfen();
				edges.add(e);
				e2.add(sum);
			}
			s = nodes[0].sum-s;
			int ans = INF;
			Utilities.Debug.dbg(s, e1, e2);
			for(int i = 0; i<e1.size(); i++) {
				int ind = Utilities.lowerBound(e2, s-e1.get(i));
				if(ind<e2.size()) {
					ans = Math.min(ans, (ind<<1)+i);
				}
			}
			pw.println(ans);
		}

		static class Node {
			int cnt;
			int ind;
			long sum;
			ArrayList<Pair<Long, E2WeightsDivisionHardVersion.Node>> children;
			boolean visited;

			public Node(int ind) {
				sum = cnt = 0;
				children = new ArrayList<>();
				visited = false;
				this.ind = ind;
			}

			public void dfs() {
				visited = true;
				for(var v: graph[ind]) {
					E2WeightsDivisionHardVersion.Node n = nodes[v.a];
					if(!n.visited) {
						children.add(new Pair<>(v.b.b, n));
						n.dfs();
						cnt += n.cnt;
						sum += n.cnt*v.b.b+n.sum;
						if(v.b.a==1) {
							edges.add(new E2WeightsDivisionHardVersion.Edge(this, n, v.b.b));
						}else {
							edges2.add(new E2WeightsDivisionHardVersion.Edge(this, n, v.b.b));
						}
					}
				}
				if(children.isEmpty()) {
					cnt++;
				}
			}

		}

		static class Edge {
			E2WeightsDivisionHardVersion.Node parent;
			E2WeightsDivisionHardVersion.Node children;
			long cost;

			public Edge(E2WeightsDivisionHardVersion.Node parent, E2WeightsDivisionHardVersion.Node children, long cost) {
				this.parent = parent;
				this.children = children;
				this.cost = cost;
			}

			public long getChange() {
				return children.cnt*(cost-(cost >> 1));
			}

			public long halfen() {
				long prev = cost;
				cost >>= 1;
				return children.cnt*(prev-cost);
			}

			public String toString() {
				return "Edge{"+
						"parent="+parent.ind+
						", children="+children.ind+
						", cost="+cost+
						'}';
			}

		}

	}

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public boolean autoFlush;
		public String LineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			autoFlush = false;
			LineSeparator = System.lineSeparator();
		}

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println() {
			sb.append(LineSeparator);
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

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = null;
		}

		public boolean hasNext() {
			try {
				while(st==null||!st.hasMoreTokens()) {
					String s = br.readLine();
					if(s==null) {
						return false;
					}
					st = new StringTokenizer(s);
				}
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		public String next() {
			if(!hasNext()) {
				throw new InputMismatchException();
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

	}

	static class Utilities {
		public static <T extends Comparable<T>> int lowerBound(AbstractList<T> arr, T target) {
			int l = 0, h = arr.size();
			while(l<h) {
				int mid = l+h >> 1;
				if(arr.get(mid).compareTo(target)<0) {
					l = mid+1;
				}else {
					h = mid;
				}
			}
			return l;
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

	static class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
		public T1 a;
		public T2 b;

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


