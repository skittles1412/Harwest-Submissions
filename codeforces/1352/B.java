//package round_640;

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
			int n = in.nextInt();
			int k = in.nextInt();
			if(n%2==1&&k%2==0) {
				pw.println("NO");
				continue;
			}
			if(n%2==0) {
				if(k%2==0) {
					if(n<k) {
						pw.println("NO");
						continue;
					}
					pw.println("YES");
					for(int i = 0; i<k-1; i++) {
						pw.print(1);
						pw.print(" ");
					}
					pw.println(n-k+1);
				}else {
					if(n<k<<1) {
						pw.println("NO");
						continue;
					}
					pw.println("YES");
					for(int i = 0; i<k-1; i++) {
						pw.print(2);
						pw.print(" ");
					}
					pw.println(n-((k-1)<<1));
				}
			}else {
				if(n<k) {
					pw.println("NO");
					continue;
				}
				pw.println("YES");
				for(int i = 0; i<k-1; i++) {
					pw.print(1);
					pw.print(" ");
				}
				pw.println(n-k+1);
			}
		}
		pw.close();
		in.close();
	}
}
