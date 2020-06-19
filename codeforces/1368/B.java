import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
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
		BCodeforcesSubsequences solver = new BCodeforcesSubsequences();
		solver.solve(1, in, out);
		out.close();
	}
	static class BCodeforcesSubsequences {
		public long pow(long base, long exp) {
			long ans = 1;
			for(int i = 0; i<exp; i++) {
				ans *= base;
			}
			return ans;
		}

		public void solve(int testNumber, Input in, PrintWriter out) {
			long k = in.nextLong();
			int po = 0;
			for(; pow(po, 10)<k; po++) ;
			int sub = 0;
			for(; pow(po, 10-sub)*pow(po-1, sub) >= k; sub++) ;
			sub--;
			String s = "codeforces";
			for(int i = 0; i<10; i++) {
				if(i<sub) {
					for(int j = 1; j<po; j++) {
						out.print(s.charAt(i));
					}
				}else {
					for(int j = 0; j<po; j++) {
						out.print(s.charAt(i));
					}
				}
			}
			out.println();
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(Reader r) {
			br = new BufferedReader(r);
			try {
				st = new StringTokenizer(br.readLine());
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public Input(InputStream s) {
			br = new BufferedReader(new InputStreamReader(s));
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
				return null;
			}
			return st.nextToken();
		}

		public long nextLong() {
			String s = next();
			if(s==null) {
				return Long.MIN_VALUE;
			}
			return Long.parseLong(s);
		}

	}
}

