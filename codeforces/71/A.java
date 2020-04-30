//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_71A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_71A program = new Problem_71A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = Integer.parseInt(br.readLine());
		while(n-->0) {
			String s = br.readLine();
			if(s.length()>10) {
				pw.printf("%c%d%c\n",s.charAt(0),s.length()-2,s.charAt(s.length()-1));
			}else {
				pw.println(s);
			}
		}
		pw.close();
		br.close();
	}
}
