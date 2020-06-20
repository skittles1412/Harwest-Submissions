import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.ArrayList;
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
		BGCDCompression solver = new BGCDCompression();
		solver.solve(1, in, out);
		out.close();
	}
	static class BGCDCompression {
		public void solve(int testNumber, Input in, PrintWriter out) {
			int kase = in.nextInt();
			while(kase-->0) {
				int n = in.nextInt();
				int[] arr = new int[2*n];
				ArrayList<Integer> odd = new ArrayList<Integer>();
				ArrayList<Integer> even = new ArrayList<Integer>();
				for(int i = 0; i<2*n; i++) {
					arr[i] = in.nextInt();
					if(arr[i]%2==1) {
						odd.add(i+1);
					}else {
						even.add(i+1);
					}
				}
				if(odd.size()%2==0) {
					if(!odd.isEmpty()) {
						odd.remove(odd.size()-1);
						odd.remove(odd.size()-1);
					}else {
						even.remove(even.size()-1);
						even.remove(even.size()-1);
					}
				}else {
					odd.remove(odd.size()-1);
					even.remove(even.size()-1);
				}
				while(!odd.isEmpty()) {
					out.printf("%d %d\n", odd.remove(odd.size()-1), odd.remove(odd.size()-1));
				}
				while(!even.isEmpty()) {
					out.printf("%d %d\n", even.remove(even.size()-1), even.remove(even.size()-1));
				}
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

