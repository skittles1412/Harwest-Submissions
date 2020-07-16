import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.StringTokenizer;
import java.io.BufferedReader;
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
			BEquivalentStrings solver = new BEquivalentStrings();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BEquivalentStrings {
		public HashMap<BEquivalentStrings.state, Boolean> hm;

		public BEquivalentStrings() {
		}

		public boolean equal(String a, String b) {
			BEquivalentStrings.state ste1 = new BEquivalentStrings.state(a, b), ste2 = new BEquivalentStrings.state(b, a);
			if(hm.containsKey(ste1)) {
				return hm.get(ste1);
			}
			if(a.equals(b)) {
				return true;
			}else if((a.length()&1)!=0) {
				return false;
			}

			int n = a.length();
			String a1 = a.substring(0, n >> 1), a2 = a.substring(n >> 1, n), b1 = b.substring(0, n >> 1), b2 = b.substring(n >> 1, n);
			boolean ans = (equal(a1, b1)&&equal(a2, b2))||(equal(a1, b2)&&equal(a2, b1));
			hm.put(ste1, ans);
			hm.put(ste2, ans);
			return ans;
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			String a = in.next(), b = in.next();
			hm = new HashMap<>();
			if(equal(a, b)) {
				pw.println("YES");
			}else {
				pw.println("NO");
			}
		}

		static class state {
			String s1;
			String s2;

			state(String a, String b) {
				s1 = a;
				s2 = b;
			}

			public boolean equals(Object o) {
				if(this==o) {
					return true;
				}else if(o==null||getClass()!=o.getClass()) {
					return false;
				}
				BEquivalentStrings.state s = (BEquivalentStrings.state) o;
				return s1.equals(s.s1)&&s2.equals(s.s2);
			}

			public int hashCode() {
				return Objects.hash(s1, s2);
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
}

