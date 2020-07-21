import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
			C2PrefixFlipHardVersion solver = new C2PrefixFlipHardVersion();
			int testCount = Integer.parseInt(in.next());
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class C2PrefixFlipHardVersion {
		String target;
		C2PrefixFlipHardVersion.Prefix cur;
		ArrayList<Integer> ans;

		public C2PrefixFlipHardVersion() {
		}

		public void dfs(int ind) {
			if(ind==-1) {
				return;
			}else if(cur.getLast()==target.charAt(ind)) {
				cur.increment();
				dfs(ind-1);
				return;
			}
			if(cur.getFirst()==target.charAt(ind)) {
				ans.add(1);
				cur.flipFirst();
			}
			cur.flip();
			cur.reverse();
			cur.increment();
			ans.add(ind+1);
			dfs(ind-1);
		}

		public void solve(int testNumber, Input in, Output pw) {
			int n = in.nextInt();
			String begin = in.next();
			target = in.next();
			ans = new ArrayList<>();
			cur = new C2PrefixFlipHardVersion.Prefix(begin);
			dfs(n-1);
			pw.print(ans.size());
			for(int i = 0; i<ans.size(); i++) {
				pw.print(" "+ans.get(i));
			}
			pw.println();
		}

		static class Prefix {
			char[] ch;
			int l;
			int r;
			boolean reversed;
			boolean flipped;

			public Prefix(String s) {
				ch = s.toCharArray();
				r = s.length()-1;
				l = 0;
				reversed = flipped = false;
			}

			public void increment() {
				if(reversed) {
					l++;
				}else {
					r--;
				}
			}

			public void reverse() {
				reversed = !reversed;
			}

			public void flip() {
				flipped = !flipped;
			}

			public void flipFirst() {
				int first = l;
				if(reversed) {
					first = r;
				}
				ch[first] = flip(ch[first]);
			}

			public char getFirst() {
				int first = l;
				if(reversed) {
					first = r;
				}
				if(flipped) {
					return flip(ch[first]);
				}
				return ch[first];
			}

			public char getLast() {
				int last = r;
				if(reversed) {
					last = l;
				}
				if(flipped) {
					return flip(ch[last]);
				}
				return ch[last];
			}

			private char flip(char c) {
				return c=='1' ? '0' : '1';
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

		public void print(int i) {
			print(String.valueOf(i));
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

	}
}

