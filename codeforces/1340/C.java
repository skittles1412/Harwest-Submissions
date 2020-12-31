import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;

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
			CNastyaAndUnexpectedGuest solver = new CNastyaAndUnexpectedGuest();
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

	static class CNastyaAndUnexpectedGuest {
		private final long linf = 4_000_000_000_000_000_000L;

		public CNastyaAndUnexpectedGuest() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			in.nextInt();
			int n = in.nextInt();
			int[] arr = in.nextInt(n);
			Utilities.sort(arr);
			int g = in.nextInt(), r = in.nextInt();
			int[][] dist = new int[n][g+1];
			for(int i = 0; i<n; i++) {
				Arrays.fill(dist[i], -1);
			}
			long ans = linf;
			ArrayDeque<CNastyaAndUnexpectedGuest.State> q = new ArrayDeque<>(4*n*g);
			dist[0][g] = 0;
			q.addLast(new CNastyaAndUnexpectedGuest.State(0, g));
			while(!q.isEmpty()) {
				var cur = q.removeFirst();
				if(cur.ind==n-1) {
					ans = Math.min(ans, (long) dist[cur.ind][cur.left]*(r+g)+g-cur.left);
				}else if(cur.left==0&&dist[cur.ind][g]==-1) {
					dist[cur.ind][g] = dist[cur.ind][cur.left]+1;
					q.addLast(new CNastyaAndUnexpectedGuest.State(cur.ind, g));
				}
				for(int i = -1; i<=1; i += 2) {
					int ind = cur.ind+i;
					if(ind>=0&&ind<n) {
						int left = cur.left-Math.abs(arr[cur.ind]-arr[ind]);
						if(left>=0&&dist[ind][left]==-1) {
							dist[ind][left] = dist[cur.ind][cur.left];
							q.addFirst(new CNastyaAndUnexpectedGuest.State(ind, left));
						}
					}
				}
			}
			pw.println(ans==linf ? -1 : ans);
		}

		static class State {
			int ind;
			int left;

			public State(int ind, int left) {
				this.ind = ind;
				this.left = left;
			}

			public String toString() {
				return "State{"+
						"ind="+ind+
						", left="+left+
						'}';
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

	}

	static class Utilities {
		public static void sort(int[] arr) {
			Random rand = new Random();
			int n = arr.length;
			for(int i = 0; i<n; i++) {
				swap(arr, i, rand.nextInt(n));
			}
			Arrays.sort(arr);
		}

		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
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
}

