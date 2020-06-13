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
			int[] arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			ArrayList<Integer> ans = new ArrayList<Integer>();
			ans.add(arr[0]);
			for(int i = 0; i<n-1;) {
				int start = arr[i], prev = arr[i];
				if(arr[i+1]>prev) {
					for(i+=1;i<n&&arr[i]>prev;prev=arr[i],i++);
					ans.add(prev);
				}else {
					for(i+=1;i<n&&arr[i]<prev;prev=arr[i],i++);
					ans.add(prev);
				}
				i--;
			}
			pw.println(ans.size());
			for(int i = 0; i<ans.size(); i++) {
				if(i!=0) {
					pw.print(" ");
				}
				pw.print(ans.get(i));
			}
			pw.println();
		}
		pw.close();
		in.close();
	}
}
