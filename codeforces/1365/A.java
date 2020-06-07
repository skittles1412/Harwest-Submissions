import java.util.*;
import java.io.*;

public class Problem_A {
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
		Problem_A program = new Problem_A();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			int n = in.nextInt();
			int m = in.nextInt();
			boolean[] row = new boolean[n];
			boolean[] column = new boolean[m];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<m; j++) {
					boolean b = in.nextInt()==1;
					row[i]|=b;
					column[j]|=b;
				}
			}
			int rowc = n, columnc = m;
			for(int i = 0; i<n; i++) {
				if(row[i]) {
					rowc--;
				}
			}
			for(int i = 0; i<m; i++) {
				if(column[i]) {
					columnc--;
				}
			}
			if(Math.min(rowc, columnc)%2==1) {
				pw.println("Ashish");
			}else {
				pw.println("Vivek");
			}
		}
		pw.close();
		in.close();
	}
}
