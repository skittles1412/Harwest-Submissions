import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Comparator;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			Input in = new Input(inputStream);
			PrintWriter out = new PrintWriter(outputStream);
			CVanyaAndExams solver = new CVanyaAndExams();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CVanyaAndExams {
		public CVanyaAndExams() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt(), r = in.nextInt();
			long targetSum = n*in.nextLong(), ans = 0;
			long[][] arr = new long[n][2];
			for(int i = 0; i<n; i++) {
				arr[i] = new long[] {in.nextLong(), in.nextLong()};
				targetSum -= arr[i][0];
			}
			Arrays.sort(arr, Comparator.comparingLong(o -> o[1]));
			for(int i = 0; i<n&&targetSum>0; i++) {
				long dif = Math.min(targetSum, r-arr[i][0]);
				ans += dif*arr[i][1];
				targetSum -= dif;
			}
			pw.println(ans);
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

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

	}
}

