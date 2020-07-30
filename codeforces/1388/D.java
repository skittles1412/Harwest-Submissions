import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Flushable;

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
			DCaptainFlintAndTreasure solver = new DCaptainFlintAndTreasure();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DCaptainFlintAndTreasure {
		Stack<Integer> topSort;
		ArrayList<Integer>[] graph;
		boolean[] visited;

		public DCaptainFlintAndTreasure() {
		}

		public void dfs(int u) {
			visited[u] = true;
			for(int i: graph[u]) {
				if(!visited[i]) {
					dfs(i);
				}
			}
			topSort.push(u);
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt();
			long[] a = new long[n], atmp = new long[n];
			int[] b = new int[n];
			for(int i = 0; i<n; i++) {
				a[i] = atmp[i] = in.nextInt();
			}
			for(int i = 0; i<n; i++) {
				b[i] = in.nextInt()-1;
			}
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 0; i<n; i++) {
				if(b[i]!=-2) {
					graph[i].add(b[i]);
				}
			}
			topSort = new Stack<>();
			visited = new boolean[n];
			for(int i = 0; i<n; i++) {
				if(!visited[i]) {
					dfs(i);
				}
			}
			ArrayList<Integer> order = new ArrayList<>();
			while(!topSort.isEmpty()) {
				order.add(topSort.pop());
			}
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i: order) {
				if(b[i]!=-2) {
					if(atmp[i]>0) {
						graph[i].add(b[i]);
						atmp[b[i]] += atmp[i];
					}else {
						graph[b[i]].add(i);
					}
				}
			}
			topSort = new Stack<>();
			visited = new boolean[n];
			for(int i = 0; i<n; i++) {
				if(!visited[i]) {
					dfs(i);
				}
			}
			ArrayList<Integer> ans = new ArrayList<>();
			while(!topSort.isEmpty()) {
				ans.add(topSort.pop());
			}
			long sum = 0;
			for(int i: ans) {
				sum += a[i];
				if(b[i]!=-2) {
					a[b[i]] += a[i];
				}
			}
			for(int i = 0; i<n; i++) {
				ans.set(i, ans.get(i)+1);
			}
			pw.println(sum);
			pw.println(ans);
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
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println(long l) {
			println(String.valueOf(l));
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
}

