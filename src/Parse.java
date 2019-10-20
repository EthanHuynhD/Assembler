import java.util.*;
import java.io.*;

/**
 * Reads input files and creates methods to traverse the file to aid the
 * translation process.
 * 
 * @author Ethan Huynh
 * 
 */
public class Parse {
	private Scanner in;
	private String current;
	private int lineNumber;

	/**
	 * Constructs a Parse instance to read a file with the handling of
	 * FileNotFoundException. Declares the current line number which is 0.
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public Parse(File file) throws FileNotFoundException {
		in = new Scanner(file);
		lineNumber = 0;
	}

	/**
	 * Check whether if there is any more command to be read. Closes Scanner stream
	 * if there is no more line/command.
	 * 
	 * @return a boolean value
	 */
	public boolean hasMoreCommands() {
		if (!in.hasNextLine()) {
			in.close();
			return false;
		} else {
			return in.hasNextLine();
		}
	}

	/**
	 * Advance to next line by making the next line a current line. filter out empty
	 * lines, white spaces, and comments.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void advance() throws IllegalArgumentException {
		String s = in.nextLine();
		while (s.trim().isEmpty()) {
			s = in.nextLine();
		}
		String r = "";
		int i = 0;
		int oc = 0;
		while (i < s.length() && oc != 2) {
			if (s.charAt(i) != ('/') && oc == 1) {
				throw new IllegalArgumentException("There is no / operation");
			} else if ((s.charAt(i) == ('/'))) {
				oc++;
			} else if (s.charAt(i) != ('/')) {
				if (s.charAt(i) != ' ') {
					r += s.charAt(i);
				}
			}
			i++;
		}
		current = r;
		if (current.trim().isEmpty()) {
			advance();
		}
	}

	/**
	 * Increment lineNumber
	 */
	public void increNumber() {
		lineNumber++;
	}

	/**
	 * Get current lineNumber
	 * 
	 * @return a line number
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Decides the command type of an instruction.
	 * 
	 * @return the command type
	 */
	public Command commandType() {
		if (current.charAt(0) == '(') {
			return Command.L_COMMAND; // pseudo-command (Xxx)
		} else if (current.charAt(0) == '@') {
			return Command.A_COMMAND;
		} else {
			return Command.C_COMMAND;
		}
	}

	/**
	 * Precondition: only for A or L command Filter out spaces, @, and () in an
	 * instruction and return a symbol.
	 * 
	 * @return a symbol
	 */
	public String symbol() {
		return current.replaceAll("[()@ ]", "");
	}

	/**
	 * Produce a mnemonic/symbol of destination instruction.
	 * 
	 * @return a mnemonic/symbol of the dest instruction.
	 */
	public String dest() {
		String r = "";
		String[] s;
		s = current.split("=");
		if (s.length == 2) {
			r = s[0];
		} else {
			r = "null";
		}
		return r;
	}
	/**
	 * Produce a mnemonic/symbol of computation bits
	 * @return a mnemonic/symbol of the comp instruction.
	 */
	public String comp() {
		String r = "";
		String[] s;
		String[] t;
		s = current.split(";");
		t = s[0].split("=");
		if (t.length == 1) {
			r = t[0];
		} else {
			r = t[1];
		}
		return r;
	}
	/**
	 * Produce a mnemonic/symbol of jump bits
	 * @return a mnemonic/symbol of the jump instruction.
	 */
	public String jump() {
		String r = "";
		String[] s;
		// if (commandType() == Command.C_COMMAND) { // <--to be used later
		s = current.split(";");
		if (s.length == 2) {
			r = s[1];
		} else {
			r = "null";
		}
		// }
		return r;
	}

	/*
	 * public static void main(String[] args) { String r = "hell=omyna;meis";
	 * String[] s = r.split("=|\\;"); if (s.length == 3) { r = s[2]; } else { r =
	 * "000"; } String t = r; System.out.println(t);
	 * 
	 * t = "     "; System.out.println(t.trim().isEmpty()); t = "  sd  as2300aa ";
	 * int i = 0; r = ""; int oc = 0; while (i < t.length() && oc != 2) { if
	 * (t.charAt(i) != ('/') && oc == 1) { throw new
	 * IllegalArgumentException("There is no / operation");
	 * 
	 * } else if ((t.charAt(i) == ('/'))) { oc++; } else if (t.charAt(i) != ('/')) {
	 * if (t.charAt(i) != ' ') { r += t.charAt(i); } } i++; }
	 * System.out.println(r.trim()); int toParse =
	 * Integer.parseInt(r.replaceAll("[^0-9?!\\.]", "")); r =
	 * Integer.toBinaryString(toParse); String k = "@asd"; k =
	 * k.replaceAll("[()@ ]", ""); int l = Integer.parseInt(k); String n =
	 * Integer.toBinaryString(l); String a = ""; for (int u = n.length(); u < 16;
	 * u++) { a += 0; } System.out.println(a + n + "\n"); }
	 */
}
