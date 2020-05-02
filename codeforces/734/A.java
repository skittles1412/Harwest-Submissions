//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_734A {
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
		public void close() throws IOException {
			br.close();
		}
	}
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_734A program = new Problem_734A();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int count1 = 0;
		int count2 = 0;
		int n = in.nextInt();
		String s = in.next();
		for(int i = 0; i<n; i++) {
			if(s.charAt(i)=='A') {
				count1++;
			}
		}
		count2 = n-count1;
		if(count2>count1) {
			pw.println("Danik");
		}else if(count2==count1) {
			pw.println("Friendship");
		}else {
			pw.println("Anton");
		}
		pw.close();
		in.close();
	}
}
