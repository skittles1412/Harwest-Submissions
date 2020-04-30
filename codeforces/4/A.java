//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_4A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_4A program = new Problem_4A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = Integer.parseInt(br.readLine());
		pw.println((n%2==0&&n>2)?"YES":"NO");
		pw.close();
		br.close();
	}
}
