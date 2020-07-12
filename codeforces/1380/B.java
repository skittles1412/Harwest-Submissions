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
		BUniversalSolution solver = new BUniversalSolution();
		int testCount = Integer.parseInt(in.next());
		for(int i = 1; i<=testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
	static class BUniversalSolution {
		public BUniversalSolution() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			String ins = in.next();
			int n = ins.length(), r = 0, s = 0, p = 0;
			for(int i = 0; i<n; i++) {
				char c = ins.charAt(i);
				if(c=='R') {
					r++;
				}else if(c=='S') {
					s++;
				}else {
					p++;
				}
			}
			char toPrint = 'S';
			if(r >= s&&r >= p) {
				toPrint = 'P';
			}else if(s >= r&&s >= p) {
				toPrint = 'R';
			}
			for(int i = 0; i<n; i++) {
				pw.print(toPrint);
			}
			pw.println();
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

	}
}

