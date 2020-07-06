import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		Input in = new Input(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		DMaximumSumOnEvenPositions solver = new DMaximumSumOnEvenPositions();
		solver.solve(1, in, out);
		out.close();
	}
	static class DMaximumSumOnEvenPositions {
		public long msum(long[] arr) {
			long ans = 0, cur = 0;
			for(int i = 0; i<arr.length; i++) {
				cur += arr[i];
				ans = Math.max(ans, cur);
				cur = Math.max(cur, 0);
			}
			return ans;
		}

		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt();
				long[] arr = new long[n], sum0 = new long[n >> 1], sum1 = new long[n-1 >> 1];
				long csum = 0;
				for(int i = 0; i<n; i++) {
					arr[i] = in.nextInt();
				}
				for(int i = 0; i<n; i += 2) {
					csum += arr[i];
				}
				for(int i = 0; i<n-1; i += 2) {
					sum0[i >> 1] = arr[i+1]-arr[i];
				}
				for(int i = 1; i<n-1; i += 2) {
					sum1[i >> 1] = arr[i]-arr[i+1];
				}
				out.println(csum+Math.max(msum(sum0), msum(sum1)));
			}
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			br = new BufferedReader(new InputStreamReader(is));
			try {
				st = new StringTokenizer(br.readLine());
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public boolean hasNext() {
			try {
				while(!st.hasMoreTokens()) {
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
			return st.nextToken("\r\n\t\f ");
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

