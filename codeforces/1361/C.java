import java.io.*;
import java.util.HashMap;
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
			CJohnnyAndMegansNecklace solver = new CJohnnyAndMegansNecklace();
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

	static class CJohnnyAndMegansNecklace {
		int n;
		int ind;
		int[] tour;
		int[][] arr;
		boolean[] used;
		CJohnnyAndMegansNecklace.Stack[] graph;

		public CJohnnyAndMegansNecklace() {
		}

		public void tour(int u) {
			CJohnnyAndMegansNecklace.Stack s = new CJohnnyAndMegansNecklace.Stack(2*n);
			s.addLast(u);
			while(!s.isEmpty()) {
				int cur = s.removeLast();
				while(!graph[cur].isEmpty()) {
					int i = graph[cur].removeLast();
					if(!used[i]) {
						used[i] = true;
						s.addLast(cur);
						cur = arr[i][0]==cur ? arr[i][1] : arr[i][0];
					}
				}
				tour[ind++] = cur;
			}
		}

		public boolean valid(int x) {
			x = (1<<x)-1;
			int n = x+1;
			int[] cnt = new int[n];
			for(int i = 0; i<this.n; i++) {
				cnt[arr[i][0]&x]++;
				cnt[arr[i][1]&x]++;
			}
			for(int i = 0; i<n; i++) {
				if(Utilities.odd(cnt[i])) {
					return false;
				}
			}
			int[] ind = new int[n];
			int[][] graph = new int[n][];
			for(int i = 0; i<n; i++) {
				graph[i] = new int[cnt[i]];
			}
			for(int i = 0; i<this.n; i++) {
				int u = arr[i][0]&x, v = arr[i][1]&x;
				graph[u][ind[u]++] = v;
				graph[v][ind[v]++] = u;
			}
			boolean[] visited = new boolean[n];
			CJohnnyAndMegansNecklace.Stack s = new CJohnnyAndMegansNecklace.Stack(n);
			for(int i = 0; i<n; i++) {
				if(cnt[i]>0) {
					s.addLast(i);
					visited[i] = true;
					break;
				}
			}
			while(!s.isEmpty()) {
				int u = s.removeLast();
				for(int v: graph[u]) {
					if(!visited[v]) {
						s.addLast(v);
						visited[v] = true;
					}
				}
			}
			for(int i = 0; i<n; i++) {
				if(!visited[i]&&cnt[i]>0) {
					return false;
				}
			}
			return true;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			arr = in.nextInt(n, 2);
			int l = 0, r = 20;
			while(l<r) {
				int mid = (l+r+1)/2;
				if(valid(mid)) {
					l = mid;
				}else {
					r = mid-1;
				}
			}
			pw.println(l);
			int x = 1<<l;
			graph = new CJohnnyAndMegansNecklace.Stack[x];
			for(int i = 0; i<x; i++) {
				graph[i] = new CJohnnyAndMegansNecklace.Stack(20);
			}
			x--;
			HashMap<CJohnnyAndMegansNecklace.Pair, CJohnnyAndMegansNecklace.Stack> edges = new HashMap<>(4*n);
			for(int i = 0; i<n; i++) {
				int u = arr[i][0] &= x, v = arr[i][1] &= x;
				graph[u].addLast(i);
				if(u!=v) {
					graph[v].addLast(i);
				}
				int finalI = i;
				edges.compute(new CJohnnyAndMegansNecklace.Pair(u, v), (a, b) -> {
					if(b==null) {
						b = new CJohnnyAndMegansNecklace.Stack(20);
					}
					b.addLast(finalI);
					return b;
				});
				edges.compute(new CJohnnyAndMegansNecklace.Pair(v, u), (a, b) -> {
					if(b==null) {
						b = new CJohnnyAndMegansNecklace.Stack(20);
					}
					b.addLast(finalI);
					return b;
				});
			}
			ind = 0;
			tour = new int[2*n];
			used = new boolean[n];
			for(int i = 0; i<=x; i++) {
				if(!graph[i].isEmpty()) {
					tour(i);
					break;
				}
			}
			boolean[] used = new boolean[n];
			for(int i = 0; i<n; i++) {
				var cur = edges.get(new CJohnnyAndMegansNecklace.Pair(tour[i], tour[i+1]));
				int j;
				while(used[j = cur.removeLast()]) ;
				used[j] = true;
				int u = j*2+1, v = j*2+2;
				if((arr[j][0]&x)==tour[i]) {
					pw.print(u+" "+v+" ");
				}else {
					pw.print(v+" "+u+" ");
				}
			}
		}

		static class Pair {
			int a;
			int b;

			public Pair(int a, int b) {
				this.a = a;
				this.b = b;
			}

			public boolean equals(Object o) {
				if(this==o) return true;
				if(o==null||getClass()!=o.getClass()) return false;
				CJohnnyAndMegansNecklace.Pair pair = (CJohnnyAndMegansNecklace.Pair) o;
				return a==pair.a&&b==pair.b;
			}

			public int hashCode() {
				return a*31+b;
			}

		}

		static class Stack {
			int n;
			int ind;
			int[] arr;

			public Stack(int n) {
				this.n = n;
				ind = 0;
				arr = new int[n];
			}

			public void addLast(int x) {
				if(ind>=n) {
					int[] narr = new int[2*n];
					System.arraycopy(arr, 0, narr, 0, n);
					n *= 2;
					arr = narr;
				}
				arr[ind++] = x;
			}

			public int removeLast() {
				return arr[--ind];
			}

			public boolean isEmpty() {
				return ind==0;
			}

		}

	}

	static class Utilities {
		public static boolean odd(int x) {
			return (x&1)>0;
		}

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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
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

	interface InputReader {
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

