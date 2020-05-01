//package round_638;

import java.util.*;
import java.io.*;

public class Problem_A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_A program = new Problem_A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = Integer.parseInt(br.readLine());
		while(n-->0) {
			int cur = Integer.parseInt(br.readLine());
			int sum = (1<<cur+1)-2;
			int i;
			for(i = sum/2; i>=0; i--) {
				String s = Integer.toBinaryString(i);
				int count = 0;
				for(int j = 0; j<s.length(); j++) {
					if(s.charAt(j)=='1') {
						count++;
					}
				}
				if(count==cur/2) {
					break;
				}
			}
			pw.println(sum-2*i);
		}
		pw.close();
		br.close();
	}
}
