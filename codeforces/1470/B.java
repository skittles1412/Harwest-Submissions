import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

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
			BStrangeDefinition solver = new BStrangeDefinition();
			int testCount = in.nextInt();
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<26);
		thread.start();
		thread.join();
	}

	static class BStrangeDefinition {
		int[] map;
		Utilities.PrimeFactorizer pf = new Utilities.PrimeFactorizer(1_000_000);

		public BStrangeDefinition() {
			map = new int[1001];
			var cur = Utilities.sieve(1000);
			for(int i = 0; i<cur.size(); i++) {
				map[cur.get(i)] = i;
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n);
			MultiHashSet<Pair<BitSet, Integer>> mhs = new MultiHashSet<>();
			for(int i = 0; i<n; i++) {
				var cur = pf.factor(arr[i]);
				int mul = 1;
				BitSet bs = new BitSet();
				for(var v: cur) {
					if(v.a>=1000) {
						mul = v.a;
					}else if(Utilities.odd(v.b)) {
						bs.set(map[v.a]);
					}
				}
				mhs.add(new Pair<>(bs, mul));
			}
			int ans = 0, z = 0;
			for(var v: mhs.entrySet()) {
				ans = Math.max(ans, v.getValue());
				if((v.getKey().a.cardinality()==0&&v.getKey().b==1)||Utilities.even(v.getValue())) {
					z += v.getValue();
				}
			}
			z = Math.max(z, ans);
			int q = in.nextInt();
			while(q-->0) {
				long x = in.nextLong();
				if(x==0) {
					pw.println(ans);
				}else {
					pw.println(z);
				}
			}
		}

	}

	static interface MultiSet<T> extends Map<T, Integer> {
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

	static interface InputReader {
		int nextInt();

		long nextLong();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

	}

	static class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
		public T1 a;
		public T2 b;

		public Pair(Pair<T1, T2> p) {
			this(p.a, p.b);
		}

		public Pair(T1 a, T2 b) {
			this.a = a;
			this.b = b;
		}

		public String toString() {
			return a+" "+b;
		}

		public int hashCode() {
			return Objects.hash(a, b);
		}

		public boolean equals(Object o) {
			if(o instanceof Pair) {
				Pair p = (Pair) o;
				return a.equals(p.a)&&b.equals(p.b);
			}
			return false;
		}

		public int compareTo(Pair<T1, T2> p) {
			int cmp = ((Comparable<T1>) a).compareTo(p.a);
			if(cmp==0) {
				return ((Comparable<T2>) b).compareTo(p.b);
			}
			return cmp;
		}

	}

	static class Utilities {
		public static ArrayList<Integer> sieve(int n) {
			ArrayList<Integer> ans = new ArrayList<>();
			boolean[] prime = new boolean[n+1];
			Arrays.fill(prime, true);
			for(int i = 2; i<=n; i++) {
				if(prime[i]) {
					ans.add(i);
				}
				if((long) i*i>n) {
					continue;
				}
				for(int j = i*i; j<=n; j += i) {
					prime[j] = false;
				}
			}
			return ans;
		}

		public static boolean even(int x) {
			return (x&1)==0;
		}

		public static boolean odd(int x) {
			return (x&1)>0;
		}

		public static class PrimeFactorizer {
			private final int[] minDiv;

			public PrimeFactorizer(int n) {
				minDiv = new int[n+1];
				boolean[] prime = new boolean[n+1];
				Arrays.fill(prime, true);
				for(int i = 2; i<=n; i++) {
					if(prime[i]) {
						minDiv[i] = i;
						if((long) i*i<=n) {
							for(int j = i*i; j<=n; j += i) {
								if(prime[j]) {
									prime[j] = false;
									minDiv[j] = i;
								}
							}
						}
					}
				}
			}

			public ArrayList<Pair<Integer, Integer>> factor(int x) {
				ArrayList<Pair<Integer, Integer>> ans = new ArrayList<>();
				while(x>1) {
					int div = minDiv[x];
					int cnt = 1;
					while(minDiv[x /= div]==div) {
						cnt++;
					}
					ans.add(new Pair<>(div, cnt));
				}
				return ans;
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

	static class MultiHashSet<T> extends HashMap<T, Integer> implements MultiSet<T> {
	}
}

