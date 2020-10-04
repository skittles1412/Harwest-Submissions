import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.TreeSet;

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
			DSegmentTree solver = new DSegmentTree();
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

	static class DSegmentTree {
		public DSegmentTree() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), cnt = 0;
			int[][] arr = in.nextInt(n, 2);
			int[][] events = new int[n<<1][2];
			for(int i = 0; i<n; i++) {
				events[i<<1] = new int[] {arr[i][0], i+1};
				events[i<<1|1] = new int[] {arr[i][1], -i-1};
			}
			Arrays.sort(events, Comparator.comparingInt(o -> o[0]));
			DSU dsu = new DSU(n);
			TreeSet<Pair<Integer, Integer>> ts = new TreeSet<>(Comparator.comparingInt(o -> o.a));
			for(int[] e: events) {
				int ind = Math.abs(e[1])-1;
				if(e[1]>0) {
					var p = new Pair<>(arr[ind][1], ind);
					Pair<Integer, Integer> v = p;
//				for(var v: ts.headSet(p)) {
//					if(cnt==n-1||!dsu.union(ind, v.b)) {
//						pw.println("NO");
//						return;
//					}
//					cnt++;
//				}
					while((v = ts.lower(v))!=null) {
						if(cnt==n-1||!dsu.union(ind, v.b)) {
							pw.println("NO");
							return;
						}
						cnt++;
					}
					ts.add(p);
				}else {
					ts.remove(new Pair<>(e[0], ind));
				}
			}
			if(cnt==n-1) {
				pw.println("YES");
			}else {
				pw.println("NO");
			}
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

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

		default int[][] nextInt(int n, int m) {
			int[][] ret = new int[n][m];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt(m);
			}
			return ret;
		}

	}
}

