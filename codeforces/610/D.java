import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

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
			DVikaAndSegments solver = new DVikaAndSegments();
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

	static class DVikaAndSegments {
		private final int iinf = 1_000_000_000;

		public DVikaAndSegments() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int hind = 0, vind = 0;
			int[][] hori = new int[n][4], vert = new int[n][4];
			{
				for(int i = 0; i<n; i++) {
					int[] cur = in.nextInt(4);
					if(cur[1]==cur[3]) {
						hori[hind++] = cur;
					}else {
						vert[vind++] = cur;
					}
				}
			}
			long ans = 0;
			ArrayList<DVikaAndSegments.Event> events = new ArrayList<>();
			{
				Arrays.sort(hori, 0, hind, Comparator.comparingInt(o -> o[1]));
				for(int i = 0; i<hind; ) {
					int y = hori[i][1];
					ArrayList<DVikaAndSegments.Event> cur = new ArrayList<>();
					for(; i<hind&&hori[i][1]==y; i++) {
						cur.add(new DVikaAndSegments.Event(Math.min(hori[i][0], hori[i][2]), 0, true));
						cur.add(new DVikaAndSegments.Event(Math.max(hori[i][0], hori[i][2])+1, 0, false));
					}
					cur.sort(null);
					int start = -1, cnt = 0;
					for(int j = 0; j<cur.size(); ) {
						int t = cur.get(j).time;
						while(j<cur.size()&&cur.get(j).time==t) {
							cnt += cur.get(j++).add();
						}
						if(cnt>0&&start==-1) {
							start = t;
						}else if(cnt==0) {
							events.add(new DVikaAndSegments.Event(start, y+iinf, true));
							events.add(new DVikaAndSegments.Event(t, y+iinf, false));
							ans += t-start;
							start = -1;
						}
					}
				}
			}
			{
				events.sort(null);
				Arrays.sort(vert, 0, vind, Comparator.comparingInt(o -> o[0]));
				TDynamicSegmentTree<Integer> dst = new TDynamicSegmentTree<>((int) 2e9, 0, Integer::sum);
				int k = 0;
				for(int i = 0; i<vind; ) {
					int x = vert[i][0];
					while(k<events.size()&&events.get(k).time<=x) {
						int ind = events.get(k).getInd();
						dst.set(ind, dst.query(ind, ind)+events.get(k++).add());
					}
					ArrayList<DVikaAndSegments.Event> cur = new ArrayList<>();
					for(; i<vind&&vert[i][0]==x; i++) {
						cur.add(new DVikaAndSegments.Event(Math.min(vert[i][1], vert[i][3]), 0, true));
						cur.add(new DVikaAndSegments.Event(Math.max(vert[i][1], vert[i][3])+1, 0, false));
					}
					cur.sort(null);
					int start = -1, cnt = 0;
					for(int j = 0; j<cur.size(); ) {
						int t = cur.get(j).time;
						while(j<cur.size()&&cur.get(j).time==t) {
							cnt += cur.get(j++).add();
						}
						if(cnt>0&&start==-1) {
							start = t;
						}else if(cnt==0) {
							ans += t-start;
							ans -= dst.query(start+iinf, t+iinf-1);
							start = -1;
						}
					}
				}
			}
			pw.println(ans);
		}

		static class Event implements Comparable<DVikaAndSegments.Event> {
			int time;
			int ind;

			public Event(int time, int ind, boolean add) {
				this.time = time;
				this.ind = ind+1;
				if(!add) {
					this.ind = -this.ind;
				}
			}

			public int add() {
				return ind>=0 ? 1 : -1;
			}

			public int getInd() {
				return Math.abs(ind)-1;
			}

			public int compareTo(DVikaAndSegments.Event o) {
				return time-o.time;
			}

		}

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

		public void println(long l) {
			println(String.valueOf(l));
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

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
		}

	}

	static class TDynamicSegmentTree<T> {
		public Node root;
		public T initialValue;
		public BinaryOperator<T> operation;

		public TDynamicSegmentTree(int n, T initialValue, BinaryOperator<T> operation) {
			root = new Node(0, n, initialValue);
			this.initialValue = initialValue;
			this.operation = operation;
		}

		public void set(int ind, T val) {
			root.set(ind, val);
		}

		public T query(int l, int r) {
			return root.query(l, r);
		}

		class Node {
			public int l;
			public int r;
			public int mid;
			public T val;
			Node lc;
			Node rc;

			public Node(int l, int r, T val) {
				this.l = l;
				this.r = r;
				this.mid = l+(r-l)/2;
				this.val = val;
				lc = null;
				rc = null;
			}

			public void set(int ind, T val) {
				if(l==r) {
					this.val = val;
					return;
				}
				if(ind<=mid) {
					if(lc==null) {
						lc = new Node(l, mid, this.val);
					}
					lc.set(ind, val);
				}else {
					if(rc==null) {
						rc = new Node(mid+1, r, this.val);
					}
					rc.set(ind, val);
				}
				if(lc==null) {
					this.val = rc.val;
				}else if(rc==null) {
					this.val = lc.val;
				}else {
					this.val = operation.apply(lc.val, rc.val);
				}
			}

			public T query(int ql, int qr) {
				if(ql<=l&&qr>=r) {
					return val;
				}
				T ret = null;
				if(ql<=mid&&lc!=null) {
					ret = lc.query(ql, qr);
				}
				if(qr>mid&&rc!=null) {
					if(ret==null) {
						ret = rc.query(ql, qr);
					}else {
						ret = operation.apply(ret, rc.query(ql, qr));
					}
				}
				return ret==null ? initialValue : ret;
			}

		}

	}
}

