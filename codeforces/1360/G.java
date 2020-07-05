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
		GABMatrix solver = new GABMatrix();
		solver.solve(1, in, out);
		out.close();
	}
	static class GABMatrix {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt(), m = in.nextInt(), a = in.nextInt(), b = in.nextInt();
				if(a*n!=b*m) {
					out.println("NO");
					continue;
				}
				int[][] arr = new int[n][m];
				int d;
				for(d = 1; d<m&&(d*n)%m!=0; d++) ;
				for(int i = 0, start = 0; i<n; i++, start += d, start %= m) {
					for(int j = 0; j<a; j++) {
						arr[i][(start+j)%m] = 1;
					}
				}
				out.println("YES");
				for(int i = 0; i<n; i++) {
					for(int j = 0; j<m; j++) {
						out.print(arr[i][j]);
					}
					out.println();
				}
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

