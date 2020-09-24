import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InputMismatchException;
import java.util.Objects;

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
			EBeautifulMirrors solver = new EBeautifulMirrors();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class EBeautifulMirrors {
		private final int mod = 998244353;

		public EBeautifulMirrors() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt();
			Fraction b = new Fraction(0), one = new Fraction(1);
			for(int i = 0; i<n; i++) {
				Fraction a = new Fraction(in.nextInt(), 100);
				b = a.multiply(b.opposite()).subtract(a).add(b).add(one).divide(a);
				b = new Fraction(b.num%mod, b.den%mod);
			}
			pw.println((b.num*Utilities.math.pow(b.den, mod-2, mod))%mod);
		}

	}

	static class Utilities {
		public static long gcd(long a, long b) {
			return b==0 ? a : gcd(b, a%b);
		}

		public static class math {
			public static long pow(long base, long exp, long mod) {
				long ans = 1, cur = base;
				while(exp>0) {
					if((exp&1)>0) {
						ans = (ans*cur)%mod;
					}
					cur = (cur*cur)%mod;
					exp >>= 1;
				}
				return ans;
			}

		}

	}

	static interface InputReader {
		int nextInt();

	}

	static final class Fraction implements Comparable<Fraction> {
		public long num;
		public long den;

		public Fraction(Fraction f) {
			num = f.num;
			den = f.den;
		}

		public Fraction(long numerator, long denominator) {
			num = numerator;
			den = denominator;
			simplify();
		}

		public Fraction(long value) {
			num = value;
			den = 1;
		}

		public boolean equals(Object o) {
			if(this==o) return true;
			if(o==null||getClass()!=o.getClass()) return false;
			Fraction f = (Fraction) o;
			return num*f.den==
					den*f.num;
		}

		public int hashCode() {
			return Objects.hash(num, den);
		}

		public String toString() {
			return num+"/"+den;
		}

		public int compareTo(Fraction f) {
			return Long.compare(num*f.den, den*f.num);
		}

		public void simplify() {
			long gcd = Utilities.gcd(num, den);
			num /= gcd;
			den /= gcd;
		}

		public Fraction multiply(Fraction f) {
			Fraction ret = new Fraction(num*f.num, den*f.den);
			ret.simplify();
			return ret;
		}

		public Fraction divide(Fraction f) {
			return multiply(f.reciprocal());
		}

		public Fraction add(Fraction f) {
			Fraction ret = new Fraction(num*f.den+den*f.num, den*f.den);
			ret.simplify();
			return ret;
		}

		public Fraction subtract(Fraction f) {
			return add(opposite());
		}

		public Fraction reciprocal() {
			return new Fraction(den, num);
		}

		public Fraction opposite() {
			return new Fraction(-num, den);
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
}

