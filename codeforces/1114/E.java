import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			EArithmeticProgression solver = new EArithmeticProgression();
			solver.solve(1, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class EArithmeticProgression {
		private final int iinf = 1_000_000_000;

		public EArithmeticProgression() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), mval;
			{
				int l = 0, r = iinf;
				while(l<r) {
					int mid = (l+r)/2;
					pw.println("> "+mid);
					pw.flush();
					if(in.nextInt()==1) {
						l = mid+1;
					}else {
						r = mid;
					}
				}
				mval = l;
			}
			HashSet<Integer> valid = new HashSet<>(n);
			for(int i = 1; i<=n; i++) {
				pw.println("? "+i);
				pw.flush();
				int x = in.nextInt();
				if(x!=mval) {
					x = mval-x;
					for(int j = 1; j<n; j++) {
						if(x%j==0) {
							valid.add(x/j);
						}
					}
					break;
				}
			}
			ArrayList<Integer> left = new ArrayList<>();
			for(int i = 1; i<=n; i++) {
				left.add(i);
			}
			Random rand = new Random();
			while(valid.size()>1) {
				int q = rand.nextInt(left.size());
				pw.println("? "+left.get(q));
				pw.flush();
				left.remove(q);
				HashSet<Integer> next = new HashSet<>(n);
				int x = in.nextInt();
				if(x!=mval) {
					x = mval-x;
					for(int j = 1; j<n; j++) {
						if(x%j==0&&valid.contains(x/j)) {
							next.add(x/j);
						}
					}
					valid = next;
				}
			}
			int d = valid.iterator().next();
			pw.println("!", mval-(n-1)*d, d);
			pw.flush();
		}

	}

	static interface InputReader {
		int nextInt();

	}

	static class Output implements Closeable, Flushable {
		public StringBuilder sb;
		public OutputStream os;
		public int BUFFER_SIZE;
		public String lineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			lineSeparator = System.lineSeparator();
		}

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
			println();
		}

		public void println(String s) {
			sb.append(s);
			println();
		}

		public void println() {
			sb.append(lineSeparator);
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

	static class Input implements InputReader {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = new StringTokenizer("");
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
}

