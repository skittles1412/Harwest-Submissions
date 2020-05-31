//package round_646;
import java.util.*;
import java.io.*;

public class Problem_B {
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
		Problem_B program = new Problem_B();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			String s = in.next();
			int n = s.length();
			int[] left = new int[n];
			int[] right = new int[n];
			for(int i = 0; i<n; i++) {
				left[i] = i==0?0:left[i-1];
				if(s.charAt(i)=='1') {
					left[i]++;
				}
			}
			for(int i = n-1; i>=0; i--) {
				right[i] = i==(n-1)?0:right[i+1];
				if(s.charAt(i)=='1') {
					right[i]++;
				}
			}
			int ans = Math.min(right[0],n-right[0]);
			for(int i = 0; i<n; i++) {
				if(i>0) {
					ans = Math.min(ans, n-i-right[i]+left[i-1]);
				}
				if(i<n-1) {
					ans = Math.min(ans, i+1-left[i]+right[i+1]);
				}
			}
			pw.println(ans);
		}
		pw.close();
		in.close();
	}
}
