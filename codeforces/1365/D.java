import java.util.*;
import java.io.*;

public class Problem_D {
	static class Input {
		BufferedReader br;
		StringTokenizer st;
		public Input(Reader r) throws IOException {
			br = new BufferedReader(r);
			st = new StringTokenizer(br.readLine());
		}
		public String nextLine() throws IOException {
			return br.readLine();
		}
		public boolean hasNext() throws IOException {
			try {
				while(!st.hasMoreTokens()) {
					String s = br.readLine();
					if(s==null) {
						return false;
					}
					st = new StringTokenizer(s);
				}
				return true;
			}catch (Exception e){
				return false;
			}
		}
		public String next() throws IOException {
			if(!hasNext()) {
				return null;
			}
			return st.nextToken();
		}
		public int nextInt() throws IOException {
			String s = next();
			if(s==null) {
				return Integer.MIN_VALUE;
			}
			return Integer.parseInt(s);
		}
		public double nextDouble() throws IOException {
			String s = next();
			if(s==null) {
				return Double.MIN_VALUE;
			}
			return Double.parseDouble(s);
		}
		public long nextLong() throws IOException {
			String s = next();
			if(s==null) {
				return Long.MIN_VALUE;
			}
			return Long.parseLong(s);
		}
		public int countTokens() {
			return st.countTokens();
		}
		public void close() throws IOException {
			br.close();
		}
	}
	public char[][] arr;
	public final int[] dx = new int[] {0,0,-1,1}, dy = new int[] {1,-1,0,0};
	public boolean[][] visited;
	public int fcount, n, m;
	public void floodfill(int x, int y) {
		if(x<0||x>=n||y<0||y>=m||visited[x][y]||arr[x][y]=='#') {
			return;
		}
		if(arr[x][y]=='G') {
			fcount++;
		}
		visited[x][y] = true;
		for(int i = 0; i<4; i++) {
			floodfill(x+dx[i],y+dy[i]);
		}
	}
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_D program = new Problem_D();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			n = in.nextInt();
			m = in.nextInt();
			int count = 0;
			fcount = 0;
			arr = new char[n][m];
			visited = new boolean[n][m];
			for(int i = 0; i<n; i++) {
				String s = in.nextLine();
				for(int j = 0; j<m; j++) {
					arr[i][j] = s.charAt(j);
					if(arr[i][j]=='G') {
						count++;
					}
				}
			}
			boolean possible = true;
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<m; j++) {
					if(arr[i][j]=='B') {
						for(int k = 0; k<4; k++) {
							int cx = i+dx[k], cy = j+dy[k];
							if(cx<0||cx>=n||cy<0||cy>=m) {
								continue;
							}
							if(arr[cx][cy]=='G') {
								possible = false;
							}else if(arr[cx][cy]=='.') {
								arr[cx][cy]='#';
							}
						}
					}
				}
			}
			if(!possible) {
				pw.println("No");
				continue;
			}
			floodfill(n-1,m-1);
			if(fcount==count) {
				pw.println("Yes");
			}else {
				pw.println("No");
			}
		}
		pw.close();
		in.close();
	}
}
