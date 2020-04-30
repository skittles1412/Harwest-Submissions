//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_118A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_118A program = new Problem_118A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		String s = br.readLine().toLowerCase();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<s.length(); i++) {
			char c = s.charAt(i);
			if(c=='a'||c=='o'||c=='y'||c=='e'||c=='u'||c=='i') {
				continue;
			}
			sb.append('.');
			sb.append(c);
		}
		pw.println(sb.toString());
		pw.close();
		br.close();
	}
}
