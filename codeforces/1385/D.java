import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.HashMap;
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
			DAGoodString solver = new DAGoodString();
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
	static class DAGoodString {
		HashMap<String, Integer> memo;
		int[] arr;
		int n;
		int[][] psum;

		public DAGoodString() {
		}

		public int dp(int l, int r, int c) {
			String key = l+" "+r+" "+c;
			if(memo.containsKey(key)) {
				return memo.get(key);
			}else if(r-l==1) {
				return arr[l]==c ? 0 : 1;
			}
			int mid = l+r >> 1, length = mid-l;
			int ans = Math.min(length-(psum[mid][c]-psum[l][c])+dp(mid, r, c+1),
					length-(psum[r][c]-psum[mid][c])+dp(l, mid, c+1));
			memo.put(key, ans);
			return ans;
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			n = in.nextInt();
			String s = in.next();
			arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = s.charAt(i)-'a';
			}
			psum = new int[n+1][26];
			for(int i = 1; i<=n; i++) {
				for(int j = 0; j<26; j++) {
					psum[i][j] = psum[i-1][j];
					if(arr[i-1]==j) {
						psum[i][j]++;
					}
				}
			}
			memo = new HashMap<>();
			pw.println(dp(0, n, 0));
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

