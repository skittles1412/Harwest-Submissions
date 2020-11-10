import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
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
			long startTime = System.currentTimeMillis();
			InputStream inputStream = System.in;
			OutputStream outputStream = System.out;
			FastReader in = new FastReader(inputStream);
			Output out = new Output(outputStream);
			ARecommendations solver = new ARecommendations();
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

	static class ARecommendations {
		public ARecommendations() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			int[][] arr = new int[n][2];
			for(int j = 0; j<2; j++) {
				for(int i = 0; i<n; i++) {
					arr[i][j] = in.nextInt();
				}
			}
			Arrays.sort(arr, Comparator.comparingInt(o -> o[0]));
			TreeMap<Integer, Integer> tm = new TreeMap<>();
			long ans = 0, sum = 0, cnt = 0;
			int i = 0, cur = -1;
			while(i<n||!tm.isEmpty()) {
				if(tm.isEmpty()) {
					cur = arr[i][0];
				}
				for(; i<n&&arr[i][0]==cur; i++) {
					Utilities.add(tm, arr[i][1]);
					sum += arr[i][1];
				}
				sum -= tm.lastKey();
				Utilities.remove(tm, tm.lastKey());
				ans += sum;
				cur++;
			}
			;
			pw.println(ans);
		}

	}

	static class Utilities {
		public static <K, V> V getOrDefault(Map<? extends K, ? extends V> map, K key, V value) {
			V ret = map.get(key);
			if(ret==null) {
				return value;
			}
			return ret;
		}

		public static <T> void add(Map<? super T, Integer> map, T key) {
			map.put(key, getOrDefault(map, key, 0)+1);
		}

		public static <T> void remove(Map<? super T, Integer> map, T key) {
			int cnt = getOrDefault(map, key, 1)-1;
			if(cnt>0) {
				map.put(key, cnt);
			}else {
				map.remove(key);
			}
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

	static interface InputReader {
		int nextInt();

	}
}

