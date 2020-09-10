import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.InputMismatchException;

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
			FElementary solver = new FElementary();
			solver.solve(1, in, out);
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(null, new TaskAdapter(), "", 1<<28);
		thread.start();
		thread.join();
	}
	static class FElementary {
		HashSet<String> valid;
		String s;

		public FElementary() {
		}

		public boolean solve(int x) {
			if(x==s.length()) {
				return true;
			}
			boolean ans = false;
			for(int i = x+1; i<=s.length(); i++) {
				if(valid.contains(s.substring(x, i))) {
					ans |= solve(i);
				}
			}
			return ans;
		}

		public void solve(int kase, InputReader in, Output pw) {
			s = in.next(10);
			String[] elements = ("H - Hydrogen\n"+
					"He - Helium\n"+
					"Li - Lithium\n"+
					"Be - Beryllium\n"+
					"B - Boron\n"+
					"C - Carbon\n"+
					"N - Nitrogen\n"+
					"O - Oxygen\n"+
					"F - Fluorine\n"+
					"Ne - Neon\n"+
					"Na - Sodium\n"+
					"Mg - Magnesium\n"+
					"Al - Aluminum, Aluminium\n"+
					"Si - Silicon\n"+
					"P - Phosphorus\n"+
					"S - Sulfur\n"+
					"Cl - Chlorine\n"+
					"Ar - Argon\n"+
					"K - Potassium\n"+
					"Ca - Calcium\n"+
					"Sc - Scandium\n"+
					"Ti - Titanium\n"+
					"V - Vanadium\n"+
					"Cr - Chromium\n"+
					"Mn - Manganese\n"+
					"Fe - Iron\n"+
					"Co - Cobalt\n"+
					"Ni - Nickel\n"+
					"Cu - Copper\n"+
					"Zn - Zinc\n"+
					"Ga - Gallium\n"+
					"Ge - Germanium\n"+
					"As - Arsenic\n"+
					"Se - Selenium\n"+
					"Br - Bromine\n"+
					"Kr - Krypton\n"+
					"Rb - Rubidium\n"+
					"Sr - Strontium\n"+
					"Y - Yttrium\n"+
					"Zr - Zirconium\n"+
					"Nb - Niobium\n"+
					"Mo - Molybdenum\n"+
					"Tc - Technetium\n"+
					"Ru - Ruthenium\n"+
					"Rh - Rhodium\n"+
					"Pd - Palladium\n"+
					"Ag - Silver\n"+
					"Cd - Cadmium\n"+
					"In - Indium\n"+
					"Sn - Tin\n"+
					"Sb - Antimony\n"+
					"Te - Tellurium\n"+
					"I - Iodine\n"+
					"Xe - Xenon\n"+
					"Cs - Cesium\n"+
					"Ba - Barium\n"+
					"La - Lanthanum\n"+
					"Ce - Cerium\n"+
					"Pr - Praseodymium\n"+
					"Nd - Neodymium\n"+
					"Pm - Promethium\n"+
					"Sm - Samarium\n"+
					"Eu - Europium\n"+
					"Gd - Gadolinium\n"+
					"Tb - Terbium\n"+
					"Dy - Dysprosium\n"+
					"Ho - Holmium\n"+
					"Er - Erbium\n"+
					"Tm - Thulium\n"+
					"Yb - Ytterbium\n"+
					"Lu - Lutetium\n"+
					"Hf - Hafnium\n"+
					"Ta - Tantalum\n"+
					"W - Tungsten\n"+
					"Re - Rhenium\n"+
					"Os - Osmium\n"+
					"Ir - Iridium\n"+
					"Pt - Platinum\n"+
					"Au - Gold\n"+
					"Hg - Mercury\n"+
					"Tl - Thallium\n"+
					"Pb - Lead\n"+
					"Bi - Bismuth\n"+
					"Po - Polonium\n"+
					"At - Astatine\n"+
					"Rn - Radon\n"+
					"Fr - Francium\n"+
					"Ra - Radium\n"+
					"Ac - Actinium\n"+
					"Th - Thorium\n"+
					"Pa - Protactinium\n"+
					"U - Uranium\n"+
					"Np - Neptunium\n"+
					"Pu - Plutonium\n"+
					"Am - Americium\n"+
					"Cm - Curium\n"+
					"Bk - Berkelium\n"+
					"Cf - Californium\n"+
					"Es - Einsteinium\n"+
					"Fm - Fermium\n"+
					"Md - Mendelevium\n"+
					"No - Nobelium\n"+
					"Lr - Lawrencium\n"+
					"Rf - Rutherfordium\n"+
					"Db - Dubnium\n"+
					"Sg - Seaborgium\n"+
					"Bh - Bohrium\n"+
					"Hs - Hassium\n"+
					"Mt - Meitnerium\n"+
					"Ds - Darmstadtium\n"+
					"Rg - Roentgenium\n"+
					"Cn - Copernicium\n"+
					"Nh - Nihonium\n"+
					"Fl - Flerovium\n"+
					"Mc - Moscovium\n"+
					"Lv - Livermorium\n"+
					"Ts - Tennessine\n"+
					"Og - Oganesson").split("\n");
			valid = new HashSet<>();
			for(String s: elements) {
				valid.add(s.split(" - ")[0].toUpperCase());
			}
			if(solve(0)) {
				pw.println("YES");
			}else {
				pw.println("NO");
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

		public String next() {
			StringBuilder ret = new StringBuilder(64);
			byte c = skip();
			while(c!=-1&&!isSpaceChar(c)) {
				ret.appendCodePoint(c);
				c = read();
			}
			return ret.toString();
		}

		public String next(int maxLength) {
			byte[] ret = new byte[maxLength];
			byte c = skip();
			int ind = 0;
			while(c!=-1&&!isSpaceChar(c)) {
				ret[ind++] = c;
				c = read();
			}
			return new String(ret, 0, ind);
		}

		private boolean isSpaceChar(byte b) {
			return b==' '||b=='\r'||b=='\n'||b=='\t'||b=='\f';
		}

		private byte skip() {
			byte ret;
			while(isSpaceChar((ret = read()))) ;
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
		public boolean autoFlush;
		public String lineSeparator;

		public Output(OutputStream os) {
			this(os, 1<<16);
		}

		public Output(OutputStream os, int bs) {
			BUFFER_SIZE = bs;
			sb = new StringBuilder(BUFFER_SIZE);
			this.os = new BufferedOutputStream(os, 1<<17);
			autoFlush = false;
			lineSeparator = System.lineSeparator();
		}

		public void println(String s) {
			sb.append(s);
			println();
			if(autoFlush) {
				flush();
			}else if(sb.length()>BUFFER_SIZE >> 1) {
				flushToBuffer();
			}
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

	static interface InputReader {
		String next();

		default String next(int maxLength) {
			return next();
		}

	}
}


