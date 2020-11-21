import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.InputMismatchException;
import java.util.StringTokenizer;

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
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			E1BitwiseQueriesEasyVersion solver = new E1BitwiseQueriesEasyVersion();
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

	static class E1BitwiseQueriesEasyVersion {
		public E1BitwiseQueriesEasyVersion() {
		}

		public boolean on(int x, int bit) {
			return Utilities.odd(x >> bit);
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int a = 0, b = 0, c = 0;
			pw.println("XOR", 1, 2);
			pw.println("AND", 1, 2);
			pw.flush();
			boolean[] unknown = new boolean[16];
			{
				int x = in.nextInt(), y = in.nextInt();
				for(int i = 0; i<16; i++) {
					boolean ab = on(x, i), bb = on(y, i);
					if(ab) {
						unknown[i] = true;
					}else if(bb) {
						a |= 1<<i;
						b |= 1<<i;
					}
				}
			}
			pw.println("OR", 1, 3);
			pw.println("AND", 1, 3);
			pw.println("AND", 2, 3);
			pw.flush();
			int x = in.nextInt(), y = in.nextInt(), z = in.nextInt();
			for(int i = 0; i<16; i++) {
				int cur = 1<<i;
				if(unknown[i]) {
					boolean ab = on(x, i), bb = on(y, i), cb = on(z, i);
					if(!ab&&!bb&&!cb) {//000
						b |= cur;
					}else if(ab&&!bb&&cb) {//101
						b |= cur;
						c |= cur;
					}else if(ab&&!bb&&!cb) {//100
						a |= cur;
					}else if(ab&&bb&&!cb) {//110
						a |= cur;
						c |= cur;
					}
				}else if(on(a, i)) {
					if(on(z, i)) {
						c |= cur;
					}
				}else {
					if(on(x, i)) {
						c |= cur;
					}
				}
			}
			int[] arr = new int[n];
			arr[0] = a;
			arr[1] = b;
			arr[2] = c;
			for(int i = 3; i<n; i++) {
				pw.println("XOR", 1, i+1);
				pw.flush();
				arr[i] = in.nextInt()^arr[0];
			}
			pw.print("! ");
			pw.println(arr);
			pw.flush();
		}

	}

	static class Utilities {
		public static boolean odd(int x) {
			return (x&1)>0;
		}

	}

	static class Input implements InputReader {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = new StringTokenizer("");
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

	static interface InputReader {
		int nextInt();

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

		public void print(int i) {
			print(String.valueOf(i));
		}

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
			println();
		}

		public void println() {
			sb.append(lineSeparator);
		}

		public void println(int[] arr) {
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
}

