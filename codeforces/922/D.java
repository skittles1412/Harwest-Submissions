import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;
import java.io.Closeable;
import java.io.BufferedReader;
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
			DRobotVacuumCleaner solver = new DRobotVacuumCleaner();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DRobotVacuumCleaner {
		public DRobotVacuumCleaner() {
		}

		public void solve(int kase, Input in, Output pw) {
			int n = in.nextInt();
			DRobotVacuumCleaner.Noise[] arr = new DRobotVacuumCleaner.Noise[n];
			for(int i = 0; i<n; i++) {
				arr[i] = new DRobotVacuumCleaner.Noise(in.next());
			}
			Arrays.sort(arr);
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i<arr.length; i++) {
				builder.append(arr[i].str);
			}
			int[] tmp = new int[builder.length()];
			for(int i = 0; i<builder.length(); i++) {
				tmp[i] = builder.charAt(i)=='h' ? 0 : 1;
			}
			pw.println(Utilities.countInversions(tmp));
		}

		static class Noise implements Comparable<DRobotVacuumCleaner.Noise> {
			String str;
			int s;
			int h;

			public Noise(String str) {
				this.str = str;
				s = 0;
				h = 0;
				for(int i = 0; i<str.length(); i++) {
					if(str.charAt(i)=='s') {
						s++;
					}else {
						h++;
					}
				}
			}

			public int compareTo(DRobotVacuumCleaner.Noise n) {
				return Long.compare((long) n.s*h, (long) s*n.h);
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
		public static long countInversions(int[] arr) {
			return countInversions(arr.clone(), 0, arr.length-1);
		}

		private static long countInversions(int[] arr, int l, int r) {
			if(l>=r) {
				return 0;
			}
			int mid = l+r >> 1, larr[] = new int[mid-l+2], rarr[] = new int[r-mid+1];
			long ans = countInversions(arr, l, mid)+countInversions(arr, mid+1, r);
			for(int i = l; i<=mid; i++) {
				larr[i-l] = arr[i];
			}
			for(int i = mid+1; i<=r; i++) {
				rarr[i-mid-1] = arr[i];
			}
			larr[mid-l+1] = rarr[r-mid] = Integer.MAX_VALUE;
			int i = 0, j = 0, ind = l;
			while(ind<=r) {
				if(larr[i]<=rarr[j]) {
					arr[ind++] = larr[i++];
					ans += j;
				}else {
					arr[ind++] = rarr[j++];
				}
			}
			return ans;
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
}

