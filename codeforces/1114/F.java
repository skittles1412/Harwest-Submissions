import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.function.BinaryOperator;

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
			FPleaseAnotherQueriesOnArray solver = new FPleaseAnotherQueriesOnArray();
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

	static class FPleaseAnotherQueriesOnArray {
		private final int mod = (int) (1e9+7);

		public FPleaseAnotherQueriesOnArray() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int[] primes;
			long[] prime = new long[301], inv = Utilities.math.modInverseArr(300, mod), decomp = new long[301];
			{
				var tmp = Utilities.sieve(300);
				primes = new int[tmp.size()];
				for(int i = 0; i<primes.length; i++) {
					primes[i] = tmp.get(i);
				}
			}
			{
				Arrays.fill(prime, -1);
				for(int i = 0; i<primes.length; i++) {
					prime[primes[i]] = i;
				}
				for(int i = 2; i<=300; i++) {
					for(int j = 0; j<=300; j++) {
						if(prime[j]!=-1&&i%j==0) {
							decomp[i] |= 1L<<prime[j];
						}
					}
				}
			}
			int n = in.nextInt(), q = in.nextInt();
			TLazySegmentTree<State, State> st;
			{
				State[] tmp = new State[n];
				for(int i = 0; i<n; i++) {
					int cur = in.nextInt();
					tmp[i] = new State(decomp[cur], cur);
				}
				st = new TLazySegmentTree<>(tmp, State::merge, State::merge, State::apply);
			}
			while(q-->0) {
				if(in.next().charAt(0)=='M') {
					int l = in.nextInt()-1, r = in.nextInt()-1, x = in.nextInt();
					st.update(l, r, new State(decomp[x], x));
				}else {
					int l = in.nextInt()-1, r = in.nextInt()-1;
					State s = st.query(l, r);
					long ans = s.x;
					for(int i = 0; i<62; i++) {
						if(Utilities.on(s.mask, i)) {
							ans = (ans*(primes[i]-1)%mod*(inv[primes[i]]))%mod;
						}
					}
					pw.println(ans);
				}
			}
		}

		private class State {
			long mask;
			long x;

			public State(long mask, long x) {
				this.mask = mask;
				this.x = x;
			}

			public State merge(State s) {
				return new State(mask|s.mask, (x*s.x)%mod);
			}

			public State apply(State s, int length) {
				return new State(mask|s.mask, (s.x*Utilities.math.pow(x, length, mod))%mod);
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
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

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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

	interface InputReader {
		String next();

		int nextInt();

	}

	static class TLazySegmentTree<T, U> {
		public int ql;
		public int qr;
		public T ans;
		public U clazy;
		public T[] values;
		public U[] lazy;
		public final int n;
		public final T[] arr;
		public final BinaryOperator<T> operation;
		public final BinaryOperator<U> lazyCombiner;
		public final TLazySegmentTree.LazyOperation<T, U> lazyOperation;

		public TLazySegmentTree(T[] arr, BinaryOperator<T> operation, BinaryOperator<U> lazyCombiner, TLazySegmentTree.LazyOperation<T, U> lazyOperation) {
			n = arr.length;
			this.arr = arr;
			this.operation = operation;
			this.lazyCombiner = lazyCombiner;
			this.lazyOperation = lazyOperation;
			values = (T[]) new Object[n*4];
			lazy = (U[]) new Object[n*4];
			build(1, 0, n-1);
		}

		public U combine(U lazy1, U lazy2) {
			if(lazy1==null) {
				return lazy2;
			}else if(lazy2==null) {
				return lazy1;
			}
			return lazyCombiner.apply(lazy1, lazy2);
		}

		public T applyToSegment(U lazy, T value, int length) {
			if(lazy==null) {
				return value;
			}
			return lazyOperation.applyToSegment(lazy, value, length);
		}

		public void maintain(int o, int l, int r) {
			if(l<r) {
				values[o] = operation.apply(values[o*2], values[o*2+1]);
			}else {
				values[o] = arr[l];
			}
			values[o] = applyToSegment(lazy[o], values[o], r-l+1);
		}

		public void pushdown(int o) {
			lazy[o*2] = combine(lazy[o*2], lazy[o]);
			lazy[o*2+1] = combine(lazy[o*2+1], lazy[o]);
			lazy[o] = null;
		}

		public void build(int o, int l, int r) {
			if(l==r) {
				values[o] = arr[l];
			}else {
				int mid = (l+r)/2, lc = o*2, rc = o*2+1;
				build(lc, l, mid);
				build(rc, mid+1, r);
				values[o] = operation.apply(values[lc], values[rc]);
			}
		}

		public void update(int o, int l, int r) {
			if(l>=ql&&r<=qr) {
				lazy[o] = combine(lazy[o], clazy);
			}else {
				pushdown(o);
				int mid = (l+r)/2, lc = o*2, rc = o*2+1;
				if(ql<=mid) {
					update(lc, l, mid);
				}else {
					maintain(lc, l, mid);
				}
				if(qr>mid) {
					update(rc, mid+1, r);
				}else {
					maintain(rc, mid+1, r);
				}
			}
			maintain(o, l, r);
		}

		public void query(int o, int l, int r, U clazy) {
			if(l>=ql&&r<=qr) {
				if(ans==null) {
					ans = applyToSegment(clazy, values[o], r-l+1);
				}else {
					ans = operation.apply(ans, applyToSegment(clazy, values[o], r-l+1));
				}
			}else {
				clazy = combine(lazy[o], clazy);
				int mid = (l+r)/2, lc = o*2, rc = o*2+1;
				if(ql<=mid) {
					query(lc, l, mid, clazy);
				}
				if(qr>mid) {
					query(rc, mid+1, r, clazy);
				}
			}
		}

		public void update(int l, int r, U lazy) {
			ql = l;
			qr = r;
			clazy = lazy;
			update(1, 0, n-1);
		}

		public T query(int l, int r) {
			ql = l;
			qr = r;
			ans = null;
			query(1, 0, n-1, null);
			return ans;
		}

		public interface LazyOperation<T, U> {
			T applyToSegment(U lazy, T value, int length);

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

		public static boolean on(long x, int bit) {
			return (x >> bit&1)>0;
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

			public static long[] modInverseArr(int n, long mod) {
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
}

