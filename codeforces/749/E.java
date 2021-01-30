import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
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
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			EInversionsAfterShuffle solver = new EInversionsAfterShuffle();
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

	static class EInversionsAfterShuffle {
		public EInversionsAfterShuffle() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n, o -> o-1);
			FenwickTree count = new FenwickTree(n);
			EInversionsAfterShuffle.FT greatl = new EInversionsAfterShuffle.FT(n), greatr = new EInversionsAfterShuffle.FT(n);
			int[] ind = new int[n];
			for(int i = 0; i<n; i++) {
				count.set(i, 1);
				greatl.set(i, (i+1.0)/n);
				greatr.set(i, (n-i)/(n+1.0));
				ind[arr[i]] = i;
			}
			double ans = 0;
			for(int i: ind) {
				double l = (i+1.0)/n, r = (n-i)/(n+1.0);
				ans += count.sum(0, i-1)-greatl.query(0, i-1)*r;
				ans += greatr.query(i+1, n-1)*l;
				count.set(i, 0);
				greatl.set(i, 0D);
				greatr.set(i, 0D);
			}
			pw.printf("%.9f\n", ans);
		}

		static class FT extends TFenwickTree<Double> {
			public FT(int n) {
				super(n, 0.0, Double::sum, (a, b) -> a-b);
			}

		}

	}

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n, IntUnaryOperator operator) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = operator.applyAsInt(nextInt());
			}
			return ret;
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

	static class FenwickTree {
		public final int n;
		public long[] sum;
		public long[] arr;

		public FenwickTree(int n) {
			this.n = n;
			arr = new long[n];
			sum = new long[n+1];
		}

		public FenwickTree(int[] arr) {
			n = arr.length;
			sum = new long[n+1];
			this.arr = new long[n];
			for(int i = 0; i<n; i++) {
				add(i, arr[i]);
			}
		}

		public FenwickTree(long[] arr) {
			n = arr.length;
			sum = new long[n+1];
			this.arr = new long[n];
			for(int i = 0; i<n; i++) {
				add(i, arr[i]);
			}
		}

		public void add(int ind, long val) {
			arr[ind] += val;
			ind++;
			while(ind<=n) {
				sum[ind] += val;
				ind += ind&-ind;
			}
		}

		public void set(int ind, long val) {
			add(ind, val-arr[ind]);
		}

		public long psum(int x) {
			long ret = 0;
			while(x>0) {
				ret += sum[x];
				x -= x&-x;
			}
			return ret;
		}

		public long sum(int l, int r) {
			return psum(r+1)-psum(l);
		}

		public String toString() {
			return Utilities.getString(arr, "{", ", ", "}");
		}

	}

	static class Utilities {
		public static String getString(long[] arr, String start, String middle, String end) {
			StringBuilder builder = new StringBuilder();
			builder.append(start);
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					builder.append(middle);
				}
				builder.append(arr[i]);
			}
			builder.append(end);
			return builder.toString();
		}

	}

	static class TFenwickTree<T> {
		public final int n;
		public final T initialValue;
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

		public T psum(int ind) {
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

		public void printf(String s, Object... o) {
			print(String.format(s, o));
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

