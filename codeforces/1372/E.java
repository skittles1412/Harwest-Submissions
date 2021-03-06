import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;
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
		EOmkarAndLastFloor solver = new EOmkarAndLastFloor();
		solver.solve(1, in, out);
		out.close();
	}
	static class EOmkarAndLastFloor {
		public int n;
		public int m;
		public EOmkarAndLastFloor.interval[][] arr;
		public int[][] memo;

		public int dp(int l, int r) {
			if(l>r) {
				return 0;
			}else if(memo[l][r]!=-1) {
				return memo[l][r];
			}
			int ans = 0;
			for(int i = l; i<=r; i++) {
				int cnt = 0;
				for(int j = 0; j<n; j++) {
					EOmkarAndLastFloor.interval tmp = arr[j][i];
					if(l<=tmp.l&&r >= tmp.r) {
						cnt++;
					}
				}
				ans = Math.max(ans, cnt*cnt+dp(l, i-1)+dp(i+1, r));
			}
			return memo[l][r] = ans;
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			n = in.nextInt();
			m = in.nextInt();
			arr = new EOmkarAndLastFloor.interval[n][m];
			for(int i = 0; i<n; i++) {
				int k = in.nextInt();
				for(int j = 0; j<k; j++) {
					int l = in.nextInt()-1, r = in.nextInt()-1;
					EOmkarAndLastFloor.interval cur = new EOmkarAndLastFloor.interval(l, r);
					for(int a = l; a<=r; a++) {
						arr[i][a] = cur;
					}
				}
			}
			memo = new int[m][m];
			for(int i = 0; i<m; i++) {
				Arrays.fill(memo[i], -1);
			}
			pw.println(dp(0, m-1));
		}

		static class interval {
			int l;
			int r;

			public interval(int a, int b) {
				l = a;
				r = b;
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
				throw new UnsupportedOperationException();
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
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

