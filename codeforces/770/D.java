import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Closeable;
import java.io.BufferedReader;
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
			DDrawBrackets solver = new DDrawBrackets();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DDrawBrackets {
		public DDrawBrackets() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt();
			String s = in.next();
			ArrayList<DDrawBrackets.bracket> brackets = new ArrayList<>();
			int csum = 0, maxh = 0;
			for(int i = 0; i<n; i++) {
				if(s.charAt(i)=='[') {
					csum++;
				}else {
					csum--;
				}
				maxh = Math.max(maxh, csum);
			}
			int level = maxh;
			boolean prevClosed = true;
			for(int i = 0; i<n; i++) {
				if(s.charAt(i)=='[') {
					if(prevClosed) {
						brackets.add(new DDrawBrackets.bracket(level, true));
					}else {
						brackets.add(new DDrawBrackets.bracket(--level, true));
					}
					prevClosed = false;
				}else {
					if(prevClosed) {
						brackets.add(new DDrawBrackets.bracket(++level, false));
					}else {
						brackets.add(new DDrawBrackets.bracket(level, false));
					}
					prevClosed = true;
				}
			}
			Utilities.Debug.dbg(brackets);
			StringBuilder[] ans = new StringBuilder[maxh+1];
			for(int i = 0; i<=maxh; i++) {
				ans[i] = new StringBuilder();
			}
			prevClosed = true;
			int plevel = maxh;
			for(DDrawBrackets.bracket b: brackets) {
				if(b.opened) {
					int start = maxh-b.height;
					if(Math.abs(b.height-plevel)==1) {
						start--;
						ans[start].append('-');
						ans[start+1].append('+');
						for(int i = start+2; i<=maxh; i++) {
							ans[i].append('|');
						}
					}else {
						ans[start].append('+');
						for(int i = start+1; i<=maxh; i++) {
							ans[i].append('|');
						}
					}
					for(int i = 0; i<start; i++) {
						ans[i].append(' ');
					}
				}else {
					int start = maxh-b.height;
					if(Math.abs(b.height-plevel)==1) {
						ans[start].setCharAt(ans[start].length()-1, '-');
						ans[start].append('+');
						for(int i = start+1; i<=maxh; i++) {
							ans[i].append('|');
						}
					}else {
						ans[start].append('-');
						for(int i = start+1; i<=maxh; i++) {
							ans[i].append(' ');
						}
						for(int i = start; i<=maxh; i++) {
							ans[i].append(' ');
						}
						ans[start].append('-');
						for(int i = start+1; i<=maxh; i++) {
							ans[i].append(' ');
						}
						ans[start].append('+');
						for(int i = start+1; i<=maxh; i++) {
							ans[i].append('|');
						}
						for(int i = 0; i<start; i++) {
							ans[i].append("   ");
						}
					}
					for(int i = 0; i<start; i++) {
						ans[i].append(' ');
					}
				}
				prevClosed = !b.opened;
				plevel = b.height;
			}
			for(int i = 0; i<=maxh; i++) {
				pw.println(ans[i]);
			}
			for(int i = maxh-1; i>=0; i--) {
				pw.println(ans[i]);
			}
		}

		static class bracket {
			int height;
			boolean opened;

			public bracket(int height, boolean opened) {
				this.height = height;
				this.opened = opened;
			}

			public String toString() {
				return "bracket{"+
						"height="+height+
						", opened="+opened+
						'}';
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

		public void print(String s) {
			sb.append(s);
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
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
}


