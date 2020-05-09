//package round_640;

import java.util.*;
import java.io.*;

public class Problem_E {
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
		Problem_E program = new Problem_E();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			int n = in.nextInt();
			int[] arr = new int[n];
			int[] psum = new int[n+1];
			int[] count = new int[n+1];
			int ans = 0;
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
				psum[i+1] = psum[i]+arr[i];
				count[arr[i]]++;
			}
			for(int i = 0; i<n; i++) {
				for(int j = i+1; j<n; j++) {
					int sum = psum[j+1]-psum[i];
					if(sum>n) {
						break;
					}
					ans+=count[sum];
					count[sum] = 0;
				}
			}
			pw.println(ans);
		}
		pw.close();
		in.close();
	}
}
