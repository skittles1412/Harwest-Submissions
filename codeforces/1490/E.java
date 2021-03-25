//not my code
//https://codeforces.com/blog/entry/89000
 
import java.util.*;
import java.io.*;
 
public class Main {
	static InputReader scn = new InputReader(System.in);
	static OutputWriter out = new OutputWriter(System.out);
	public static void main(String[] args) {
		// Running Number Of TestCases (t)
		int t = scn.nextInt();
		// int t = 1;
		while(t-- > 0)
			solve();
		out.close();
	}
 
	public static void dummy(){
		// Dummy Code for Rough
		int n = scn.nextInt();
		out.println(n);
	}
	static boolean[] dp;
	public static void solve() {
		// Main Solution (AC)
		int n = scn.nextInt();
		int[] arr = scn.readArrays(n);
		if(n == 1){
			out.println(1);
			out.println(1);
			return;
		}
		int[] temp = new int[n];
		for(int i = 0; i < n; i++) temp[i] = arr[i];
		{
		    Random rand = new Random();
		    for(int i = 1; i < n; i++) {
		        int j = rand.nextInt(i);
		        int tmp = temp[j];
		        temp[j] = temp[i];
		        temp[i] = tmp;
		    }
		}
		Arrays.sort(temp);
		HashSet<Integer> set = new HashSet<>();
		long[] prefix = new long[n];
		long sum = 0;	
		for(int i = 0; i < n; i++){
			sum += temp[i];
			prefix[i] = sum;
		}
		set.add(temp[n-1]);
		// out.println("Prefix Arrays is " + Arrays.toString(prefix));
		for(int i = n - 2; i >= 0; i--){
			if(prefix[i] >= temp[i+1]) set.add(temp[i]);
			else break;
		}
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < n; i++){
			if(set.contains(arr[i])) list.add(i+1);
		}
		out.println(list.size());
		for(int i : list) out.print(i + " ");
		out.println();
	}
 
	public static HashMap<Integer, Integer> CountFrequencies(int[] arr) {
 
		HashMap<Integer, Integer> map = new HashMap<>();
 
		for (int i : arr) {
			if (map.containsKey(i))
				map.put(i, map.get(i) + 1);
			else
				map.put(i, 1);
		}
		return map;
	}
 
	public static HashMap<Integer, Integer> sortByValue(HashMap<Integer, Integer> hm) {
        List<Map.Entry<Integer, Integer> > list = new LinkedList<Map.Entry<Integer, Integer> >(hm.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() { 
            public int compare(Map.Entry<Integer, Integer> o1,  
                               Map.Entry<Integer, Integer> o2){ 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        });
        HashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>(); 
        for (Map.Entry<Integer, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
 
	public static void sortbyColumn(int[][] arr, int col) {
		Arrays.sort(arr, new Comparator<int[]>() {
			public int compare(final int[] entry1, final int[] entry2) {
				if (entry1[col] > entry2[col])
					return 1;
				else
					return -1;
			}
		});
	}
 
	public static HashSet<Long> primeFactors(long n, HashSet<Long> list, int d) {
		list.add(1L);
		list.add(n);
		for (long i = 2; i <= Math.sqrt(n); i += 1) {
			long a = i;
			if (n % a == 0) {
				list.add(a);
				if (n % (n / a) == 0)
					list.add(n / a);
			}
		}
		return list;
	}
 
	public static void ArraySort2D(int[][] arr, int xy) {
		// xy == 0, for sorting wrt X-Axis
		// xy == 1, for sorting wrt Y-Axis
		Arrays.sort(arr, Comparator.comparingDouble(o -> o[xy]));
	}
 	
 	public static long lcm(long a, long b) {
		return (a * b) / gcd(a, b);
	}
 
	public static long gcd(long a, long b) {
		if (a == 0)
			return b;
		return gcd(b % a, a);
	}
 	
 	public static int binarySearch(int arr[], int l, int r, int x) {
		if (r >= l) {
			int mid = l + (r - l) / 2;
			if (arr[mid] == x)
				return mid;
			if (arr[mid] > x)
				return binarySearch(arr, l, mid - 1, x);
			return binarySearch(arr, mid + 1, r, x);
		}
		return -1;
	}
 
	static class InputReader {
		private InputStream stream;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int numChars;
		private SpaceCharFilter filter;
 
		public InputReader(InputStream stream) {
			this.stream = stream;
		}
 
		public int[] readArrays(int n) {
			int[] a = new int[n];
			for (int i = 0; i < n; i++) {
				a[i] = scn.nextInt();
			}
			return a;
		}
 
		public int read() {
			if (numChars == -1)
				throw new InputMismatchException();
			if (curChar >= numChars) {
				curChar = 0;
				try {
					numChars = stream.read(buf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (numChars <= 0)
					return -1;
			}
			return buf[curChar++];
		}
 
		public int nextInt() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			int res = 0;
			do {
				if (c < '0' || c > '9')
					throw new InputMismatchException();
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res * sgn;
		}
 
		public long nextLong() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			long res = 0;
			do {
				if (c < '0' || c > '9')
					throw new InputMismatchException();
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res * sgn;
		}
 
		public String nextLine() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}
 
		public boolean isSpaceChar(int c) {
			if (filter != null)
				return filter.isSpaceChar(c);
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}
 
		public String next() {
			return nextLine();
		}
 
		public interface SpaceCharFilter {
			public boolean isSpaceChar(int ch);
		}
	}
 
	static class OutputWriter {
		private final PrintWriter writer;
 
		public OutputWriter(OutputStream outputStream) {
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
		}
 
		public OutputWriter(Writer writer) {
			this.writer = new PrintWriter(writer);
		}
 
		public void print(Object... objects) {
			for (int i = 0; i < objects.length; i++) {
				if (i != 0) {
					writer.print(' ');
//                    writer.print(1);
				}
				writer.print(objects[i]);
			}
		}
 
		public void println(Object... objects) {
			print(objects);
			writer.println();
		}
 
		public void close() {
			writer.close();
		}
	}
 
}