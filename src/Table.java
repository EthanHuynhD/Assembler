import java.util.*;
/**
 * Produces a Key-Value table that handles pre-defined symbols, Label symols, and variable symbols.
 * @author Ethan Huynh
 *
 */
public class Table {
	private HashMap<String, Integer> table;
	/**
	 * Constructs a Key-Value table using HashMap
	 */
	public Table() {
		table = new HashMap<>(); // initialize the table
	}

	/**
	 * Add a new entry into the table.
	 * @param symbol
	 * @param address
	 */
	public void addEntry(String symbol, int address) {
		table.put(symbol, address);
	}

	/**
	 * Checks whether the table contains a symbol/mnemonic.
	 * @param symbol
	 * @return a boolean value
	 */
	public boolean contains(String symbol) {
		boolean r = false;
		for (String k : table.keySet()) {
			if (k.equals(symbol)) {
				r = true;
			}
		}
		return r;

	}

	// returns address if contains symbol
	/**
	 * Look for an address of a symbol if the table contains the symbol-value combination.
	 * @param symbol
	 * @return
	 */
	public int getAddress(String symbol) {
		int v=-1;
		Iterator<Map.Entry<String, Integer>> entries = table.entrySet().iterator();
		while (entries.hasNext()&&v==-1) {
			Map.Entry<String, Integer> entry = entries.next();
			if (entry.getKey().equals(symbol)) {
				v= entry.getValue();
			}
		}
		return v;
	}
}
