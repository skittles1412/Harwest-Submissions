//package round_640;

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
			while(!st.hasMoreTokens()) {
				String s = br.readLine();
				if(s==null) {
					return false;
				}
				st = new StringTokenizer(s);
			}
			return true;
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
		Problem_D program = new Problem_D();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			int n = in.nextInt();
			int[] arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			int a = 0;
			int b = 0;
			int pa = 0;
			int pb = n-1;
			int prev = 0;
			int move = 0;
			while(pa<=pb) {
				if(move%2==0) {
					int sum = 0;
					for(; pa<=pb&&sum<=prev; pa++) {
						sum+=arr[pa];
					};
					prev = sum;
					a+=sum;
				}else {
					int sum = 0;
					for(; pa<=pb&&sum<=prev; pb--) {
						sum+=arr[pb];
					};
					prev = sum;
					b+=sum;
				}
				move++;
			}
			pw.printf("%d %d %d\n", move, a, b);
		}
		pw.close();
		in.close();
	}
}
