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
		COmkarAndBaseball solver = new COmkarAndBaseball();
		int testCount = Integer.parseInt(in.next());
		for(int i = 1; i<=testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
	static class COmkarAndBaseball {
		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			int[] arr = new int[n];
			int prev = 0;
			boolean sorted = true, correct[] = new boolean[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
				sorted &= arr[i] >= prev;
				prev = arr[i];
				correct[i] = arr[i]==i+1;
			}
			if(sorted) {
				pw.println(0);
				return;
			}
			boolean prv = true;
			int cnt = 0;
			for(int i = 0; i<n; i++) {
				if(prv&&!correct[i]) {
					cnt++;
				}
				prv = correct[i];
			}
			pw.println(Math.min(2, cnt));
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

