import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.io.Closeable;
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
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			PIndependentSet solver = new PIndependentSet();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class PIndependentSet {
		private final int mod = (int) (1e9+7);
		static PIndependentSet.Node[] nodes;
		static ArrayList<Integer>[] graph;
		int[][] memo;

		public PIndependentSet() {
		}

		int dp(PIndependentSet.Node n, int color) {
			if(memo[n.ind][color]!=-1) {
				return memo[n.ind][color];
			}
			long ans = 1;
			for(PIndependentSet.Node u: n.children) {
				long tmp = 0;
				for(int i = 0; i<2; i++) {
					if(!(i==1&&color==1)) {
						tmp = (tmp+dp(u, i))%mod;
					}
				}
				ans = (ans*tmp)%mod;
			}
			return memo[n.ind][color] = (int) ans;
		}

		public void solve(int kase, FastReader in, Output pw) {
			int n = in.nextInt();
			nodes = new PIndependentSet.Node[n];
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
				nodes[i] = new PIndependentSet.Node(i);
			}
			for(int i = 0; i<n-1; i++) {
				int u = in.nextInt()-1, v = in.nextInt()-1;
				graph[u].add(v);
				graph[v].add(u);
			}
			nodes[0].dfs();
			memo = new int[n][2];
			for(int i = 0; i<n; i++) {
				memo[i] = new int[] {-1, -1};
			}
			pw.println((dp(nodes[0], 0)+dp(nodes[0], 1))%mod);
		}

		static class Node {
			PIndependentSet.Node parent;
			ArrayList<PIndependentSet.Node> children;
			int ind;
			boolean visited;

			public Node(int ind) {
				this.ind = ind;
				parent = null;
				children = new ArrayList<>();
				visited = false;
			}

			public void dfs() {
				visited = true;
				for(int i: graph[ind]) {
					PIndependentSet.Node n = nodes[i];
					if(!n.visited) {
						children.add(n);
						n.parent = this;
						n.dfs();
					}
				}
			}

		}

	}

	static class FastReader {
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
}

