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
			DUnmerge solver = new DUnmerge();
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
	static class DUnmerge {
		int[] a;
		int n;
		int[][] memo;

		public DUnmerge() {
		}

		public boolean dp(int sum, int ind) {
			if(sum==0) {
				return true;
			}else if(ind==n) {
				return false;
			}else if(memo[sum][ind]!=-1) {
				return memo[sum][ind]==1;
			}
			boolean ans = dp(sum, ind+1);
			if(sum-a[ind] >= 0) {
				ans |= dp(sum-a[ind], ind+1);
			}
			memo[sum][ind] = ans ? 1 : 0;
			return ans;
		}

		public void solve(int testNumber, Input in, Output pw) {
			n = in.nextInt()<<1;
			int[] arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			ArrayList<Integer> nums = new ArrayList<>();
			for(int i = n-1; i >= 0; i--) {
				int ind = 0, max = -1;
				for(int j = i; j >= 0; j--) {
					if(arr[j]>max) {
						max = arr[j];
						ind = j;
					}
				}
				nums.add(i-ind+1);
				i = ind;
			}
			int sum = n >> 1;
			n = nums.size();
			a = new int[n];
			for(int i = 0; i<n; i++) {
				a[i] = nums.get(i);
			}
			memo = new int[sum+1][n];
			for(int i = 0; i<=sum; i++) {
				Arrays.fill(memo[i], -1);
			}
			if(dp(sum, 0)) {
				pw.println("YES");
			}else {
				pw.println("NO");
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

