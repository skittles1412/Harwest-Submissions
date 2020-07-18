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
import java.util.ArrayList;
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
			EDirectingEdges solver = new EDirectingEdges();
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
	static class EDirectingEdges {
		int n;
		int m;
		ArrayList<Integer>[] graph;
		Stack<Integer> sort;
		boolean[] marked;

		public EDirectingEdges() {
		}

		public void topsort() {
			sort = new Stack<>();
			marked = new boolean[n];
			for(int i = 0; i<n; i++) {
				if(!marked[i]) {
					dfs(i);
				}
			}
		}

		public void dfs(int v) {
			marked[v] = true;
			for(int i: graph[v]) {
				if(!marked[i]) {
					dfs(i);
				}
			}
			sort.add(v);
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			n = in.nextInt();
			m = in.nextInt();
			ArrayList<int[]> undir = new ArrayList<>();
			graph = new ArrayList[n];
			for(int i = 0; i<n; i++) {
				graph[i] = new ArrayList<>();
			}
			for(int i = 0; i<m; i++) {
				int type = in.nextInt(), u = in.nextInt()-1, v = in.nextInt()-1;
				if(type==0) {
					undir.add(new int[] {u, v});
				}else {
					graph[u].add(v);
				}
			}
			int[] conv = new int[n];
			topsort();
			int ind = 0;
			while(!sort.isEmpty()) {
				conv[sort.pop()] = ind++;
			}
			for(int i = 0; i<n; i++) {
				for(int j: graph[i]) {
					if(conv[i]>conv[j]) {
						pw.println("NO");
						return;
					}
				}
			}
			pw.println("YES");
			for(int[] i: undir) {
				int u = conv[i[0]], v = conv[i[1]];
				if(u<v) {
					pw.println((i[0]+1)+" "+(i[1]+1));
				}else {
					pw.println((i[1]+1)+" "+(i[0]+1));
				}
			}
			for(int i = 0; i<n; i++) {
				for(int j: graph[i]) {
					pw.println((i+1)+" "+(j+1));
				}
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

