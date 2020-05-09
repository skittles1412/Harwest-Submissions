//package round_640;

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
	public boolean possible;
	public String ans;
	public void find(StringBuilder sb, int a, int b, int c) {
		if(a<0||b<0||c<0||possible) {
			return;
		}
		if(a==0&&b==0&&c==0) {
			possible = true;
			ans = sb.toString();
			return;
		}
		if(sb.length()==0) {
			sb.append('1');
			find(new StringBuilder(sb),a,b,c);
			sb.setCharAt(0, '0');
			find(new StringBuilder(sb),a,b,c);
		}else {
			if(sb.charAt(sb.length()-1)=='1') {
				sb.append('1');
				find(new StringBuilder(sb),a,b,c-1);
				sb.setCharAt(sb.length()-1, '0');
				find(new StringBuilder(sb),a,b-1,c);
			}else {
				sb.append('1');
				find(new StringBuilder(sb),a,b-1,c);
				sb.setCharAt(sb.length()-1, '0');
				find(new StringBuilder(sb),a-1,b,c);
			}
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
			possible = false;
			find(new StringBuilder(),in.nextInt(),in.nextInt(),in.nextInt());
			pw.println(ans);
		}
		pw.close();
		in.close();
	}
}
