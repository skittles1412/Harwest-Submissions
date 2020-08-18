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
			DFullBinaryTreeQueries solver = new DFullBinaryTreeQueries();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DFullBinaryTreeQueries {
		long[] level;
		long[] size;

		public DFullBinaryTreeQueries() {
		}

		public void rotate(int l, long val) {
			level[l] += val;
			level[l] %= size[l];
		}

		public void nodeRotate(int l, long val) {
			if(l<60) {
				val %= size[l];
				rotate(l, val);
				nodeRotate(l+1, val<<1);
			}
		}

		public int getLevel(long x) {
			return 63-Long.numberOfLeadingZeros(x);
		}

		public void solve(int kase, Input in, Output pw) {
			level = new long[60];
			size = new long[60];
			size[0] = 1;
			for(int i = 1; i<60; i++) {
				size[i] = size[i-1]<<1L;
			}
			int q = in.nextInt();
			while(q-->0) {
				int type = in.nextInt();
				if(type==1) {
					int level = getLevel(in.nextLong());
					rotate(level, (size[level]+in.nextLong())%size[level]);
				}else if(type==2) {
					int level = getLevel(in.nextLong());
					nodeRotate(level, (size[level]+in.nextLong())%size[level]);
				}else {
					long x = in.nextLong();
					int l = getLevel(x);
					x = (x+level[l])%size[l]+size[l];
					while(x>0) {
						pw.print((x-level[l])%size[l]+size[l]+" ");
						x >>= 1;
						l--;
					}
					pw.println();
				}
			}
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

		public long nextLong() {
			return Long.parseLong(next());
		}

	}
}


