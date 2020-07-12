import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Collections;
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
		CCreateTheTeams solver = new CCreateTheTeams();
		int testCount = Integer.parseInt(in.next());
		for(int i = 1; i<=testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
	static class CCreateTheTeams {
		long[] arr;
		long x;
		int n;
		int[] memo;

		public CCreateTheTeams() {
		}

		public int dp(int cur) {
			if(cur >= n) {
				return 0;
			}else if(memo[cur]!=-1) {
				return memo[cur];
			}
			int ans = dp(cur+1);
			int move = (int) ((x+arr[cur]-1)/arr[cur]);
			if(cur+move<=n) {
				ans = Math.max(ans, 1+dp(cur+move));
			}
			return memo[cur] = ans;
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			n = in.nextInt();
			x = in.nextInt();
			arr = new long[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			Utilities.sort(arr);
			memo = new int[n];
			Arrays.fill(memo, -1);
			pw.println(dp(0));
		}

	}

	static class Utilities {
		public static void sort(long[] arr) {
			ArrayList<Long> al = new ArrayList<Long>();
			for(long i: arr) {
				al.add(i);
			}
			Collections.sort(al);
			for(int i = 0; i<al.size(); i++) {
				arr[i] = al.get(i);
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

