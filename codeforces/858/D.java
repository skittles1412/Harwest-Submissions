import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.HashMap;
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
			DPolycarpsPhoneBook solver = new DPolycarpsPhoneBook();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DPolycarpsPhoneBook {
		public DPolycarpsPhoneBook() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			HashMap<String, Integer> hm = new HashMap<>();
			String[] arr = new String[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.next();
				HashSet<String> hs = new HashSet<>();
				for(int l = 0; l<arr[i].length(); l++) {
					for(int r = l; r<arr[i].length(); r++) {
						hs.add(arr[i].substring(l, r+1));
					}
				}
				for(String s: hs) {
					hm.put(s, hm.getOrDefault(s, 0)+1);
				}
			}
			loop:
			for(int i = 0; i<n; i++) {
				for(int j = 1; j<=arr[i].length(); j++) {
					for(int k = 0; k<arr[i].length()&&k+j<=arr[i].length(); k++) {
						if(hm.get(arr[i].substring(k, j+k))==1) {
							pw.println(arr[i].substring(k, j+k));
							continue loop;
						}
					}
				}
				throw new AssertionError();
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

