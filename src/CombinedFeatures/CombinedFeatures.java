package CombinedFeatures;

import AuxiliarFiles.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 DAL
 comment w=word, ee=pleasantness, aa=activation, ii=imagery
 comment scores for pleasantness range from 1 (unpleasant) to 3 (pleasant)
 scores for activation range from 1 (passive) to 3 (active)
 scores for imagery range from 1 (difficult to form a meantal
 picture of this word) to 3 (easy to form a mental picture)
 comment pleasantness mean=1.84, sd=.44
 activation mean=1.85, sd=.39
 imagery (does word give you a clear mental picture) mean=1.94, sd=.63
 comment these values have been tested on 348,000 words of natural language
 the dictionary has a 90% matching rate for this corpus
 mean ee is 1.85, with an sd of .36
 mean aa is 1.67, with an sd of .36
 mean ii is 1.52, with an sd of .63
 */

/**
 * 1. ler a pasta com liricas <pasta origem>
 *  2. para cada file da pasta origem separar linha a linha 
 *  3. para cada linha, percorrer o file de anew-rsmal.txt ou dal-rsmal.txt (ou outro que insira) e linha a linha destem verificar se 
 *  a linha da lirica contem as linhas do file selecionado (ex: anew-rsmal.txt) 
 *  4. contar as ocorrencias e mandar para ficheiro o relatorio. 
 *  Esse ficheiro ser� anew-rsmal.txtInorigem.txt ou dal-rsmal.txtInorigem.txt conforme o caso.
 * O sistema cria ainda um relat�rio detalhado que armazena no ficheiro outputDetails.txt
 * 
 * @author rsmal
 * @date
 */

public class CombinedFeatures {
	boolean gazeteersFeatures;
	boolean DAL_ANEWFeatures;
	boolean WordsDictionaryFeatures;
	
	static final String originFolder = "src/AuxiliarFiles/";
	
	// gazeteers
	static final String Gazeteer_Q1Q2Q3Q4_dal = "Gazeteers.txt";

	//static final String gazeteerFolder = "src/Gazeteers/GazeteersFiles/";
	static final String gazeteerFile = originFolder + Gazeteer_Q1Q2Q3Q4_dal;
	
	// DAL_ANEW

	static final String dicFile1 = "DAL_ANEW.txt";
	//static final String dicFile2 = "anew-rsmal.txt";
	static String sourceFolder = "src/Origem";
	//static final String dalAnewFolder = "src/DAL_ANEW/DAL_ANEWFiles/";
	
	//Words Dictionary
	
	static final String wordsDictionaryFile = "src/AuxiliarFiles/slang.txt";
	
	
	static final String outputFolder = "src/Output/";
	
	static final String dicFile = originFolder + dicFile1;
	static String str = dicFile + " into " + sourceFolder + " - Details";
	
	String outputFile;

