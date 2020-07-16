import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.InputMismatchException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;
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
			CLongestRegularBracketSequence solver = new CLongestRegularBracketSequence();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CLongestRegularBracketSequence {
		public CLongestRegularBracketSequence() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			char[] carr = in.next().toCharArray();
			int n = carr.length;
			boolean[] arr = new boolean[n];
			int[] prev = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = carr[i]==')';
			}
			Stack<Integer> s = new Stack<>();
			int ans = 0, count = 1;
			for(int i = 0; i<n; i++) {
				if(arr[i]) {
					if(s.isEmpty()) {
						prev[i] = -1;
					}else {
						int cur = s.pop();
						prev[i] = cur;
						if(cur-1 >= 0&&arr[cur-1]&&prev[cur-1]!=-1) {
							prev[i] = prev[cur-1];
						}
					}
				}else {
					s.push(i);
				}
			}
			for(int i = 0; i<n; i++) {
				if(prev[i]==-1||!arr[i]) {
					continue;
				}
				int x = i-prev[i]+1;
				if(x==ans) {
					count++;
				}else if(x>ans) {
					ans = x;
					count = 1;
				}
			}
			pw.printf("%d %d\n", ans, count);
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

