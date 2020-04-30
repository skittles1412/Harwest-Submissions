//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_231A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_231A program = new Problem_231A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(br.readLine());
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int ans = 0;
		while(n-->0) {
			int sum = 0;
			StringTokenizer st = new StringTokenizer(br.readLine());
			for(int i = 0; i<3; i++) {
				sum+=Integer.parseInt(st.nextToken());
			}
			if(sum>=2) {
				ans++;
			}
		}
		pw.println(ans);
		pw.close();
		br.close();
	}
}
