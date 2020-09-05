import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.InputMismatchException;

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
			DThreeLogos solver = new DThreeLogos();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
	static class DThreeLogos {
		public DThreeLogos() {
		}

		public char[][] solve(int[] a, int[] b, int[] c, char[] arr) {
			char[][] ret = new char[a[1]][a[1]];
			if(a[0]>ret.length) {
				return null;
			}
			for(int i = 0; i<a[0]; i++) {
				Arrays.fill(ret[i], arr[0]);
			}
			if(b[1]==c[1]&&b[1]==a[1]&&b[0]+c[0]+a[0]==ret.length) {
				for(int i = a[0]; i<a[0]+b[0]; i++) {
					Arrays.fill(ret[i], arr[1]);
				}
				for(int i = a[0]+b[0]; i<a[1]; i++) {
					Arrays.fill(ret[i], arr[2]);
				}
				return ret;
			}
			if(b[0]==c[0]&&b[1]+c[1]==a[1]&&b[0]+a[0]==ret.length) {
				for(int i = a[0]; i<a[1]; i++) {
					for(int j = 0; j<b[1]; j++) {
						ret[i][j] = arr[1];
					}
					for(int j = b[1]; j<a[1]; j++) {
						ret[i][j] = arr[2];
					}
				}
				return ret;
			}
			return null;
		}

		public void solve(int kase, InputReader in, Output pw) {
			int[] arr = new int[] {0, 1, 2};
			int[] a = in.nextInt(2), b = in.nextInt(2), c = in.nextInt(2);
			do {
				int[] ac, bc, cc;
				if(arr[0]==0) {
					ac = a;
				}else if(arr[0]==1) {
					ac = b;
				}else {
					ac = c;
				}
				if(arr[1]==0) {
					bc = a;
				}else if(arr[1]==1) {
					bc = b;
				}else {
					bc = c;
				}
				if(arr[2]==0) {
					cc = a;
				}else if(arr[2]==1) {
					cc = b;
				}else {
					cc = c;
				}
				for(int i = 0; i<1<<3; i++) {
					int[] acc, bcc, ccc;
					acc = ac.clone();
					bcc = bc.clone();
					ccc = cc.clone();
					if((i&1)==0) {
						Utilities.swap(acc, 0, 1);
					}
					if((i&2)==0) {
						Utilities.swap(bcc, 0, 1);
					}
					if((i&4)==0) {
						Utilities.swap(ccc, 0, 1);
					}
					char[][] cur = solve(acc, bcc, ccc, new char[] {(char) (arr[0]+'A'), (char) (arr[1]+'A'), (char) (arr[2]+'A')});
					if(cur!=null) {
						pw.println(cur.length);
						for(int j = 0; j<cur.length; j++) {
							pw.println(cur[j]);
						}
						return;
					}
				}
			} while(Utilities.nextPermutation(arr));
			pw.println(-1);
		}

	}

	static class Utilities {
		public static void swap(int[] arr, int i, int j) {
			if(i!=j) {
				arr[i] ^= arr[j];
				arr[j] ^= arr[i];
				arr[i] ^= arr[j];
			}
		}

		public static void reverse(int[] arr, int i, int j) {
			while(i<j) {
				swap(arr, i++, j--);
			}
		}

		public static boolean nextPermutation(int[] data) {
			if(data.length<=1) {
				return false;
			}
			int last = data.length-2;
			while(last>=0) {
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

		public void println(int i) {
			println(String.valueOf(i));
		}

		public void println(char[] c) {
			println(String.valueOf(c));
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

		default int[] nextInt(int n) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = nextInt();
			}
			return ret;
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
}



