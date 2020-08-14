import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.InputMismatchException;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Closeable;
import java.io.BufferedReader;
import java.io.InputStream;

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
			DColoredRectangles solver = new DColoredRectangles();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DColoredRectangles {
		int a;
		int b;
		int c;
		int[] ar;
		int[] br;
		int[] cr;
		long[][][] memo;

		public DColoredRectangles() {
		}

		public long dp(int ai, int bi, int ci) {
			int cnt = 0;
			if(ai==a) {
				cnt++;
			}
			if(bi==b) {
				cnt++;
			}
			if(ci==c) {
				cnt++;
			}
			if(cnt>=2) {
				return 0;
			}else if(memo[ai][bi][ci]!=-1) {
				return memo[ai][bi][ci];
			}
			long ans = 0;
			if(ai<a&&bi<b) {
				ans = Math.max(ans, ar[ai]*br[bi]+dp(ai+1, bi+1, ci));
			}
			if(bi<b&&ci<c) {
				ans = Math.max(ans, br[bi]*cr[ci]+dp(ai, bi+1, ci+1));
			}
			if(ai<a&&ci<c) {
				ans = Math.max(ans, ar[ai]*cr[ci]+dp(ai+1, bi, ci+1));
			}
			return memo[ai][bi][ci] = ans;
		}

		public void solve(int kase, Input in, Output pw) {
			a = in.nextInt();
			b = in.nextInt();
			c = in.nextInt();
			ar = new int[a];
			br = new int[b];
			cr = new int[c];
			for(int i = 0; i<a; i++) {
				ar[i] = in.nextInt();
			}
			for(int i = 0; i<b; i++) {
				br[i] = in.nextInt();
			}
			for(int i = 0; i<c; i++) {
				cr[i] = in.nextInt();
			}
			Utilities.rsort(ar);
			Utilities.rsort(br);
			Utilities.rsort(cr);
			memo = new long[a+1][b+1][c+1];
			for(int i = 0; i<=a; i++) {
				for(int j = 0; j<=b; j++) {
					Arrays.fill(memo[i][j], -1);
				}
			}
			pw.println(dp(0, 0, 0));
			Utilities.Debug.dbg(memo);
		}

	}

	static class Utilities {
		public static void rsort(int[] arr) {
			Random rand = new Random();
			int n = arr.length;
			for(int i = 0; i<n; i++) {
				swap(arr, i, rand.nextInt(n));
			}
			Arrays.sort(arr);
			reverse(arr, 0, n-1);
		}

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

		public static class Debug {
			public static final boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

			private static <T> String ts(T t) {
				if(t==null) {
					return "null";
				}
				try {
					return ts((Iterable) t);
				}catch(ClassCastException e) {
					if(t instanceof int[]) {
						String s = Arrays.toString((int[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof long[]) {
						String s = Arrays.toString((long[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof char[]) {
						String s = Arrays.toString((char[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof double[]) {
						String s = Arrays.toString((double[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof boolean[]) {
						String s = Arrays.toString((boolean[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}
					try {
						return ts((Object[]) t);
					}catch(ClassCastException e1) {
						return t.toString();
					}
				}
			}

			private static <T> String ts(T[] arr) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: arr) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			private static <T> String ts(Iterable<T> iter) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: iter) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			public static void dbg(Object... o) {
				if(LOCAL) {
					System.err.print("Line #"+Thread.currentThread().getStackTrace()[2].getLineNumber()+": [");
					for(int i = 0; i<o.length; i++) {
						if(i!=0) {
							System.err.print(", ");
						}
						System.err.print(ts(o[i]));
					}
					System.err.println("]");
				}
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

		public void println(long l) {
			println(String.valueOf(l));
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
}


