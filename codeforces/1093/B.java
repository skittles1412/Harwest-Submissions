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
			BLettersRearranging solver = new BLettersRearranging();
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
	static class BLettersRearranging {
		public BLettersRearranging() {
		}

		public boolean isPalindrome(char[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(arr[i]!=arr[arr.length-i-1]) {
					return false;
				}
			}
			return true;
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			char[] arr = in.next().toCharArray();
			Utilities.sort(arr);
			if(isPalindrome(arr)) {
				pw.println(-1);
			}else {
				pw.println(new String(arr));
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

	static class Utilities {
		public static void sort(char[] arr) {
			ArrayList<Character> al = new ArrayList<Character>();
			for(char i: arr) {
				al.add(i);
			}
			Collections.sort(al);
			for(int i = 0; i<al.size(); i++) {
				arr[i] = al.get(i);
			}
		}

	}
}

