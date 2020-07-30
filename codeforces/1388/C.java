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
			CUncleBogdanAndCountryHappiness solver = new CUncleBogdanAndCountryHappiness();
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
	static class CUncleBogdanAndCountryHappiness {
		CUncleBogdanAndCountryHappiness.Node[] nodes;
		int n;
		int m;

		public CUncleBogdanAndCountryHappiness() {
		}

		public void solve(int kase, Input in, Output pw) {
			n = in.nextInt();
			m = in.nextInt();
			nodes = new CUncleBogdanAndCountryHappiness.Node[n];
			for(int i = 0; i<n; i++) {
				nodes[i] = new CUncleBogdanAndCountryHappiness.Node(i, in.nextInt());
			}
			for(int i = 0; i<n; i++) {
				nodes[i].happy = in.nextInt();
			}
			for(int i = 0; i<n-1; i++) {
				nodes[in.nextInt()-1].addNode(nodes[in.nextInt()-1]);
			}
			nodes[0].dfs();
			if(nodes[0].check()) {
				pw.println("YES");
			}else {
				pw.println("NO");
			}
		}

		static class Node {
			public int here;
			public int total;
			public int ind;
			public int happy;
			public ArrayList<CUncleBogdanAndCountryHappiness.Node> children;
			public CUncleBogdanAndCountryHappiness.Node parent;

			public Node(int ind, int here) {
				this.ind = ind;
				this.here = here;
				total = here;
				happy = 0;
				children = new ArrayList<>();
			}

			public void addNode(CUncleBogdanAndCountryHappiness.Node n) {
				children.add(n);
				n.children.add(this);
			}

			public void dfs() {
				for(CUncleBogdanAndCountryHappiness.Node n: children) {
					if(n==parent) {
						continue;
					}
					n.parent = this;
					n.dfs();
					total += n.total;
				}
			}

			public boolean check() {
				boolean ans = true;
				int maxGood = happy+total >> 1, gsum = 0;
				for(CUncleBogdanAndCountryHappiness.Node n: children) {
					if(n==parent) {
						continue;
					}
					ans &= n.check();
					gsum += n.happy+n.total >> 1;
				}
				if(gsum>maxGood||(happy&1)!=(total&1)||Math.abs(happy)>total) {
					return false;
				}
				return ans;
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

