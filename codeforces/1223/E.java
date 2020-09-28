import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;

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
			EPaintTheTree solver = new EPaintTheTree();
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

	static class EPaintTheTree {
		int n;
		int k;
		ArrayList<Pair<Integer, Long>>[] graph;

		public EPaintTheTree() {
		}

		public Pair<Long, Long> dfs(int u) {
			int m = graph[u].size();
			long[] add = new long[m];
			long sum = 0;
			for(int i = 0; i<m; i++) {
				var v = graph[u].get(i);
				graph[v.a].remove(new Pair<>(u, v.b));
				var p = dfs(v.a);
				p.a += v.b;
				sum += p.b;
				add[i] = p.a-p.b;
			}
			Utilities.rsort(add);
			for(int i = 0; i<m&&i<k-1&&add[i]>=0; i++) {
				sum += add[i];
			}
			long a = 0;
			if(m>=k) {
				a = Math.max(0, add[k-1]);
			}
			return new Pair<>(sum, sum+a);
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			k = in.nextInt();
			graph = in.nextUndirectedWeightedLongGraph(n, n-1);
			pw.println(dfs(0).b);
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

	static interface InputReader {
		int nextInt();

		long nextLong();

		default ArrayList<Pair<Integer, Long>>[] nextUndirectedWeightedLongGraph(int n, int m) {
			ArrayList<Pair<Integer, Long>>[] ret = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				ret[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				int u = nextInt()-1, v = nextInt()-1;
				long w = nextLong();
				ret[u].add(new Pair<>(v, w));
				ret[v].add(new Pair<>(u, w));
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
		public static void rsort(long[] arr) {
			Random rand = new Random();
			int n = arr.length;
			for(int i = 0; i<n; i++) {
				swap(arr, i, rand.nextInt(n));
			}
			Arrays.sort(arr);
			reverse(arr, 0, n-1);
		}

		public static void swap(long[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

		public static void reverse(long[] arr, int i, int j) {
			while(i<j) {
				swap(arr, i++, j--);
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

