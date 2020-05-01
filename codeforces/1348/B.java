//package round_638;

import java.util.*;
import java.io.*;

public class Problem_B {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_B program = new Problem_B();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = Integer.parseInt(br.readLine());
		while(kase-->0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			ArrayList<Integer> al = new ArrayList<Integer>();
			HashSet<Integer> hs = new HashSet<Integer>();
			st = new StringTokenizer(br.readLine());
			while(n-->0) {
				int cur = Integer.parseInt(st.nextToken());
				al.add(cur);
				hs.add(cur);
			}
			if(hs.size()>k) {
				pw.println(-1);
				continue;
			}
			al.addAll(0, hs);
			for(int i = k; i<al.size(); i++) {
				if(al.get(i)!=al.get(i-k)) {
					al.add(i, al.get(i-k));
				}
			}
			pw.println(al.size());
			for(int i = 0; i<al.size(); i++) {
				if(i!=0) {
					pw.print(" ");
				}
				pw.print(al.get(i));
			}
			pw.println();
		}
		pw.close();
		br.close();
	}
}
