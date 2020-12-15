import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
			FTheTreasureOfTheSegments solver = new FTheTreasureOfTheSegments();
			int testCount = in.nextInt();
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class FTheTreasureOfTheSegments {
		public FTheTreasureOfTheSegments() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = n*2+5;
			int[][] arr = in.nextInt(n, 2);
			Arrays.sort(arr, Arrays::compare);
			{
				int[] tmp = new int[n*2];
				for(int i = 0; i<n; i++) {
					tmp[i*2] = arr[i][0];
					tmp[i*2+1] = arr[i][1];
				}
				Arrays.sort(tmp);
				HashMap<Integer, Integer> hm = new HashMap<>();
				int prev = -1, ind = 0;
				for(int i = 0; i<2*n; i++) {
					if(tmp[i]!=prev) {
						hm.put(tmp[i], ind++);
					}
					prev = tmp[i];
				}
				for(int i = 0; i<n; i++) {
					arr[i][0] = hm.get(arr[i][0]);
					arr[i][1] = hm.get(arr[i][1]);
				}
			}
			FenwickTree start = new FenwickTree(m), end = new FenwickTree(m);
			for(int i = 0; i<n; i++) {
				start.add(arr[i][0], 1);
			}
			long ans = 0;
			for(int i = 0; i<n; i++) {
				ans = Math.max(ans, 1+end.sum(arr[i][0], arr[i][1])+start.sum(arr[i][0]+1, arr[i][1]));
				end.add(arr[i][1], 1);
			}
			pw.println(n-ans);
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

	static class FenwickTree {
		private int n;
		public long[] sum;
		public long[] arr;

		public FenwickTree(int n) {
			this.n = n;
			sum = new long[n+1];
			arr = new long[n];
		}

		public FenwickTree(int[] arr) {
			n = arr.length;
			sum = new long[n+1];
			this.arr = new long[n];
			for(int i = 0; i<n; i++) {
				add(i, arr[i]);
			}
		}

		public FenwickTree(long[] arr) {
			n = arr.length;
			sum = new long[n+1];
			this.arr = new long[n];
			for(int i = 0; i<n; i++) {
				add(i, arr[i]);
			}
		}

		public void add(int ind, long val) {
			arr[ind] += val;
			ind++;
			while(ind<=n) {
				sum[ind] += val;
				ind += ind&-ind;
			}
		}

		public long psum(int x) {
			long ret = 0;
			while(x>0) {
				ret += sum[x];
				x -= x&-x;
			}
			return ret;
		}

		public long sum(int l, int r) {
			return psum(r+1)-psum(l);
		}

		public String toString() {
			StringBuilder ret = new StringBuilder(n<<2);
			ret.append("{");
			for(int i = 0; i<n; i++) {
				if(i!=0) {
					ret.append(", ");
				}
				ret.append(sum(i, i));
			}
			return ret.append("}").toString();
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
}

