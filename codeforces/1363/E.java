//package round_646;

import java.util.*;
import java.io.*;

public class Problem_E {
	static class Input {
		BufferedReader br;
		StringTokenizer st;
		public Input(Reader r) throws IOException {
			br = new BufferedReader(r);
			st = new StringTokenizer(br.readLine());
		}
		public String nextLine() throws IOException {
			return br.readLine();
		}
		public boolean hasNext() throws IOException {
			try {
				while(!st.hasMoreTokens()) {
					String s = br.readLine();
					if(s==null) {
						return false;
					}
					st = new StringTokenizer(s);
				}
				return true;
			}catch (Exception e){
				return false;
			}
		}
		public String next() throws IOException {
			if(!hasNext()) {
				return null;
			}
			return st.nextToken();
		}
		public int nextInt() throws IOException {
			String s = next();
			if(s==null) {
				return Integer.MIN_VALUE;
			}
			return Integer.parseInt(s);
		}
		public double nextDouble() throws IOException {
			String s = next();
			if(s==null) {
				return Double.MIN_VALUE;
			}
			return Double.parseDouble(s);
		}
		public long nextLong() throws IOException {
			String s = next();
			if(s==null) {
				return Long.MIN_VALUE;
			}
			return Long.parseLong(s);
		}
		public int countTokens() {
			return st.countTokens();
		}
		public void close() throws IOException {
			br.close();
		}
	}
	class Node implements Comparable<Node>{
		public int cur, goal, rank, ind, mon, moff;
		public long cost;
		public ArrayList<Node> child;
		public Node parent;
		public Node(int a, int b, int c, int i) {
			cost = a;
			cur = b;
			goal = c;
			ind = i;
			parent = null;
			if(cur!=goal) {
				if(cur==1) {
					mon++;
				}else {
					moff++;
				}
			}
			child = new ArrayList<Node>();
		}
		public int compareTo(Node n) {
			return Integer.compare(rank, n.rank);
		}
		public void dfs(int r) {
			rank = r;
			visited[ind] = true;
			for(int i:al[ind]) {
				if(!visited[i]) {
					arr[i].parent = this;
					arr[i].cost = Math.min(cost, arr[i].cost);
					child.add(arr[i]);
					arr[i].dfs(rank+1);
					mon+=arr[i].mon;
					moff+=arr[i].moff;
				}
			}
		}
		public void update() {
			mon = moff = 0;
			if(cur!=goal) {
				if(cur==1) {
					mon++;
				}else {
					moff++;
				}
			}
			for(Node n:child) {
				mon+=n.mon;
				moff+=n.moff;
			}
		}
	}
	public Node[] arr;
	public ArrayList<Integer>[] al;
	public boolean[] visited;
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Problem_E program = new Problem_E();
		program.Begin();
	}
	void Begin() throws IOException{
		Input in = new Input(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(System.out));
		int n = in.nextInt();
		al = new ArrayList[n];
		arr = new Node[n];
		visited = new boolean[n];
		for(int i = 0; i<n; i++) {
			al[i] = new ArrayList<Integer>();
		}
		for(int i = 0; i<n; i++) {
			arr[i] = new Node(in.nextInt(),in.nextInt(),in.nextInt(),i);
		}
		for(int i = 0; i<n-1; i++) {
			int u = in.nextInt()-1;
			int v = in.nextInt()-1;
			al[u].add(v);
			al[v].add(u);
		}
		arr[0].dfs(1);
		if(arr[0].mon!=arr[0].moff) {
			pw.println(-1);
		}else {
			Arrays.sort(arr);
			long ans = 0;
			for(int i = n-1; i>=0; i--) {
				arr[i].update();
				int sub = Math.min(arr[i].mon,arr[i].moff);
				ans+=arr[i].cost*sub*2;
				arr[i].mon-=sub;
				arr[i].moff-=sub;
			}
			pw.println(ans);
		}
		pw.close();
		in.close();
	}
}
