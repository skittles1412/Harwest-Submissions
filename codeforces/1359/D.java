import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractCollection;
import java.util.AbstractList;
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
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			DYetAnotherYetAnotherTask solver = new DYetAnotherYetAnotherTask();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DYetAnotherYetAnotherTask {
		private final int iinf = 1_000_000_000;
		int n;
		int[] arr;
		int[][] start;
		int[][] end;
		ArrayList<Integer>[] values;

		public DYetAnotherYetAnotherTask() {
		}

		public int solve(int l, int r, int v) {
			if(l>r) {
				return -iinf;
			}
			int ans = -iinf;
			for(int i = l; i<=r; i++) {
				if(arr[i]==v-30) {
					ans = Math.max(ans, start[v][i]+end[v][i]);
				}
			}
			if(v!=-1) {
				int prev = l;
				for(int i = Utilities.lowerBound(values[v], l); i<values[v].size()&&values[v].get(i)<=r; i++) {
					ans = Math.max(ans, solve(prev, i-1, v-1));
					prev = i+1;
				}
				ans = Math.max(ans, solve(prev, r, v-1));
			}
			return ans;
		}

		public void solve(int kase, InputReader in, Output pw) {
			values = new ArrayList[61];
			for(int i = 0; i<=60; i++) {
				values[i] = new ArrayList<>();
			}
			n = in.nextInt();
			arr = new int[n];
			for(int i = 0; i<n; i++) {
				values[(arr[i] = in.nextInt())+30].add(i);
			}
			start = new int[61][n];
			end = new int[61][n];
			for(int i = 60; i>=0; i--) {
				for(int j = n-2; j>=0; j--) {
					if(arr[j+1]<=i-30) {
						start[i][j] = Math.max(0, arr[j+1]+start[i][j+1]);
					}
				}
				for(int j = 1; j<n; j++) {
					if(arr[j-1]<=i-30) {
						end[i][j] = Math.max(0, arr[j-1]+end[i][j-1]);
					}
				}
			}
			pw.println(solve(0, n-1, 60));
		}

	}

	static class Utilities {
		public static <T extends Comparable<T>> int lowerBound(AbstractList<T> arr, T target) {
			int l = 0, h = arr.size();
			while(l<h) {
				int mid = l+h >> 1;
				if(arr.get(mid).compareTo(target)<0) {
					l = mid+1;
				}else {
					h = mid;
				}
			}
			return l;
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

	static interface InputReader {
		int nextInt();

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


