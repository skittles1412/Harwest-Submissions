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
		DGrid00100 solver = new DGrid00100();
		solver.solve(1, in, out);
		out.close();
	}
	static class DGrid00100 {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt(), k = in.nextInt(), arr[][] = new int[n][n];
				if(k%n==0) {
					out.println(0);
				}else {
					out.println(2);
				}
				for(int i = 0; i<n&&k>0; i++) {
					for(int j = 0; j<n&&k>0; j++) {
						arr[j][(j+i)%n] = 1;
						k--;
					}
				}
				for(int i = 0; i<n; i++) {
					for(int j = 0; j<n; j++) {
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

