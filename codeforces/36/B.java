import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			InputStream inputStream;
			try {
				inputStream = new FileInputStream("input.txt");
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
			OutputStream outputStream;
			try {
				outputStream = new FileOutputStream("output.txt");
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			BFractal solver = new BFractal();
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

	static class BFractal {
		public BFractal() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), k = in.nextInt();
			char[][] pattern = new char[n][n], cur = new char[n][n];
			for(int i = 0; i<n; i++) {
				cur[i] = pattern[i] = in.next(n+5).toCharArray();
			}
			for(int i = 1; i<k; i++) {
				int nn = cur.length;
				char[][] next = new char[nn*n][nn*n];
				for(int x = 0; x<cur.length; x++) {
					for(int y = 0; y<cur.length; y++) {
						char[][] tmp = new char[n][n];
						if(cur[x][y]=='*') {
							for(int a = 0; a<n; a++) {
								Arrays.fill(tmp[a], '*');
							}
						}else {
							for(int a = 0; a<n; a++) {
								tmp[a] = pattern[a].clone();
							}
						}
						for(int a = x*n; a<x*n+n; a++) {
							System.arraycopy(tmp[a-x*n], 0, next[a], y*n, n);
						}
					}
				}
				cur = next;
			}
			for(int i = 0; i<cur.length; i++) {
				pw.println(cur[i]);
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

		public void println(char[] c) {
			println(String.valueOf(c));
		}

		public void println(String s) {
			sb.append(s);
			println();
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

	static interface InputReader {
		String next();

		int nextInt();

		default String next(int maxLength) {
			return next();
		}

	}
}

