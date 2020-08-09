import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.util.Map;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.io.Closeable;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.util.SortedMap;
import java.io.InputStream;

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
			BApplejackAndStorages solver = new BApplejackAndStorages();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BApplejackAndStorages {
		public BApplejackAndStorages() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt();
			TreeMap<Integer, Integer> arr = new TreeMap<>(), conv = new TreeMap<>();
			for(int i = 0; i<n; i++) {
				int x = in.nextInt();
				arr.put(x, arr.getOrDefault(x, 0)+1);
			}
			Utilities.Debug.dbg(arr);
			for(int i: arr.values()) {
				conv.put(i, conv.getOrDefault(i, 0)+1);
			}
			int q = in.nextInt();
			for(int i = 0; i<q; i++) {
				boolean add = in.next().charAt(0)=='+';
				int val = in.nextInt();
				if(add) {
					int x = arr.getOrDefault(val, 0);
					arr.put(val, x+1);
					conv.put(x+1, conv.getOrDefault(x+1, 0)+1);
					if(conv.containsKey(x)) {
						int cnt = conv.get(x);
						if(cnt>1) {
							conv.put(x, cnt-1);
						}else {
							conv.remove(x);
						}
					}
				}else {
					int cnt = arr.get(val);
					if(cnt>1) {
						arr.put(val, cnt-1);
					}else {
						arr.remove(val);
					}
					conv.put(cnt-1, conv.getOrDefault(cnt-1, 0)+1);
					if(conv.containsKey(cnt)) {
						int x = conv.get(cnt);
						if(x>1) {
							conv.put(cnt, x-1);
						}else {
							conv.remove(cnt);
						}
					}
				}
				int sum = 0;
				boolean used = false;
				for(Map.Entry<Integer, Integer> e: conv.descendingMap().entrySet()) {
					int cur = e.getKey() >> 1;
					if(cur>=2&&!used) {
						used = true;
						sum += cur-2+cur*(e.getValue()-1);
					}else if(sum>=2) {
						break;
					}else {
						sum += cur*e.getValue();
					}
				}
				if(used&&sum>=2) {
					pw.println("YES");
				}else {
					pw.println("NO");
				}
				Utilities.Debug.dbg(sum, used, conv);
			}
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

	static class Utilities {
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



