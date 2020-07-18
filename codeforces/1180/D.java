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
			DTolikAndHisUncle solver = new DTolikAndHisUncle();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DTolikAndHisUncle {
		public DTolikAndHisUncle() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt(), m = in.nextInt();
			for(int i = 0; i<n >> 1; i++) {
				for(int j = 0; j<m; j++) {
					pw.println((i+1)+" "+(j+1));
					pw.println((n-i)+" "+(m-j));
				}
			}
			if((n&1)==1) {
				for(int j = 0; j<m >> 1; j++) {
					pw.println((n+1 >> 1)+" "+(j+1));
					pw.println((n+1 >> 1)+" "+(m-j));
				}
				if((m&1)==1) {
					pw.println((n+1 >> 1)+" "+(m+1 >> 1));
				}
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

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

