package AuxiliarFiles;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class WriteCSVFinal {
	String inputGaz, inputDAL, inputSynesk, inputGI, outputFile;
	
	String inputSlang, inputCL;
	
	
	public static void main(String[] args) throws IOException {
		WriteCSVFinal writecsv = new WriteCSVFinal();
		writecsv.WriteSemantic(null,null,null,null,null);
		writecsv.WriteStylistic(null,null,null);
		//WriteAll();
		
	}
	public void WriteSemantic(String inputGaz, String inputDAL, String inputSynesk, String inputGI, String outputFile) {
		ArrayList<String> inputSemantic = new ArrayList<String>();
		ArrayList<String> songsSemantic = new ArrayList<String>();
		
		if(inputGaz != null && !inputGaz.isEmpty()) {
			this.inputGaz = inputGaz;				
		}
		else {
			this.inputGaz = "src/Output/Gazeteers.csv";
		}
		
		if(inputDAL != null && !inputDAL.isEmpty()) {
			this.inputDAL = inputDAL;				
		}
		else {
			this.inputDAL = "src/Output/DAL_ANEW.csv";
		}
		
		if(inputSynesk != null && !inputSynesk.isEmpty()) {
			this.inputSynesk = inputSynesk;				
		}
		else {
			this.inputSynesk = "src/Output/Synesketch.csv";
		}
		
		if(inputGI != null && !inputGI.isEmpty()) {
			this.inputGI = inputGI;				
		}
		else {
			this.inputGI = "src/Output/GI_Features.csv";
		}
		
		if(outputFile != null && !outputFile.isEmpty()) {
			this.outputFile = outputFile;				
		}
		else {
			this.outputFile = "src/Output/Semantic.csv";
		}
		

			
		inputSemantic.add(this.inputGaz);
		inputSemantic.add(this.inputDAL);
		inputSemantic.add(this.inputSynesk);
		inputSemantic.add(this.inputGI);
		
		int firstline;
		String headerSemantic="Id,";
		boolean firstFile=true;
		
		//ler todos os ficheiros com os valores das features semanticas
		for(int i = 0; i < inputSemantic.size(); i++)
		{
			String line = "";
			// use comma as separator
		    String cvsSplitBy = ",";
		    
		    System.out.println("Going to write");
		    
		    File f = new File(inputSemantic.get(i));
		    if(f.exists() && !f.isDirectory()) { 
		    	
		    	 try (BufferedReader br = new BufferedReader(new FileReader(inputSemantic.get(i)))) {
				    	firstline=0;
				        while ((line = br.readLine()) != null) {
				
				        	//no ficheiro  do synesketch os valores em vez de estar separado por virgula e um espa�o, estao separados apenas virgula 
				        	if(i==2 && firstline!=0) {
						    	 cvsSplitBy = ",";
						    }
				            String[] values = line.split(cvsSplitBy);
				            
				          
				            if(firstline==0) {
				            	//pegar no header das features e adicionar o nome antes
				            	for(int colunas = 1; colunas < values.length; colunas++) {
				            		if(i==0)
				            			headerSemantic+="Gazeteers_"+values[colunas]+ ",";
				            		else
				            			if (i==1)
				            				headerSemantic+="DAL_ANEW_"+values[colunas]+ ",";
			            			else
				            			if (i==2)
				            				headerSemantic+="Synesktech_"+values[colunas] + ",";
			            			else {
				            			if (i==3) {
				            				headerSemantic+="GI_"+values[colunas];
					            			if (colunas +1 != values.length) {
					            				headerSemantic += ",";
					            			}
				            			}
			            			}
				            	}
				            	 
				            }else {
				            	
				            	 if(firstFile) {
				            		 //no primeiro ficheiro que lemos podemos pegar logo no id e valores e adiconar no lugar do array que corresponde a esta musica
				            		 String song="";
				            		 
				            		 for(int colunas = 0; colunas < values.length; colunas++) {
				            			 song+=values[colunas];
				            		//	 if (colunas +1 != values.length) {
				            				 song += ",";
				            		//	 }
				            		 }
				            		
				            		 songsSemantic.add(song);
				            		
				            		 
				            		 
				            	 }else {
				            		 //a partir do segundo ficheiro, temos de procurar onde esta a musica no array e juntar os novos valores que lemos
				            		 String addValuesToSong="";
				            		 System.out.println("Ln de baixo");
				            		 
				            		 if(!songsSemantic.isEmpty()) {
				            			 System.out.println("if");
				            			
				            			 addValuesToSong=songsSemantic.get(firstline-1);
				            			 for(int colunas = 1; colunas < values.length; colunas++) {
					            			 addValuesToSong+=values[colunas];	
					            			 
					            			 //if (colunas +1 != values.length) {
					            				 addValuesToSong += ",";
					            			// }
					            		 }
				            			 System.out.printf("Ultimo caracter �  %s (em cima) (%d)\n",addValuesToSong.charAt(addValuesToSong.length()-1),i);
				            			 if (i+1 == inputSemantic.size()) {
				            				 StringBuilder sb = new StringBuilder(addValuesToSong);
			            					 sb.deleteCharAt(addValuesToSong.length()-1);
			            					 songsSemantic.set(firstline-1,sb.toString());
				            			 }
				            			 else {
				            				 songsSemantic.set(firstline-1,addValuesToSong);
				            			 }
				            			 
				            		 }else {
				            			 System.out.println("else");
				            			 for(int colunas = 0; colunas < values.length; colunas++) {
				            				 addValuesToSong+=values[colunas];
					            			// if (colunas +1 != values.length) {
					            				 addValuesToSong += ",";
					            			// }
					            		 }
				            			 System.out.println("Ultimo caracter � " + addValuesToSong.charAt(addValuesToSong.length()-1));
					            		 
				            		 }
				            		
				            		 
				            	 }
				            }
				           
				            firstline+=1;
				        }
				        br.close();
				
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
		    	
		    	
		    	
		    	 firstFile=false;
		    }
		
		   
		}
		//escrever para um ficheiro todos os valores das features semanticas das songs analisadas
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(this.outputFile);
			
			fileWriter.write(headerSemantic);
			fileWriter.write("\n");
			
			for(int i = 0; i < songsSemantic.size(); i++)
			{
				fileWriter.write(songsSemantic.get(i));
				fileWriter.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (fileWriter != null) {
					fileWriter.flush();
					fileWriter.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void WriteStylistic(String inputSlang, String inputCL, String outputFile) {
		ArrayList<String> inputStylistic = new ArrayList<String>();
		ArrayList<String> songsStylistic  = new ArrayList<String>();
		
		if(inputSlang != null && !inputSlang.isEmpty()) {
			this.inputSlang = inputSlang;				
		}
		else {
			this.inputSlang = "src/Output/WordsDictionary.csv";
		}
		
		if(inputCL != null && !inputCL.isEmpty()) {
			this.inputCL = inputCL;				
		}
		else {
			this.inputCL = "src/Output/CapitalLetters.csv";
		}
		
		if(outputFile != null && !outputFile.isEmpty()) {
			this.outputFile = outputFile;				
		}
		else {
			this.outputFile = "src/Output/Stylistic.csv";
		}
		
		
		inputStylistic.add(this.inputSlang);
		inputStylistic.add(this.inputCL);
		
		int firstline;
		String headerStylistic ="Id,";
		boolean firstFile=true;
				
		
		//ler todos os ficheiros com os valores das features estilisticas
		for(int i = 0; i < inputStylistic.size(); i++)
		{
			String line = "";
		    String cvsSplitBy = ",";
		    
		    File f = new File(inputStylistic.get(i));
		    if(f.exists() && !f.isDirectory()) { 
		
			    try (BufferedReader br = new BufferedReader(new FileReader(inputStylistic.get(i)))) {
			    	firstline=0;
			        while ((line = br.readLine()) != null) {
			
			            String[] values = line.split(cvsSplitBy);
			           
			            if(firstline==0) {
			            	//pegar no header das features e adicionar o nome antes
			            	for(int colunas = 1; colunas < values.length; colunas++) {
			            		if(i==0)
			            			headerStylistic+="WordsDictionary_"+values[colunas]+",";
			            		else {
									headerStylistic+="CapitalLetters_"+values[colunas];
			            			if (colunas +1 != values.length) {
			            				headerStylistic += ",";
			            			}
			            		}
			            			         		
			            	}
			            	 
			            }else {
			            	 if(firstFile) {
			            		 //no primeiro ficheiro que lemos podemos pegar logo no id e valores e adiconar no lugar do array que corresponde a esta musica
			            		 String song="";
			            		 
			            		 for(int colunas = 0; colunas < values.length; colunas++) {
			            			 song+=values[colunas] +",";
			            			 //if (colunas + 1 != values.length) {
			            			//	 song += ",";
			            			// }
			            		 }
			            		 
			            		 songsStylistic.add(song);
			            	 }else {
			            		//a partir do segundo ficheiro, temos de procurar onde esta a musica no array e juntar os novos valores que lemos 
			            		 
			            		 String addValuesToSong="";
			            		 
			            		 if(!songsStylistic.isEmpty()) {
			            			
			            			 addValuesToSong=songsStylistic.get(firstline-1);
			            			 for(int colunas = 1; colunas < values.length; colunas++) {
				            			 addValuesToSong+=values[colunas];
			            				 addValuesToSong += ",";
				            		 }
			            			 if (i+1 == inputStylistic.size()) {
			            				 StringBuilder sb = new StringBuilder(addValuesToSong);
		            					 sb.deleteCharAt(addValuesToSong.length()-1);
		            					 songsStylistic.set(firstline-1,sb.toString());
			            			 }
			            			 else {
			            				 songsStylistic.set(firstline-1,addValuesToSong);
			            			 }
			            			 
			            		 }else {
			            			 for(int colunas = 0; colunas < values.length; colunas++) {
			            				 addValuesToSong+=values[colunas]+ ",";
				            			// if (colunas +1 != values.length) {
				            			//	 addValuesToSong += ",";
				            			// }
				            		 }
			            			 songsStylistic.add(addValuesToSong);
				            		 
			            		 }
			            	 }
			            }
			           
			            firstline+=1;
			        }
			        br.close();
			
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    firstFile=false;
		    }
		    
		    
		}
	
		//escrever para um ficheiro todos os valores das features estilisticas das songs analisadas
		FileWriter fileWriter2 = null;
		try {
			fileWriter2 = new FileWriter(this.outputFile);
			
			fileWriter2.write(headerStylistic);
			fileWriter2.write("\n");
			
			for(int i = 0; i < songsStylistic.size(); i++)
			{
				fileWriter2.write(songsStylistic.get(i));
				fileWriter2.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (fileWriter2 != null) {
					fileWriter2.flush();
					fileWriter2.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		
		
	}
	public void WriteAll(String outputFile) {
		ArrayList<String> input = new ArrayList<String>();
		ArrayList<String> songs  = new ArrayList<String>();
		input.add("src/Output/Semantic.csv");
		input.add("src/Output/Stylistic.csv");
		
		if(outputFile != null && !outputFile.isEmpty()) {
			this.outputFile = outputFile;				
		}
		else {
			this.outputFile = "src/Output/AllFeatures.csv";
		}
		
		
		int firstline;
		String header ="Id,";
		boolean firstFile=true;
		
		//ler todos os ficheiros com os valores das features 
		for(int i = 0; i < input.size(); i++)
		{
			String line = "";
		    
		    File f = new File(input.get(i));
		    if(f.exists() && !f.isDirectory()) { 
		
			    try (BufferedReader br = new BufferedReader(new FileReader(input.get(i)))) {
			    	firstline=0;
			        while ((line = br.readLine()) != null) {
			        				        	
			            String[] values = line.split(",");			       
			           
			            if(firstline==0) {
			            	//pegar no header das features e adicionar o nome antes
			            	for(int colunas = 1; colunas < values.length; colunas++) {
			            		if(i==0)
			            			header+="Semantic_"+values[colunas] + ",";
			            		
			            		else {
			            			header+="Stylistic_"+values[colunas];
			            			if (colunas +1 != values.length) {
			            				header +=",";
			            			}
			            		}
			            	}			            	
			            	 
			            }else {
			            	 if(firstFile) {
			            		 //no primeiro ficheiro que lemos podemos pegar logo no id e valores e adiconar no lugar do array que corresponde a esta musica
			            		 String song="";
			            		 
			            		 for(int colunas = 0; colunas < values.length; colunas++) {
			            			 song+=values[colunas];
			            			// if (colunas +1 != values.length) {
			            				song +=",";
			            			// }			            			 
			            		 }
			            		 
			            		 songs.add(song);
			            	 }else {
			            		//a partir do segundo ficheiro, temos de procurar onde esta a musica no array e juntar os novos valores que lemos 
			            		 
			            		 String addValuesToSong="";
			            		 
			            		 if(!songs.isEmpty()) {
			            			
			            			 addValuesToSong=songs.get(firstline-1);
			            			 for(int colunas = 1; colunas < values.length; colunas++) {
				            			 addValuesToSong+=values[colunas];	
				            			// if (colunas +1 != values.length) {
				            				 addValuesToSong +=",";
				            			// }
				            		 }
			            			 if (i+1 == input.size()) {
			            				 StringBuilder sb = new StringBuilder(addValuesToSong);
		            					 sb.deleteCharAt(addValuesToSong.length()-1);
		            					 songs.set(firstline-1,sb.toString());
			            			 }
			            			 else {
			            				 songs.set(firstline-1,addValuesToSong);
			            			 }
			            			 
			            			 
			            		 }else {
			            			 for(int colunas = 0; colunas < values.length; colunas++) {
			            				 addValuesToSong+=values[colunas]; 
				            			// if (colunas +1 != values.length) {
				            				 addValuesToSong +=",";
				            			// }
				            		 }
			            			 songs.add(addValuesToSong);
				            		 
			            		 }
			            	 }
			            }
			           
			            firstline+=1;
			        }
			        br.close();
			
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    firstFile=false;
		    }
		    
		    
		}
		
		//escrever para um ficheiro todos os valores das features estilisticas das songs analisadas
		FileWriter fileWriter2 = null;
		try {
			fileWriter2 = new FileWriter(this.outputFile);
			
			fileWriter2.write(header);
			fileWriter2.write("\n");
			
			for(int i = 0; i < songs.size(); i++)
			{
				fileWriter2.write(songs.get(i));
				fileWriter2.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (fileWriter2 != null) {
					fileWriter2.flush();
					fileWriter2.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		
	}
	
}

	
	

