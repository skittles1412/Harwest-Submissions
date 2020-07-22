import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
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
			BCubesForMasha solver = new BCubesForMasha();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BCubesForMasha {
		public BCubesForMasha() {
		}

		public void solve(int testNumber, Input in, Output pw) {
			int n = in.nextInt();
			int[][] cubes = new int[n][6];
			boolean[] possible = new boolean[1000];
			possible[0] = true;
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<6; j++) {
					cubes[i][j] = in.nextInt();
					possible[cubes[i][j]] = true;
				}
			}
			if(n>1) {
				for(int i = 0; i<n; i++) {
					for(int j = i+1; j<n; j++) {
						for(int k = 0; k<6; k++) {
							for(int l = 0; l<6; l++) {
								possible[cubes[i][k]*10+cubes[j][l]] =
										possible[cubes[j][l]*10+cubes[i][k]] = true;
							}
						}
					}
				}
			}
			if(n>2) {
				for(int i = 0; i<6; i++) {
					for(int j = 0; j<6; j++) {
						for(int k = 0; k<6; k++) {
							int[] arr = new int[] {0, 1, 2};
							do {
								possible[cubes[arr[0]][i]*100+cubes[arr[1]][j]*10+cubes[arr[2]][k]]
										= true;
							}while(Utilities.nextPermutation(arr));
						}
					}
				}
			}
			for(int i = 0; i<1000; i++) {
				if(!possible[i]) {
					pw.println(i-1);
					return;
				}
			}
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

	static class Utilities {
		public static void swap(int[] arr, int i, int j) {
			arr[i] ^= arr[j];
			arr[j] ^= arr[i];
			arr[i] ^= arr[j];
		}

		public static void reverse(int[] arr, int i, int j) {
			while(i<j) {
				int cur = arr[i];
				arr[i++] = arr[j];
				arr[j--] = cur;
			}
		}

		public static boolean nextPermutation(int[] data) {
			if(data.length<=1) {
				return false;
			}
			int last = data.length-2;
			while(last >= 0) {
				if(data[last]<data[last+1]) {
					break;
				}
				last--;
			}
			if(last<0)
				return false;
			int nextGreater = data.length-1;
			for(int i = data.length-1; i>last; i--) {
				if(data[i]>data[last]) {
					nextGreater = i;
					break;
				}
			}
			swap(data, nextGreater, last);
			reverse(data, last+1, data.length-1);
			return true;
		}

	}
}

