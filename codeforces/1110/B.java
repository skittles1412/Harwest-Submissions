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
			BTape solver = new BTape();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BTape {
		public BTape() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt(), m = in.nextInt(), k = in.nextInt(), ans = n;
			int[] arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			int[] dif = new int[n-1];
			for(int i = 0; i<n-1; i++) {
				dif[i] = arr[i+1]-arr[i];
			}
			Utilities.sort(dif);
			for(int i = 0; i<n-k; i++) {
				ans += dif[i]-1;
			}
			pw.println(ans);
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

