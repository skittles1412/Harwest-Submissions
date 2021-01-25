import java.io.*;
import java.util.InputMismatchException;

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
			CDeliveringCarcinogen solver = new CDeliveringCarcinogen();
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

	static class CDeliveringCarcinogen {
		private final long linf = 4_000_000_000_000_000_000L;
		final double eps = 1e-7;
		double xp;
		double yp;
		double vp;
		double rp;
		double thetap;
		double lengthp;
		double x;
		double y;
		double v;
		double r;

		public CDeliveringCarcinogen() {
		}

		public double[] solveQuadratic(double a, double b, double c) {
			double delta = b*b-4*a*c;
			if(delta<0) {
				return new double[0];
			}else if(delta==0) {
				return new double[] {-b/(2*a)};
			}
			delta = Math.sqrt(delta);
			return new double[] {(-b+delta)/(2*a), (-b-delta)/(2*a)};
		}

		public CDeliveringCarcinogen.Point[] lineCircleIntersections(double m, double b, double r) {
			if(Double.isNaN(m)) {
				//x^2+y^2 = r^2
				//y^2 = r^2-x^2
				double c = r*r-b*b;
				if(c>0) {
					c = Math.sqrt(c);
					return new CDeliveringCarcinogen.Point[] {new CDeliveringCarcinogen.Point(b, c), new CDeliveringCarcinogen.Point(b, -c)};
				}else if(c==0) {
					return new CDeliveringCarcinogen.Point[] {new CDeliveringCarcinogen.Point(b, 0)};
				}
				return new CDeliveringCarcinogen.Point[0];
			}
			//y = mx+b
			//x^2+(mx+b)^2 = r^2
			//x^2+m^2x^2+2mbx+b^2-r^2 = 0
			double[] x = solveQuadratic(1+m*m, 2*m*b, b*b-r*r);
			CDeliveringCarcinogen.Point[] ret = new CDeliveringCarcinogen.Point[x.length];
			for(int i = 0; i<x.length; i++) {
				ret[i] = new CDeliveringCarcinogen.Point(x[i], m*x[i]+b);
			}
			return ret;
		}

		public boolean lineSegmentCircleIntersects(double x1, double y1, double x2, double y2, double r) {
			double m = (y2-y1)/(x2-x1), b = y1-m*x1;
			if(Double.isNaN(m)) {
				b = x1;
			}
			CDeliveringCarcinogen.Point[] intersections = lineCircleIntersections(m, b, r);
			if(intersections.length<=1) {
				return false;
			}
			for(CDeliveringCarcinogen.Point p: intersections) {
				if(p.x>=Math.min(x1, x2)&&p.x<=Math.max(x1, x2)) {
					return true;
				}
			}
			return false;
		}

		public CDeliveringCarcinogen.Point[] pointCircleTangents(double x, double y, double r) {
			//square of distance from (x, y) to the tangents
			double dist = Math.sqrt(x*x+y*y-r*r);
			if(dist==0) {
				return new CDeliveringCarcinogen.Point[] {new CDeliveringCarcinogen.Point(x, y)};
			}
			//angle from (x, y) -> circle center
			double angle = Math.atan2(-y, -x);
			//angle between (x, y) -> tangent and (x, y) -> circle center
			double angleDiff = Math.atan(r/dist);
			CDeliveringCarcinogen.Point[] ret = new CDeliveringCarcinogen.Point[2];
			for(int i = 0; i<=1; i++) {
				for(int j = -1; j<=1; j += 2) {
					CDeliveringCarcinogen.Point cur = CDeliveringCarcinogen.Point.fromPolar(x, y, dist, i*Math.PI+angle+j*angleDiff);
					if(Math.abs(cur.hypot()-r)<eps) {
						if(ret[0]==null) {
							ret[0] = cur;
						}else {
							ret[1] = cur;
							return ret;
						}
					}
				}
			}
			throw new AssertionError();
		}

		public double extra(CDeliveringCarcinogen.Point a, CDeliveringCarcinogen.Point b) {
			double theta1 = Math.atan2(a.y, a.x), theta2 = Math.atan2(b.y, b.x);
			if(theta1>theta2) {
				double tmp = theta1;
				theta1 = theta2;
				theta2 = tmp;
			}
			double diff = Math.min(theta2-theta1, theta1+2*Math.PI-theta2);
			return diff*r;
		}

		public boolean valid(double t) {
			CDeliveringCarcinogen.Point newPos = CDeliveringCarcinogen.Point.fromPolar(rp, thetap+vp*t);
			double nx = newPos.x, ny = newPos.y;
			double dist = Math.hypot(x-nx, y-ny);
			if(lineSegmentCircleIntersects(x, y, nx, ny, r)) {
				dist = linf;
				for(CDeliveringCarcinogen.Point a: pointCircleTangents(x, y, r)) {
					for(CDeliveringCarcinogen.Point b: pointCircleTangents(nx, ny, r)) {
						dist = Math.min(dist, Math.hypot(x-a.x, y-a.y)+Math.hypot(nx-b.x, ny-b.y)+extra(a, b));
					}
				}
			}
			return dist/v<=t;
		}

		public void solve(int kase, InputReader in, Output pw) {
			xp = in.nextInt();
			yp = in.nextInt();
			vp = in.nextInt();
			rp = Math.hypot(xp, yp);
			thetap = Math.atan2(yp, xp);
			lengthp = 2*Math.PI*rp;
			vp = 2*Math.PI*vp/lengthp;
			x = in.nextInt();
			y = in.nextInt();
			v = in.nextInt();
			r = in.nextInt();
			double l = 0, r = linf;
			while(r-l>eps) {
				double mid = (l+r)/2;
				if(valid(mid)) {
					r = mid;
				}else {
					l = mid;
				}
			}
			pw.printf("%.6f\n", l);
		}

		static class Point {
			double x;
			double y;

			public Point(double x, double y) {
				this.x = x;
				this.y = y;
			}

			public double hypot() {
				return Math.hypot(x, y);
			}

			public static CDeliveringCarcinogen.Point fromPolar(double r, double theta) {
				return new CDeliveringCarcinogen.Point(r*Math.cos(theta), r*Math.sin(theta));
			}

			public static CDeliveringCarcinogen.Point fromPolar(double x, double y, double r, double theta) {
				return new CDeliveringCarcinogen.Point(x+r*Math.cos(theta), y+r*Math.sin(theta));
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

		public void print(String s) {
			sb.append(s);
			if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
		}

		public void printf(String s, Object... o) {
			print(String.format(s, o));
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
		private final DataInputStream din;
		private final byte[] buffer;
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

	interface InputReader {
		int nextInt();

	}
}

