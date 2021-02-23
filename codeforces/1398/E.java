import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
			ETwoTypesOfSpells solver = new ETwoTypesOfSpells();
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

	static class ETwoTypesOfSpells {
		private static final int iinf = 1_000_000_000;

		public ETwoTypesOfSpells() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			ETwoTypesOfSpells.MTS all = new ETwoTypesOfSpells.MTS();
			MultiTreeSet<Integer> f = new MultiTreeSet<>(), l = new MultiTreeSet<>();
			for(int i = 0; i<n; i++) {
				if(in.nextInt()==0) {
					int x = in.nextInt();
					if(x>0) {
						f.add(x);
						all.add(x);
					}else {
						x = -x;
						f.delete(x);
						all.remove(x);
					}
				}else {
					int x = in.nextInt();
					if(x>0) {
						l.add(x);
						all.add(x);
						all.incr();
					}else {
						x = -x;
						l.remove(x);
						all.decr();
						all.remove(x);
					}
				}
				int ff = f.isEmpty() ? 0 : f.lastKey(), ll = l.isEmpty() ? 0 : l.firstKey();
				long ans = all.tsum+all.sum;
				if(ll>ff) {
					ans -= ll;
					ans += ff;
				}
				pw.println(ans);
			}
		}

		static class State implements Comparable<ETwoTypesOfSpells.State> {
			int ind;
			int x;

			public State(int ind, int x) {
				this.ind = ind;
				this.x = x;
			}

			public int compareTo(ETwoTypesOfSpells.State s) {
				if(x==s.x) {
					return ind-s.ind;
				}
				return x-s.x;
			}

		}

		static class MTS extends TreeSet<ETwoTypesOfSpells.State> {
			int ind = -2;
			long sum = 0;
			long tsum = -iinf-1;
			ETwoTypesOfSpells.State bord = new ETwoTypesOfSpells.State(ind, iinf+1);
			HashMap<Integer, ArrayDeque<ETwoTypesOfSpells.State>> hm = new HashMap<>();

			public MTS() {
				ind = -2;
				add(iinf+1);
				add(0);
			}

			public boolean add(int x) {
				tsum += x;
				ETwoTypesOfSpells.State s = new ETwoTypesOfSpells.State(ind++, x);
				hm.computeIfAbsent(x, o -> new ArrayDeque<>()).addLast(s);
				super.add(s);
				if(s.compareTo(bord)>0) {
					sum -= bord.x;
					sum += s.x;
					bord = higher(bord);
				}
				return false;
			}

			public void remove(int x) {
				tsum -= x;
				ETwoTypesOfSpells.State s = hm.get(x).removeLast();
				if(s.compareTo(bord)>=0) {
					sum -= s.x;
					sum += (bord = lower(bord)).x;
				}
				super.remove(s);
			}

			public void incr() {
				sum += (bord = lower(bord)).x;
			}

			public void decr() {
				sum -= bord.x;
				bord = higher(bord);
			}

		}

	}

	static interface MultiSet<T> extends Map<T, Integer> {
		default void add(T key) {
			add(key, 1);
		}

		default void add(T key, int count) {
			if(count<0) {
				delete(key, -count);
			}else if(count>0) {
				compute(key, (k, v) -> {
					if(v==null) {
						v = 0;
					}
					return v+count;
				});
			}
		}

		default void delete(T key) {
			delete(key, 1);
		}

		default void delete(T key, int count) {
			if(count<0) {
				add(key, -count);
			}else if(count>0) {
				compute(key, (k, v) -> {
					if(v==null) {
						v = 0;
					}
					v -= count;
					return v<=0 ? null : v;
				});
			}
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

		public void println(long l) {
			println(String.valueOf(l));
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

	static class MultiTreeSet<T> extends TreeMap<T, Integer> implements MultiSet<T> {
	}
}