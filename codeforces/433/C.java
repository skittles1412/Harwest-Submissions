import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.BufferedReader;
import java.util.Collections;
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
			CRyoukosMemoryNote solver = new CRyoukosMemoryNote();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CRyoukosMemoryNote {
		public CRyoukosMemoryNote() {
		}

		public long getCost(ArrayList<Integer> al, int x) {
			long ans = 0;
			for(int i: al) {
				ans += Math.abs(x-i);
			}
			return ans;
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			long sum = 0;
			int[] arr = new int[m];
			ArrayList<Integer>[] adj = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				adj[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				arr[i] = in.nextInt()-1;
			}
			for(int i = 0; i<m; i++) {
				if(i>0) {
					if(arr[i]!=arr[i-1]) {
						adj[arr[i]].add(arr[i-1]);
					}
					sum += Math.abs(arr[i-1]-arr[i]);
				}
				if(i<m-1) {
					if(arr[i]!=arr[i+1]) {
						adj[arr[i]].add(arr[i+1]);
					}
				}
			}
			for(int i = 0; i<n; i++) {
				Collections.sort(adj[i]);
			}
			long best = 0;
			for(int i = 0; i<n; i++) {
				if(adj[i].size()>0) {
					best = Math.max(best, getCost(adj[i], i)-getCost(adj[i], adj[i].get(adj[i].size() >> 1)));
				}
			}
//		System.out.println(sum+" "+best);
			pw.println(sum-best);
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

