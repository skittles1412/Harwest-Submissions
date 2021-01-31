import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
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
			EAvoidRainbowCycles solver = new EAvoidRainbowCycles();
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

	static class EAvoidRainbowCycles {
		public EAvoidRainbowCycles() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int m = in.nextInt(), n = in.nextInt();
			int[] a = in.nextInt(m), b = in.nextInt(n);
			ArrayList<int[]> edges = new ArrayList<>();
			long ans = 0;
			for(int i = 0; i<m; i++) {
				int x = in.nextInt();
				for(int j = 0; j<x; j++) {
					int cur = in.nextInt()-1;
					ans += a[i]+b[cur];
					edges.add(new int[] {i, m+cur, a[i]+b[cur]});
				}
			}
			edges.sort(Comparator.comparingInt(o -> -o[2]));
			DSU dsu = new DSU(m+n);
			for(int[] edge: edges) {
				if(dsu.union(edge[0], edge[1])) {
					ans -= edge[2];
				}
			}
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

		public void println(long l) {
			println(String.valueOf(l));
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
		public final int n;
		public int[] parent;
		public int[] size;

		public DSU(int n) {
			this.n = n;
			parent = new int[this.n];
			size = new int[this.n];
			for(int i = 0; i<this.n; i++) {
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

	interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

	}
}

