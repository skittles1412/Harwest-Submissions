import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.Objects;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.BufferedReader;
import java.util.Collections;
import java.io.InputStream;
import java.io.Flushable;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			CInteractiveBullsAndCowsHard solver = new CInteractiveBullsAndCowsHard();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class CInteractiveBullsAndCowsHard {
		private final int INF = 1_000_000_000;
		ArrayList<String> all;
		ArrayList<String> possible;

		public CInteractiveBullsAndCowsHard() {
		}

		public String tryAll() {
			if(possible.size()==5040) {
				Random rand = new Random();
				int x = rand.nextInt(10), a = rand.nextInt(10), b = rand.nextInt(10);
				while(a==x) {
					a = rand.nextInt(10);
				}
				while(b==a||b==x) {
					b = rand.nextInt(10);
				}
				return String.valueOf(a)+x+b+x;
			}else if(possible.size()==1) {
				return possible.get(0);
			}
			int best = INF;
			ArrayList<String> ans = new ArrayList<>();
			for(String s: all) {
				boolean[] has = new boolean[10];
				for(int i = 0; i<4; i++) {
					has[s.charAt(i)-'0'] = true;
				}
				int cur = 0;
				int[][] cnt = new int[5][5];
				for(String left: possible) {
					int a = 0, b = 0;
					for(int i = 0; i<4; i++) {
						if(left.charAt(i)==s.charAt(i)) {
							a++;
						}else if(has[left.charAt(i)-'0']) {
							b++;
						}
					}
					cnt[a][b]++;
				}
				for(int i = 0; i<=4; i++) {
					for(int j = 0; j<=4; j++) {
						cur = Math.max(cur, cnt[i][j]);
					}
				}
				cur += cnt[4][0];
				if(cur<best) {
					best = cur;
					ans = new ArrayList<>(Collections.singletonList(s));
				}else if(cur==best) {
					ans.add(s);
				}
			}
			return ans.get(new Random().nextInt(ans.size()));
		}

		public void solve(int testNumber, Input in, Output pw) {
			all = new ArrayList<>();
			possible = new ArrayList<>();
			for(int i = 0; i<10; i++) {
				for(int j = 0; j<10; j++) {
					if(i!=j) {
						for(int k = 0; k<10; k++) {
							if(k!=i&&k!=j) {
								for(int l = 0; l<10; l++) {
									if(l!=i&&l!=j&&l!=k) {
										possible.add(String.valueOf(i)+j+k+l);
									}
								}
							}
						}
					}
				}
			}
			for(int i = 0; i<10000; i++) {
				String s = String.valueOf(i);
				while(s.length()<4) {
					s = "0"+s;
				}
				all.add(s);
			}
			while(true) {
				String guess = tryAll();
				pw.println(guess);
				pw.flush();
				Pair<Integer, Integer> response = new Pair<>(in.nextInt(), in.nextInt());
				if(response.equals(new Pair(4, 0))) {
					return;
				}
				ArrayList<String> left = new ArrayList<>();
				boolean[] has = new boolean[10];
				for(int i = 0; i<4; i++) {
					has[guess.charAt(i)-'0'] = true;
				}
				for(String s: possible) {
					int a = 0, b = 0;
					for(int i = 0; i<4; i++) {
						if(s.charAt(i)==guess.charAt(i)) {
							a++;
						}else if(has[s.charAt(i)-'0']) {
							b++;
						}
					}
					if(response.a==a&&response.b==b) {
						left.add(s);
					}
				}
				possible = left;
			}
		}

	}

	static class Pair<T1, T2> {
		public T1 a;
		public T2 b;

		public Pair(T1 a, T2 b) {
			this.a = a;
			this.b = b;
		}

		public String toString() {
			return a+" "+b;
		}

		public int hashCode() {
			return Objects.hash(a, b);
		}

		public boolean equals(Object o) {
			if(o instanceof Pair) {
				Pair p = (Pair) o;
				return a==p.a&&b==p.b;
			}
			return false;
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = null;
		}

		public boolean hasNext() {
			try {
				while(st==null||!st.hasMoreTokens()) {
					String s = br.readLine();
					if(s==null) {
						return false;
					}
					st = new StringTokenizer(s);
				}
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		public String next() {
			if(!hasNext()) {
				throw new InputMismatchException();
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public boolean autoFlush;
		public String LineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			autoFlush = false;
			LineSeparator = System.lineSeparator();
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println() {
			sb.append(LineSeparator);
		}

		private void flushToBuffer() {
			try {
				os.write(sb.toString().getBytes());
			}catch(IOException e) {
				e.printStackTrace();
			}
			sb = new StringBuilder(BUFFER_SIZE);
		}

		public void flush() {
			try {
				flushToBuffer();
				os.flush();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public void close() {
			flush();
			try {
				os.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

	}
}

