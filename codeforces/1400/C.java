import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InputMismatchException;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
*/public class Main {
	static class TaskAdapter implements Runnable {
		@Override
		public void run() {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		FastReader in = new FastReader(inputStream);
		Output out = new Output(outputStream);
		CBinaryStringReconstruction solver = new CBinaryStringReconstruction();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<29);
		thread.start();
		thread.join();
	}
static class CBinaryStringReconstruction   {
public CBinaryStringReconstruction() {
    }

private String generate(String s, int x) {
		int n = s.length();
		char[] ret = new char[n];
		for(int i = 0; i<n; i++) {
			boolean val = false;
			if(i-x>=0) {
				val |= s.charAt(i-x)=='1';
			}
			if(i+x<n) {
				val |= s.charAt(i+x)=='1';
			}
			ret[i] = val?'1':'0';
		}
		return String.valueOf(ret);
	}

public void solve(int kase, InputReader in, Output pw) {
    	char[] arr = in.next(100005).toCharArray();
    	int n = arr.length, x = in.nextInt();
		char[] ans = new char[n];
    	boolean[] marked = new boolean[n];
		for(int i = 0; i<n; i++) {
			if(i-x<0&&i+x>=n) {
				ans[i] = '0';
			}else if(i-x<0) {
				if(arr[i+x]=='1') {
					ans[i] = '1';
					marked[i+x] = true;
				}else {
					ans[i] = '0';
				}
			}else if(i+x>=n) {
				ans[i] = arr[i-x];
			}else {
				int a = i-x, b = i+x;
				boolean ca = arr[a]=='1', cb = arr[b]=='1';
				if(ca==cb) {//11
					if(ca) {
						ans[i] = '1';
						marked[a] = marked[b] = true;
					}else {
						ans[i] = '0';
					}
				}else if(ca) {//10
					ans[i] = '0';
					if(!marked[a]) {
						pw.println(-1);
						return;
					}
				}else {//01
					ans[i] = '0';
				}
			}
		}
		String ins = String.valueOf(arr), anss = String.valueOf(ans);
		if(!generate(anss, x).equals(ins)) {
			pw.println(-1);
		}else {
			pw.println(ans);
		}
	}

}
static interface InputReader   {
String next();

int nextInt();

default String next(int maxLength) {
		return next();
	}

}
static class Output  implements Closeable, Flushable {
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
static class FastReader  implements InputReader {
final   private int BUFFER_SIZE = 1<<16;
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

public String next(int maxLength) {
		byte[] ret = new byte[maxLength];
		byte c = skip();
		int ind = 0;
		while(c!=-1&&!isSpaceChar(c)) {
			ret[ind++] = c;
			c = read();
		}
		return new String(ret, 0, ind);
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
}


