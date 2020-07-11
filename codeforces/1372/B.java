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
		BOmkarAndLastClassOfMath solver = new BOmkarAndLastClassOfMath();
		int testCount = Integer.parseInt(in.next());
		for(int i = 1; i<=testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
	static class BOmkarAndLastClassOfMath {
		public int[] rec(int x) {
			int f = 2, mdiv = (int) Math.ceil(Math.sqrt(x)+0.5);
			for(; f<mdiv&&x%f!=0; f++) ;
			if(x%f!=0||x==f) {
				return new int[] {1, x-1};
			}
			int[] s = rec(f);
			s[0] *= x/f;
			s[1] *= x/f;
			return s;
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int[] ans = rec(in.nextInt());
			pw.printf("%d %d\n", ans[0], ans[1]);
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

