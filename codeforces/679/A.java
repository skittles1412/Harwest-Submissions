import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
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
		ABearAndPrime100 solver = new ABearAndPrime100();
		solver.solve(1, in, out);
		out.close();
	}
	static class ABearAndPrime100 {
		public ABearAndPrime100() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			pw.println("2\n3\n5\n7\n4\n9\n25\n49\n11\n13\n17\n19\n23\n29\n31\n37\n41\n43\n47");
			pw.flush();
			int cnt = 0;
			for(int i = 0; i<19; i++) {
				cnt += in.next().equals("yes") ? 1 : 0;
			}
			if(cnt<=1) {
				pw.println("prime");
			}else {
				pw.println("composite");
			}
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			br = new BufferedReader(new InputStreamReader(is), 1<<16);
			st = null;
		}

		public boolean hasNext() {
			try {
				while(st==null||!st.hasMoreTokens()) {
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

