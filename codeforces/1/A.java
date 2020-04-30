//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_1A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_1A program = new Problem_1A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		long n = Long.parseLong(st.nextToken());
		long m = Long.parseLong(st.nextToken());
		long a = Long.parseLong(st.nextToken());
		long l = n/a;
		long w = m/a;
		if(n%a!=0) {
			l++;
		}
		if(m%a!=0) {
			w++;
		}
		pw.println(l*w);
		pw.close();
		br.close();
	}
}
