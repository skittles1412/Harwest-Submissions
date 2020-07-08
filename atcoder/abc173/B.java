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
		BJudgeStatusSummary solver = new BJudgeStatusSummary();
		solver.solve(1, in, out);
		out.close();
	}
	static class BJudgeStatusSummary {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int n = in.nextInt();
			int[] arr = new int[4];
			for(int i = 0; i<n; i++) {
				String s = in.next();
				if(s.equals("AC")) {
					arr[0]++;
				}else if(s.equals("WA")) {
					arr[1]++;
				}else if(s.equals("TLE")) {
					arr[2]++;
				}else {
					arr[3]++;
				}
			}
			out.printf("AC x %d\nWA x %d\nTLE x %d\nRE x %d\n", arr[0], arr[1], arr[2], arr[3]);
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

