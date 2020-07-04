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
		BNeighborGrid solver = new BNeighborGrid();
		solver.solve(1, in, out);
		out.close();
	}
	static class BNeighborGrid {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt(), m = in.nextInt();
				int[][] arr = new int[n][m];
				for(int i = 0; i<n; i++) {
					arr[i][0] = arr[i][m-1] = 3;
				}
				for(int i = 0; i<m; i++) {
					arr[0][i] = arr[n-1][i] = 3;
				}
				arr[0][0] = arr[n-1][0] = arr[0][m-1] = arr[n-1][m-1] = 2;
				for(int i = 1; i<n-1; i++) {
					for(int j = 1; j<m-1; j++) {
						arr[i][j] = 4;
					}
				}
				boolean valid = true;
				for(int i = 0; i<n; i++) {
					for(int j = 0; j<m; j++) {
						int x = in.nextInt();
						valid &= arr[i][j] >= x;
					}
				}
				if(!valid) {
					out.println("NO");
				}else {
					out.println("YES");
					for(int i = 0; i<n; i++) {
						for(int j = 0; j<m; j++) {
							if(j!=0) {
								out.print(" ");
							}
							out.print(arr[i][j]);
						}
						out.println();
					}
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

