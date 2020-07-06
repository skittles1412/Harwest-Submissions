import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.util.StringTokenizer;
import java.util.Map.Entry;
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
		CPlusesAndMinuses solver = new CPlusesAndMinuses();
		solver.solve(1, in, out);
		out.close();
	}
	static class CPlusesAndMinuses {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				String s = in.next();
				int n = s.length();
				int[] arr = new int[n], psum = new int[n];
				for(int i = 0; i<n; i++) {
					arr[i] = 44-s.charAt(i);
				}
				psum[0] = arr[0];
				for(int i = 1; i<n; i++) {
					psum[i] = arr[i]+psum[i-1];
				}
				TreeMap<Integer, Integer> tm = new TreeMap<>();
				for(int i = 0; i<psum.length; i++) {
					if(!tm.containsKey(psum[i])) {
						tm.put(psum[i], i+1);
					}
				}
				long ans = 0;
				for(int i = 0; ; i++) {
					if(tm.lowerKey(-i)==null) {
						ans += n;
						break;
					}
					Entry<Integer, Integer> e = tm.lowerEntry(-i);
					ans += e.getValue();
				}
				out.println(ans);
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

