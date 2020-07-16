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
			BBalancedArray solver = new BBalancedArray();
			int testCount = Integer.parseInt(in.next());
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BBalancedArray {
		public BBalancedArray() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			if(n%4!=0) {
				pw.println("NO");
				return;
			}
			int[] arr = new int[n];
			for(int i = 0; i<n >> 1; i++) {
				if((i&1)==0) {
					arr[i] = i+1<<2;
					arr[i+(n >> 1)] = (i+1<<2)+1;
				}else {
					arr[i] = i+1<<2;
					arr[i+(n >> 1)] = (i+1<<2)-1;
				}
			}
			pw.println("YES");
			pw.println(Utilities.getString(arr));
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

	static class Utilities {
		public static String getString(int[] arr) {
			return getString(arr, "", " ", "");
		}

		public static String getString(int[] arr, String start, String middle, String end) {
			StringBuilder builder = new StringBuilder();
			builder.append(start);
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					builder.append(middle);
				}
				builder.append(arr[i]);
			}
			builder.append(end);
			return builder.toString();
		}

	}
}

