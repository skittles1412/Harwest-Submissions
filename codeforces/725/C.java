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
			CHiddenWord solver = new CHiddenWord();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CHiddenWord {
		public CHiddenWord() {
		}

		public void solve(int testNumber, Input in, Output pw) {
			String s = in.next();
			int[] count = new int[26];
			for(int i = 0; i<27; i++) {
				count[s.charAt(i)-'A']++;
			}
			int val;
			for(val = 0; val<26&&count[val]==1; val++) ;
			int start = -1, end = -1;
			for(int i = 0; i<27; i++) {
				if(s.charAt(i)-'A'==val) {
					if(start==-1) {
						start = i;
					}else {
						end = i;
					}
				}
			}
			if(end-start==1) {
				pw.println("Impossible");
				return;
			}
			char[][] arr = new char[2][13];
			int mid = start+end+1 >> 1, j = mid-1;
			for(int i = 12; i >= 0&&j >= 0; i--, j--) {
				arr[0][i] = s.charAt(j);
			}
			for(int i = 0; j >= 0; i++, j--) {
				arr[1][i] = s.charAt(j);
			}
			j = mid;
			for(int i = 12; i >= 0&&j<27; i--, j++) {
				if(s.charAt(j)-'A'==val) {
					i++;
					continue;
				}
				arr[1][i] = s.charAt(j);
			}
			for(int i = 0; j<27; i++, j++) {
				arr[0][i] = s.charAt(j);
			}
			pw.println(new String(arr[0]));
			pw.println(new String(arr[1]));
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
}

