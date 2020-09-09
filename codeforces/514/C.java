import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.InputMismatchException;
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
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			CWattoAndMechanism solver = new CWattoAndMechanism();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}
	static class CWattoAndMechanism {
		private final long[] somePrimes = new long[] {100888889L, 1000099999L, 10123457689L, 111161191111L,
				1000000999999L, 10011111122113L, 100110101011001L, 1011001110001111L, 13666666666666613L, 111101234567891111L};
		private final long hashMod = somePrimes[new Random().nextInt(10)];

		public CWattoAndMechanism() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int x = in.nextInt(), m = in.nextInt();
			HashSet<Long> hs = new HashSet<>(x<<2);
			for(int i = 0; i<x; i++) {
				hs.add(Utilities.hash(in.next(), 29, hashMod, 'a'));
			}
			loop:
			for(int i = 0; i<m; i++) {
				String str = in.next();
				int n = str.length();
				int[] cur = new int[n];
				for(int j = 0; j<n; j++) {
					cur[j] = str.charAt(j)-'a'+1;
				}
				long[] s = new long[n+1], e = new long[n+1], pow = new long[n+1];
				pow[0] = 1;
				for(int j = 1; j<=n; j++) {
					pow[j] = (pow[j-1]*29)%hashMod;
				}
				for(int j = 1; j<=n; j++) {
					s[j] = (s[j-1]+cur[j-1]*pow[j-1])%hashMod;
				}
				for(int j = n-1; j>=0; j--) {
					e[j] = (e[j+1]+cur[j]*pow[j])%hashMod;
				}
				for(int j = 0; j<n; j++) {
					for(int k = 1; k<=3; k++) {
						if(cur[j]!=k&&hs.contains((s[j]+e[j+1]+k*pow[j])%hashMod)) {
							pw.println("YES");
							continue loop;
						}
					}
				}
				pw.println("NO");
			}
		}

	}

	static interface InputReader {
		String next();

		int nextInt();

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

	static class Input implements InputReader {
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

	static class Utilities {
		public static long hash(String s, long prime, long mod, int offset) {
			long ret = 0, pow = 1;
			for(int i = 0; i<s.length(); i++) {
				ret = (ret+((s.charAt(i)-offset+1)*pow))%mod;
				pow = (pow*prime)%mod;
			}
			return ret;
		}

	}
}


