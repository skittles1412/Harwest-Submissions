import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.util.Queue;
import java.io.BufferedReader;
import java.util.LinkedList;
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
			FRemovingLeaves solver = new FRemovingLeaves();
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
	static class FRemovingLeaves {
		public FRemovingLeaves() {
		}

		public void solve(int testNumber, Input in, Output pw) {
			int n = in.nextInt(), k = in.nextInt();
			ArrayList<Integer>[] graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			int[] leaves = new int[n], degree = new int[n];
			for(int i = 0; i<n-1; i++) {
				int u = in.nextInt()-1, v = in.nextInt()-1;
				graph[u].add(v);
				graph[v].add(u);
				degree[u]++;
				degree[v]++;
			}
			if(k==1) {
				pw.println(n-1);
				return;
			}
			for(int i = 0; i<n; i++) {
				if(degree[i]==1) {
					for(int x: graph[i]) {
						leaves[x]++;
					}
				}
			}
			Queue<Integer> q = new LinkedList<>();
			for(int i = 0; i<n; i++) {
				if(leaves[i] >= k) {
					q.add(i);
				}
			}
			int ans = 0;
			while(!q.isEmpty()) {
				int x = q.poll();
				while(leaves[x] >= k) {
					leaves[x] -= k;
					degree[x] -= k;
					ans++;
				}
				if(degree[x]==1) {
					for(int i: graph[x]) {
						if(degree[i]!=1) {
							leaves[i]++;
							if(leaves[i]==k) {
								q.add(i);
							}
						}
					}
				}
			}
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

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush||sb.length()>BUFFER_SIZE >> 1) {
				flush();
			}
		}

		public void println() {
			sb.append(LineSeparator);
		}

		public void flush() {
			try {
				os.write(sb.toString().getBytes());
			}catch(IOException e) {
				e.printStackTrace();
			}
			sb = new StringBuilder(BUFFER_SIZE);
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

