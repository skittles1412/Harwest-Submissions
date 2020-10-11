import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
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
			ETwoArrays solver = new ETwoArrays();
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

	static class ETwoArrays {
		private final int mod = 998244353;
		int n;
		int m;
		int[] a;
		int[] b;
		int[] min;
		HashMap<Integer, ArrayDeque<Integer>> hm;

		public ETwoArrays() {
		}

		public long solve(int x, int y) {
			if(x==n) {
				return 0;
			}
			if(min[x]!=b[y]) {
				return 0;
			}
			if(y==m-1) {
				return 1;
			}
			var v = hm.get(b[y+1]);
			if(v==null||v.isEmpty()) {
				return 0;
			}
			int start = Utilities.lowerBound(min, b[y+1])-1, end = v.getLast();
			for(int i = x; i<end; i++) {
				hm.get(a[i]).removeFirst();
			}
			return (end-start)*solve(end, y+1)%mod;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			m = in.nextInt();
			a = in.nextInt(n);
			b = in.nextInt(m);
			min = new int[n];
			hm = new HashMap<>(n<<1);
			min[n-1] = a[n-1];
			for(int i = n-2; i>=0; i--) {
				min[i] = Math.min(a[i], min[i+1]);
			}
			for(int i = 0; i<n; i++) {
				var v = hm.get(a[i]);
				if(v==null) {
					v = new ArrayDeque<>();
					v.addLast(i);
					hm.put(a[i], v);
				}else {
					v.addLast(i);
				}
			}
			pw.println(solve(0, 0));
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

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
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

	static class Utilities {
		public static int lowerBound(int[] arr, int target) {
			int l = 0, h = arr.length;
			while(l<h) {
				int mid = l+h >> 1;
				if(arr[mid]<target) {
					l = mid+1;
				}else {
					h = mid;
				}
			}
			return l;
		}

	}
}

