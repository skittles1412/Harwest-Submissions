//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_50A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_50A program = new Problem_50A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		pw.println(Integer.parseInt(st.nextToken())*Integer.parseInt(st.nextToken())/2);
		pw.close();
		br.close();
	}
}
