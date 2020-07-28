import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Flushable;

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
			Output out = new Output(outputStream);
			BMonopoleMagnets solver = new BMonopoleMagnets();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BMonopoleMagnets {
		int n;
		int m;
		boolean[][] arr;
		boolean[][] visited;
		int[] dx = new int[] {0, 0, 1, -1};
		int[] dy = new int[] {1, -1, 0, 0};

		public BMonopoleMagnets() {
		}

		public void dfs(int x, int y) {
			if(visited[x][y]) {
				return;
			}
			visited[x][y] = true;
			for(int i = 0; i<4; i++) {
				int cx = x+dx[i], cy = y+dy[i];
				if(valid(cx, cy)) {
					dfs(cx, cy);
				}
			}
		}

		public boolean valid(int x, int y) {
			return x >= 0&&x<n&&y >= 0&&y<m&&arr[x][y];
		}

		public void solve(int kase, Input in, Output pw) {
			n = in.nextInt();
			m = in.nextInt();
			arr = new boolean[n][m];
			visited = new boolean[n][m];
			for(int i = 0; i<n; i++) {
				String s = in.next();
				for(int j = 0; j<m; j++) {
					arr[i][j] = s.charAt(j)=='#';
				}
			}
			int er = 0, ec = 0;
			for(int i = 0; i<n; i++) {
				int cur = 0;
				for(int j = 0; j<m; j++) {
					if(arr[i][j]) {
						cur++;
						for(; j<m&&arr[i][j]; j++) ;
						j--;
					}
				}
				if(cur==0) {
					er++;
				}else if(cur>1) {
					pw.println(-1);
					return;
				}
			}
			for(int i = 0; i<m; i++) {
				int cur = 0;
				for(int j = 0; j<n; j++) {
					if(arr[j][i]) {
						cur++;
						for(; j<n&&arr[j][i]; j++) ;
						j--;
					}
				}
				if(cur==0) {
					ec++;
				}else if(cur>1) {
					pw.println(-1);
					return;
				}
			}
			if((ec>0&&er==0)||(er>0&&ec==0)) {
				pw.println(-1);
				return;
			}
			int ans = 0;
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<m; j++) {
					if(arr[i][j]&&!visited[i][j]) {
						ans++;
						dfs(i, j);
					}
				}
			}
			pw.println(ans);
		}

	}

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public boolean autoFlush;
		public String LineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			autoFlush = false;
			LineSeparator = System.lineSeparator();
		}

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println() {
			sb.append(LineSeparator);
		}

		private void flushToBuffer() {
			try {
				os.write(sb.toString().getBytes());
			}catch(IOException e) {
				e.printStackTrace();
			}
			sb = new StringBuilder(BUFFER_SIZE);
		}

		public void flush() {
			try {
				flushToBuffer();
				os.flush();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public void close() {
			flush();
			try {
				os.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
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

