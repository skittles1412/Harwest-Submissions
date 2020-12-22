import java.io.*;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.function.LongBinaryOperator;

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
			FAttackOnRedKingdom solver = new FAttackOnRedKingdom();
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

	static class FAttackOnRedKingdom {
		LongBinaryOperator[][][] grundy;

		public FAttackOnRedKingdom() {
			int n = 5;
			grundy = new LongBinaryOperator[n+1][n+1][n+1];
			for(int i = 1; i<=5; i++) {
				for(int j = 1; j<=5; j++) {
					for(int k = 1; k<=5; k++) {
						var p = new FAttackOnRedKingdom.Provider(i, j, k);
						HashMap<FAttackOnRedKingdom.State, Integer> hm = new HashMap<>();
						int[][] prev = new int[5][3];
						for(int b = 1; b<5; b++) {
							prev[b] = p.get(b-1);
						}
						int start, len;
						for(int b = 4; ; b++) {
							Utilities.leftShift(prev, 1);
							prev[4] = p.get(b);
							FAttackOnRedKingdom.State cur = new FAttackOnRedKingdom.State(prev);
							if(hm.containsKey(cur)) {
								int val = hm.get(cur);
								start = val;
								len = b-4-val;
								break;
							}else {
								hm.put(cur, b-4);
							}
						}
//						Utilities.Debug.dbg(start, len);
						grundy[i][j][k] = (a, b) -> {
							if(a<start) {
								return p.dp((int) a, (int) b);
							}
							return p.dp((int) ((a-start)%len+start), (int) b);
						};
					}
				}
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), x = in.nextInt(), y = in.nextInt(), z = in.nextInt();
			long[] arr = in.nextLong(n);
			long xor = 0;
			long[] sub = new long[] {x, y, z};
			long[] val = new long[n];
			for(int i = 0; i<n; i++) {
				xor ^= val[i] = grundy[x][y][z].applyAsLong(arr[i], 0);
			}
			int ans = 0;
			for(int i = 0; i<n; i++) {
				xor ^= val[i];
				for(int j = 0; j<3; j++) {
					if((xor^grundy[x][y][z].applyAsLong(Math.max(0, arr[i]-sub[j]), j))==0) {
						ans++;
					}
				}
				xor ^= val[i];
			}
			pw.println(ans);
		}

		static class Provider {
			int n;
			int[] v;
			int[][] dp;

			public static boolean valid(int cur, int prev) {
				return prev==0||cur!=prev;
			}

			public Provider(int... v) {
				this.v = v;
				n = 16;
				dp = new int[16][3];
				for(int i = 1; i<16; i++) {
					dp[i][0] = dp[i][1] = dp[i][2] = -1;
				}
			}

			public int[] get(int left) {
				for(int i = 0; i<3; i++) {
					dp(left, i);
				}
				return dp[left];
			}

			private int dp(int left, int prev) {
				if(left>=n) {
					int[][] ndp = new int[n*2][3];
					System.arraycopy(dp, 0, ndp, 0, n);
					for(int j = n; j<2*n; j++) {
						ndp[j][0] = ndp[j][1] = ndp[j][2] = -1;
					}
					dp = ndp;
					n *= 2;
				}
				if(dp[left][prev]!=-1) {
					return dp[left][prev];
				}
				BitSet ans = new BitSet();
				for(int i = 0; i<3; i++) {
					if(valid(i, prev)) {
						ans.set(dp(Math.max(0, left-v[i]), i));
					}
				}
				return dp[left][prev] = ans.nextClearBit(0);
			}

		}

		static class State {
			int[][] a;

			public State(int[][] a) {
				this.a = Utilities.deepClone(a);
			}

			public boolean equals(Object o) {
				if(this==o) return true;
				if(o==null||getClass()!=o.getClass()) return false;
				FAttackOnRedKingdom.State state = (FAttackOnRedKingdom.State) o;
				return Arrays.deepEquals(a, state.a);
			}

			public int hashCode() {
				return Arrays.deepHashCode(a);
			}

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

	static class FastReader implements InputReader {
		final private int BUFFER_SIZE = 1<<16;
		private final DataInputStream din;
		private final byte[] buffer;
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

		public long nextLong() {
			long ret = 0;
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

	static class Utilities {
		public static <T> void leftShift(T[] arr, int offSet) {
			T[] tmp = (T[]) new Object[offSet];
			System.arraycopy(arr, 0, tmp, 0, offSet);
			System.arraycopy(arr, offSet, arr, 0, arr.length-offSet);
			System.arraycopy(tmp, 0, arr, arr.length-offSet, offSet);
		}

		public static int[][] deepClone(int[][] arr) {
			int[][] ret = new int[arr.length][];
			for(int i = 0; i<arr.length; i++) {
				ret[i] = arr[i].clone();
			}
			return ret;
		}

		public static class Debug {
			public static boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

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

	interface InputReader {
		int nextInt();

		long nextLong();

		default long[] nextLong(int n) {
			long[] ret = new long[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextLong();
			}
			return ret;
		}

	}
}

