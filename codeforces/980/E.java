import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
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
			ETheNumberGames solver = new ETheNumberGames();
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

	static class ETheNumberGames {
		int n;
		int k;
		int[] par;
		int[] tin;
		int[] tout;
		int[] rank;
		ArrayList<Integer>[] graph;

		public ETheNumberGames() {
		}

		public void dfs() {
			int t = 0;
			ArrayDeque<Integer> s = new ArrayDeque<>(n), process = new ArrayDeque<>(n);
			s.addLast(n-1);
			while(!s.isEmpty()) {
				int u = s.removeLast();
				tin[u] = t++;
				process.addLast(u);
				for(int v: graph[u]) {
					graph[v].remove((Integer) u);
					par[v] = u;
					rank[v] = rank[u]+1;
					s.addLast(v);
				}
			}
			while(!process.isEmpty()) {
				int u = process.removeLast();
				if(graph[u].isEmpty()) {
					tout[u] = tin[u];
				}else {
					tout[u] = tout[graph[u].get(0)];
				}
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			k = in.nextInt();
			graph = in.nextUndirectedGraph(n, n-1);
			par = new int[n];
			tin = new int[n];
			tout = new int[n];
			rank = new int[n];
			dfs();
			ETheNumberGames.FT dist = new ETheNumberGames.FT(n);
			for(int i = 0; i<n; i++) {
				dist.add(tin[i], tin[i], rank[i]);
			}
			int left = n-k-1;
			boolean[] visited = new boolean[n];
			visited[n-1] = true;
			ArrayDeque<Integer> s = new ArrayDeque<>(n);
			ArrayList<Integer> ans = new ArrayList<>();
			for(int i = n-2; i>=0; i--) {
				int cur;
				if((cur = dist.get(tin[i]))<=left) {
					left -= cur;
					for(int j = i; !visited[j]; j = par[j]) {
						visited[j] = true;
						s.addLast(j);
					}
					while(!s.isEmpty()) {
						dist.add(tin[s.getLast()], tout[s.removeLast()], -1);
					}
				}else {
					ans.add(i+1);
				}
			}
			Collections.reverse(ans);
			pw.println(ans);
		}

		static class FT {
			int n;
			int[] arr;

			public FT(int n) {
				this.n = n;
				this.arr = new int[n+1];
			}

			private void add(int ind, int x) {
				ind++;
				while(ind<=n) {
					arr[ind] += x;
					ind += ind&-ind;
				}
			}

			public void add(int l, int r, int x) {
				add(l, x);
				add(r+1, -x);
			}

			public int get(int ind) {
				ind++;
				int ret = 0;
				while(ind>0) {
					ret += arr[ind];
					ind -= ind&-ind;
				}
				return ret;
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

		public void print(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
		}

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println() {
			sb.append(lineSeparator);
		}

		public <T> void println(Iterable<T> iter) {
			boolean first = true;
			for(T t: iter) {
				if(!first) {
					print(" ");
				}
				first = false;
				print(t);
			}
			println();
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
}