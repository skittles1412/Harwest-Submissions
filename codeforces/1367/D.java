import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
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
		DTaskOnTheBoard solver = new DTaskOnTheBoard();
		solver.solve(1, in, out);
		out.close();
	}
	static class DTaskOnTheBoard {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int[] cnt = new int[26];
				String s = in.next();
				for(int i = 0; i<s.length(); i++) {
					cnt[s.charAt(i)-'a']++;
				}
				int m = in.nextInt();
				int[] arr = new int[m];
				char[] ans = new char[m];
				boolean[] visited = new boolean[m];
				for(int i = 0; i<m; i++) {
					arr[i] = in.nextInt();
				}
				int ind = 25;
				while(ind >= 0) {
					ArrayList<Integer> inds = new ArrayList<>();
					for(int i = 0; i<arr.length; i++) {
						if(!visited[i]&&arr[i]<=0) {
							inds.add(i);
						}
					}
					for(; cnt[ind]<inds.size(); ind--) ;
					for(int i: inds) {
						ans[i] = (char) (ind+'a');
						visited[i] = true;
					}
					for(int i = 0; i<arr.length; i++) {
						if(!visited[i]) {
							for(int j: inds) {
								arr[i] -= Math.abs(i-j);
							}
						}
					}
					ind--;
				}
				out.println(new String(ans));
			}
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			br = new BufferedReader(new InputStreamReader(is));
			try {
				st = new StringTokenizer(br.readLine());
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public boolean hasNext() {
			try {
				while(!st.hasMoreTokens()) {
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
			return st.nextToken("\r\n\t\f ");
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

