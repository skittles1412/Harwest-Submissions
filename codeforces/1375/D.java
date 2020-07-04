import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.TreeSet;
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
		DReplaceByMEX solver = new DReplaceByMEX();
		solver.solve(1, in, out);
		out.close();
	}
	static class DReplaceByMEX {
		public int mex(int[] arr) {
			TreeSet<Integer> used = new TreeSet<>();
			for(int i = 0; i<=arr.length; i++) {
				used.add(i);
			}
			for(int i = 0; i<arr.length; i++) {
				used.remove(arr[i]);
			}
			return used.first();
		}

		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt();
				int[] arr = new int[n];
				TreeSet<Integer> good = new TreeSet<>();
				for(int i = 0; i<n; i++) {
					arr[i] = in.nextInt();
					if(arr[i]!=i) {
						good.add(i);
					}
				}
				ArrayList<Integer> ans = new ArrayList<>();
				for(int i = 0; i<n; i++) {
					int mex = mex(arr);
					if(mex==n) {
						if(good.isEmpty()) {
							break;
						}
						int chg = good.first();
						ans.add(chg);
						arr[chg] = n;
						mex = mex(arr);
					}
					arr[mex] = mex;
					good.remove(mex);
					ans.add(mex);
				}
				out.println(ans.size());
				for(int i = 0; i<ans.size(); i++) {
					if(i!=0) {
						out.print(" ");
					}
					out.print(ans.get(i)+1);
				}
				out.println();
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

