import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.TreeSet;

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
			BSpaceIsaac solver = new BSpaceIsaac();
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

	static class BSpaceIsaac {
		public BSpaceIsaac() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), m = in.nextInt();
			int[] arr = in.nextInt(n);
			int[] diff = new int[n-1];
			for(int i = 0; i<n-1; i++) {
				diff[i] = arr[i+1]-arr[i];
			}
			BSpaceIsaac.MultiChecker pc = new BSpaceIsaac.MultiChecker(diff.clone());
			TreeSet<Integer> ans = new TreeSet<>();
			for(int i = 0; i<n-1; i++) {
				int sum = (arr[0]+arr[i])%m;
				if(sum>=arr[i]&&sum<=arr[i+1]&&(arr[i+1]+arr[n-1])%m==sum
						&&(i==0||pc.isPalin(0, i-1))&&(i==n-2||pc.isPalin(i+1, n-2))) {
					ans.add(sum);
				}
			}
			loop:
			{
				int sum = (arr[0]+arr[n-1])%m;
				if(sum<=arr[0]||sum>=arr[n-1]&&pc.isPalin(0, n-2)) {
					for(int i = 0; i<n-1; i++) {
						if(diff[i]!=diff[n-2-i]) {
							break loop;
						}
					}
					ans.add(sum);
				}
			}
			pw.println(ans.size());
			pw.println(ans);
		}

		static class PalindromeChecker {
			int n;
			Utilities.Hasher forw;
			Utilities.Hasher rev;

			public PalindromeChecker(int[] arr) {
				n = arr.length;
				long mod;
				while((mod = BigInteger.probablePrime(30, new Random()).longValueExact())<=(1<<29)) ;
				long mul = BigInteger.probablePrime(28, new Random()).longValueExact()%mod;
				forw = new Utilities.Hasher(arr, mul, mod);
				Utilities.reverse(arr);
				rev = new Utilities.Hasher(arr, mul, mod);
			}

			public long revHash(int l, int r) {
				return rev.hash(n-1-r, n-1-l);
			}

			public boolean isPalin(int l, int r) {
				return forw.hash(l, r)==revHash(l, r);
			}

		}

		static class MultiChecker {
			int n = 25;
			BSpaceIsaac.PalindromeChecker[] checkers;

			public MultiChecker(int[] arr) {
				if(arr.length==0) {
					return;
				}
				checkers = new BSpaceIsaac.PalindromeChecker[n];
				for(int i = 0; i<n; i++) {
					checkers[i] = new BSpaceIsaac.PalindromeChecker(arr.clone());
				}
			}

			public boolean isPalin(int l, int r) {
				for(int i = 0; i<25; i++) {
					if(!checkers[i].isPalin(l, r)) {
						return false;
					}
				}
				return true;
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

		interface CharToIntFunction {
			int apply(char c);

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

		public void print(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(String.valueOf(o[i]));
			}
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

		public void println(String s) {
			sb.append(s);
			println();
		}

		public void println() {
			sb.append(lineSeparator);
		}

		public <T> void println(Iterable<T> iter) {
			boolean first = true;
			for(T t: iter) {
				if(!first) {
					print(" ");
				}
				first = false;
				print(t);
			}
			println();
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

		public static void reverse(int[] arr) {
			reverse(arr, 0, arr.length-1);
		}

		public static class math {
			public static long modInverse(long n, long mod) {
				//https://www.extendedeuclideanalgorithm.com/code.php
				long q = 0, r = 1, s1 = 1, s2 = 0, s3 = 1, t1 = 0, t2 = 1, t3 = 0, a = mod, b = n;
				while(r>0) {
					q = a/b;
					r = a-q*b;
					s3 = s1-q*s2;
					t3 = t1-q*t2;
					if(r>0) {
						a = b;
						s1 = s2;
						t1 = t2;
						b = r;
						s2 = s3;
						t2 = t3;
					}
				}
				return (t2+mod)%mod;
			}

		}

		public static class Hasher {
			private final long mul;
			private final long mod;
			private final long[] powDiv;
			private final long[] hash;

			public Hasher(String s, InputReader.CharToIntFunction conv, long mul, long mod) {
				this(s.toCharArray(), conv, mul, mod);
			}

			public Hasher(char[] arr, InputReader.CharToIntFunction conv, long mul, long mod) {
				int n = arr.length;
				this.mul = mul;
				this.mod = mod;
				long div = Utilities.math.modInverse(mul, mod);
				powDiv = new long[n];
				hash = new long[n];
				powDiv[0] = 1;
				hash[0] = conv.apply(arr[0])%mod;
				long pow = 1;
				for(int i = 1; i<n; i++) {
					powDiv[i] = (powDiv[i-1]*div)%mod;
					hash[i] = (hash[i-1]+conv.apply(arr[i])*pow)%mod;
					pow = (pow*mul)%mod;
				}
			}

			public Hasher(int[] arr, long mul, long mod) {
				int n = arr.length;
				this.mul = mul;
				this.mod = mod;
				long div = Utilities.math.modInverse(mul, mod);
				powDiv = new long[n];
				hash = new long[n];
				powDiv[0] = 1;
				hash[0] = arr[0]%mod;
				long pow = 1;
				for(int i = 1; i<n; i++) {
					powDiv[i] = (powDiv[i-1]*div)%mod;
					pow = (pow*mul)%mod;
					hash[i] = (hash[i-1]+arr[i]*pow)%mod;
				}
			}

			public Hasher(long[] arr, long mul, long mod) {
				int n = arr.length;
				this.mul = mul;
				this.mod = mod;
				long div = Utilities.math.modInverse(mul, mod);
				powDiv = new long[n];
				hash = new long[n];
				powDiv[0] = 1;
				hash[0] = arr[0]%mod;
				long pow = 1;
				for(int i = 1; i<n; i++) {
					powDiv[i] = (powDiv[i-1]*div)%mod;
					hash[i] = (hash[i-1]+arr[i]*pow)%mod;
					pow = (pow*mul)%mod;
				}
			}

			public long hash(int l, int r) {
				if(l==0) {
					return hash[r];
				}
				return ((hash[r]+mod-hash[l-1])*powDiv[l])%mod;
			}

		}

	}
}

