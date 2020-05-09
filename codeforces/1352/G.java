//package round_640;

import java.util.*;
import java.io.*;

public class Problem_G {
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
	public ArrayList<Integer> al;
	public int n;
	public void find(BitSet mask, ArrayList<Integer> prev) {
		if(possible) {
			return;
		}
		if(prev.size()==n) {
			al = (ArrayList<Integer>) prev.clone();
			possible = true;
			return;
		}
		if(prev.size()==0) {
			prev.add(0);
			for(int i = 0; i<n; i++) {
				mask.set(i);
				prev.set(prev.size()-1, i);
				find((BitSet) mask.clone(), (ArrayList<Integer>) prev.clone());
				mask.clear(i);
			}
		}else {
			int cur = prev.get(prev.size()-1);
			prev.add(0);
			for(int i = cur-4; i<=cur+4; i++) {
				if(Math.abs(cur-i)<2||i<0||i>=n||mask.get(i)) {
					continue;
				}
				mask.set(i);
				prev.set(prev.size()-1, i);
				find((BitSet) mask.clone(), (ArrayList<Integer>) prev.clone());
				mask.clear(i);
			}
		}
	}
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_G program = new Problem_G();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = in.nextInt();
		while(kase-->0) {
			n = in.nextInt();
			possible = false;
			al = new ArrayList<Integer>();
			find(new BitSet(n),new ArrayList<Integer>());
			if(!possible) {
				pw.println(-1);
			}else {
				for(int i = 0; i<al.size(); i++) {
					if(i!=0) {
						pw.print(" ");
					}
					pw.print(al.get(i)+1);
				}
				pw.println();
			}
		}
		pw.close();
		in.close();
	}
}
