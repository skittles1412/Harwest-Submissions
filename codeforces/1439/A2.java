import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

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
			A2BinaryTableHardVersion solver = new A2BinaryTableHardVersion();
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

	static class A2BinaryTableHardVersion {
		int n;
		int m;
		ArrayList<Pair<Integer, Integer>> ans;
		int[][] arr;

		public A2BinaryTableHardVersion() {
		}

		public void solve(int x, int y) {
			int ox = 0, oy = 0, zx = 0, zy = 0, cnt = 0;
			for(int i = x; i<=x+1; i++) {
				for(int j = y; j<=y+1; j++) {
					if(arr[i][j]==1) {
						ox = i;
						oy = j;
						cnt++;
					}else {
						zx = i;
						zy = j;
					}
				}
			}
			if(cnt==0) {
				return;
			}
			int ax, ay;
			if(cnt==1) {
				ax = zx;
				ay = zy;
			}else if(cnt==2) {
				ax = ox;
				ay = oy;
			}else if(cnt==3) {
				ax = zx;
				ay = zy;
			}else {
				ax = ox;
				ay = oy;
			}
			for(int i = x; i<=x+1; i++) {
				for(int j = y; j<=y+1; j++) {
					if(i!=ax||j!=ay) {
						ans.add(new Pair<>(i, j));
						arr[i][j] ^= 1;
					}
				}
			}
			if(cnt!=3) {
				solve(x, y);
			}
		}

		public void randomize(int cnt) {
			Random rand = new Random();
			int x = rand.nextInt(n-1), y = rand.nextInt(m-1);
			int ax = x+rand.nextInt(2), ay = y+rand.nextInt(2);
			for(int i = x; i<=x+1; i++) {
				for(int j = y; j<=y+1; j++) {
					if(i!=ax||j!=ay) {
						ans.add(new Pair<>(i, j));
						arr[i][j] ^= 1;
					}
				}
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			ans = new ArrayList<>();
			n = in.nextInt();
			m = in.nextInt();
			int[][] tmp = in.nextIntChar(n, o -> o-'0');
			do {
				ans.clear();
				arr = Utilities.clone(tmp);
				randomize(Math.min(5, n*m/8));
				for(int i = 0; i<n-1; i += 2) {
					for(int j = 0; j<m-1; j += 2) {
						solve(i, j);
					}
				}
				if(Utilities.odd(n)) {
					for(int i = 0; i<m-1; i += 2) {
						solve(n-2, i);
					}
				}
				if(Utilities.odd(m)) {
					for(int i = 0; i<n-1; i += 2) {
						solve(i, m-2);
					}
				}
				if(Utilities.odd(n)&&Utilities.odd(m)) {
					solve(n-2, m-2);
				}
			} while(ans.size()/3>n*m);
			pw.println(ans.size()/3);
			for(var v: ans) {
				pw.println(v.a+1, v.b+1);
			}
		}

	}

	static class Utilities {
		public static int[][] clone(int[][] arr) {
			int[][] ret = new int[arr.length][];
			for(int i = 0; i<arr.length; i++) {
				ret[i] = arr[i].clone();
			}
			return ret;
		}

		public static boolean odd(int x) {
			return (x&1)>0;
		}

	}

	static interface InputReader {
		String next();

		int nextInt();

		default int[] nextIntChar(Function<Character, Integer> f) {
			String s = next();
			int[] ret = new int[s.length()];
			for(int i = 0; i<s.length(); i++) {
				ret[i] = f.apply(s.charAt(i));
			}
			return ret;
		}

		default int[][] nextIntChar(int n, Function<Character, Integer> f) {
			int[][] ret = new int[n][];
			for(int i = 0; i<n; i++) {
				ret[i] = nextIntChar(f);
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

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
			println();
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

