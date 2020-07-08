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
		CHAndV solver = new CHAndV();
		solver.solve(1, in, out);
		out.close();
	}
	static class CHAndV {
		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt(), m = in.nextInt(), k = in.nextInt();
			int[][] arr = new int[n][m];
			for(int i = 0; i<n; i++) {
				String s = in.next();
				for(int j = 0; j<m; j++) {
					arr[i][j] = s.charAt(j)=='.' ? 0 : 1;
				}
			}
			int ans = 0;
			for(int rows = 0; rows<1<<n; rows++) {
				for(int cols = 0; cols<1<<m; cols++) {
					int sum = 0;
					for(int i = 0; i<n; i++) {
						for(int j = 0; j<m; j++) {
							if((rows&(1<<i))==0&&(cols&(1<<j))==0&&arr[i][j]==1) {
								sum++;
							}
						}
					}
					if(sum==k) {
						ans++;
					}
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

