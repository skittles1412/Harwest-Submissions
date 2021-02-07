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
import java.util.function.IntUnaryOperator;

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
			ASearchingLocalMinimum solver = new ASearchingLocalMinimum();
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

	static class ASearchingLocalMinimum {
		private final int iinf = 1_000_000_000;

		public ASearchingLocalMinimum() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			IntUnaryOperator query = (x) -> {
				if(x<0||x>=n) {
					return iinf;
				}
				pw.println("?", x+1);
				pw.flush();
				return in.nextInt();
			};
			int l = 0, r = n-1;
			int cur = query.applyAsInt(0);
			while(l<r) {
				int mid = (l+r)/2;
				int x = query.applyAsInt(mid), xl = query.applyAsInt(mid-1), xr = query.applyAsInt(mid+1);
				if(x<xl&&x<xr) {
					pw.println("!", mid+1);
					pw.flush();
					return;
				}
				if(x>cur) {
					r = mid-1;
				}else {
					if(x<xl) {
						l = mid+1;
						cur = xr;
					}else {
						r = mid-1;
					}
				}
			}
			pw.println("!", l+1);
			pw.flush();
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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE/2) {
				flushToBuffer();
			}
		}

		public void print(int i) {
			print(String.valueOf(i));
		}

		public void print(long l) {
			print(String.valueOf(l));
		}

		public void print(double d) {
			print(String.valueOf(d));
		}

		public void print(char[] c) {
			print(String.valueOf(c));
		}

		public void print(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(o[i]);
			}
		}

		public void print(Object o) {
			if(o==null) {
				print("null");
			}else if(o instanceof Iterable) {
				print((Iterable<?>) o);
			}else if(o instanceof int[]) {
				print((int[]) o);
			}else if(o instanceof long[]) {
				print((long[]) o);
			}else if(o instanceof char[]) {
				print((char[]) o);
			}else if(o instanceof double[]) {
				print((double[]) o);
			}else if(o instanceof Object[]) {
				print((Object[]) o);
			}else {
				print(o.toString());
			}
		}

		public void print(int[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
			}
		}

		public void print(long[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
			}
		}

		public void print(double[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
			}
		}

		public <T> void print(Iterable<T> iter) {
			boolean first = true;
			for(T t: iter) {
				if(!first) {
					print(" ");
				}
				first = false;
				print(t);
			}
		}

		public void println() {
			sb.append(lineSeparator);
		}

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(o[i]);
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
}

