package StructuralFeatures;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import AuxiliarFiles.MinimumEditDistance;



public class ChorusDetection {
	ArrayList<FraseLetra> bloco = new ArrayList<>();
	ArrayList <ArrayList<FraseLetra>> array_blocos = new ArrayList <ArrayList<FraseLetra>> ();
	final int threshold = 3;
	public ChorusDetection(String sourceFile) {
		// TODO Auto-generated constructor stub
		Path path = Paths.get(sourceFile);
		String content = null;
		try {
			content = Files.readString(path, StandardCharsets.UTF_8).toLowerCase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String [] lista_string = content.split("\\r\\n");
				
		System.out.println("----------------------------------------");
		

		for (int i = 0; i< lista_string.length; i++) {
			if (lista_string[i].equals("")) { // se encontrarmos vazio
				array_blocos.add(bloco);
				bloco = new ArrayList<FraseLetra>();
			}
			else {
				FraseLetra fl = new FraseLetra(lista_string[i],i);
				bloco.add(fl);
				if (i +1 == lista_string.length) {
					array_blocos.add(bloco);
					break;
				}
			}	
		}
		countOccorencesOfEachSentence();

		
		
		for (ArrayList<FraseLetra> bloco_frases : array_blocos) {
			for (FraseLetra fl: bloco_frases) { // isto aqui permite-nos livrarmo-nos de alguns versos
				if (fl.getOcorrencias() == 1) { 
					/* se for igual a 1, vamos ver se todas a frases que estao contidas neste bloco sao 1,
					 * se forem, nao e refrao, se nao forem, podera ser*/
					checkBlock(bloco_frases);
					break;
				}
			}
		}
		
		int maximo_ocorrencias = getMaxOcorrences();
		
		ArrayList<Float> lista_medias = getAverageBlocks();

		int contador = 0;
		for (ArrayList<FraseLetra> bloco_frases : array_blocos) {
			updateBlock(bloco_frases,lista_medias.get(contador++) * 10);
		}
		
		removeBadChorusBasedOnEditDistance();
		
		
		printLetra(true);
	}
	
	public void countOccorencesOfEachSentence() {
		for (ArrayList<FraseLetra> bloco_frases : array_blocos) {
			for (FraseLetra fl : bloco_frases) {
				if (!fl.getLetra().equals("")){
					fl.setOcorrencias(countOcorrencesofString(fl.getLetra()));
				}
			}
		}		
	}
	
	public void removeBadChorusBasedOnEditDistance() {	
		ArrayList<FraseLetra> blocoPrincipal = new ArrayList<>();
		float media_maxima = 0;
		int indice_melhor = -1;
		int contador = 0;
		for (ArrayList<FraseLetra> bloco_frases : array_blocos) {
			float media = 0;
			for (FraseLetra fl : bloco_frases) {
					media += fl.getOcorrencias();
			}
			media /= bloco_frases.size();
			if (media > media_maxima) {
				media_maxima = media;
				blocoPrincipal = bloco_frases;
				indice_melhor = contador;	
			}
			contador++;
		}
		
		
		int threshold = getThreshHold(blocoPrincipal);
		for (int i = 0; i< array_blocos.size(); i++) {
			if (i!=indice_melhor) {
				ArrayList <Integer> lista_edit_distances = new ArrayList <Integer>();
					// caso em  que os tamanhos sao iguais			
					if (blocoPrincipal.size() == array_blocos.get(i).size() ) {
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
						updateBlock(array_blocos.get(i),-10);
						
					}	
					else {
						updateBlock(array_blocos.get(i),+10);
						updateBlock(blocoPrincipal,+10);
					}	
			}
		}  
	}
	
	public int getThreshHold(ArrayList <FraseLetra> blocoPrincipal) {
		int tamanho = 0;
		for (FraseLetra fl : blocoPrincipal) {
			tamanho += fl.getLetra().length();
		}
		tamanho /= blocoPrincipal.size();
		tamanho /= this.threshold;
		return tamanho;
	}
	
	
	public ArrayList <Integer> getMinList(ArrayList <FraseLetra> blocoComparar, ArrayList <FraseLetra> blocoPrincipal, int option) {
		int editDistance;
		int min_ed = 1000000;
		ArrayList <Integer> lista_edit_distances = new ArrayList <Integer>();
		switch(option) {
			case 1: // quando tamanho da lista principal � mais pequeno do que o bloco a comparar
				for (FraseLetra fl_bp : blocoPrincipal) {
					for (FraseLetra fl_bc : blocoComparar) {
						editDistance = MinimumEditDistance.calculateEditDistance(fl_bp.getLetra(), fl_bc.getLetra());
						if (editDistance < min_ed) {
							min_ed = editDistance;
						}
					}
					lista_edit_distances.add(min_ed);
				}
				break;
			case 2: // quando tamanho da lista principal � igual ao tamanho do bloco
				for (int i = 0; i< blocoPrincipal.size(); i++) {
					editDistance = MinimumEditDistance.calculateEditDistance(blocoPrincipal.get(i).getLetra(), blocoComparar.get(i).getLetra());
					if (editDistance < min_ed) {
						min_ed = editDistance;
					}
					lista_edit_distances.add(min_ed);					
				}
				break;	
		}
		return lista_edit_distances;
	}
	
	
	/*public void removeBadChorusBasedOnMaxOcorr(){
		ArrayList<Float> lista_medias = getAverageBlocks();
		/*for (Float wtf : lista_medias) {
			System.out.println(wtf);
		}
		float maximo = Collections.max(lista_medias);
		for (int i = 0; i< array_blocos.size(); i++) {
			for (int j = 0; j< array_blocos.get(i).size(); j++) {
				if (lista_medias.get(i) == maximo) {
					updateBlock(array_blocos.get(i),true);
					break;
				}
			}
		}
	}*/
	
	
	public ArrayList<Float> getAverageBlocks() {
		ArrayList<Float> lista_float = new ArrayList<>();
		for (ArrayList<FraseLetra> bloco_frases : array_blocos) {
			float media = 0;
			for (FraseLetra fl : bloco_frases) {
				media += fl.getOcorrencias();
			}
			lista_float.add(media/bloco_frases.size());		
		}						
		return lista_float;		
	}
	
	
	public int getMaxOcorrences() {
		int max = 0;
		for (ArrayList<FraseLetra> bloco_frases : array_blocos) {
			for (FraseLetra fl: bloco_frases) {
				if (fl.getOcorrencias() > max) {
					max = fl.getOcorrencias();
				}				
			}
		}
		return max;
	}
	
	public int isBlockGood(ArrayList<FraseLetra> bloco_frases, int maximo) {
		int contador_maximos = 0;
		
		//System.out.printf("ISBG %d %d\n", indice_inicio, indice_fim);
		for (FraseLetra fl : bloco_frases) {
			if (fl.getOcorrencias() == maximo) {
				contador_maximos++;
			}
		}
		return contador_maximos;	
	}
	
	
	public void checkBlock(ArrayList<FraseLetra> bloco_frases) {	
		boolean canBlockBeChorus = false;
		for (FraseLetra fl : bloco_frases) {
			if (fl.getOcorrencias() > 1) {
				canBlockBeChorus = true;
				break;
			}
		}
		if (!canBlockBeChorus) {
			updateBlock(bloco_frases,-20);
		}
	}
	
	
	public void updateBlock(ArrayList<FraseLetra> bloco_frases, double offset) {
		//System.out.printf("Vou dar update a %d, %d\n",indice_inicio, indice_fim);
		for (FraseLetra fl : bloco_frases) {
			fl.setChorusProbability(fl.getChorusProbability()+offset);
		}
	}
	
	
	public void printLetra(boolean printOnlyChorus) {
		float media = 0;
		for (int i = 0; i< array_blocos.size(); i++) {
			for (int j = 0; j < array_blocos.get(i).size(); j++) {
				media += array_blocos.get(i).get(j).getOcorrencias();
				System.out.println(array_blocos.get(i).get(j));
			}
			System.out.println("Media deste bloco" + media/array_blocos.get(i).size());
			System.out.println("Probabilidade do bloco ser chorus " + array_blocos.get(i).get(0).getChorusProbability() + "%");
			media = 0;
			System.out.println("------------------------------");
		}
	}
	
	public int countOcorrencesofString(String s) {
		int ocorrencias = 0;
		for (ArrayList<FraseLetra> bloco_frases : array_blocos) {
			for (FraseLetra frase_letra : bloco_frases) {
				if (frase_letra.getLetra().equals(s)) {
					ocorrencias++;
				}
			}
		}
		return ocorrencias;
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
		// TODO Auto-generated method stub
		String file = "src/Origem/mc_ai.txt";
		ChorusDetection chorusDetection = new ChorusDetection(file);	
	}

}
