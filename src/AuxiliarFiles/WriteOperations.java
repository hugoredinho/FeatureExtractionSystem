/**
 * 
 */
package AuxiliarFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author rsmal
 * 
 */
public class WriteOperations {

	public void writeFileArray(String filename, ArrayList<String> filecontent)
			throws IOException {
		File file = new File("filename");

		boolean isFile = file.exists();

		// se o ficheiro nao existir cria um novo, caso contrario
		// escreve por cima
		if (!isFile) {
			file.createNewFile();
		}

		// abre o ficheiro anterior para escrita
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < filecontent.size(); i++) {
			out.write(filecontent.get(i));
			out.newLine();
		}
		out.close();
	}

	public void writeFile(String filename, String filecontent)
			throws IOException {
		File file = new File(filename);

		// System.out.println("filename## " + filename);

		boolean isFile = file.exists();

		// se o ficheiro nao existir cria um novo, caso contrario
		// escreve por cima
		if (!isFile) {
			file.createNewFile();
		}

		// abre o ficheiro anterior para escrita
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		String[] data;
		data = filecontent.split("\n");

		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
			out.write(data[i]);
			out.newLine();
		}
		out.close();

	}

	/**
	 * escreve a matriz criada no ecra
	 */
	public void writeMatrixInConsole(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != null && !matrix[i][j].isEmpty()) {
					System.out.print(matrix[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * escreve a matriz num file .txt
	 * 
	 * @throws IOException
	 */
	public void writeMatrixInFile(String[][] matrix, String filename, int option)
			throws IOException {
		if (!filename.endsWith(".csv")) {
			filename = filename + ".csv";
		}
		File file = new File(filename);
		System.out.println(file.getAbsolutePath());
		boolean isFile = file.exists();

		// se o ficheiro nao existir cria um novo, caso contrario
		// escreve por cima
		if (!isFile) {
			file.createNewFile();
		}
		// abre o ficheiro anterior para escrita
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		
		if(option==2) { 
			//Capital Letters
			out.write("Id,cfcl,cacl");
			out.newLine();
			
		}
		

		

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != null && !matrix[i][j].isEmpty()) {
					out.write(matrix[i][j]);
					if(j+2 == matrix[i].length) {
						if (matrix[i][j+1] != null) {
							out.write(",");
						}
					}
					else if (j+1 < matrix[i].length) {
						out.write(",");
					} 
				}
			}
			out.newLine();
		}
		out.close();
	}
	
	public ArrayList<String> writeLinesInList(ArrayList<String> al, String str) {		
		al.add(str);
		al.add("\n");
		return al;
	}
	
	public String writeLinesInList(String st, String str) {		
		st = st + str;
		st = st + "\n";
		return st;
	}
	/**
	 * escreve a matriz num file .csv
	 * 
	 * @throws IOException
	 */
	public void writeMatrixInFile2(String[][] matrix, String filename,boolean WordsDictionaryFeatures, boolean DAL_ANEWFeatures)
			throws IOException {

		if (!filename.endsWith(".csv")) {
			filename = filename + ".csv";
		}
		
		File file = new File(filename);
		System.out.println(file.getAbsolutePath());
		boolean isFile = file.exists();
		// create CSVWriter object filewriter object as parameter 
        

		// se o ficheiro nao existir cria um novo, caso contrario
		// escreve por cima
		if (!isFile) {
			file.createNewFile();
		}

		// abre o ficheiro anterior para escrita
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		if(WordsDictionaryFeatures) {
			out.write("Id,Count");
		}else {
			if(DAL_ANEWFeatures){		
				out.write("Id,AvgValence,AvgArousal,AvgDominance");
			}else {
				out.write("Id,AvgValence,AvgArousal");
			}
			
		}
		out.newLine();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != null && !matrix[i][j].isEmpty()) {
					out.write(matrix[i][j]);
					if(j+2 == matrix[i].length) {
						if (matrix[i][j+1] != null) {
							out.write(",");
						}
					}
					else if (j+1 < matrix[i].length) {
						out.write(",");
					} 
					//System.out.printf(" %s (%d,%d)", matrix[i][j], i, j);
				}
				System.out.printf(" %s (%d,%d)\n", matrix[i][j], i, j);
				/*if (i+1 < matrix.length && j+1 < matrix[i].length) {
					out.write(",");
				}*/
			}
			System.out.println();
			out.newLine();
		}
		out.close();
	}
	
	
}
