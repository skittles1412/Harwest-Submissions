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
		Problem_C program = new Problem_C();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = in.nextInt();
		TreeSet<Integer> next = new TreeSet<Integer>();
		TreeSet<Integer> mex = new TreeSet<Integer>();
		int[] arr = new int[n];
		int[] ans = new int[n];
		next.add(0);
		for(int i = 0; i<n; i++) {
			arr[i] = in.nextInt();
			next.add(i+1);
			next.remove(arr[i]);
			mex.add(i);
		}
		mex.add(n);
		for(int i = 0; i<n; i++) {
			if(mex.higher(mex.first())==arr[i]) {
				ans[i] = mex.first();
				mex.remove(ans[i]);
			}else if(mex.first()==arr[i]){
				ans[i] = next.ceiling(arr[i]);
				next.remove(ans[i]);
				mex.remove(ans[i]);
			}else {
				pw.println(-1);
				pw.close();
				System.exit(0);
			}
		}
		for(int i = 0; i<n; i++) {
			if(i!=0) {
				pw.print(" ");
			}
			pw.print(ans[i]);
		}
		pw.println();
		pw.close();
		in.close();
	}
}
