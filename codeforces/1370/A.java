import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		Input in = new Input(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		AMaximumGCD solver = new AMaximumGCD();
		solver.solve(1, in, out);
		out.close();
	}
	static class AMaximumGCD {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt();
				out.println(n/2);
			}
		}

	}

	static class Input {
		private InputStream stream;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int numChars;

		public Input(InputStream stream) {
			this.stream = stream;
		}

		int read() {
			if(numChars==-1)
				throw new InputMismatchException();
			if(curChar >= numChars) {
				curChar = 0;
				try {
					numChars = stream.read(buf);
				}catch(IOException e) {
					throw new InputMismatchException();
				}
				if(numChars<=0) return -1;
			}
			return buf[curChar++];
		}

		boolean isSpaceChar(int c) {
			return c==' '||c=='\n'||c=='\r'||c=='\t'||c==-1;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public String next() {
			int c = read();
			while(isSpaceChar(c)) c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			}while(!isSpaceChar(c));
			return res.toString();
		}

	}
}

