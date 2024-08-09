package CombinedFeatures;
import AuxiliarFiles.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.math4.legacy.stat.descriptive.DescriptiveStatistics;
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
	int gazeteersFeatures;
	boolean dal_features;
	boolean anew_features;
	boolean WordsDictionaryFeatures;
	boolean NRCVADFeatures;
	
	static final String originFolder = "src/AuxiliarFiles/";
	
	// gazeteers
	static final String Gazeteer_Q1Q2Q3Q4_dal = "Gazeteers.txt";
	//static final String gazeteerFolder = "src/Gazeteers/GazeteersFiles/";
	String gazeteerFile = originFolder + Gazeteer_Q1Q2Q3Q4_dal;
	
	// DAL_ANEW
	static final String dicFile1 = "dal-rsmal.txt";
	static final String dicFile2 = "Warriner.txt";
	static final String dicFile3 = "anew-rsmal.txt";
	static final String dicFile4 = "NRC-VAD-Lexicon.txt";
	
	
	static String sourceFolder = "src/Origem";
	//static final String dalAnewFolder = "src/DAL_ANEW/DAL_ANEWFiles/";
	
	//Words Dictionary
	
	static final String wordsDictionaryFile = "src/AuxiliarFiles/slang.txt";
	
	
	static final String outputFolder = "src/Output/";
	
	static final String dicFile = originFolder + dicFile1;
	static String str = dicFile + " into " + sourceFolder + " - Details";
	
	
	static final String dicFileW = originFolder + dicFile2;
	static String strW = dicFileW + " into " + sourceFolder + " - Details";
	static final String dicFileA = originFolder + dicFile3;
	static String strA = dicFileA + " into " + sourceFolder + " - Details";

	static final String dicFileNRC = originFolder + dicFile4;	
	
	String outputFile;
	public static void main(String[] args) throws ClassNotFoundException, IOException  {
		CombinedFeatures initial_anew  = new CombinedFeatures(false,0, false,true, false,false, false,null, null);
		
		// por default vamos buscar as DAL_ANEW
	}
	public CombinedFeatures(boolean onlyOneFile, int gazeteersFeatures, boolean dal_features, boolean anew_features, boolean WarrinerFeatures, boolean WordsDictionaryFeatures, boolean NRCVADFeatures, String input, String outputFile) throws ClassNotFoundException, IOException{	
		if (gazeteersFeatures == 1) {
			
			///String gazeteerFile = originFolder + "GazQ1-dal.txt";
			gazeteerFile =  gazeteerFile.replace("Gazeteers", "GazQ1-dal");
		}
		
		else if (gazeteersFeatures == 2) {
			
			gazeteerFile =  gazeteerFile.replace("Gazeteers", "GazQ2-dal");
			
		}
		
		else if (gazeteersFeatures == 3) {
			
			gazeteerFile =  gazeteerFile.replace("Gazeteers", "GazQ3-dal");
		
		}
	
		else if (gazeteersFeatures == 4) {
			
	
			gazeteerFile =  gazeteerFile.replace("Gazeteers", "GazQ4-dal");
	
		}
		
		else if (gazeteersFeatures == 5) {
			
			gazeteerFile =  gazeteerFile.replace("Gazeteers", "GazQ1Q2Q3Q4_dal");
			
	
		}	
		
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
		strW = dicFileW + " into " + sourceFolder + " - Details";
		strA = dicFileA + " into " + sourceFolder + " - Details";
		
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
		//String[][] matrix = new String[numberFiles][4];
		String[][] matrix = new String[numberFiles][23];
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

			// now we make an array list to store the valence and arousal values for the file
			ArrayList<Double> valenceFileValues = new ArrayList<Double>();
			ArrayList<Double> arousalFileValues = new ArrayList<Double>();
			
			int countFileValue = 0;
			
			// DAL_ANEW features especificas
			double dominanceFileValue = 0;
			double averageDominanceFileValue = 0;
			ArrayList <Double> dominanceFileValues = new ArrayList<Double>();
			
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
						if (dal_features) {
							dict = new FileReader(dicFile);
						}
						else if (anew_features) {
							dict = new FileReader(dicFileA);
						}
						
						else if (WarrinerFeatures) {
							dict = new FileReader(dicFileW);
						}

						else if (NRCVADFeatures) {
							dict = new FileReader(dicFileNRC);
						}
						
						else if (gazeteersFeatures != 0) {
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
							
							if (gazeteersFeatures != 0) {
								linha = linha.toLowerCase();
							}
							
							if (linha.equals(dataDict[0])) {
								valenceFileValue += Double.parseDouble(dataDict[1]);
								arousalFileValue += Double.parseDouble(dataDict[2]);
								valenceLineValue += Double.parseDouble(dataDict[1]);
								arousalLineValue += Double.parseDouble(dataDict[2]);
								if (dal_features || anew_features || WarrinerFeatures || NRCVADFeatures) {
									dominanceFileValue += Double.parseDouble(dataDict[3]);
									dominanceLineValue += Double.parseDouble(dataDict[3]);
									dominanceFileValues.add(Double.parseDouble(dataDict[3]));
								}

								valenceFileValues.add(Double.parseDouble(dataDict[1]));
								arousalFileValues.add(Double.parseDouble(dataDict[2]));

								countFileValue++;
								countLineValue++;
								StringBuilder write = new StringBuilder(dataDict[0] + " " + dataDict[1] + " " + dataDict[2]);
								
								if (dal_features || anew_features || WarrinerFeatures || NRCVADFeatures) {
									write.append(" ").append(dataDict[3]);
								}
								outputDetails = woDetails.writeLinesInList(outputDetails, write.toString());
							}
						}
					}// end for
				} 
				if (!WordsDictionaryFeatures) {
					averageValenceLineValue = valenceLineValue / countLineValue;
					averageArousalLineValue = arousalLineValue / countLineValue;
					if (dal_features || anew_features || WarrinerFeatures || NRCVADFeatures) {
						averageDominanceLineValue = dominanceLineValue / countLineValue;
					}
					
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"\n");
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"\n Valence Line " + averageValenceLineValue);
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"Arousal Line " + averageArousalLineValue);
					if (dal_features || anew_features) {
					outputDetails = woDetails.writeLinesInList(outputDetails,
							"Dominance Line " + averageDominanceLineValue);
					}
					else if (WarrinerFeatures) {
						outputDetails = woDetails.writeLinesInList(outputDetails,
								"Dominance Line " + averageDominanceLineValue);
						}
				}
			} // end while
			if (!WordsDictionaryFeatures) {
				averageValenceFileValue = valenceFileValue / countFileValue;
				averageArousalFileValue = arousalFileValue / countFileValue;

				// For Valence
				DescriptiveStatistics valenceStats = new DescriptiveStatistics();
				valenceFileValues.forEach(valenceStats::addValue);

				double valenceMean = valenceStats.getMean();
				double valenceStdDev = valenceStats.getStandardDeviation();
				double valenceSkewness = valenceStats.getSkewness();
				double valenceKurtosis = valenceStats.getKurtosis();
				double valenceMax = valenceStats.getMax();
				double valenceMin = valenceStats.getMin();
				double valenceMedian = valenceStats.getPercentile(50);

				// For Arousal
				DescriptiveStatistics arousalStats = new DescriptiveStatistics();
				arousalFileValues.forEach(arousalStats::addValue);

				double arousalMean = arousalStats.getMean();
				double arousalStdDev = arousalStats.getStandardDeviation();
				double arousalSkewness = arousalStats.getSkewness();
				double arousalKurtosis = arousalStats.getKurtosis();
				double arousalMax = arousalStats.getMax();
				double arousalMin = arousalStats.getMin();
				double arousalMedian = arousalStats.getPercentile(50.0);

				// Print results
				/*
				System.out.println("\nValence Statistics:");
				System.out.println("Mean: " + valenceMean);
				System.out.println("Standard Deviation: " + valenceStdDev);
				System.out.println("Skewness: " + valenceSkewness);
				System.out.println("Kurtosis: " + valenceKurtosis);
				System.out.println("Maximum: " + valenceMax);
				System.out.println("Minimum: " + valenceMin);
				System.out.println("Median: " + valenceMedian);

				System.out.println("\nArousal Statistics:");
				System.out.println("Mean: " + arousalMean);
				System.out.println("Standard Deviation: " + arousalStdDev);
				System.out.println("Skewness: " + arousalSkewness);
				System.out.println("Kurtosis: " + arousalKurtosis);
				System.out.println("Maximum: " + arousalMax);
				System.out.println("Minimum: " + arousalMin);
				System.out.println("Median: " + arousalMedian);*/
				
				
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
				Integer base = 4;
				if (dal_features || anew_features || WarrinerFeatures || NRCVADFeatures) {
					averageDominanceFileValue = dominanceFileValue / countFileValue;
					matrix[i][3] = Double.toString(averageDominanceFileValue);
					matrix[i][4] = Integer.toString(countFileValue);
					base = 5;
				}
				else if (gazeteersFeatures != 0) {
					matrix[i][3] = Integer.toString(countFileValue);		
				}

				matrix[i][base] = Double.toString(valenceStdDev);
				matrix[i][base + 1] = Double.toString(valenceSkewness);
				matrix[i][base + 2] = Double.toString(valenceKurtosis);
				matrix[i][base + 3] = Double.toString(valenceMax);
				matrix[i][base + 4] = Double.toString(valenceMin);
				matrix[i][base + 5] = Double.toString(valenceMedian);

				matrix[i][base + 6] = Double.toString(arousalStdDev);
				matrix[i][base + 7 ] = Double.toString(arousalSkewness);
				matrix[i][base + 8] = Double.toString(arousalKurtosis);
				matrix[i][base + 9] = Double.toString(arousalMax);
				matrix[i][base + 10] = Double.toString(arousalMin);
				matrix[i][base + 11] = Double.toString(arousalMedian);
				
				if (dal_features || anew_features || WarrinerFeatures  || NRCVADFeatures) {
					DescriptiveStatistics dominanceStats = new DescriptiveStatistics();
					dominanceFileValues.forEach(dominanceStats::addValue);

					double dominanceMean = dominanceStats.getMean();
					double dominanceStdDev = dominanceStats.getStandardDeviation();
					double dominanceSkewness = dominanceStats.getSkewness();
					double dominanceKurtosis = dominanceStats.getKurtosis();
					double dominanceMax = dominanceStats.getMax();
					double dominanceMin = dominanceStats.getMin();
					double dominanceMedian = dominanceStats.getPercentile(50.0);

					// Print results
					/*
					System.out.println("\nDominance Statistics:");
					System.out.println("Mean: " + dominanceMean);
					System.out.println("Standard Deviation: " + dominanceStdDev);
					System.out.println("Skewness: " + dominanceSkewness);
					System.out.println("Kurtosis: " + dominanceKurtosis);
					System.out.println("Maximum: " + dominanceMax);
					System.out.println("Minimum: " + dominanceMin);
					System.out.println("Median: " + dominanceMedian);*/

					matrix[i][base + 12] = Double.toString(dominanceStdDev);
					matrix[i][base + 13] = Double.toString(dominanceSkewness);
					matrix[i][base + 14] = Double.toString(dominanceKurtosis);
					matrix[i][base + 15] = Double.toString(dominanceMax);
					matrix[i][base + 16] = Double.toString(dominanceMin);	
					matrix[i][base + 17] = Double.toString(dominanceMedian);	
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
			if (gazeteersFeatures == 1) {
				
				
				this.outputFile = outputFolder + "Gazeteers_Q1";
			}
			
			else if (gazeteersFeatures == 2) {
				
				
				this.outputFile = outputFolder + "Gazeteers_Q2";
			}
			
			else if (gazeteersFeatures == 3) {
				
				
				this.outputFile = outputFolder + "Gazeteers_Q3";
			}
			
			
			else if (gazeteersFeatures == 4) {
		
				
				this.outputFile = outputFolder + "Gazeteers_Q4";
			}
			
			else if (gazeteersFeatures == 5) {
		
				
				this.outputFile = outputFolder + "Gazeteers_All";
			}
			
			
			if (dal_features) {
				this.outputFile = outputFolder + "Dal";
				
			}
			if (anew_features) {
				this.outputFile = outputFolder + "Anew";
				
			}
				
			if (WarrinerFeatures) {
				this.outputFile = outputFolder + "Warriner";
				
			}

			if (NRCVADFeatures) {
				this.outputFile = outputFolder + "NRCVAD";
			}
			
			
			if (WordsDictionaryFeatures) {
				this.outputFile = outputFolder + "Slang";
			}
		}

		if (dal_features || anew_features || WarrinerFeatures || NRCVADFeatures){
			wo.writeMatrixInFile2(matrix, this.outputFile,WordsDictionaryFeatures,true);
		}

		else{
			wo.writeMatrixInFile2(matrix, this.outputFile,WordsDictionaryFeatures,false);
		}
		
	}
}
