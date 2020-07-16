import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Comparator;
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
			EDominoPrinciple solver = new EDominoPrinciple();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class EDominoPrinciple {
		public EDominoPrinciple() {
		}

		public void solve(int testNumber, Input in, PrintWriter pw) {
			int n = in.nextInt();
			int[][] arr = new int[n][2];
			int[] cpy = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = new int[] {in.nextInt(), in.nextInt()};
				cpy[i] = arr[i][0];
			}
			Arrays.sort(arr, Comparator.comparingInt(o1 -> o1[0]));
			int[] memo = new int[n], vals = new int[n];
			for(int i = 0; i<n; i++) {
				vals[i] = arr[i][0];
			}
			for(int i = 0; i<n; i++) {
				memo[i] = i;
			}
			SegmentTree st = new SegmentTree(memo);
			for(int i = n-2; i >= 0; i--) {
				int end = Utilities.upperBound(vals, arr[i][0]+arr[i][1]-1);
				memo[i] = st.max(i, end);
				st.set(i, memo[i]);
			}
			int[] ans = new int[n];
			for(int i = 0; i<n; i++) {
				int x = Utilities.upperBound(vals, cpy[i]);
				ans[i] = memo[x]-x+1;
			}
			pw.println(Utilities.getString(ans));
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

		public int max(int l, int r) {
			query(l, r);
			return _max;
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

		public static int upperBound(int[] arr, int target) {
			int l = -1, h = arr.length-1;
			while(l<h) {
				int mid = l+h+1 >> 1;
				if(arr[mid]<=target) {
					l = mid;
				}else {
					h = mid-1;
				}
			}
			return l;
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

