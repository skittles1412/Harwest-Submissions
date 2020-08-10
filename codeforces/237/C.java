import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
			CPrimesOnInterval solver = new CPrimesOnInterval();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CPrimesOnInterval {
		boolean[] prime;
		int a;
		int b;
		int k;

		public CPrimesOnInterval() {
		}

		public boolean valid(int x) {
			int sum = 0;
			for(int i = a; i<a+x; i++) {
				if(prime[i]) {
					sum += 1;
				}
			}
			for(int i = a; i<=b-x; i++) {
				if(sum<k) {
					return false;
				}
				if(prime[i]) {
					sum--;
				}
				if(prime[i+x]) {
					sum++;
				}
			}
			return sum>=k;
		}

		public void solve(int kase, Input in, Output pw) {
			a = in.nextInt();
			b = in.nextInt();
			k = in.nextInt();
			ArrayList<Integer> al = Utilities.sieve(b);
			prime = new boolean[b+1];
			for(int i: al) {
				prime[i] = true;
			}
			int sum = 0;
			for(int i = a; i<=b; i++) {
				if(prime[i]) {
					sum++;
				}
			}
			if(sum<k) {
				pw.println(-1);
				return;
			}
			int l = 1, r = b-a+1;
			while(l<r) {
				int mid = l+r >> 1;
				if(valid(mid)) {
					r = mid;
				}else {
					l = mid+1;
				}
			}
			pw.println(l);
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

		public void println(int i) {
			println(String.valueOf(i));
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

	static class Utilities {
		public static ArrayList<Integer> sieve(int n) {
			ArrayList<Integer> ans = new ArrayList<>();
			boolean[] prime = new boolean[n+1];
			Arrays.fill(prime, true);
			for(int i = 2; i<=n; i++) {
				if(prime[i]) {
					ans.add(i);
				}
				if((long) i*i>n) {
					continue;
				}
				for(int j = i*i; j<=n; j += i) {
					prime[j] = false;
				}
			}
			return ans;
		}

	}
}


