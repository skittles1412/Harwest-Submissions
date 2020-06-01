//package round_646;

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
			int k = in.nextInt();
			HashSet<Integer>[] al = new HashSet[k];
			for(int i = 0; i<k; i++) {
				int c = in.nextInt();
				al[i] = new HashSet<Integer>();
				while(c-->0) {
					al[i].add(in.nextInt());
				}
			}
			pw.printf("? %d",n);
			for(int i = 1; i<=n; i++) {
				pw.print(" "+i);
			}
			pw.println();
			pw.flush();
			int max = in.nextInt();
			int l = 1, h = n;
			while(l<h) {
				int mid = (l+h)>>1;
				pw.printf("? %d",mid-l+1);
				for(int j = l; j<=mid; j++) {
					pw.print(" "+j);
				}
				pw.println();
				pw.flush();
				int x = in.nextInt();
				if(x==max) {
					h = mid;
				}else {
					l = mid+1;
				}
			}
			int ind = -1;
			for(int j = 0; j<k; j++) {
				if(al[j].contains(l)) {
					ind = j;
					break;
				}
			}
			if(ind==-1) {
				pw.print("!");
				for(int i = 0; i<k; i++) {
					pw.print(" "+max);
				}
				pw.println();
				pw.flush();
			}else {
				pw.printf("? %d", n-al[ind].size());
				for(int i = 1; i<=n; i++) {
					if(!al[ind].contains(i)) {
						pw.print(" "+i);
					}
				}
				pw.println();
				pw.flush();
				int max2 = in.nextInt();
				pw.print("!");
				for(int i = 0; i<k; i++) {
					if(i!=ind) {
						pw.print(" "+max);
					}else {
						pw.print(" "+max2);
					}
				}
				pw.println();
				pw.flush();
			}
			String rep = in.next();
			if(rep.equals("Incorrect")) {
				System.exit(0);
			}
		}
		pw.close();
		in.close();
	}
}
