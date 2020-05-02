//package round_638;

import java.util.*;
import java.io.*;

public class Problem_D {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_D program = new Problem_D();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = Integer.parseInt(br.readLine());
		while(kase-->0) {
			int n = Integer.parseInt(br.readLine());
			ArrayList<Integer> ans = new ArrayList<Integer>();
			ans.add(1);
			int s = 1;
			while((s<<2)<=n) {
				ans.add(s<<=1);
			}
			int cur = n-((s<<1)-1);
			int ind = Collections.binarySearch(ans, cur);
			if(ind<0) {
				ind = -(ind+1);
			}
			ans.add(ind, cur);
			pw.println(ans.size()-1);
			for(int i = 0; i<ans.size()-1; i++) {
				if(i!=0) {
					pw.print(" ");
				}
				pw.print(ans.get(i+1)-ans.get(i));
			}
			pw.println();
		}
		pw.close();
		br.close();
	}
}
