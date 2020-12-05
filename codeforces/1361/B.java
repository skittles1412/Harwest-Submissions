import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

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
			BJohnnyAndGrandmaster solver = new BJohnnyAndGrandmaster();
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

	static class BJohnnyAndGrandmaster {
		private final int mod = (int) (1e9+7);
		final int maxn = (int) 1e6;

		public BJohnnyAndGrandmaster() {
		}

		public long mod(long x) {
			return x>=mod ? x-mod : x;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), p = in.nextInt();
			int[] arr = in.nextInt(n);
			if(p==1) {
				pw.println(n&1);
				return;
			}
			MultiHashSet<Integer> ms = new MultiHashSet<>();
			ArrayList<Integer> al = new ArrayList<>();
			for(int i = 0; i<n; i++) {
				if(!ms.containsKey(arr[i])) {
					al.add(arr[i]);
				}
				ms.add(arr[i]);
			}
			al.sort(null);
			for(int x = al.size()-1; x>=0; x--) {
				int k = al.get(x);
				Integer v = ms.get(k);
				if(v==null) {
					continue;
				}
				if(Utilities.odd(v)) {
					int need = p;
					for(int i = k-1; i>=0&&need>0; i--, need *= p) {
						need -= ms.count(i);
						if(need>maxn/p) {
							break;
						}
					}
					if(need<=0) {
						need = p;
						for(int i = k-1; i>=0&&need>0; i--, need *= p) {
							int cur = ms.count(i);
							ms.delete(i, Math.min(cur, need));
							need -= cur;
							if(need>maxn/p) {
								break;
							}
						}
					}else {
						long ans = Utilities.math.pow(p, k, mod);
						for(int i: al) {
							if(i<k) {
								ans = mod(ans+mod-(Utilities.math.pow(p, i, mod)*ms.get(i))%mod);
							}else {
								break;
							}
						}
						pw.println(ans);
						return;
					}
				}
			}
			pw.println("0");
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

	static class MultiHashSet<T> extends HashMap<T, Integer> implements MultiSet<T> {
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

	static interface MultiSet<T> extends Map<T, Integer> {
		default int count(T key) {
			Integer cnt = get(key);
			return cnt==null ? 0 : cnt;
		}

		default void add(T key) {
			add(key, 1);
		}

		default void add(T key, int count) {
			if(count<0) {
				delete(key, -count);
			}else if(count>0) {
				compute(key, (k, v) -> {
					if(v==null) {
						v = 0;
					}
					return v+count;
				});
			}
		}

		default void delete(T key, int count) {
			if(count<0) {
				add(key, -count);
			}else if(count>0) {
				compute(key, (k, v) -> {
					if(v==null) {
						v = 0;
					}
					v -= count;
					return v<=0 ? null : v;
				});
			}
		}

	}

	static class Utilities {
		public static boolean odd(int x) {
			return (x&1)>0;
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

		}

	}
}

