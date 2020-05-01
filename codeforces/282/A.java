//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_282A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_282A program = new Problem_282A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int ans = 0;
		int n = Integer.parseInt(br.readLine());
		while(n-->0) {
			char c = br.readLine().charAt(1);
			if(c=='+') {
				ans++;
			}else {
				ans--;
			}
		}
		pw.println(ans);
		pw.close();
		br.close();
	}
}
