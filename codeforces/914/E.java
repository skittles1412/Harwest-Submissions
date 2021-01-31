import java.io.*;
import java.util.ArrayList;
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
			EPalindromesInATree solver = new EPalindromesInATree();
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

	static class EPalindromesInATree {
		int clock;
		int ind;
		int[] arr;
		int[] size;
		int[] tin;
		int[] tout;
		int[] masks;
		int[] vertex;
		int[] mask;
		long[] ans;
		long[] add;
		boolean[] visited;
		ArrayList<Integer>[] graph;

		public EPalindromesInATree() {
		}

		public void pdfs(int u, int p) {
			tin[u] = clock++;
			size[u] = 1;
			for(int v: graph[u]) {
				if(v!=p) {
					pdfs(v, u);
					size[u] += size[v];
				}
			}
			tout[u] = clock++;
		}

		public void dfs(int u, int p, int x) {
			masks[x ^= arr[u]]++;
			for(int v: graph[u]) {
				if(v!=p&&!visited[v]) {
					dfs(v, u, x);
				}
			}
			vertex[ind] = u;
			mask[ind++] = x;
		}

		public void decomp(int u) {
			loop:
			while(true) {
				for(int v: graph[u]) {
					if(!visited[v]&&size[v]>size[u]/2) {
						size[u] -= size[v];
						size[v] += size[u];
						u = v;
						continue loop;
					}
				}
				break;
			}
			ans[u]++;
			visited[u] = true;
			long sub = 0;
			int n = graph[u].size();
			int[][] vertex = new int[n][], mask = new int[n][];
			for(int i = 0; i<n; i++) {
				int v = graph[u].get(i);
				if(!visited[v]) {
					this.vertex = new int[size[v]];
					this.mask = new int[size[v]];
					ind = 0;
					dfs(v, u, 0);
					vertex[i] = this.vertex;
					mask[i] = this.mask;
				}
			}
			for(int i = 0; i<n; i++) {
				if(vertex[i]!=null) {
					for(int x: mask[i]) {
						masks[x]--;
					}
					for(int j = 0; j<vertex[i].length; j++) {
						int v = vertex[i][j], m = mask[i][j];
						int cur = m^arr[u];
						if(cur==0) {
							add[v] = 1;
						}
						sub += masks[cur];
						add[v] += masks[cur];
						for(int k = 0; k<20; k++) {
							if(cur==(1<<k)) {
								add[v]++;
							}
							sub += masks[cur^1<<k];
							add[v] += masks[cur^1<<k];
						}
						for(int x: graph[v]) {
							add[v] += add[x];
						}
						ans[v] += add[v];
					}
					for(int x: mask[i]) {
						masks[x]++;
					}
				}
			}
			ans[u] -= sub/2;
			for(int i = 0; i<n; i++) {
				if(vertex[i]!=null) {
					ans[u] += add[graph[u].get(i)];
					for(int j = 0; j<vertex[i].length; j++) {
						add[vertex[i][j]] = 0;
						masks[mask[i][j]] = 0;
					}
				}
			}
			for(int i = 0; i<n; i++) {
				if(vertex[i]!=null) {
					decomp(graph[u].get(i));
				}
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			graph = in.nextUndirectedGraph(n, n-1);
			arr = in.nextIntChar(o -> 1<<(o-'a'));
			clock = 0;
			tin = new int[n];
			tout = new int[n];
			size = new int[n];
			pdfs(0, -1);
			add = new long[n];
			ans = new long[n];
			masks = new int[1<<20];
			visited = new boolean[n];
			decomp(0);
			pw.println(ans);
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

		public void print(long l) {
			print(String.valueOf(l));
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

		public void println(long[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
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

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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

	interface InputReader {
		String next();

		int nextInt();

		default int[] nextIntChar(InputReader.CharToIntFunction f) {
			String s = next();
			int[] ret = new int[s.length()];
			for(int i = 0; i<s.length(); i++) {
				ret[i] = f.apply(s.charAt(i));
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

		interface CharToIntFunction {
			int apply(char c);

		}

	}
}

