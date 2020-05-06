//package round_639;

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
		int[] arr = new int[31624];
		arr[1] = 2;
		for(int i = 2; i<arr.length; i++) {
			arr[i] = arr[i-1]+2*i+i-1;
		}
		int kase = in.nextInt();
		while(kase-->0) {
			int n = in.nextInt();
			int ans = 0;
			while(n>1) {
				int cur = Arrays.binarySearch(arr, n);
				if(cur<0) {
					cur = -(cur+1);
					cur--;
				}
				n-=arr[cur];
				ans++;
			}
			pw.println(ans);
		}
		pw.close();
		in.close();
	}
}
