package CapitalLetters;

import AuxiliarFiles.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * 1. ler a pasta com liricas (origem) 
 * 2.1. faz contagem de palavras come�adas por maiuscula, execpto a 1� palavra de cada linha
 * 2.2. faz contagem de palavras todas em maiusculas
 * 3. guarda o output em ficheiro output (M45-capitalLetters.txt)
 * 
 * @author rsmal
 * @date
 */
public class CapitalLetters_Initial {

	String sourceFolder; // pasta onde estao as liricas
	static String outputFolder = "src/Output/";
	String outputFile;
	
	public static void main(String[] args) throws ClassNotFoundException,
	IOException {
		CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(true,"src/Origem/L001-141.txt",null);
	}
	
	
	public CapitalLetters_Initial(boolean onlyOneFile, String input, String outputFile) throws ClassNotFoundException, IOException{	
		if(input != null && !input.isEmpty()) {
			this.sourceFolder = input;				
		}
		else {
			this.sourceFolder = "src/Origem/";
		}
		
		if(outputFile != null && !outputFile.isEmpty()) {
			this.outputFile = outputFile;			
		}
		else {
			this.outputFile = outputFolder;
		}

		// read the names of the files from a folder and save them into a
		// String[] (files)
		int numberFiles = 0;
		String [] files = null;
		if (!onlyOneFile) {
			ReadOperations ro = new ReadOperations();
			files = ro.openDirectory(sourceFolder);
			numberFiles = ro.filesLength(files);			
		}
		else {
			files = new String [] {input};
			numberFiles = 1;
		}

		String[][] matrix = new String[numberFiles][3];

		// for each file...
		for (int i = 0; i < numberFiles; i++) {
			String[] data = files[i].split("\\.");
			if (data[0].contains("/")) {
				String [] nome = data[0].split("/");
				data[0] = nome[nome.length-1];
			}
			matrix[i][0] = data[0];

			System.out.println("Ficheiro - "+i);
			
			
			// location of the file
			String filename_path = new String();
			if (onlyOneFile) {
				filename_path = input;
			}
			else {
				filename_path = sourceFolder + "/" + files[i];
			}

			boolean fcl = false;
			boolean acl = false;
			int cfcl = 0;
			int cacl = 0;

			// open the file for reading
			FileReader fileReader = new FileReader(filename_path);
			BufferedReader in = new BufferedReader(fileReader);
			String thisLine;

			//CapitalLetters_Initial ini = new CapitalLetters_Initial();
			while ((thisLine = in.readLine()) != null) {
				// Ignore empty lines.
				if (thisLine.equals(""))
					continue;

				thisLine = thisLine.replace("\'", "");
				// remover punctuation marks
				thisLine = thisLine.replaceAll("[^a-zA-Z0-9-]", " ");
				
				//System.out.println("AAAA --> "+thisLine);

				String[] words = thisLine.split(" ");

				int numberWordsPerLine = words.length;

				for (int j = 0; j < numberWordsPerLine; j++) {

					if (words[j].equals("")) {
						continue;
					}
					fcl = this.firstCapitalLetter(words[j]);
					acl = this.allCapitalLetters(words[j]);

					if (fcl) {
						cfcl++;
					}

					if (acl) {
						cacl++;
					}

				}

			} // end while
			matrix[i][1] = Integer.toString(cfcl);
			matrix[i][2] = Integer.toString(cacl);
			in.close();
		} // end for
		WriteOperations wo = new WriteOperations();
		wo.writeMatrixInConsole(matrix);
		wo.writeMatrixInFile(matrix, this.outputFile + "_with_POStags.csv",2); //enviar option para imprimir o header para Capital Letters = 2

	}


	public boolean firstCapitalLetter(String word) {
		if (Character.isLowerCase(word.charAt(0))
				|| (Character.isDigit(word.charAt(0))) || (word.length() == 1)) {
			return false;
		} else {
			for (int i = 1; i < word.length(); i++) {
				if (Character.isUpperCase(word.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean allCapitalLetters(String word) {
		int cons = 0;
		for (int i = 0; i < word.length(); i++) {
			if (Character.isLowerCase(word.charAt(i)) || (word.length() == 1)) {
				return false;
			} else if (!Character.isDigit(word.charAt(i))) {
				cons++;
			}
		}
		if (cons != 0) {
			return true;
		} else
			return false;

	}

}
