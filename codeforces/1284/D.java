import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.TreeMap;

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
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			DNewYearAndConference solver = new DNewYearAndConference();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", Runtime.getRuntime().freeMemory() >> 1L);
		thread.start();
		thread.join();
	}
	static class DNewYearAndConference {
		int n;

		public DNewYearAndConference() {
		}

		public boolean valid(int[][] a, int[][] b) {
			int[][] events = new int[n<<1][2];
			for(int i = 0, ind = 0; i<n; i++) {
				events[ind++] = new int[] {a[i][0], i+1};
				events[ind++] = new int[] {a[i][1], -i-1};
			}
			Arrays.sort(events, (o1, o2) -> {
				if(o1[0]==o2[0]) {
					return o2[1]-o1[1];
				}
				return o1[0]-o2[0];
			});
			TreeMap<DNewYearAndConference.Segment, Integer> start = new TreeMap<>((o1, o2) -> {
				if(o1.l==o2.l) {
					return o1.r-o2.r;
				}
				return o1.l-o2.l;
			}), end = new TreeMap<>((o1, o2) -> {
				if(o1.r==o2.r) {
					return o1.l-o2.l;
				}
				return o1.r-o2.r;
			});
			for(int[] event: events) {
				int ind = Math.abs(event[1])-1;
				DNewYearAndConference.Segment s = new DNewYearAndConference.Segment(b[ind]);
				if(event[1]<0) {
					int count = start.get(s);
					if(count>1) {
						start.put(s, count-1);
						end.put(s, count-1);
					}else {
						start.remove(s);
						end.remove(s);
					}
				}else {
					if(!start.isEmpty()&&(end.firstKey().r<s.l||start.lastKey().l>s.r)) {
						return false;
					}
					start.put(s, start.getOrDefault(s, 0)+1);
					end.put(s, end.getOrDefault(s, 0)+1);
				}
			}
			return true;
		}

		public void solve(int kase, InputReader in, Output pw) {
			n = in.nextInt();
			int[][] a = new int[n][2], b = new int[n][2];
			for(int i = 0; i<n; i++) {
				a[i] = new int[] {in.nextInt(), in.nextInt()};
				b[i] = new int[] {in.nextInt(), in.nextInt()};
			}
			if(valid(a, b)&&valid(b, a)) {
				pw.println("YES");
			}else {
				pw.println("NO");
			}
		}

		static class Segment {
			int l;
			int r;

			public Segment(int[] arr) {
				this.l = arr[0];
				this.r = arr[1];
			}

			public String toString() {
				return String.format("Segment{%d %d}", l, r);
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

	static interface InputReader {
		int nextInt();

	}

	static class FastReader implements InputReader {
		final private int BUFFER_SIZE = 1<<16;
		private DataInputStream din;
		private byte[] buffer;
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
}


