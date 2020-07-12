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
		DOmkarAndCircle solver = new DOmkarAndCircle();
		solver.solve(1, in, out);
		out.close();
	}
	static class DOmkarAndCircle {
		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			int[] arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			int[] cur = new int[n<<1];
			int i = 0;
			for(int j = 0; j<n; j += 2, i++) {
				cur[i] = cur[i+n] = arr[j];
			}
			i = n+1 >> 1;
			for(int j = 1; j<n; j += 2, i++) {
				cur[i] = cur[i+n] = arr[j];
			}
			long sum = 0;
			for(i = 0; i<n+1 >> 1; i++) {
				sum += cur[i];
			}
			long best = sum;
			for(i = (n+1 >> 1); i<n<<1; i++) {
				sum += cur[i]-cur[i-(n+1 >> 1)];
				best = Math.max(best, sum);
			}
			pw.println(best);
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

