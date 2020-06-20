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
		DOddEvenSubsequence solver = new DOddEvenSubsequence();
		solver.solve(1, in, out);
		out.close();
	}
	static class DOddEvenSubsequence {
		int[] arr;
		int k;

		public boolean valid(int x) {
			int s0 = 0, s1 = 0;
			for(int i = 0; i<arr.length-1+k%2; i++) {
				if(arr[i]<=x) {
					s0++;
					i++;
				}
			}
			for(int i = 1; i<arr.length-k%2; i++) {
				if(arr[i]<=x) {
					s1++;
					i++;
				}
			}
			return s0 >= (k+1)/2||s1 >= k/2;
		}

		public void solve(int testNumber, Input in, PrintWriter out) {
			int n = in.nextInt();
			k = in.nextInt();
			arr = new int[n];
			for(int i = 0; i<n; i++) {
				arr[i] = in.nextInt();
			}
			int l = 1, r = 1000000000;
			while(l<r) {
				int mid = (l+r)/2;
				if(valid(mid)) {
					r = mid;
				}else {
					l = mid+1;
				}
			}
			out.println(l);
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

