import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
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
			BSmallestNumber solver = new BSmallestNumber();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class BSmallestNumber {
		public BSmallestNumber() {
		}

		public long oper(long a, long b, char c) {
			if(c=='+') {
				return a+b;
			}else if(c=='-') {
				return a-b;
			}
			return a*b;
		}

		public void solve(int kase, Input in, Output pw) {
			long[] arr = new long[] {in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()};
			Utilities.sort(arr);
			long ans = Long.MAX_VALUE;
			char[] oper = new char[] {in.next().charAt(0), in.next().charAt(0), in.next().charAt(0)};
			do {
				long val1 = oper(oper(oper(arr[0], arr[1], oper[0]), arr[2], oper[1]), arr[3], oper[2]);
				long val2 = oper(oper(arr[0], arr[1], oper[0]), oper(arr[2], arr[3], oper[1]), oper[2]);
				ans = Math.min(ans, Math.min(val1, val2));
			}while(Utilities.nextPermutation(arr));
			pw.println(ans);
		}

	}

	static class Utilities {
		public static void sort(long[] arr) {
			ArrayList<Long> al = new ArrayList<>();
			for(long i: arr) {
				al.add(i);
			}
			Collections.sort(al);
			for(int i = 0; i<al.size(); i++) {
				arr[i] = al.get(i);
			}
		}

		public static void swap(long[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

		public static void reverse(long[] arr, int i, int j) {
			while(i<j) {
				long cur = arr[i];
				arr[i++] = arr[j];
				arr[j--] = cur;
			}
		}

		public static boolean nextPermutation(long[] data) {
			if(data.length<=1) {
				return false;
			}
			int last = data.length-2;
			while(last >= 0) {
				if(data[last]<data[last+1]) {
					break;
				}
				last--;
			}
			if(last<0)
				return false;
			int nextGreater = data.length-1;
			for(int i = data.length-1; i>last; i--) {
				if(data[i]>data[last]) {
					nextGreater = i;
					break;
				}
			}
			swap(data, nextGreater, last);
			reverse(data, last+1, data.length-1);
			return true;
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

		public void println(long l) {
			println(String.valueOf(l));
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
}

