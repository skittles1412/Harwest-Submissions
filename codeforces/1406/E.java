import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
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
			EDeletingNumbers solver = new EDeletingNumbers();
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

	static class EDeletingNumbers {
		public EDeletingNumbers() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), cnt = 0;
			ArrayList<Integer> primes = Utilities.sieve(Math.max(316, n));
			primes = new ArrayList<>(primes.subList(65, primes.size()));
			int block = 98, size = n, ans = 1;
			loop:
			for(int i = 0; i<primes.size(); i += 98) {
				int end = Math.min(primes.size(), i+98), cblock = 0;
				for(int j = i; j<end; j++) {
					cblock += n/primes.get(j);
					pw.println("B "+(primes.get(j)));
					cnt++;
				}
				pw.println("A 1");
				pw.flush();
				cnt++;
				for(int j = i; j<end; j++) {
					in.nextInt();
				}
				int response = in.nextInt();
				if(response!=size-cblock) {
					for(int j = i; j<end; j++) {
						pw.println("A "+(primes.get(j)));
						cnt++;
						pw.flush();
						response = in.nextInt();
						if(response==1) {
							ans = primes.get(j);
							break loop;
						}
					}
				}else {
					size -= cblock;
				}
			}
			primes = Utilities.sieve(316);
			for(int p: primes) {
				if(p>n) {
					break;
				}
				pw.println("B "+p);
				pw.println("A "+p);
				cnt += 2;
				pw.flush();
				in.nextInt();
				int response = in.nextInt();
				if(response==1) {
					long pow = p*p;
					for(int i = 2; i<=18&&pow<=n; i++) {
						pw.println("A "+pow);
						cnt++;
						pw.flush();
						response = in.nextInt();
						if(response==0) {
							break;
						}
						pow *= p;
					}
					pow /= p;
					ans *= pow;
				}
			}
			Utilities.Debug.dbg(cnt);
			pw.println("C "+ans);
			cnt++;
			pw.flush();
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

		public void println(String s) {
			sb.append(s);
			println();
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
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

	static interface InputReader {
		int nextInt();

	}

	static class Utilities {
		public static ArrayList<Integer> sieve(int n) {
			ArrayList<Integer> ans = new ArrayList<>();
			boolean[] prime = new boolean[n+1];
			Arrays.fill(prime, true);
			for(int i = 2; i<=n; i++) {
				if(prime[i]) {
					ans.add(i);
				}
				if((long) i*i>n) {
					continue;
				}
				for(int j = i*i; j<=n; j += i) {
					prime[j] = false;
				}
			}
			return ans;
		}

		public static class Debug {
			public static final boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

			private static <T> String ts(T t) {
				if(t==null) {
					return "null";
				}
				try {
					return ts((Iterable) t);
				}catch(ClassCastException e) {
					if(t instanceof int[]) {
						String s = Arrays.toString((int[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof long[]) {
						String s = Arrays.toString((long[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof char[]) {
						String s = Arrays.toString((char[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof double[]) {
						String s = Arrays.toString((double[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}else if(t instanceof boolean[]) {
						String s = Arrays.toString((boolean[]) t);
						return "{"+s.substring(1, s.length()-1)+"}";
					}
					try {
						return ts((Object[]) t);
					}catch(ClassCastException e1) {
						return t.toString();
					}
				}
			}

			private static <T> String ts(T[] arr) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: arr) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			private static <T> String ts(Iterable<T> iter) {
				StringBuilder ret = new StringBuilder();
				ret.append("{");
				boolean first = true;
				for(T t: iter) {
					if(!first) {
						ret.append(", ");
					}
					first = false;
					ret.append(ts(t));
				}
				ret.append("}");
				return ret.toString();
			}

			public static void dbg(Object... o) {
				if(LOCAL) {
					System.err.print("Line #"+Thread.currentThread().getStackTrace()[2].getLineNumber()+": [");
					for(int i = 0; i<o.length; i++) {
						if(i!=0) {
							System.err.print(", ");
						}
						System.err.print(ts(o[i]));
					}
					System.err.println("]");
				}
			}

		}

	}
}

