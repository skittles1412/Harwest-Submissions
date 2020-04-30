import java.util.*;
import java.io.*;

public class Problem_404A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_404A program = new Problem_404A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = Integer.parseInt(br.readLine());
		int sum = 0;
		while(n-->0) {
			char c = br.readLine().charAt(0);
			if(c=='T') {
				sum+=4;
			}else if(c=='C') {
				sum+=6;
			}else if(c=='O') {
				sum+=8;
			}else if(c=='D') {
				sum+=12;
			}else {
				sum+=20;
			}
		}
		pw.println(sum);
		pw.close();
		br.close();
	}
}
