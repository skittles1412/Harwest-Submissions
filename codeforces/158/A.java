//package past_problems;

import java.util.*;
import java.io.*;

public class Problem_158A {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_158A program = new Problem_158A();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = Integer.parseInt(st.nextToken());
		int k = Integer.parseInt(st.nextToken());
		int ans = 0;
		int[] arr = new int[n];
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i<n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		k = arr[k-1];
		for(int i = 0; i<n; i++) {
			if(arr[i]>=k&&arr[i]>0) {
				ans++;
			}
		}
		pw.println(ans);
		pw.close();
		br.close();
	}
}
