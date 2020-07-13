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
		DBerserkAndFireball solver = new DBerserkAndFireball();
		solver.solve(1, in, out);
		out.close();
	}
	static class DBerserkAndFireball {
		public DBerserkAndFireball() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt(), m = in.nextInt();
			long x = in.nextInt(), k = in.nextInt(), y = in.nextInt(), minCost = Math.min(x, y*k);
			long[] a = new long[n+1], b = new long[m+1];
			for(int i = 0; i<n; i++) {
				a[i] = in.nextInt();
			}
			for(int i = 0; i<m; i++) {
				b[i] = in.nextInt();
			}
			a[n] = b[m] = -100;
			long ans = 0;
			int rind = 0;
			for(int i = 0; i<=m; i++, rind++) {
				int lind = rind;
				for(; rind<=n&&a[rind]!=b[i]; rind++) ;
				if(rind==n+1) {
					pw.println(-1);
					return;
				}
				long l = i==0 ? -1 : b[i-1], r = b[i], maxc = -1, length = rind-lind;
				for(int j = lind; j<rind; j++) {
					maxc = Math.max(maxc, a[j]);
				}
				if(l>maxc||r>maxc) {
					ans += minCost*(length/k)+y*(length%k);
				}else {
					if(length<k) {
						pw.println(-1);
						return;
					}
					ans += x+y*(length%k)+minCost*(length/k-1);
				}
			}
			pw.println(ans);
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

