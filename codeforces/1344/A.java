//package round_639;

import java.util.*;
import java.io.*;

public class Problem_C {
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
		public long nextLong() throws IOException {
			String s = next();
			if(s==null) {
				return Long.MIN_VALUE;
			}
			return Long.parseLong(s);
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
		Problem_C program = new Problem_C();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			int n = in.nextInt();
			long add = ((1000000000/n)+10)*n;
			long[] arr = new long[n];
			boolean works = true;
			HashMap<Long, Integer> hs = new HashMap<Long, Integer>();
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextLong()+add;
			}
			for(int i = 0; i<n; i++) {
				long key = (i+arr[i])%n;
				if(hs.containsKey(key)) {
					works = false;
					break;
				}
				hs.put(key, i);
			}
			if(!works) {
				pw.println("NO");
				continue;
			}
			for(int i = 0; i<n; i++) {
				long key = (i+arr[i])%n;
				if(hs.containsKey(key)) {
					int cur = hs.get(key);
					if(cur>i) {
						works = false;
						break;
					}
				}
			}
			if(works) {
				pw.println("YES");
			}else {
				pw.println("NO");
			}
		}
		pw.close();
		in.close();
	}
}
