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
			int n = in.nextInt(), x = in.nextInt();
			int[] arr = new int[n];
			int count = 0;
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt()%x;
				if(arr[i]==0) {
					count++;
				}
			}
			if(count==n) {
				pw.println(-1);
			}else {
				int sum = 0;
				for(int i = 0; i<n; i++) {
					sum+=arr[i];
				}
				if(sum%x!=0) {
					pw.println(n);
				}else {
					sum = 0;
					int i,j,jc=0;
					for(i = 0;i<n&&arr[i]%x==0;i++);
					for(j = n-1;j<n&&arr[j]%x==0;j--,jc++);
					pw.println(n-Math.min(i+1, jc+1));
				}
			}
		}
		pw.close();
		in.close();
	}
}
