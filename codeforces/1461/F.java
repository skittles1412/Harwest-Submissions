import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
			FMathematicalExpression solver = new FMathematicalExpression();
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

	static class FMathematicalExpression {
		public FMathematicalExpression() {
		}

		public String solve(int[] arr) {
			int n = arr.length;
			StringBuilder ans = new StringBuilder();
			int l, r;
			for(l = 0; l<n&&arr[l]==1; l++) {
				ans.append("1+");
			}
			if(l==n) {
				return ans.substring(0, ans.length()-1);
			}
			for(r = n-1; r>=0&&arr[r]==1; r--) ;
			solve(ans, Arrays.copyOfRange(arr, l, r+1));
			ans.append("+1".repeat(Math.max(0, n-(r+1))));
			return ans.toString();
		}

		public void solve(StringBuilder ans, int[] arr) {
			int n = arr.length;
			{
				long cur = 1;
				for(int i = 0; i<n; i++) {
					if(cur>2*n/arr[i]) {
						for(int j = 0; j<n; j++) {
							if(j>0) {
								ans.append("*");
							}
							ans.append(arr[j]);
						}
						return;
					}
					cur *= arr[i];
				}
			}
			ArrayList<Integer> al = new ArrayList<>(n);
			int prev = -1;
			for(int i = 0; i<n; i++) {
				if(prev!=1||arr[i]!=1) {
					al.add(i);
				}
				prev = arr[i];
			}
			long[] pmul = new long[n+1], psum = new long[n+1];
			pmul[0] = 1;
			for(int i = 0; i<n; i++) {
				pmul[i+1] = pmul[i]*arr[i];
				psum[i+1] = psum[i]+arr[i];
			}
			long[] dp = new long[n+1];
			int[] next = new int[n+1];
			Collections.reverse(al);
			for(int i: al) {
				next[i] = n;
				dp[i] = pmul[n]/pmul[i];
				for(int j: al) {
					if(j<=i) {
						break;
					}
					long cur = dp[j];
					if(arr[i]==1) {
						cur += psum[j]-psum[i];
					}else {
						cur += pmul[j]/pmul[i];
					}
					if(cur>dp[i]) {
						dp[i] = cur;
						next[i] = j;
					}
				}
			}
			for(int i = 0; i<n; ) {
				String s = i>0 ? "+" : "", o = arr[i]==1 ? "+" : "*";
				int end = next[i];
				for(; i<end; i++) {
					if(s!=null) {
						ans.append(s);
						s = null;
					}else {
						ans.append(o);
					}
					ans.append(arr[i]);
				}
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n);
			boolean add, sub, mul;
			{
				String s = in.next(3);
				add = s.contains("+");
				sub = s.contains("-");
				mul = s.contains("*");
			}
			if(!mul) {
				for(int i = 0; i<n; i++) {
					if(i>0) {
						if(add) {
							pw.print("+");
						}else {
							pw.print("-");
						}
					}
					pw.print(arr[i]);
				}
			}else {
				if(!add) {
					for(int i = 0; i<n; i++) {
						if(i>0) {
							if(sub&&arr[i]==0) {
								pw.print("-");
							}else {
								pw.print("*");
							}
						}
						pw.print(arr[i]);
					}
				}else {
					for(int i = 0; i<n; ) {
						if(i>0) {
							pw.print("+");
						}
						if(arr[i]==0) {
							pw.print(arr[i++]);
						}else {
							int start = i;
							for(; i<n&&arr[i]!=0; i++) ;
							pw.print(solve(Arrays.copyOfRange(arr, start, i)));
						}
					}
				}
			}
			pw.println();
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

		public void print(int i) {
			print(String.valueOf(i));
		}

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
		}

		public String next(int maxLength) {
			byte[] ret = new byte[maxLength];
			byte c = skip();
			int ind = 0;
			while(c!=-1&&!isSpaceChar(c)) {
				ret[ind++] = c;
				c = read();
			}
			return new String(ret, 0, ind);
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

	static interface InputReader {
		String next();

		int nextInt();

		default String next(int maxLength) {
			return next();
		}

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

	}
}

