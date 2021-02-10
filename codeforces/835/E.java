import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
			Input in = new Input(inputStream);
			Output out = new Output(outputStream);
			EThePenguinsGame solver = new EThePenguinsGame();
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

	static class EThePenguinsGame {
		public EThePenguinsGame() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), x = in.nextInt(), y = in.nextInt();
			IntUnaryOperator expected = (length) -> (length&1)*x;
			ToIntFunction<Collection<Integer>> query = (arr) -> {
				int[] toPrint = arr.stream().mapToInt(o -> o+1).filter(o -> o<=n).toArray();
				pw.println("?", toPrint.length, toPrint);
				pw.flush();
				return expected.applyAsInt(arr.size()-toPrint.length)^in.nextInt();
			};
			ToIntFunction<Collection<ArrayList<Integer>>> mquery = (arr) -> query.applyAsInt(arr.stream().collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll));
			ToIntFunction<ArrayList<Integer>> bsearch = (arr) -> {
				int l = 0, r = arr.size();
				while(l+1<r) {
					int mid = (l+r)/2;
					if(query.applyAsInt(arr.subList(l, mid))!=expected.applyAsInt(mid-l)) {
						r = mid;
					}else {
						l = mid;
					}
				}
				return arr.get(l);
			};
			Pair<ArrayList<ArrayList<Integer>>, ArrayList<ArrayList<Integer>>>[] queries = new Pair[10];
			for(int i = 0, diff = 512; i<10; i++, diff /= 2) {
				ArrayList<ArrayList<Integer>> a = new ArrayList<>(), b = new ArrayList<>();
				for(int j = 0; j<1024; j += 2*diff) {
					{
						ArrayList<Integer> cur = new ArrayList<>();
						for(int k = j; k<j+diff; k++) {
							cur.add(k);
						}
						a.add(cur);
					}
					{
						ArrayList<Integer> cur = new ArrayList<>();
						for(int k = j+diff; k<j+2*diff; k++) {
							cur.add(k);
						}
						b.add(cur);
					}
				}
				queries[i] = new Pair<>(a, b);
			}
			int i;
			for(i = 0; i<9&&mquery.applyAsInt(queries[i].a)==0; i++) ;
			ArrayList<Integer> poss = new ArrayList<>();
			for(var v: queries[i].a) {
				poss.addAll(v);
			}
			int a = bsearch.applyAsInt(poss);
			int j;
			for(j = 0; !queries[i].a.get(j).contains(a); j++) ;
			int b = bsearch.applyAsInt(queries[i].b.get(j));
			pw.println("!", a+1, b+1);
			pw.flush();
		}

	}

	static class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {
		public T1 a;
		public T2 b;

		public Pair(Pair<T1, T2> p) {
			this(p.a, p.b);
		}

		public Pair(T1 a, T2 b) {
			this.a = a;
			this.b = b;
		}

		public String toString() {
			return Utilities.Debug.ts(a)+" "+Utilities.Debug.ts(b);
		}

		public int hashCode() {
			return Objects.hash(a, b);
		}

		public boolean equals(Object o) {
			if(o instanceof Pair) {
				Pair p = (Pair) o;
				return a.equals(p.a)&&b.equals(p.b);
			}
			return false;
		}

		public int compareTo(Pair<T1, T2> p) {
			int cmp = ((Comparable<T1>) a).compareTo(p.a);
			if(cmp==0) {
				return ((Comparable<T2>) b).compareTo(p.b);
			}
			return cmp;
		}

	}

	static class Utilities {
		public static class Debug {
			public static <T> String ts(T t) {
				if(t==null) {
					return "null";
				}
				if(t instanceof Iterable) {
					return ts((Iterable<?>) t);
				}else if(t instanceof int[]) {
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
				}else if(t instanceof Object[]) {
					return ts((Object[]) t);
				}
				return t.toString();
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

		}

	}

	static interface InputReader {
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
			if(sb.length()>BUFFER_SIZE/2) {
				flushToBuffer();
			}
		}

		public void print(int i) {
			print(String.valueOf(i));
		}

		public void print(long l) {
			print(String.valueOf(l));
		}

		public void print(double d) {
			print(String.valueOf(d));
		}

		public void print(char[] c) {
			print(String.valueOf(c));
		}

		public void print(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(o[i]);
			}
		}

		public void print(Object o) {
			if(o==null) {
				print("null");
			}else if(o instanceof Iterable) {
				print((Iterable<?>) o);
			}else if(o instanceof int[]) {
				print((int[]) o);
			}else if(o instanceof long[]) {
				print((long[]) o);
			}else if(o instanceof char[]) {
				print((char[]) o);
			}else if(o instanceof double[]) {
				print((double[]) o);
			}else if(o instanceof Object[]) {
				print((Object[]) o);
			}else {
				print(o.toString());
			}
		}

		public void print(int[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
			}
		}

		public void print(long[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
			}
		}

		public void print(double[] arr) {
			for(int i = 0; i<arr.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(arr[i]);
			}
		}

		public <T> void print(Iterable<T> iter) {
			boolean first = true;
			for(T t: iter) {
				if(!first) {
					print(" ");
				}
				first = false;
				print(t);
			}
		}

		public void println() {
			sb.append(lineSeparator);
		}

		public void println(Object... o) {
			for(int i = 0; i<o.length; i++) {
				if(i!=0) {
					print(" ");
				}
				print(o[i]);
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

	static class Input implements InputReader {
		BufferedReader br;
		StringTokenizer st;

		public Input(InputStream is) {
			this(is, 1<<20);
		}

		public Input(InputStream is, int bs) {
			br = new BufferedReader(new InputStreamReader(is), bs);
			st = new StringTokenizer("");
		}

		public boolean hasNext() {
			try {
				while(st==null||!st.hasMoreTokens()) {
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
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}

