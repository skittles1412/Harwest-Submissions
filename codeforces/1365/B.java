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
			int n = in.nextInt();
			int[] a = new int[n];
			int[] b = new int[n];
			for(int i = 0; i<n; i++) {
				a[i] = in.nextInt();
			}
			for(int i = 0; i<n; i++) {
				b[i] = in.nextInt();
			}
			boolean sorted = true;
			for(int i = 1; i<n&&sorted; i++) {
				if(a[i]<a[i-1]) {
					sorted = false;
				}
			}
			if(sorted) {
				pw.println("Yes");
				continue;
			}
			int c0 = 0, c1 = 0;
			for(int i = 0; i<n; i++) {
				if(b[i]==0) {
					c0++;
				}else {
					c1++;
				}
			}
			if(c0*c1!=0) {
				pw.println("Yes");
			}else {
				pw.println("No");
			}
		}
		pw.close();
		in.close();
	}
}
