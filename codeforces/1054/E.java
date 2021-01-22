import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
			EChipsPuzzle solver = new EChipsPuzzle();
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

	static class EChipsPuzzle {
		ArrayList<EChipsPuzzle.Operation> ans;
		ArrayDeque<Integer>[][] arr;

		public EChipsPuzzle() {
		}

		public void move(int a, int b, int c, int d) {
			if(a!=c||b!=d) {
				arr[c][d].addFirst(arr[a][b].removeLast());
				ans.add(new EChipsPuzzle.Operation(a, b, c, d));
			}
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			arr = new ArrayDeque[n][m];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<m; j++) {
					arr[i][j] = new ArrayDeque<>();
					String s = in.next();
					for(int k = 0; k<s.length(); k++) {
						arr[i][j].addLast(s.charAt(k)-'0');
					}
				}
			}
			ans = new ArrayList<>();
			for(int i = 1; i<n; i++) {
				int j = 0;
				while(!arr[i][j].isEmpty()) {
					move(i, j, 0, 0);
				}
			}
			for(int j = 1; j<m; j++) {
				int i = 0;
				while(!arr[i][j].isEmpty()) {
					move(i, j, 0, 0);
				}
			}
			for(int i = 1; i<n; i++) {
				for(int j = 1; j<m; j++) {
					while(!arr[i][j].isEmpty()) {
						if(arr[i][j].getLast()==0) {
							move(i, j, 0, j);
						}else {
							move(i, j, i, 0);
						}
					}
				}
			}
			for(int i = 2; i<n; i++) {
				int j = 0;
				while(!arr[i][j].isEmpty()) {
					move(i, j, 1, 0);
				}
			}
			for(int j = 2; j<m; j++) {
				int i = 0;
				while(!arr[i][j].isEmpty()) {
					move(i, j, 0, 1);
				}
			}
			while(!arr[0][0].isEmpty()) {
				if(arr[0][0].getLast()==0) {
					move(0, 0, 0, 1);
				}else {
					move(0, 0, 1, 0);
				}
			}
//			Utilities.Debug.dbg(arr);
			ArrayList<Integer>[][] target = new ArrayList[n][m];
			for(int i = 0; i<n; i++) {
				for(int j = 0; j<m; j++) {
					target[i][j] = new ArrayList<>();
					String s = in.next();
					for(int k = 0; k<s.length(); k++) {
						target[i][j].add(s.charAt(k)-'0');
					}
					Collections.reverse(target[i][j]);
				}
			}
			for(int i = 1; i<n; i++) {
				for(int j = 1; j<m; j++) {
					for(int k: target[i][j]) {
						if(k==0) {
							move(0, 1, 0, j);
							move(0, j, i, j);
						}else {
							move(1, 0, i, 0);
							move(i, 0, i, j);
						}
					}
				}
			}
			for(int i = 2; i<n; i++) {
				int j = 0;
				for(int k: target[i][j]) {
					if(k==0) {
						move(0, 1, 0, j);
						move(0, j, i, j);
					}else {
						move(1, 0, i, 0);
						move(i, 0, i, j);
					}
				}
			}
			for(int j = 2; j<m; j++) {
				int i = 0;
				for(int k: target[i][j]) {
					if(k==0) {
						move(0, 1, 0, j);
						move(0, j, i, j);
					}else {
						move(1, 0, i, 0);
						move(i, 0, i, j);
					}
				}
			}
			for(int i: target[0][1]) {
				if(i==0) {
					move(0, 1, 0, 0);
				}else {
					move(1, 0, 0, 0);
				}
			}
			for(int i: target[1][0]) {
				if(i==0) {
					move(0, 1, 0, 0);
				}else {
					move(1, 0, 0, 0);
				}
			}
			for(int i: target[0][0]) {
				if(i==0) {
					move(0, 1, 0, 0);
				}else {
					move(1, 0, 0, 0);
				}
			}
			for(int i = 0; i<target[0][1].size(); i++) {
				move(0, 0, 0, 1);
			}
			for(int i = 0; i<target[1][0].size(); i++) {
				move(0, 0, 1, 0);
			}
			pw.println(ans.size());
			for(var v: ans) {
				pw.println(v);
			}
//			Utilities.Debug.dbg(arr);
		}

		static class Operation {
			int a;
			int b;
			int c;
			int d;

			public Operation(int a, int b, int c, int d) {
				this.a = a;
				this.b = b;
				this.c = c;
				this.d = d;
			}

			public String toString() {
				return (a+1)+" "+(b+1)+" "+(c+1)+" "+(d+1);
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
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

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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

	static class Utilities {
		public static class Debug {
			public static boolean LOCAL = System.getProperty("ONLINE_JUDGE")==null;

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

	static interface InputReader {
		String next();

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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void println(int i) {
			println(String.valueOf(i));
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
}

