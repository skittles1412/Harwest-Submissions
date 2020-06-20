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
		CEvenPicture solver = new CEvenPicture();
		solver.solve(1, in, out);
		out.close();
	}
	static class CEvenPicture {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int n = in.nextInt();
			out.println(4+3*n);
			out.println("0 0\n0 1\n1 0\n1 1");
			int cur = 2;
			while(n-->0) {
				out.printf("%d %d\n%d %d\n%d %d\n", cur, cur, cur-1, cur, cur, cur-1);
				cur++;
			}
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

		public int nextInt() {
			String s = next();
			if(s==null) {
				return Integer.MIN_VALUE;
			}
			return Integer.parseInt(s);
		}

	}
}

