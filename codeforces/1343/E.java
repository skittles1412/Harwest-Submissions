import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collections;
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
			EWeightsDistributing solver = new EWeightsDistributing();
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
	static class EWeightsDistributing {
		public ArrayList<Integer>[] graph;
		public int n;

		public EWeightsDistributing() {
		}

		public int[] bfs(int v) {
			int[] ans = new int[n];
			Arrays.fill(ans, Integer.MAX_VALUE);
			ans[v] = 0;
			Queue<Integer> q = new LinkedList<>();
			q.add(v);
			while(!q.isEmpty()) {
				int x = q.poll();
				for(int i: graph[x]) {
					if(ans[i]==Integer.MAX_VALUE) {
						q.add(i);
						ans[i] = ans[x]+1;
					}
				}
			}
			return ans;
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			n = in.nextInt();
			int m = in.nextInt(), a = in.nextInt()-1, b = in.nextInt()-1, c = in.nextInt()-1;
			int[] costs = new int[m];
			long[] psum = new long[m+1];
			graph = new ArrayList[n];
			for(int i = 0; i<m; i++) {
				costs[i] = in.nextInt();
			}
			Utilities.sort(costs);
			for(int i = 1; i<=m; i++) {
				psum[i] = psum[i-1]+(long) costs[i-1];
			}
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				int u = in.nextInt()-1, v = in.nextInt()-1;
				graph[u].add(v);
				graph[v].add(u);
			}
			int[] adist = bfs(a), bdist = bfs(b), cdist = bfs(c);
			long ans = Long.MAX_VALUE;
			for(int i = 0; i<n; i++) {
				if(adist[i]+bdist[i]+cdist[i]<=m) {
					ans = Math.min(ans, psum[bdist[i]]+psum[adist[i]+bdist[i]+cdist[i]]);
				}
			}
			pw.println(ans);
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
}

