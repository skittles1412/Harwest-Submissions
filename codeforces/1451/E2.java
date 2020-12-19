import java.io.*;
import java.util.Arrays;
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
			E2BitwiseQueriesHardVersion solver = new E2BitwiseQueriesHardVersion();
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

	static class E2BitwiseQueriesHardVersion {
		public E2BitwiseQueriesHardVersion() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = new int[n];
			{
				for(int i = 2; i<=n; i++) {
					pw.println("XOR", "1", i);
					pw.flush();
					arr[i-1] = in.nextInt();
				}
			}
			int val = -1;
			{
				int[] ind = new int[n];
				Arrays.fill(ind, -1);
				for(int i = 0; i<n; i++) {
					if(ind[arr[i]]!=-1) {
						pw.println("AND", ind[arr[i]]+1, i+1);
						pw.flush();
						val = in.nextInt()^arr[i];
						break;
					}
					ind[arr[i]] = i;
				}
			}
			if(val==-1) {
				int a = -1;
				for(int i = 0; i<n; i++) {
					if(arr[i]==n-1) {
						a = i;
						break;
					}
				}
				int b = a==n-1 ? 1 : a+1;
				pw.println("AND", a+1, b+1);
				pw.flush();
				int and = in.nextInt();
				int sum = (arr[a]^arr[b])+and*2;
				pw.println("OR", 1, b+1);
				pw.flush();
				int or = in.nextInt();
				for(int i = 0; i<n; i++) {
					int av = n-1-i, bv = sum-av;
					if((i|bv)==or&&(av&bv)==and) {
						val = i;
						break;
					}
				}
			}
			pw.print("!");
			for(int i = 0; i<n; i++) {
				pw.print(" "+(val^arr[i]));
			}
			pw.println();
			pw.flush();
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

	interface InputReader {
		int nextInt();

	}
}

