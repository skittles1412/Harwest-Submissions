import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
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
			DConstantPalindromeSum solver = new DConstantPalindromeSum();
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
	static class DConstantPalindromeSum {
		public DConstantPalindromeSum() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt(), k = in.nextInt();
			int[] karr = new int[(k<<1)+1];
			SegmentTree st = new SegmentTree(karr);
			int[] arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			for(int i = 0; i<n >> 1; i++) {
				st.add(2, k<<1, 2);
				st.add(Math.min(arr[i], arr[n-i-1])+1, Math.max(arr[i], arr[n-i-1])+k, -1);
				st.add(arr[i]+arr[n-i-1], arr[i]+arr[n-i-1], -1);
			}
			int ans = Integer.MAX_VALUE;
			for(int i = 2; i<=k<<1; i++) {
				ans = Math.min(ans, st.sum(i, i));
			}
			pw.println(ans);
		}

	}

	static class SegmentTree {
		int[] arr;
		int[] sumv;
		int[] minv;
		int[] maxv;
		int[] addv;
		int[] setv;
		int _sum;
		int _min;
		int _max;
		int n;
		int y1;
		int y2;
		int v;
		boolean[] setc;
		boolean add;

		private int left(int x) {
			return x<<1;
		}

		private int right(int x) {
			return (x<<1)+1;
		}

		public SegmentTree(int[] arr) {
			this.arr = arr.clone();
			n = arr.length;
			sumv = new int[n*4];
			minv = new int[n*4];
			maxv = new int[n*4];
			addv = new int[n*4];
			setv = new int[n*4];
			setc = new boolean[n*4];
			for(int i = 0; i<n; i++) {
				set(i, arr[i]);
			}
		}

		public void query(int o, int l, int r, int add) {
			if(setc[o]) {
				int v = setv[o]+add+addv[o];
				_sum += v*(Math.min(r, y2)-Math.max(l, y1)+1);
				_min = Math.min(_min, v);
				_max = Math.max(_max, v);
			}else if(y1<=l&&y2 >= r) {
				_sum += sumv[o]+add*(r-l+1);
				_min = Math.min(_min, minv[o]+add);
				_max = Math.max(_max, maxv[o]+add);
			}else {
				int m = (l+r)/2;
				if(y1<=m) {
					query(left(o), l, m, add+addv[o]);
				}
				if(y2>m) {
					query(right(o), m+1, r, add+addv[o]);
				}
			}
		}

		public void update(int o, int l, int r) {
			int lc = left(o), rc = right(o);
			if(y1<=l&&y2 >= r) {
				if(add) {
					addv[o] += v;
				}else {
					setv[o] = v;
					setc[o] = true;
					addv[o] = 0;
				}
			}else {
				pushdown(o);
				int m = (l+r)/2;
				if(y1<=m) {
					update(lc, l, m);
				}else {
					maintain(lc, l, m);
				}
				if(y2>m) {
					update(rc, m+1, r);
				}else {
					maintain(rc, m+1, r);
				}
			}
			maintain(o, l, r);
		}

		public void maintain(int o, int l, int r) {
			int lc = left(o), rc = right(o);
			if(r>l) {
				sumv[o] = sumv[lc]+sumv[rc];
				minv[o] = Math.min(minv[lc], minv[rc]);
				maxv[o] = Math.max(maxv[lc], maxv[rc]);
			}
			if(setc[o]) {
				minv[o] = maxv[o] = setv[o];
				sumv[o] = setv[o]*(r-l+1);
			}
			if(addv[o]!=0) {
				minv[o] += addv[o];
				maxv[o] += addv[o];
				sumv[o] += addv[o]*(r-l+1);
			}
		}

		public void pushdown(int o) {
			int lc = left(o), rc = right(o);
			if(setc[o]) {
				setv[lc] = setv[rc] = setv[o];
				addv[lc] = addv[rc] = 0;
				setc[o] = false;
				setc[lc] = setc[rc] = true;
			}
			if(addv[o]!=0) {
				addv[lc] += addv[o];
				addv[rc] += addv[o];
				addv[o] = 0;
			}
		}

		public void add(int l, int r, int v) {
			y1 = l;
			y2 = r;
			this.v = v;
			add = true;
			update(1, 0, n-1);
		}

		public void set(int p, int v) {
			y1 = p;
			y2 = p;
			this.v = v;
			add = false;
			update(1, 0, n-1);
		}

		public void query(int l, int r) {
			y1 = l;
			y2 = r;
			_sum = 0;
			_max = Integer.MIN_VALUE;
			_min = Integer.MAX_VALUE;
			query(1, 0, n-1, 0);
		}

		public int sum(int l, int r) {
			query(l, r);
			return _sum;
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

