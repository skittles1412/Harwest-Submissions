import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
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
		DTwoDivisors solver = new DTwoDivisors();
		solver.solve(1, in, out);
		out.close();
	}
	static class DTwoDivisors {
		public void solve(int testNumber, Input in, PrintWriter out) {
			Prime_Factorization pf = new Prime_Factorization(10000000);
			int n = in.nextInt();
			int[][] ans = new int[n][2];
			for(int i = 0; i<n; i++) {
				int cur = in.nextInt();
				ArrayList<Integer> div = new ArrayList<>(new HashSet<>(pf.factor(cur)));
				if(div.size()==1) {
					ans[i][0] = ans[i][1] = -1;
				}else {
					ans[i][0] = div.get(0);
					ans[i][1] = 1;
					for(int j = 1; j<div.size(); j++) {
						ans[i][1] *= div.get(j);
					}
				}
			}
			for(int i = 0; i<2; i++) {
				for(int j = 0; j<n; j++) {
					if(j!=0) {
						out.print(" ");
					}
					out.print(ans[j][i]);
				}
				out.println();
			}
		}

	}

	static class Prime_Factorization {
		int[] minDiv;

		public Prime_Factorization(int n) {//n = maximum value to factorize
			minDiv = new int[n+1];
			boolean[] prime = new boolean[n+1];
			Arrays.fill(prime, true);
			for(int i = 2; i<=n; i++) {
				if(prime[i]) {
					minDiv[i] = i;
					for(int j = i<<1; j<=n; j += i) {
						if(prime[j]) {
							prime[j] = false;
							minDiv[j] = i;
						}
					}
				}
			}
		}

		public ArrayList<Integer> factor(int x) {//nondecreasing list of factors, with duplicates
			ArrayList<Integer> ans = new ArrayList<>();
			while(x>1) {
				ans.add(minDiv[x]);
				x /= minDiv[x];
			}
			return ans;
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

