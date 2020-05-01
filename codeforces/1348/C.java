//package round_638;

import java.util.*;
import java.io.*;

public class Problem_C {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_C program = new Problem_C();
		program.Begin();
	}
	void Begin() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int kase = Integer.parseInt(br.readLine());
		while(kase-->0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			char[] arr = br.readLine().toCharArray();
			Arrays.sort(arr);
			if(n==k) {
				pw.println(arr[k-1]);
				continue;
			}
			String[] srr = new String[k];
			Arrays.fill(srr, "");
			boolean same = true;
			char first = arr[0];
			for(int i = 0; i<k; i++) {
				srr[i]+=arr[i];
				if(first!=arr[i]) {
					same = false;
				}
			}
			if(!same) {
				pw.println(srr[k-1]);
			}else {
				same = true;
				first = arr[k];
				for(int i = k; i<n; i++) {
					if(first!=arr[i]) {
						same = false;
					}
				}
				if(same) {
					for(int i = k; i<n; i++) {
						srr[i%k]+=arr[i];
					}
					pw.println(srr[0]);
				}else {
					for(int i = k; i<n; i++) {
						srr[0]+=arr[i];
					}
					pw.println(srr[0]);
				}
			}
		}
		pw.close();
		br.close();
	}
}
