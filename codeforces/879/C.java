import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Flushable;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			CShortProgram solver = new CShortProgram();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CShortProgram {
		public CShortProgram() {
		}

		public void solve(int testNumber, Input in, Output pw) {
			int n = in.nextInt();
			int[][] command = new int[n][2];
			for(int i = 0; i<n; i++) {
				String type = in.next();
				if(type.equals("|")) {
					command[i][0] = 1;
				}else if(type.equals("^")) {
					command[i][0] = 2;
				}
				command[i][1] = in.nextInt();
			}
			int test0 = 0, test1 = 1023;
			for(int i = 0; i<n; i++) {
				if(command[i][0]==0) {
					test0 &= command[i][1];
					test1 &= command[i][1];
				}else if(command[i][0]==1) {
					test0 |= command[i][1];
					test1 |= command[i][1];
				}else {
					test0 ^= command[i][1];
					test1 ^= command[i][1];
				}
			}
			int xor = 0, or = 0, and = 1023;
			for(int i = 0; i<10; i++) {
				boolean b0 = (test0&1<<i)>0, b1 = (test1&1<<i)>0;
				if(b0&&b1) {
					or |= 1<<i;
				}else if(!(b0||b1)) {
					and &= ~(1<<i);
				}else if(b0&&(!b1)) {
					xor ^= 1<<i;
				}
			}
			pw.println(3);
			pw.println("&", and);
			pw.println("|", or);
			pw.println("^", xor);
		}

	}

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public boolean autoFlush;
		public String LineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			autoFlush = false;
			LineSeparator = System.lineSeparator();
		}

		public void print(String s) {
			sb.append(s);
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
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
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println() {
			sb.append(LineSeparator);
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

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = null;
		}

		public boolean hasNext() {
			try {
				while(st==null||!st.hasMoreTokens()) {
					String s = br.readLine();
					if(s==null) {
						return false;
					}
					st = new StringTokenizer(s);
				}
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		public String next() {
			if(!hasNext()) {
				throw new InputMismatchException();
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

