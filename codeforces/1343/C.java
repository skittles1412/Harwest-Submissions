import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.InputMismatchException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

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
			PrintWriter out = new PrintWriter(outputStream);
			CAlternatingSubsequence solver = new CAlternatingSubsequence();
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
	static class CAlternatingSubsequence {
		public CAlternatingSubsequence() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			long[] arr = new long[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			long length = 0, sum = 0;
			int i = 0;
			boolean positive = true;
			for(; i<n&&arr[i]<0; i++) ;
			for(; i<n; length++, positive = !positive) {
				long max = Long.MIN_VALUE;
				if(positive) {
					for(; i<n&&arr[i]>0; max = Math.max(max, arr[i]), i++) ;
				}else {
					for(; i<n&&arr[i]<0; max = Math.max(max, arr[i]), i++) ;
				}
				sum += max;
			}
			i = 0;
			positive = false;
			long clength = 0, csum = 0;
			for(; i<n&&arr[i]>0; i++) ;
			for(; i<n; clength++, positive = !positive) {
				long max = Long.MIN_VALUE;
				if(positive) {
					for(; i<n&&arr[i]>0; max = Math.max(max, arr[i]), i++) ;
				}else {
					for(; i<n&&arr[i]<0; max = Math.max(max, arr[i]), i++) ;
				}
				csum += max;
			}
			if(length<clength) {
				length = clength;
				sum = csum;
			}else if(length==clength) {
				sum = Math.max(sum, csum);
			}
			pw.println(sum);
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			br = new BufferedReader(new InputStreamReader(is), 1<<16);
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

