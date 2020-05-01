//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_263A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_263A program = new Problem_263A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int x = 0;
		int y = 0;
		for(int i = 0; i<5; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			for(int j = 0; j<5; j++) {
				if(st.nextToken().charAt(0)=='1') {
					x = i;
					y = j;
				}
			}
		}
		pw.println(Math.abs(2-x)+Math.abs(2-y));
		pw.close();
		br.close();
	}
}
