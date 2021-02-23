import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;

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
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			ELISOfSequence solver = new ELISOfSequence();
			solver.solve(1, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<26);
		thread.start();
		thread.join();
	}

	static class ELISOfSequence {
		private final int iinf = 1_000_000_000;

		public ELISOfSequence() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[] arr = in.nextInt(n);
			ArrayList<Integer>[] here = new ArrayList[n];
			int[] min = new int[n];
			Arrays.fill(min, iinf);
			for(int i = 0; i<n; i++) {
				Utilities.Debug.dbg(min);
				int l = Utilities.lowerBound(min, arr[i]);
				assert l<n;
				if(here[l]==null) {
					here[l] = new ArrayList<>();
				}
				here[l].add(i);
				min[l] = Math.min(min[l], arr[i]);
			}
			Utilities.Debug.dbg(here);
			int i;
			for(i = n-1; here[i]==null; i--) ;
			ArrayList<Integer> valid = here[i];
			valid.sort(Comparator.comparingInt(o -> -o));
			char[] ans = new char[n];
			Arrays.fill(ans, '1');
			for(; i>=0; i--) {
				Utilities.Debug.dbg(i, valid);
				assert !valid.isEmpty();
				char set = '2';
				if(valid.size()==1) {
					set = '3';
				}
				for(int j: valid) {
					ans[j] = set;
				}
				Utilities.Debug.dbg(ans);
				if(i>0) {
					ArrayList<Integer> next = here[i-1], nvalid = new ArrayList<>();
					next.sort(Comparator.comparingInt(o -> -o));
					int k = 0, cmax = 0;
					for(int j: valid) {
						while(k<next.size()&&next.get(k)>j) {
							if(arr[next.get(k)]<cmax) {
								nvalid.add(next.get(k));
							}
							k++;
						}
						cmax = Math.max(cmax, arr[j]);
					}
					while(k<next.size()) {
						if(arr[next.get(k)]<cmax) {
							nvalid.add(next.get(k));
						}
						k++;
					}
					valid = nvalid;
				}
			}
			pw.println(ans);
		}

	}

	interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

	}

	static class Utilities {
		public static int lowerBound(int[] arr, int target) {
			int l = 0, h = arr.length;
			while(l<h) {
				int mid = l+h >> 1;
				if(arr[mid]<target) {
					l = mid+1;
				}else {
					h = mid;
				}
			}
			return l;
		}

		public static class Debug {
			public static boolean LOCAL = getLocal();

			public static boolean getLocal() {
				try {
					return System.getProperty("LOCAL")!=null;
				}catch(SecurityException e) {
					return false;
				}
			}

			public static <T> String ts(T t) {
				if(t==null) {
					return "null";
				}
				if(t instanceof Iterable) {
					return ts((Iterable<?>) t);
				}else if(t instanceof int[]) {
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
				}else if(t instanceof Object[]) {
					return ts((Object[]) t);
				}
				return t.toString();
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

	static class FastReader implements InputReader {
		final private int BUFFER_SIZE = 1<<16;
		private final DataInputStream din;
		private final byte[] buffer;
		private int bufferPointer;
		private int bytesRead;

		public FastReader(InputStream is) {
			din = new DataInputStream(is);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public int nextInt() {
			int ret = 0;
			byte c = skipToDigit();
			boolean neg = (c=='-');
			if(neg) {
				c = read();
			}
			do {
				ret = ret*10+c-'0';
			} while((c = read())>='0'&&c<='9');
			if(neg) {
				return -ret;
			}
			return ret;
		}

		private boolean isDigit(byte b) {
			return b>='0'&&b<='9';
		}

		private byte skipToDigit() {
			byte ret;
			while(!isDigit(ret = read())&&ret!='-') ;
			return ret;
		}

		private void fillBuffer() {
			try {
				bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			}catch(IOException e) {
				e.printStackTrace();
				throw new InputMismatchException();
			}
			if(bytesRead==-1) {
				buffer[0] = -1;
			}
		}

		private byte read() {
			if(bytesRead==-1) {
				throw new InputMismatchException();
			}else if(bufferPointer==bytesRead) {
				fillBuffer();
			}
			return buffer[bufferPointer++];
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

		public void println(char[] c) {
			println(String.valueOf(c));
		}

		public void println() {
			sb.append(lineSeparator);
		}

		public void println(String s) {
			sb.append(s);
			println();
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