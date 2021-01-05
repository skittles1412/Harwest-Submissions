import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
			GMonstersAndPotions solver = new GMonstersAndPotions();
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

	static class GMonstersAndPotions {
		private final int iinf = 1_000_000_000;

		public GMonstersAndPotions() {
		}

		public ArrayList<Integer> solve(int[] arr, int[] ind, boolean reverse) {
			if(reverse) {
				Utilities.reverse(arr);
				Utilities.reverse(ind);
				return solve(arr, ind, false);
			}
			int n = arr.length;
			ArrayList<Integer> ret = new ArrayList<>();
			for(int i = 0; i<n; i++) {
				int start = 0;
				int min = 0;
				for(int j = 0; j<n; j++) {
					min = Math.max(0, min);
					if(arr[j]>=iinf) {
						if(arr[j]-iinf>=min) {
							min = 0;
							arr[j] = 0;
							for(int k = start; k<=j; k++) {
								if(arr[k]<iinf) {
									arr[k] = 0;
								}
							}
							start = j+1;
							ret.add(ind[j]);
						}
					}else {
						min -= arr[j];
					}
				}
			}
			return ret;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			int[] arr = new int[n];
			int[] ind = new int[n];
			for(int i = 0; i<m; i++) {
				int x = in.nextInt()-1;
				arr[x] = in.nextInt()+iinf;
				ind[x] = i+1;
			}
			for(int i = 0; i<n; i++) {
				int cur = in.nextInt();
				if(arr[i]==0) {
					arr[i] = cur;
				}
			}
			for(int i = 0; i<n; i++) {
				var l = solve(Arrays.copyOfRange(arr, 0, i+1), Arrays.copyOfRange(ind, 0, i+1), true);
				int add = l.isEmpty() ? 0 : 1;
				var r = solve(Arrays.copyOfRange(arr, i+add, n), Arrays.copyOfRange(ind, i+add, n), false);
				if(l.size()+r.size()==m) {
					pw.println(i+1);
					for(int k: l) {
						pw.print(k+" ");
					}
					for(int k: r) {
						pw.print(k+" ");
					}
					return;
				}
			}
			pw.println("-1");
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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println(int i) {
			println(String.valueOf(i));
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
		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

		public static void reverse(int[] arr, int i, int j) {
			while(i<j) {
				swap(arr, i++, j--);
			}
		}

		public static void reverse(int[] arr) {
			reverse(arr, 0, arr.length-1);
		}

	}
}