	public static void main(String[] args) throws ClassNotFoundException, IOException  {
		//CombinedFeatures initial_anew  = new CombinedFeatures(false,false, false,true,null);
		CombinedFeatures initial_anew  = new CombinedFeatures(true,false, false,true,"src/Origem/L002-157.txt","output.csv");
		// por default vamos buscar as DAL_ANEW
	}
	public CombinedFeatures(boolean onlyOneFile, boolean gazeteersFeatures, boolean DAL_ANEWFeatures, boolean WordsDictionaryFeatures, String input, String outputFile) throws ClassNotFoundException, IOException{	

		// read the names of the files (lyrics) from a folder of lyrics and save
		// them into a
		// String[] (files)
		if(input != null && !input.isEmpty()) {
			sourceFolder = input;				
		}
		else {
			sourceFolder = "src/Origem/";
		}
		
		str = dicFile + " into " + sourceFolder + " - Details";
		
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

		// ArrayList<String> listOfLinesFromTextFile = new ArrayList<String>();
		// listOfLinesFromTextFile = ro.openTxtFile(dicFile); //the lines of dal
		// or anew are in an arraylist
		String[][] matrix = new String[numberFiles][4];

		// ArrayList <String> outputDetails = new ArrayList<String>();
		// write details in a specific file
		String outputDetails = "";
		WriteOperations woDetails = new WriteOperations();
		outputDetails = woDetails.writeLinesInList(outputDetails, str);
		outputDetails = woDetails.writeLinesInList(outputDetails, "\n");

		// for each file (lyric)...
		for (int i = 0; i < numberFiles; i++) {

			double valenceFileValue = 0;
			double arousalFileValue = 0;
			
			double averageValenceFileValue = 0;
			double averageArousalFileValue = 0;
			
			int countFileValue = 0;
			
			// DAL_ANEW features especificas
			double dominanceFileValue = 0;
			double averageDominanceFileValue = 0;
			
			// Words Dictionary 
			int count = 0;
			

			System.out.println("File Number: "+i);
			
			
			String[] data = files[i].split("\\.");
			if (data[0].contains("/")) {
				String [] nome = data[0].split("/");
				data[0] = nome[nome.length-1];
			}	
			matrix[i][0] = data[0];

			// location of the file
			String filename_path = new String();
			if (onlyOneFile) {
				filename_path = input;
			}
			else {
				filename_path = sourceFolder + "/" + files[i];
			}
			outputDetails = woDetails.writeLinesInList(outputDetails, "\n");
			outputDetails = woDetails.writeLinesInList(outputDetails,
					"###############################################");
			outputDetails = woDetails.writeLinesInList(outputDetails,
					filename_path);
			outputDetails = woDetails.writeLinesInList(outputDetails,
					"###############################################");

			
			
			// open the file (lyric) for reading
			FileReader fileReader = new FileReader(filename_path);
			BufferedReader in = new BufferedReader(fileReader);
			String thisLine;

			// for each line of the lyric...
			while ((thisLine = in.readLine()) != null) {

				double valenceLineValue = 0;
				double arousalLineValue = 0;
				double averageValenceLineValue = 0;
				double averageArousalLineValue = 0;
				int countLineValue = 0;
				
				// DAL_ANEW features especificas
				double dominanceLineValue = 0;
				double averageDominanceLineValue = 0;

				// Ignore empty lines.
				if (thisLine.equals(""))
					continue;

				// remove punctuation marks
				thisLine = thisLine.replaceAll("[^a-zA-Z0-9- ]", "");

				outputDetails = woDetails.writeLinesInList(outputDetails, "\n");
				outputDetails = woDetails.writeLinesInList(outputDetails,
						"--------------------------------------------");
				outputDetails = woDetails.writeLinesInList(outputDetails,
						"Linha -> " + thisLine);
				outputDetails = woDetails.writeLinesInList(outputDetails,
						"--------------------------------------------");

				
				if (WordsDictionaryFeatures) {
					thisLine = " " + thisLine + " ";
					FileReader dict = new FileReader(wordsDictionaryFile);
					BufferedReader in2 = new BufferedReader(dict);
					String dictLine;
					
					while ((dictLine = in2.readLine()) != null) {
						// if (dictLine.equals(""))
						// continue;

						dictLine = " " + dictLine + " ";
						int index = thisLine.indexOf(dictLine);

						while (index >= 0) {
							index++;
							count++;
							String str2 = dictLine;
							outputDetails = woDetails.writeLinesInList(
									outputDetails, str2);
							index = thisLine.indexOf(dictLine, index);

						}

					}
				}
				else {
					String[] dataLineLyric = thisLine.split(" ");
	
					// for each word of the line of the lyric
					for (int k = 0; k < dataLineLyric.length; k++) {
						FileReader dict = null;
						// open the dict for reading
						if (DAL_ANEWFeatures) {
							dict = new FileReader(dicFile);
						}
						else if (gazeteersFeatures) {
							dict = new FileReader(gazeteerFile);
						}
						else if (WordsDictionaryFeatures) {
							dict = new FileReader(wordsDictionaryFile);
						}
						
						BufferedReader in2 = new BufferedReader(dict);
						String dictLine;
						
						while ((dictLine = in2.readLine()) != null) {					
							String[] dataDict = dictLine.split("\t");
	
							// compare each word of the lyric to each word of the
							String linha = dataLineLyric[k];
							
							if (gazeteersFeatures) {
								linha = linha.toLowerCase();
							}
							
							if (linha.equals(dataDict[0])) {
								valenceFileValue = valenceFileValue
										+ Double.parseDouble(dataDict[1]);
								arousalFileValue = arousalFileValue
										+ Double.parseDouble(dataDict[2]);
								if (DAL_ANEWFeatures) {
									dominanceFileValue = dominanceFileValue
											+ Double.parseDouble(dataDict[3]);
								}
	
								valenceLineValue = valenceLineValue
										+ Double.parseDouble(dataDict[1]);
								arousalLineValue = arousalLineValue
										+ Double.parseDouble(dataDict[2]);
								if (DAL_ANEWFeatures) {
									dominanceLineValue = dominanceLineValue
											+ Double.parseDouble(dataDict[3]);
								}
								countFileValue++;
								countLineValue++;
								String write=	dataDict[0] + " "
										+ dataDict[1] + " " + dataDict[2];
								if (DAL_ANEWFeatures) {
									write.concat(" " + dataDict[3]);
								}
								outputDetails = woDetails.writeLinesInList(
										outputDetails,write);								
							}
						}
					}// end for
				} 
				if (!WordsDictionaryFeatures) {
					averageValenceLineValue = valenceLineValue / countLineValue;
					averageArousalLineValue = arousalLineValue / countLineValue;
					if (DAL_ANEWFeatures) {
						averageDominanceLineValue = dominanceLineValue / countLineValue;
					}
					
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"\n");
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"\n Valence Line " + averageValenceLineValue);
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"Arousal Line " + averageArousalLineValue);
					if (DAL_ANEWFeatures) {
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"Dominance Line " + averageDominanceLineValue);
					}
				}

			} // end while
			if (!WordsDictionaryFeatures) {
				averageValenceFileValue = valenceFileValue / countFileValue;
				averageArousalFileValue = arousalFileValue / countFileValue;
				if (DAL_ANEWFeatures) {
					averageDominanceFileValue = dominanceFileValue / countFileValue;
				}
				
				outputDetails = woDetails.writeLinesInList(outputDetails,
						"\n\n");
				outputDetails = woDetails.writeLinesInList(outputDetails,
						"Valence File " + averageValenceFileValue);
				outputDetails = woDetails.writeLinesInList(outputDetails,
						"Arousal File " + averageArousalFileValue);
				outputDetails = woDetails.writeLinesInList(outputDetails,
						"Dominance File " + averageDominanceFileValue);
				
				matrix[i][1] = Double.toString(averageValenceFileValue);
				matrix[i][2] = Double.toString(averageArousalFileValue);
				if (DAL_ANEWFeatures) {
					matrix[i][3] = Double.toString(averageDominanceFileValue);
				}
			}
			else {
				matrix[i][1] = Integer.toString(count);
			}
			in.close();

		} // end for
			// ConvertToTFIDF ct = new ConvertToTFIDF();
			// ct.convertTfidf(matrix);

		
		WriteOperations wo = new WriteOperations();
		wo.writeMatrixInConsole(matrix);
		
		if(outputFile != null && !outputFile.isEmpty()) {
			this.outputFile = outputFile;			
		}
		else {
			if (gazeteersFeatures) {
				this.outputFile = outputFolder + "Gazeteers";
			}
			if (DAL_ANEWFeatures) {
				this.outputFile = outputFolder + "DAL_ANEW";
			}
			if (WordsDictionaryFeatures) {
				this.outputFile = outputFolder + "Slang";
			}
		}
		
	
		wo.writeMatrixInFile2(matrix, this.outputFile,WordsDictionaryFeatures,DAL_ANEWFeatures);
		//wo.writeFile(outputFolder + "Combined_Features_outputDetails.txt", outputDetails);
	}

}
