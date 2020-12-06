import java.io.*;
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
			C2ErrichTacToeHardVersion solver = new C2ErrichTacToeHardVersion();
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

	static class C2ErrichTacToeHardVersion {
		private final int iinf = 1_000_000_000;

		public C2ErrichTacToeHardVersion() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			char[][] arr = new char[n][n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.next(n).toCharArray();
			}
			int[] x = new int[3], y = new int[3];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<n; j++) {
					if(arr[i][j]=='X') {
						x[(i+j)%3]++;
					}else if(arr[i][j]=='O') {
						y[(i+j)%3]++;
					}
				}
			}
			int xind = 0, yind = 0, min = iinf;
			for(int i = 0; i<3; i++) {
				for(int j = 0; j<3; j++) {
					if(i!=j&&x[i]+y[j]<min) {
						min = x[i]+y[j];
						xind = i;
						yind = j;
					}
				}
			}
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<n; j++) {
					int diag = (i+j)%3;
					if(diag==xind&&arr[i][j]=='X') {
						arr[i][j] = 'O';
					}else if(diag==yind&&arr[i][j]=='O') {
						arr[i][j] = 'X';
					}
				}
			}
			for(int i = 0; i<n; i++) {
				pw.println(arr[i]);
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

	interface InputReader {
		String next();

		int nextInt();

		default String next(int maxLength) {
			return next();
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

