import java.io.*;

import javax.swing.*;

import java.awt.*;

/**
 * Assemble all the classes presenting in this project in order to translate
 * assembly languages into Binary Machine Language. There are three instruction
 * symbols to be translated if found within the .hack instruction file,
 * Predefined symbols, Label symbols, and Variable symbols.
 * 
 * @author Ethan Huynh
 *
 */
public class Assembler {
	/**
	 * Assemble the instances of classes in main. Creates a Key-Value table to
	 * contain symbols. Add predefined symbols into the table. The program then
	 * reads a list of files in ./File directory. Translate the files and output
	 * each file into ./outputs directory.
	 * 
	 * @param args
	 */
	static JTextArea output = new JTextArea();

	public static void main(String[] args) {
		JFrame frame = new JFrame("Convert to Binary");
		frame.setLayout(new GridBagLayout());
		GridBagConstraints t = new GridBagConstraints();
		t.insets = new Insets(5, 5, 5, 5);
		Table table = new Table();
		table.addEntry("SP", 0);
		table.addEntry("LCL", 1);
		table.addEntry("ARG", 2);
		table.addEntry("THIS", 3);
		table.addEntry("THAT", 4);
		table.addEntry("R0", 0);
		table.addEntry("R1", 1);
		table.addEntry("R2", 2);
		table.addEntry("R3", 3);
		table.addEntry("R4", 4);
		table.addEntry("R5", 5);
		table.addEntry("R6", 6);
		table.addEntry("R7", 7);
		table.addEntry("R8", 8);
		table.addEntry("R9", 9);
		table.addEntry("R10", 10);
		table.addEntry("R11", 11);
		table.addEntry("R12", 12);
		table.addEntry("R13", 13);
		table.addEntry("R14", 14);
		table.addEntry("R15", 15);
		table.addEntry("SCREEN", 16384);
		table.addEntry("KBD", 24576);
		// File folder = new File("./Files");
		// File[] files = folder.listFiles();
		JTextField directory = new JTextField();
		// directory.setPreferredSize(new Dimension(300,30));
		t.gridx = 0;
		t.gridy = 1;
		t.gridwidth = 3;
		t.ipadx = 600;
		frame.add(directory, t);
		JButton convert = new JButton("Convert Files");
		convert.addActionListener((e) -> {
			String s = "";
			/*
			 * for (int i = 0; i < files.length; i++) {
			 * 
			 * if (files[i].isFile()) { s+="File " + files[i].getName()+"\n";
			 * output.setText(s); firstPass(files[i], table); try { convert(files[i],
			 * table);// 2nd pass } catch (AddressNotFoundException x) {
			 * System.out.println(x.getMessage()); } } else if (files[i].isDirectory()) {
			 * System.out.println("Directory " + files[i].getName()); } }
			 */
			try {
				firstPass(new File(directory.getText()), table);
				convert(new File(directory.getText()), table);// 2nd pass
				s += "File Converted to: " +"\n"+ directory.getText();
				output.setText(s);
			} catch (AddressNotFoundException x) {
				JOptionPane.showMessageDialog(null, x.getMessage());
			} catch (FileNotFoundException fe) {
				JOptionPane.showMessageDialog(null, "File does not exist.");
			}
		});
		t.gridx = 2;
		t.gridy = 0;
		t.gridwidth = 1;
		t.ipadx = 0;
		frame.add(convert, t);

		JButton sel = new JButton("Browse");
		sel.addActionListener((e) -> {
			if (e.getSource() == sel) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./"));
				int returnVal = fc.showOpenDialog(sel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// This is where a real application would open the file.
					directory.setText(file.getPath());
				} else {
					directory.setText("Open command cancelled by user.");
				}
				output.setText("");
			}
		});
		t.gridx = 5;
		t.gridy = 1;
		t.gridwidth = 1;
		t.ipadx = 0;
		frame.add(sel, t);
		//t.ipady = 40;
		t.gridx = 0;
		t.gridy = 3;
		t.gridwidth = 3;
		// output.setPreferredSize(new Dimension(500,200));
		
		JScrollPane sp = new JScrollPane(output);
		sp.setPreferredSize(new Dimension(500,50));
		frame.add(sp, t);
		frame.setVisible(true);
		// frame.setSize(new Dimension(500, 500));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Creates a first pass through the .asm file to collect Label symbols. Add the
	 * collected Label symbols into the Key-Value table.
	 * 
	 * @param file
	 * @param table
	 * @throws FileNotFoundException
	 */
	public static void firstPass(File file, Table table) throws FileNotFoundException {
		Parse parse = new Parse(file);
		while (parse.hasMoreCommands()) {
			parse.advance();
			if (parse.commandType() != Command.L_COMMAND) {
				parse.increNumber();
			} else if (parse.commandType() == Command.L_COMMAND) {
				table.addEntry(parse.symbol(), parse.getLineNumber());
			}
		}
	}

	/**
	 * Creates a second pass through the .as, file to collect and add Variable
	 * symbols into the Key-Value table as well as translating the Assembly codes
	 * into binary-form of the Binary Machine Language.
	 * 
	 * @param file
	 * @param table
	 * @throws AddressNotFoundException
	 */
	public static void convert(File file, Table table) throws AddressNotFoundException {
		String output = "";
		Code code = new Code();
		int varLocation = 16;
		try {
			Parse parse = new Parse(file);
			while (parse.hasMoreCommands()) {
				parse.advance();
				if (parse.commandType() == Command.L_COMMAND) {
					continue;
				} else if (parse.commandType() == Command.A_COMMAND) {
					if (table.contains(parse.symbol())) {
						int add = table.getAddress(parse.symbol());
						if (add == -1) {
							throw new AddressNotFoundException("Could not find the symbol's address" + parse.symbol());
						}
						String r = Integer.toBinaryString(add);
						String a = "";
						for (int u = r.length(); u < 16; u++) {
							a += 0;
						}
						output += a + r + "\n";
					} else {
						try {
							int toParse = Integer.parseInt(parse.symbol());// needs modification
																			// for 2nd pass
							String r = Integer.toBinaryString(toParse);
							String a = "";
							for (int u = r.length(); u < 16; u++) {
								a += 0;
							}
							output += a + r + "\n";
						} catch (NumberFormatException x) {
							table.addEntry(parse.symbol(), varLocation);
							String r = Integer.toBinaryString(varLocation);
							String a = "";
							for (int u = r.length(); u < 16; u++) {
								a += 0;
							}
							output += a + r + "\n";
							varLocation++;
						}
					}
				} else if (parse.commandType() == Command.C_COMMAND) {
					output += "111" + code.comp(parse.comp()) + code.dest(parse.dest()) + code.jump(parse.jump())
							+ "\n";
				}
			}
			try {
				int index = file.getName().lastIndexOf(".");
				BufferedWriter print = new BufferedWriter(
						new FileWriter("./Outputs/" + file.getName().substring(0, index) + ".hack"));
				print.write(output);
				print.close();
			} catch (IOException x) {
				System.out.println(x.getMessage());
			}
		} catch (FileNotFoundException x) {
			System.out.println(x.getMessage());
		}
	}
}
