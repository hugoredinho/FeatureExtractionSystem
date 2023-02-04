package StructuralFeatures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import AuxiliarFiles.MinimumEditDistance;

public class ChorusDetection {
	Bloco bloco = new Bloco();
	static ArrayList <Bloco> array_blocos = new ArrayList <> ();
	final double threshold = 0.2;
	public int nrBlocoAtual = 0;
	final static String path_musicas = "C:\\Users\\Red\\Desktop\\Investigacao2020\\datasets\\resultados_detecao_chorus_400\\400_sem_anotacao";
	final static String path_final = "C:\\Users\\Red\\Desktop\\Investigacao2020\\datasets\\resultados_detecao_chorus_400\\script_400";
	final static String file_indexs = "C:\\Users\\Red\\Desktop\\Investigacao2020\\datasets\\resultados_detecao_chorus_400\\file_indexs_script_chorus.txt";
	public ChorusDetection(String sourceFile) {
		
		// TODO Auto-generated constructor stub
		Path path = Paths.get(sourceFile);
		String content = null;
		try {
			content = Files.readString(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String [] lista_string = content.split("\\r\\n");
				
		System.out.println("----------------------------------------");
		

		for (int i = 0; i< lista_string.length; i++) {
			if (lista_string[i].equals("")) { // se encontrarmos vazio
				array_blocos.add(bloco);
				bloco = new Bloco();
			}
			else {
				FraseLetra fl = new FraseLetra(lista_string[i],i);
				bloco.adicionarFrase(fl);
				if (i +1 == lista_string.length) {
					array_blocos.add(bloco);
					break;
				}
			}	
		}
		
		//System.out.println(content);
		
		countOccorencesOfEachSentence();
		
		
		for (Bloco bloco_frases : array_blocos) {
			for (FraseLetra fl: bloco_frases.getBloco()) { // isto aqui permite-nos livrarmo-nos de alguns versos
				if (fl.getOcorrencias() == 1) { 
					 // se for igual a 1, vamos ver se todas a frases que estao contidas neste bloco sao 1,
					 // se forem, nao e refrao, se nao forem, podera ser 
					checkBlock(bloco_frases);
					break;
				}
			}
			nrBlocoAtual++;
		}
		nrBlocoAtual = 0;
		
		ArrayList<Float> lista_medias = getAverageBlocks();

		int contador = 0;
		for (Bloco bloco_frases : array_blocos) {
			//System.out.printf("Para o bloco %d foi adicionado %f na segunda verifica��o\n",nrBlocoAtual,lista_medias.get(contador) * 10);
			updateBlock(bloco_frases,lista_medias.get(contador++) * 10);
			nrBlocoAtual++;
		}
		
		
		nrBlocoAtual = 0;
		removeBadChorusBasedOnEditDistance();
		//getMainBlock();
		
		
		//printLetra(true);
		
		writeExpectedChorusToFile(sourceFile, true);
		
	}
	
	public void countOccorencesOfEachSentence() {
		for (Bloco bloco_frases : array_blocos) {
			for (FraseLetra fl : bloco_frases.getBloco()) {
				if (!fl.getLetra().equals("")){
					fl.setOcorrencias(countOcorrencesofString(fl.getLetra()));
				}
			}
		}		
	}
	
	public void getMainBlock() {
		ArrayList<Bloco> listaPotenciaisBlocos = new ArrayList<> (array_blocos);
		
		ArrayList<Float> lista_medias = getAverageBlocks(); 
		
		double maximo = Collections.max(lista_medias);
		double minimo = Collections.min(lista_medias);
		
		double diferenca = maximo / minimo;
		
		if (diferenca > 2) { // se houver uma descrepancia na media de ocorrencias, vamos remover
			for (Bloco bloco : array_blocos) {
				if (bloco.getMediaOcorrencias() == minimo) {
					listaPotenciaisBlocos.remove(bloco);
				}
			}							
		}
		
		for (Bloco bloco : array_blocos) {
			if (listaPotenciaisBlocos.contains(bloco)) {
				if (isBlockSeparate(bloco) || bloco.getMediaOcorrencias() == maximo) {
					listaPotenciaisBlocos.remove(bloco);
				}
			}
		}
		
		
		for (Bloco bloco : listaPotenciaisBlocos) {
			bloco.printBloco();
		}
		
		
		
	}
	
	public boolean isBlockSeparate(Bloco blocoComparar) {
		for (Bloco bloco: array_blocos) {
			if (blocoComparar != bloco) {
				ArrayList <Integer> lista_edit_distances = new ArrayList <Integer>();
				// caso em  que os tamanhos sao iguais			
				if (blocoComparar.getTamanho() == bloco.getTamanho() ) {
					lista_edit_distances = getMinList(bloco, blocoComparar, 2);
				}
				else {	
					lista_edit_distances = getMinList(bloco, blocoComparar, 1);	
				}			
				float media = 0;
				for (int a : lista_edit_distances) {
					media += a;
				}
				media /= lista_edit_distances.size();
				//System.out.printf("%f %d\n",media,threshold);
				if (media < threshold) {	
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	public void removeBadChorusBasedOnEditDistance() {	
		Bloco blocoPrincipal = new Bloco();
		float media_maxima = 0;
		int indice_melhor = -1;
		int contador = 0;
		for (Bloco bloco_frases : array_blocos) {
			float media = 0;
			for (FraseLetra fl : bloco_frases.getBloco()) {
					media += fl.getOcorrencias();
			}
			media /= bloco_frases.getTamanho();
			if (media > media_maxima) {
				media_maxima = media;
				blocoPrincipal = bloco_frases;
				indice_melhor = contador;	
			}
			contador++;
		}
		
		//blocoPrincipal = getMainBlock();
		
		
		int threshold = getThreshHold(blocoPrincipal);
		for (int i = 0; i< array_blocos.size(); i++) {
			if (i!=indice_melhor) {
				ArrayList <Integer> lista_edit_distances = new ArrayList <Integer>();
					// caso em  que os tamanhos sao iguais			
					if (blocoPrincipal.getTamanho() == array_blocos.get(i).getTamanho() ) {
						lista_edit_distances = getMinList(array_blocos.get(i), blocoPrincipal, 2);
					}
					else {	
						lista_edit_distances = getMinList(array_blocos.get(i), blocoPrincipal, 1);	
					}			
					float media = 0;
					for (int a : lista_edit_distances) {
						media += a;
					}
					media /= lista_edit_distances.size();
					//System.out.printf("%f %d\n",media,threshold);
					if (media > threshold) {
						//System.out.printf("Para o bloco %d foi retirado -10 na terceira verifica��o\n",i);
						updateBlock(array_blocos.get(i),-10);						
					}	
					else {
						//System.out.printf("Para o bloco %d foi adicionado +10 na terceira verifica��o\nBem Como ao bloco principal (%d)\n",i,indice_melhor);
						updateBlock(array_blocos.get(i),+10);
						updateBlock(blocoPrincipal,+10);
					}	
			}
		}  
	}
	
	public int getThreshHold(Bloco blocoPrincipal) {
		int tamanho = 0;
		for (FraseLetra fl : blocoPrincipal.getBloco()) {
			tamanho += fl.getLetra().length();
		}
		tamanho /= blocoPrincipal.getTamanho();
		tamanho *= this.threshold;
		return tamanho;
	}
	
	
	public ArrayList <Integer> getMinList(Bloco blocoComparar, Bloco blocoPrincipal, int option) {
		int editDistance;
		int min_ed = 1000000;
		ArrayList <Integer> lista_edit_distances = new ArrayList <Integer>();
		switch(option) {
			case 1: // quando tamanho da lista principal � mais pequeno do que o bloco a comparar
				for (FraseLetra fl_bp : blocoPrincipal.getBloco()) {
					for (FraseLetra fl_bc : blocoComparar.getBloco()) {
						editDistance = MinimumEditDistance.calculateEditDistance(fl_bp.getLetra(), fl_bc.getLetra());
						if (editDistance < min_ed) {
							min_ed = editDistance;
						}
					}
					lista_edit_distances.add(min_ed);
				}
				break;
			case 2: // quando tamanho da lista principal � igual ao tamanho do bloco
				for (int i = 0; i< blocoPrincipal.getTamanho(); i++) {
					editDistance = MinimumEditDistance.calculateEditDistance(blocoPrincipal.getFraseByIndex(i).getLetra(), blocoComparar.getFraseByIndex(i).getLetra());
					if (editDistance < min_ed) {
						min_ed = editDistance;
					}
					lista_edit_distances.add(min_ed);					
				}
				break;	
		}
		return lista_edit_distances;
	}
	
	
	
	
	public static ArrayList<Float> getAverageBlocks() {
		ArrayList<Float> lista_float = new ArrayList<>();
		for (Bloco bloco_frases : array_blocos) {
			float media = 0;
			for (FraseLetra fl : bloco_frases.getBloco()) {
				media += fl.getOcorrencias();
			}
			lista_float.add(media/bloco_frases.getTamanho());	
			bloco_frases.setMediaOcorrencias(media/bloco_frases.getTamanho());
		}						
		return lista_float;		
	}
	
	
	public int getMaxOcorrences() {
		int max = 0;
		for (Bloco bloco_frases : array_blocos) {
			for (FraseLetra fl: bloco_frases.getBloco()) {
				if (fl.getOcorrencias() > max) {
					max = fl.getOcorrencias();
				}				
			}
		}
		return max;
	}
	
	public int isBlockGood(Bloco bloco_frases, int maximo) {
		int contador_maximos = 0;
		
		//System.out.printf("ISBG %d %d\n", indice_inicio, indice_fim);
		for (FraseLetra fl : bloco_frases.getBloco()) {
			if (fl.getOcorrencias() == maximo) {
				contador_maximos++;
			}
		}
		return contador_maximos;	
	}
	
	
	public void checkBlock(Bloco bloco_frases) {	
		boolean canBlockBeChorus = false;
		for (FraseLetra fl : bloco_frases.getBloco()) {
			if (fl.getOcorrencias() > 1) {
				canBlockBeChorus = true;
				break;
			}
		}
		if (!canBlockBeChorus) {
			//System.out.printf("Para o bloco %d foi retirado -20 na primeira verifica��o\n" ,nrBlocoAtual);
			updateBlock(bloco_frases,-20);
		}
	}
	
	
	public void updateBlock(Bloco bloco_frases, double offset) {
		//System.out.printf("Vou dar update a %d, %d\n",indice_inicio, indice_fim);
		for (FraseLetra fl : bloco_frases.getBloco()) {
			fl.setChorusProbability(fl.getChorusProbability()+offset);
		}
	}
	
	
	public void printLetra(boolean printOnlyChorus) {
		float media = 0;
		for (int i = 0; i< array_blocos.size(); i++) {
			for (int j = 0; j < array_blocos.get(i).getTamanho(); j++) {
				media += array_blocos.get(i).getFraseByIndex(j).getOcorrencias();
				System.out.println(array_blocos.get(i).getFraseByIndex(j));
			}
			System.out.println("Media deste bloco" + media/array_blocos.get(i).getTamanho());
			System.out.println("Probabilidade do bloco ser chorus " + array_blocos.get(i).getFraseByIndex(0).getChorusProbability() + "%");
			media = 0;
			System.out.println("------------------------------");
		}
	}
	
	public int countOcorrencesofString(String s) {
		int ocorrencias = 0;
		int editDistance = 0;
		double limite = 0;
		
		for (Bloco bloco_frases : array_blocos) {
			for (FraseLetra frase_letra : bloco_frases.getBloco()) {
				//if (frase_letra.getLetra().equals(s)) {
				//	ocorrencias++;
				//}
				editDistance = MinimumEditDistance.calculateEditDistance(frase_letra.getLetra(), s);
				//System.out.printf("Edit distance entre %s e %s � %d \n",frase_letra.getLetra(),s,editDistance);
				limite = s.length() * this.threshold;
				if (editDistance <= limite) {
					//System.out.printf("%s e %s sao iguais\n",frase_letra.getLetra(),s);
					ocorrencias++;
				}
				else {
					//System.out.printf("%s e %s sao \"diferentes\"\n",frase_letra.getLetra(),s);
				}
			}
			//System.out.println("-----------------------------");
		}
		return ocorrencias;
	}
	
	public static void writeExpectedChorusToFile(String sourceFile, boolean writeToIndexFile) {
		ArrayList<Double> listaPercentagensChorus = getChorusPercentage();
		ArrayList<Bloco> listaPotenciaisBlocos = new ArrayList<> (array_blocos);
		double maximo = Collections.max(listaPercentagensChorus);
		double minimo = Collections.min(listaPercentagensChorus);
		
		ArrayList<Float> mediaOcorrencias = getAverageBlocks();
		
		double maximo_ocorr = Collections.max(mediaOcorrencias);
		
		ArrayList<Integer> listaIndexsRemovidos = new ArrayList<>();
		
		int contador_blocos = 0;
		
		// first we remove the blocks with low %
		for(Bloco bloco  : array_blocos) {
			if (bloco.getProbability() == minimo) {
				listaPotenciaisBlocos.remove(bloco);
				listaIndexsRemovidos.add(contador_blocos);
			}
			else if (bloco.getProbability() < 0.60 * maximo) {
				if (bloco.getMediaOcorrencias() < 0.70 * maximo_ocorr) {
					listaPotenciaisBlocos.remove(bloco);
					listaIndexsRemovidos.add(contador_blocos);
				}
			}
			contador_blocos++;
		}	
		
		
		String filename = sourceFile.split("desanotated_")[1];
		
		String file_path = path_final + "\\script_chorus_" + filename;
		
		
		FileWriter myWriter = null;
		try {
			myWriter = new FileWriter(file_path);

			int contador = 0;
			
			for (Bloco b : listaPotenciaisBlocos) {
				for (FraseLetra f : b.getBloco()) {
					try {					
						myWriter.write(f.getLetra() + "\n");
					} catch (IOException e) {
					    System.out.println("An error occurred.");
					    e.printStackTrace();
					}
				}
				if (contador +1 != listaPotenciaisBlocos.size()) {
					try {					
						myWriter.write("\n");
					} catch (IOException e) {
					    System.out.println("An error occurred.");
					    e.printStackTrace();
					}	
				}
				contador ++;
				//System.out.println();
			}
					
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}finally {
			try {
				if (myWriter != null) {
					myWriter.flush();
					myWriter.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		
		if (writeToIndexFile) {
			FileWriter writerIndexs = null;
			try {
				writerIndexs = new FileWriter(file_indexs , true);
				int numeros_added = 0;
				for (int i = 0; i < array_blocos.size(); i++) {
					if (!listaIndexsRemovidos.contains(i)) { // se nao tiver sido removido, ou seja, se for refrao
						numeros_added++;
						System.out.print(i + ",");
						try {					
							writerIndexs.write(String.valueOf(i));
							if (numeros_added != array_blocos.size() - listaIndexsRemovidos.size()) {
								writerIndexs.write(',');
							}
						
						}
						catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				writerIndexs.write('\n');
					
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}finally {
			try {
				if (writerIndexs != null) {
					writerIndexs.flush();
					writerIndexs.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
		
	
		
	}
	
	
	public static ArrayList<Double> getChorusPercentage(){
		ArrayList<Double> lista_double = new ArrayList<>();
		for (Bloco bloco_frases : array_blocos) {
			bloco_frases.setProbability(bloco_frases.getBloco().get(0).getChorusProbability());
			lista_double.add(bloco_frases.getProbability());
		}						
		return lista_double;		
		
	}
	
	public static void writeCSV(String titulo, int value) {
		String outputFolder  = "src/Output/";
		FileWriter fileWriter2 = null;
		try {
			fileWriter2 = new FileWriter(outputFolder+"Titles.csv");
			
			fileWriter2.write("title, count");
			fileWriter2.write("\n");
			fileWriter2.write(titulo);
			fileWriter2.write(", ");
			fileWriter2.write(String.valueOf(value));		
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
	
	

	public static void main(String[] args) throws IOException {
		
		File folder = new File(path_musicas);
				
		for (File fileEntry : folder.listFiles()) {
			//System.out.println(fileEntry.getName());
			System.out.println(fileEntry.getAbsolutePath());
			ChorusDetection chorusDetection = new ChorusDetection(fileEntry.getAbsolutePath());
			array_blocos.clear();
			
		}
		
		// TODO Auto-generated method stub
		//String file = "src/Lirica/rh_ot.txt";
			
	}

}
