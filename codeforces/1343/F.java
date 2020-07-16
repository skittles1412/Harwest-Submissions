import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
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
			FRestoreThePermutationBySortedSegments solver = new FRestoreThePermutationBySortedSegments();
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
	static class FRestoreThePermutationBySortedSegments {
		public FRestoreThePermutationBySortedSegments() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			HashSet<Integer>[] segs = new HashSet[n-1];
			for(int i = 0; i<n-1; i++) {
				segs[i] = new HashSet<>();
				int x = in.nextInt();
				for(int j = 0; j<x; j++) {
					segs[i].add(in.nextInt());
				}
			}
			loop:
			for(int first = 1; first<=n; first++) {
				HashSet<Integer>[] tmp = new HashSet[n-1];
				for(int i = 0; i<tmp.length; i++) {
					tmp[i] = new HashSet<>(segs[i]);
				}
				ArrayList<Integer> cur = new ArrayList<>();
				cur.add(first);
				for(int i = 0; i<n-1; i++) {
					int x = cur.get(i);
					ArrayList<HashSet<Integer>> size1 = new ArrayList<>();
					for(int j = 0; j<tmp.length; j++) {
						if(tmp[j].remove(x)&&tmp[j].size()==1) {
							size1.add(tmp[j]);
						}
					}
					if(size1.size()!=1) {
						continue loop;
					}
					cur.add(size1.get(0).iterator().next());
				}
				boolean[] marked = new boolean[n];
				inner:
				for(HashSet<Integer> hs: segs) {
					HashSet<Integer> curhs = new HashSet<>(hs);
					iner:
					for(int i = 0; i<=n-curhs.size(); i++) {
						if(marked[i+curhs.size()-1]) {
							continue;
						}
						for(int j = i; j<i+curhs.size(); j++) {
							if(!curhs.contains(cur.get(j))) {
								continue iner;
							}
						}
						marked[i+curhs.size()-1] = true;
						continue inner;
					}
					continue loop;
				}
				int[] ans = new int[n];
				for(int i = 0; i<n; i++) {
					ans[i] = cur.get(i);
				}
				pw.println(Utilities.getString(ans));
				return;
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

