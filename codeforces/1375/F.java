import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;
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
		FIntegerGame solver = new FIntegerGame();
		solver.solve(1, in, out);
		out.close();
	}
	static class FIntegerGame {
		public void solve(int testNumber, Input in, PrintWriter out) {
			long[] arr = new long[] {in.nextInt(), in.nextInt(), in.nextInt()};
			out.println("First");
			out.flush();
			int large = 0, ot1 = -1, ot2 = -1;
			if(arr[1]>arr[0]&&arr[1]>arr[2]) {
				large = 1;
			}else if(arr[2]>arr[0]&&arr[2]>arr[1]) {
				large = 2;
			}
			for(int i = 0; i<3; i++) {
				if(i!=large) {
					if(ot1==-1) {
						ot1 = i;
					}else {
						ot2 = i;
					}
				}
			}
			long y = 2*arr[large]-arr[ot1]-arr[ot2];
			out.println(y);
			out.flush();
			int response = in.nextInt()-1;
			if(response!=large) {
				out.println(arr[large]-arr[response==ot1 ? ot2 : ot1]);
				out.flush();
				response = in.nextInt();
				if(response==0) {
					System.exit(0);
				}else {
					System.out.println("The judge is trolling");
				}
			}else {
				arr[large] += y;
				out.println(2*arr[large]-arr[ot1]-arr[ot2]);
				out.flush();
				response = in.nextInt()-1;
				out.println(arr[large]-arr[response==ot1 ? ot2 : ot1]);
				out.flush();
				response = in.nextInt();
				if(response==0) {
					System.exit(0);
				}else {
					System.out.println("The judge is trolling");
				}
			}
		}

	}

	static class Input {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			br = new BufferedReader(new InputStreamReader(is));
			try {
				st = new StringTokenizer(br.readLine());
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		public boolean hasNext() {
			try {
				while(!st.hasMoreTokens()) {
					String s = br.readLine();
					if(s==null) {
						return false;
					}
					st = new StringTokenizer(s);
				}
				return true;
			}catch(Exception e) {
				return false;
			}
		}

		public String next() {
			if(!hasNext()) {
				throw new InputMismatchException();
			}
			return st.nextToken("\r\n\t\f ");
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

