import java.io.*;
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
			F2WrongAnswerOnTest233HardVersion solver = new F2WrongAnswerOnTest233HardVersion();
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

	static class F2WrongAnswerOnTest233HardVersion {
		private final int mod = 998244353;
		final long half = Utilities.math.pow(2, mod-2, mod);

		public F2WrongAnswerOnTest233HardVersion() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), k = in.nextInt();
			int[] arr = in.nextInt(n);
			if(k==1) {
				pw.println("0");
				return;
			}
			int cnt = 0;
			long mul = 1;
			for(int i = 0; i<n; i++) {
				if(arr[i]==arr[(i+1)%n]) {
					mul = (mul*k)%mod;
				}else {
					cnt++;
				}
			}
//			Utilities.Debug.dbg(mul, cnt);
			long ans = 0;
			Utilities.BinomCoefficient bc = new Utilities.BinomCoefficient(cnt, mod);
			for(int i = 0; i<=cnt; i++) {
				long add;
				if(Utilities.odd(i)) {
					add = Utilities.math.pow(2, i-1, mod);
				}else {
					add = (Utilities.math.pow(2, i, mod)+mod-bc.choose(i, i/2))*half%mod;
				}
				ans += Utilities.math.pow(k-2, cnt-i, mod)*bc.choose(cnt, cnt-i)%mod*add%mod;
			}
			pw.println(ans%mod*mul%mod);
		}

	}

	interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
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

	static class Utilities {
		public static boolean odd(int x) {
			return (x&1)>0;
		}

		public static long[] factorial(int n, long mod) {
			long[] ret = new long[n+1];
			ret[0] = 1;
			for(int i = 1; i<=n; i++) {
				ret[i] = (i*ret[i-1])%mod;
			}
			return ret;
		}

		public static long[] inverseFactorial(int n, long mod) {
			long[] ret = Utilities.math.modInverse(n, mod);
			ret[0] = 1;
			Arrays.parallelPrefix(ret, (a, b) -> a*b%mod);
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

		public static class BinomCoefficient {
			private final int n;
			private final long mod;
			private final long[] fact;
			private final long[] invFact;

			public BinomCoefficient(int n, long mod) {
				this.n = n;
				this.mod = mod;
				fact = factorial(n, mod);
				invFact = inverseFactorial(n, mod);
			}

			public long choose(int n, int k) {
				return fact[n]*invFact[k]%mod*invFact[n-k]%mod;
			}

		}

		public static class math {
			public static long pow(long base, long exp, long mod) {
				long ans = 1, cur = base;
				while(exp>0) {
					if((exp&1)>0) {
						ans = (ans*cur)%mod;
					}
					cur = (cur*cur)%mod;
					exp >>= 1;
				}
				return ans;
			}

			public static long[] modInverse(int n, long mod) {
				if(n==0) {
					return new long[] {0};
				}
				long[] ret = new long[n+1];
				ret[1] = 1;
				for(int i = 2; i<=n; i++) {
					ret[i] = (-(mod/i)*ret[(int) (mod%i)])%mod;
					ret[i] += mod;
				}
				return ret;
			}

		}

	}
}

