import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.InputMismatchException;
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
			ETheLastFightBetweenHumanAndAI solver = new ETheLastFightBetweenHumanAndAI();
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

	static class ETheLastFightBetweenHumanAndAI {
		final int m;
		final int[] mods;
		int n;
		int k;
		int[] arr;

		public ETheLastFightBetweenHumanAndAI() {
			m = 100;
			mods = new int[100];
			for(int i = 0; i<m; i++) {
				mods[i] = BigInteger.probablePrime(30, new Random()).intValueExact();
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt()+1;
			k = in.nextInt();
			if(k==0) {
				String val = in.next();
				if(val.equals("0")) {
					pw.println("Yes");
				}else if(!val.equals("?")) {
					pw.println("No");
				}else {
					int cnt = 0;
					for(int i = 1; i<n; i++) {
						if(!in.next().equals("?")) {
							cnt++;
						}
					}
					pw.println(Utilities.odd(cnt) ? "Yes" : "No");
				}
				return;
			}
			arr = new int[n];
			for(int i = 0; i<n; i++) {
				String s = in.next();
				if(s.charAt(0)=='?') {
					pw.println(Utilities.even(n) ? "Yes" : "No");
					return;
				}
//			arr[i] = Integer.parseInt(s);
				arr[n-i-1] = Integer.parseInt(s);
			}
			long[] value = new long[m];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<m; j++) {
					value[j] = (value[j]*k+arr[i])%mods[j];
				}
			}
			for(int i = 0; i<m; i++) {
				if(value[i]!=0) {
					pw.println("No");
					return;
				}
			}
			pw.println("Yes");
//		for(int i = 0; i<n; i++) {
//			if(k<0&&odd(i)) {
//				arr[i] *= -1;
//			}
//		}
//		k = abs(k);
//		if(k==1) {
//			int sum = 0;
//			for(int i = 0; i<n; i++) {
//				sum += arr[i];
//			}
//			pw.println(sum==0 ? "Yes" : "No");
//			return;
//		}
//		var pos = solve();
//		for(int i = 0; i<n; i++) {
//			arr[i] *= -1;
//		}
//		var neg = solve();
//		dbg(pos, neg);
//		pw.println(pos.equals(neg) ? "Yes" : "No");
		}

	}

	static class Utilities {
		public static boolean even(int x) {
			return (x&1)==0;
		}

		public static boolean odd(int x) {
			return (x&1)>0;
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

	static interface InputReader {
		String next();

		int nextInt();

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

