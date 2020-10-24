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
			DBanditInACity solver = new DBanditInACity();
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

	static class DBanditInACity {
		private final long linf = 4_000_000_000_000_000_000L;
		int n;
		int[] parent;
		int[] rank;
		int[] order;
		boolean[] leaf;
		long[] arr;

		public DBanditInACity() {
		}

		public int rank(int u) {
			if(rank[u]!=-1) {
				return rank[u];
			}
			leaf[parent[u]] = false;
			return rank[u] = rank(parent[u])+1;
		}

		public boolean valid(long x) {
			if(x==0) {
				boolean ans = true;
				for(int i = 0; i<n; i++) {
					ans &= arr[i]==0;
				}
				return ans;
			}
			long[] sum = new long[n], cnt = new long[n];
			for(int i: order) {
				if(leaf[i]) {
					cnt[i]++;
				}
				sum[i] += arr[i];
				if((sum[i]+x-1)/x>cnt[i]) {
					return false;
				}
				sum[parent[i]] += sum[i];
				cnt[parent[i]] += cnt[i];
			}
			return true;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			parent = new int[n];
			for(int i = 1; i<n; i++) {
				parent[i] = in.nextInt()-1;
			}
			leaf = new boolean[n];
			Arrays.fill(leaf, true);
			rank = new int[n];
			Arrays.fill(rank, -1);
			rank[0] = 0;
			for(int i = 0; i<n; i++) {
				rank[i] = rank(i);
			}
			arr = in.nextLong(n);
			{
				Integer[] order = new Integer[n];
				for(int i = 0; i<n; i++) {
					order[i] = i;
				}
				Arrays.sort(order, Comparator.comparingInt(o -> -rank[o]));
				this.order = new int[n];
				for(int i = 0; i<n; i++) {
					this.order[i] = order[i];
				}
			}
			long l = 0, r = linf;
			while(l<r) {
				long mid = l+r >> 1;
				if(valid(mid)) {
					r = mid;
				}else {
					l = mid+1;
				}
			}
			pw.println(l);
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

	static interface InputReader {
		int nextInt();

		long nextLong();

		default long[] nextLong(int n) {
			long[] ret = new long[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextLong();
			}
			return ret;
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

		public long nextLong() {
			long ret = 0;
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
}
