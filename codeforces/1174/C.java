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
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			Input in = new Input(inputStream);
			PrintWriter out = new PrintWriter(outputStream);
			CEhabAndASpecialColoringProblem solver = new CEhabAndASpecialColoringProblem();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CEhabAndASpecialColoringProblem {
		public CEhabAndASpecialColoringProblem() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			int[] arr = new int[n+1];
			int ind = 1;
			for(int i = 2; i<=n; i++) {
				if(arr[i]==0) {
					arr[i] = ind++;
					for(int j = i<<1; j<=n; j += i) {
						arr[j] = arr[i];
					}
				}
			}
			for(int i = 2; i<=n; i++) {
				pw.printf("%d ", arr[i]);
			}
			pw.println();
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

	}
}

