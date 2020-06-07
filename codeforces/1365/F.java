import java.util.*;
import java.io.*;

public class Problem_F {
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
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_F program = new Problem_F();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			int n = in.nextInt();
			int[] a = new int[n];
			int[] b = new int[n];
			for(int i = 0; i<n; i++) {
				a[i] = in.nextInt();
			}
			for(int i = 0; i<n; i++) {
				b[i] = in.nextInt();
			}
			int[][] aset = new int[(n+1)/2][2];
			int[][] bset = new int[(n+1)/2][2];
			for(int i = 0; i<(n+1)/2; i++) {
				aset[i][0] = a[i];
				aset[i][1] = a[n-i-1];
				bset[i][0] = b[i];
				bset[i][1] = b[n-i-1];
				Arrays.sort(aset[i]);
				Arrays.sort(bset[i]);
			}
			Arrays.sort(aset, new Comparator<int[]>() {
				public int compare(int[] a, int[] b) {
					if(a[0]==b[0]) {
						return a[1]-b[1];
					}
					return a[0]-b[0];
				}
			});
			Arrays.sort(bset, new Comparator<int[]>() {
				public int compare(int[] a, int[] b) {
					if(a[0]==b[0]) {
						return a[1]-b[1];
					}
					return a[0]-b[0];
				}
			});
			boolean valid = true;
			for(int i = 0; i<(n+1)/2&&valid; i++) {
				if(aset[i][0]!=bset[i][0]||aset[i][1]!=bset[i][1]) {
					valid = false;
				}
			}
			if(!valid) {
				pw.println("No");
			}else {
				pw.println("Yes");
			}
		}
		pw.close();
		in.close();
	}
}
