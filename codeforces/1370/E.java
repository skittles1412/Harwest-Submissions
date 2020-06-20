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
		EBinarySubsequenceRotation solver = new EBinarySubsequenceRotation();
		solver.solve(1, in, out);
		out.close();
	}
	static class EBinarySubsequenceRotation {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int n = in.nextInt();
			String s1 = in.next();
			String s2 = in.next();
			int[] a = new int[n];
			int sum = 0;
			for(int i = 0; i<n; i++) {
				int ab = s1.charAt(i)-'0';
				int bb = s2.charAt(i)-'0';
				if(ab==bb) {
					a[i] = 0;
				}else if(ab==1) {
					a[i] = 1;
				}else {
					a[i] = -1;
				}
				sum += a[i];
			}
			if(sum!=0) {
				out.println(-1);
				return;
			}
			int psum = 0, nsum = 0;
			sum = 0;
			for(int i = 0; i<n; i++) {
				sum += a[i];
				psum = Math.max(psum, sum);
				if(sum<0) {
					sum = 0;
				}
			}
			sum = 0;
			for(int i = 0; i<n; i++) {
				sum += -a[i];
				nsum = Math.max(nsum, sum);
				if(sum<0) {
					sum = 0;
				}
			}
			out.println(Math.max(psum, nsum));
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

