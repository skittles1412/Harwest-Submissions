import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.function.BiFunction;
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
			CFixedPointRemoval solver = new CFixedPointRemoval();
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

	static class CFixedPointRemoval {
		TFenwickTree<Integer> ft;
		int n;

		public CFixedPointRemoval() {
		}

		public int upperBound(int h, int target) {
			int l = -1;
			while(l<h) {
				int mid = l+h+1 >> 1;
				if(get(mid)<target) {
					h = mid-1;
				}else {
					l = mid;
				}
			}
			return l;
		}

		public void add(int ind) {
			ft.set(ind, ft.get(ind)+1);
		}

		public int get(int ind) {
			return ft.query(ind, n-1);
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int q = in.nextInt();
			int[] arr = in.nextInt(n);
			for(int i = 0; i<n; i++) {
				arr[i] = i-arr[i]+1;
			}
//			Utilities.Debug.dbg(arr);
			ArrayList<Pair<Integer, Integer>>[] queries = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				queries[i] = new ArrayList<>();
			}
			for(int i = 0; i<q; i++) {
				int x = in.nextInt(), y = in.nextInt();
				queries[n-y-1].add(new Pair<>(x, i));
			}
			int[] ans = new int[q];
			ft = new TFenwickTree<>(n, 0, Integer::sum, (o1, o2) -> o1-o2);
			for(int i = 0; i<n; i++) {
				int ind = arr[i]>=0 ? upperBound(i, arr[i]) : -1;
//				Utilities.Debug.dbg(ind);
				if(ind>=0) {
					add(ind);
				}
				for(var v: queries[i]) {
					ans[v.b] = get(v.a);
				}
			}
			for(int i: ans) {
				pw.println(i);
			}
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

	static class TFenwickTree<T> {
		int n;
		T initialValue;
		public T[] arr;
		public T[] value;
		BinaryOperator<T> operation;
		BinaryOperator<T> undo;

		public TFenwickTree(T[] arr, T initialValue, BinaryOperator<T> operation, BinaryOperator<T> undo) {
			n = arr.length;
			this.initialValue = initialValue;
			this.arr = (T[]) new Object[n];
			value = (T[]) new Object[n+1];
			Arrays.fill(value, initialValue);
			Arrays.fill(this.arr, initialValue);
			this.operation = operation;
			this.undo = undo;
			for(int i = 0; i<n; i++) {
				set(i, arr[i]);
			}
		}

		public TFenwickTree(int n, T initialValue, BinaryOperator<T> operation, BinaryOperator<T> undo) {
			this.n = n;
			this.initialValue = initialValue;
			this.arr = (T[]) new Object[n];
			value = (T[]) new Object[n+1];
			Arrays.fill(value, initialValue);
			Arrays.fill(this.arr, initialValue);
			this.operation = operation;
			this.undo = undo;
		}

		public void set(int ind, T val) {
			T old = arr[ind];
			arr[ind] = val;
			ind++;
			while(ind<=n) {
				value[ind] = operation.apply(undo.apply(value[ind], old), val);
				ind += ind&-ind;
			}
		}

		private T psum(int ind) {
			T ret = initialValue;
			while(ind>0) {
				ret = operation.apply(ret, value[ind]);
				ind -= ind&-ind;
			}
			return ret;
		}

		public T query(int l, int r) {
			return undo.apply(psum(r+1), psum(l));
		}

		public T get(int ind) {
			return arr[ind];
		}

	}

	static class Utilities {
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
}

