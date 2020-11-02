import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.IntUnaryOperator;

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
			BIdentifyTheOperations solver = new BIdentifyTheOperations();
			int testCount = in.nextInt();
			for(int i = 1; i<=testCount; i++)
				solver.solve(i, in, out);
			out.close();
			System.err.println(System.currentTimeMillis()-startTime+"ms");
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}

	static class BIdentifyTheOperations {
		private final int mod = (int) 998244353;

		public BIdentifyTheOperations() {
		}

		public void solve(int kase, InputReader in, Output pw) {
			int n = in.nextInt(), k = in.nextInt();
			int[] arr = new int[k];
			{
				int[] a = in.nextInt(n, o -> o-1);
				int[] ind = new int[n];
				for(int i = 0; i<n; i++) {
					ind[a[i]] = i;
				}
				int[] b = in.nextInt(k, o -> o-1);
				for(int i = 0; i<k; i++) {
					arr[i] = ind[b[i]];
				}
			}
			HashSet<Integer> hs = new HashSet<>();
			RBSet<Integer> ts = new RBSet<>();
			for(int i = 0; i<k; i++) {
				hs.add(arr[i]);
			}
			for(int i = 0; i<n; i++) {
				ts.add(i);
			}
			long ans = 1;
			for(int i = 0; i<k; i++) {
				hs.remove(arr[i]);
				int ind = ts.rank(arr[i]);
				int l = -1, r = -1;
				boolean lv = ind>0&&!hs.contains(l = ts.kth(ind-1)), rv = ind<n-i-1&&!hs.contains(r = ts.kth(ind+1));
				if(!(lv||rv)) {
					ans = 0;
					break;
				}else if(lv&&rv) {
					ans = (ans*2)%mod;
					ts.remove(l);
				}else if(lv) {
					ts.remove(l);
				}else {
					ts.remove(r);
				}
			}
			pw.println(ans);
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

	static class RBSet<Key> extends AbstractSet<Key> {
		private RBMap<Key, Object> map;
		private Object valueKey = new Object();

		public RBSet() {
			map = new RBMap<>();
		}

		public RBSet(Comparator<? super Key> comparator) {
			map = new RBMap<>(comparator);
		}

		public RBSet(Collection<? extends Key> c) {
			this();
			addAll(c);
		}

		public boolean add(Key key) {
			map.put(key, valueKey);
			return false;
		}

		public boolean addAll(Collection<? extends Key> c) {
			for(Key k: c) {
				add(k);
			}
			return false;
		}

		public void clear() {
			map.clear();
		}

		public boolean contains(Object key) {
			return map.containsKey(key);
		}

		public boolean isEmpty() {
			return map.isEmpty();
		}

		public Iterator<Key> iterator() {
			return map.keySet().iterator();
		}

		public Key kth(int k) {
			return map.kth(k);
		}

		public int rank(Key key) {
			return map.rank(key);
		}

		public boolean remove(Object o) {
			map.remove(o);
			return false;
		}

		public int size() {
			return map.size();
		}

	}

	static interface InputReader {
		int nextInt();

		default int[] nextInt(int n, IntUnaryOperator operator) {
			int[] ret = new int[n];
			for(int i = 0; i<n; i++) {
				ret[i] = operator.applyAsInt(nextInt());
			}
			return ret;
		}

	}

	static class RBMap<Key, Value> extends AbstractMap<Key, Value> {
		private static final boolean RED = true;
		private static final boolean BLACK = false;
		private Comparator<? super Key> comparator;
		private Node root;

		public RBMap() {
			comparator = null;
		}

		public RBMap(Comparator<? super Key> comparator) {
			this.comparator = comparator;
		}

		public RBMap(Map<? extends Key, ? extends Value> m) {
			this();
			putAll(m);
		}

		private boolean isRed(Node x) {
			if(x==null) return false;
			return x.color==RED;
		}

		private int size(Node x) {
			if(x==null) return 0;
			return x.size;
		}

		private int compare(Key a, Key b) {
			if(comparator==null) {
				return ((Comparable<Key>) a).compareTo(b);
			}
			return comparator.compare(a, b);
		}

		public int size() {
			return size(root);
		}

		public boolean isEmpty() {
			return root==null;
		}

		public void clear() {
			root = null;
		}

		private Map.Entry<Key, Value> toEntry(Node n) {
			return n==null ? null : new AbstractMap.SimpleImmutableEntry<>(n.key, n.val);
		}

		private Key getKey(Node n) {
			return n==null ? null : n.key;
		}

		private Value getValue(Node n) {
			return n==null ? null : n.val;
		}

		public Value get(Object key) {
			return getValue(get(root, (Key) key));
		}

		private Node get(Node x, Key key) {
			Key k = (Key) key;
			while(x!=null) {
				int cmp = compare(k, x.key);
				if(cmp<0) x = x.left;
				else if(cmp>0) x = x.right;
				else return x;
			}
			return null;
		}

		public boolean containsKey(Object key) {
			return get(key)!=null;
		}

		public Value put(Key key, Value val) {
			root = put(root, key, val);
			root.color = BLACK;
			// assert check();
			return null;
		}

		public void putAll(Map<? extends Key, ? extends Value> m) {
			for(Map.Entry<? extends Key, ? extends Value> e: m.entrySet()) {
				put(e.getKey(), e.getValue());
			}
		}

		private Node put(Node h, Key key, Value val) {
			if(h==null) return new Node(key, val, RED, 1);

			int cmp = compare(key, h.key);
			if(cmp<0) h.left = put(h.left, key, val);
			else if(cmp>0) h.right = put(h.right, key, val);
			else h.val = val;

			// fix-up any right-leaning links
			if(isRed(h.right)&&!isRed(h.left)) h = rotateLeft(h);
			if(isRed(h.left)&&isRed(h.left.left)) h = rotateRight(h);
			if(isRed(h.left)&&isRed(h.right)) flipColors(h);
			h.size = size(h.left)+size(h.right)+1;

			return h;
		}

		private Node pollFirst(Node h) {
			if(h.left==null)
				return null;

			if(!isRed(h.left)&&!isRed(h.left.left))
				h = moveRedLeft(h);

			h.left = pollFirst(h.left);
			return balance(h);
		}

		public Value remove(Object key) {
			if(!containsKey(key)) return null;

			// if both children of root are black, set root to red
			if(!isRed(root.left)&&!isRed(root.right))
				root.color = RED;

			root = delete(root, (Key) key);
			if(!isEmpty()) root.color = BLACK;
			// assert check();
			return null;
		}

		private Node delete(Node h, Key key) {
			// assert get(h, key) != null;

			if(compare(key, h.key)<0) {
				if(!isRed(h.left)&&!isRed(h.left.left))
					h = moveRedLeft(h);
				h.left = delete(h.left, key);
			}else {
				if(isRed(h.left))
					h = rotateRight(h);
				if(compare(key, h.key)==0&&(h.right==null))
					return null;
				if(!isRed(h.right)&&!isRed(h.right.left))
					h = moveRedRight(h);
				if(compare(key, h.key)==0) {
					Node x = first(h.right);
					h.key = x.key;
					h.val = x.val;
					// h.val = get(h.right, min(h.right).key);
					// h.key = min(h.right).key;
					h.right = pollFirst(h.right);
				}else h.right = delete(h.right, key);
			}
			return balance(h);
		}

		private Node rotateRight(Node h) {
			// assert (h != null) && isRed(h.left);
			Node x = h.left;
			h.left = x.right;
			x.right = h;
			x.color = x.right.color;
			x.right.color = RED;
			x.size = h.size;
			h.size = size(h.left)+size(h.right)+1;
			return x;
		}

		private Node rotateLeft(Node h) {
			// assert (h != null) && isRed(h.right);
			Node x = h.right;
			h.right = x.left;
			x.left = h;
			x.color = x.left.color;
			x.left.color = RED;
			x.size = h.size;
			h.size = size(h.left)+size(h.right)+1;
			return x;
		}

		private void flipColors(Node h) {
			// h must have opposite color of its two children
			// assert (h != null) && (h.left != null) && (h.right != null);
			// assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
			//    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
			h.color = !h.color;
			h.left.color = !h.left.color;
			h.right.color = !h.right.color;
		}

		private Node moveRedLeft(Node h) {
			// assert (h != null);
			// assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

			flipColors(h);
			if(isRed(h.right.left)) {
				h.right = rotateRight(h.right);
				h = rotateLeft(h);
				flipColors(h);
			}
			return h;
		}

		private Node moveRedRight(Node h) {
			// assert (h != null);
			// assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
			flipColors(h);
			if(isRed(h.left.left)) {
				h = rotateRight(h);
				flipColors(h);
			}
			return h;
		}

		private Node balance(Node h) {
			// assert (h != null);

			if(isRed(h.right)) h = rotateLeft(h);
			if(isRed(h.left)&&isRed(h.left.left)) h = rotateRight(h);
			if(isRed(h.left)&&isRed(h.right)) flipColors(h);

			h.size = size(h.left)+size(h.right)+1;
			return h;
		}

		private Node first(Node x) {
			// assert x != null;
			if(x.left==null) return x;
			else return first(x.left);
		}

		public Key kth(int rank) {
			return kth(root, rank);
		}

		private Key kth(Node x, int rank) {
			if(x==null) return null;
			int leftSize = size(x.left);
			if(leftSize>rank) return kth(x.left, rank);
			else if(leftSize<rank) return kth(x.right, rank-leftSize-1);
			else return x.key;
		}

		public int rank(Key key) {
			if(key==null) throw new IllegalArgumentException("argument to rank() is null");
			return rank(key, root);
		}

		private int rank(Key key, Node x) {
			if(x==null) return 0;
			int cmp = compare(key, x.key);
			if(cmp<0) return rank(key, x.left);
			else if(cmp>0) return 1+size(x.left)+rank(key, x.right);
			else return size(x.left);
		}

		public Set<Entry<Key, Value>> entrySet() {
			ArrayList<Map.Entry<Key, Value>> ret = new ArrayList<>(size()+1);
			getEntrySet(root, ret);
			return new LinkedHashSet<>(ret);
		}

		private void getEntrySet(Node n, ArrayList<Map.Entry<Key, Value>> list) {
			if(n==null) {
				return;
			}
			getEntrySet(n.left, list);
			list.add(toEntry(n));
			getEntrySet(n.right, list);
		}

		public Set<Key> keySet() {
			ArrayList<Key> ret = new ArrayList<>(size()+1);
			getKeySet(root, ret);
			return new LinkedHashSet<>(ret);
		}

		private void getKeySet(Node n, ArrayList<Key> list) {
			if(n==null) {
				return;
			}
			getKeySet(n.left, list);
			list.add(getKey(n));
			getKeySet(n.right, list);
		}

		private class Node {
			private Key key;
			private Value val;
			private Node left;
			private Node right;
			private boolean color;
			private int size;

			public Node(Key key, Value val, boolean color, int size) {
				this.key = key;
				this.val = val;
				this.color = color;
				this.size = size;
			}

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

