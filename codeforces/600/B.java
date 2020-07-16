import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.InputMismatchException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.io.InputStream;
import java.util.ArrayList;

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
			BQueriesAboutLessOrEqualElements solver = new BQueriesAboutLessOrEqualElements();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BQueriesAboutLessOrEqualElements {
		public BQueriesAboutLessOrEqualElements() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt(), m = in.nextInt(), a[] = new int[n], b[] = new int[m];
			for(int i = 0; i<n; i++) {
				a[i] = in.nextInt();
			}
			Utilities.sort(a);
			for(int i = 0; i<m; i++) {
				int x = in.nextInt(), l = -1, h = n-1;
				while(l<h) {
					int mid = l+h+1 >> 1;
					if(a[mid]<=x) {
						l = mid;
					}else {
						h = mid-1;
					}
				}
				b[i] = l+1;
			}
			pw.println(Utilities.getString(b));
		}

	}

	static class Utilities {
		public static void sort(int[] arr) {
			ArrayList<Integer> al = new ArrayList<Integer>();
			for(int i: arr) {
				al.add(i);
			}
			Collections.sort(al);
			for(int i = 0; i<al.size(); i++) {
				arr[i] = al.get(i);
			}
		}

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

