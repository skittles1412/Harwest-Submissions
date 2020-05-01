//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_791A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_791A program = new Problem_791A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int a = Integer.parseInt(st.nextToken());
		int b = Integer.parseInt(st.nextToken());
		int ans = 0;
		while(a<=b) {
			a*=3;
			b*=2;
			ans++;
		}
		pw.println(ans); 
		pw.close();
		br.close();
	}
}
