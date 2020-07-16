import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Collections;
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
			BMakingAString solver = new BMakingAString();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BMakingAString {
		public BMakingAString() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			long[] arr = new long[n+1];
			long ans = 0;
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextLong();
			}
			arr[n] = Integer.MAX_VALUE;
			Utilities.sort(arr);
			for(int i = n-1; i >= 0; i--) {
				int j;
				for(j = i; j >= 0&&arr[j]==arr[j+1]; j--) ;
				for(; j<=i; j++, arr[j]--) ;
				arr[i] = Math.max(arr[i], 0);
				ans += arr[i];
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

	static class Utilities {
		public static void sort(long[] arr) {
			ArrayList<Long> al = new ArrayList<Long>();
			for(long i: arr) {
				al.add(i);
			}
			Collections.sort(al);
			for(int i = 0; i<al.size(); i++) {
				arr[i] = al.get(i);
			}
		}

	}
}

