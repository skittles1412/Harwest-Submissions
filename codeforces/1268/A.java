//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_1268A {
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
		public int countTokens() {
			return st.countTokens();
		}
		public void close() throws IOException {
			br.close();
		}
	}
	public int compare(char[] arr1, char[] arr2) {
		int i,j;
		i = j = 0;
		while(i<arr1.length&&j<arr2.length&&arr1[i]==arr2[j]) {
			i++;
			j++;
		}
		if(i==arr1.length&&j==arr2.length) {
			return 0;
		}else if(i==arr1.length) {
			return -1;
		}else if(j==arr1.length) {
			return 1;
		}
		return arr1[i]-arr2[j];
	}
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_1268A program = new Problem_1268A();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = in.nextInt();
		int k = in.nextInt();
		char[] old = in.next().toCharArray();
		char[] neww = new char[n];
		for(int i = 0; i<n; i++) {
			neww[i] = old[i%k];
		}
		if(compare(old,neww)<=0) {
			pw.println(neww.length);
			pw.println(String.valueOf(neww));
		}else {
			int add = 1;
			for(int i = k-1; add!=0; i--) {
				add = 0;
				if(neww[i]=='9') {
					neww[i] = '0';
					add = 1;
				}else {
					neww[i]++;
				}
			}
			for(int i = 0; i<n; i++) {
				neww[i] = neww[i%k];
			}
			pw.println(neww.length);
			pw.println(String.valueOf(neww));
		}
		pw.close();
		in.close();
	}
}
