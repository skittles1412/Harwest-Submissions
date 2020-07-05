import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.TreeSet;
import java.util.StringTokenizer;
import java.io.BufferedReader;
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
		CSocialDistance solver = new CSocialDistance();
		solver.solve(1, in, out);
		out.close();
	}
	static class CSocialDistance {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt(), k = in.nextInt();
				TreeSet<Integer> ts = new TreeSet<>();
				ts.add(-1000000);
				ts.add(1000000);
				String s = in.next();
				for(int i = 0; i<n; i++) {
					if(s.charAt(i)=='1') {
						ts.add(i);
					}
				}
				int ans = 0;
				for(int i = 0; i<n; i++) {
					int low = ts.floor(i), hi = ts.ceiling(i);
					if(Math.abs(low-i)>k&&Math.abs(hi-i)>k) {
						ans++;
						ts.add(i);
					}
				}
				out.println(ans);
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

