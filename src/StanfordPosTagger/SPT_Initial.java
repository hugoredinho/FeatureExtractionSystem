package StanfordPosTagger;

import AuxiliarFiles.ReadOperations;
import AuxiliarFiles.WriteOperations;
import CapitalLetters.CapitalLetters_Initial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Utilizando o Stanford POSTagger le um conjunto de files .txt e transforma
 * cada file na versao withTags ou onlyTags
 * 
 * Exemplo: onlyTags para o 1� verso da musica L001:
 * JJ TO VB RP WRB DT NN VBZ
 * WRB PRP VBZ DT JJ NN
 * CC NN VBZ RB TO NN
 * NNP VBZ TO NN
 * NNP VBZ TO NN
 * 
 * Exemplo: withTags para o 1� verso da musica L001:
 * Want/JJ to/TO find/VB out/RP where/WRB the/DT moon/NN goes/VBZ 
 * When/WRB it/PRP leaves/VBZ the/DT western/JJ sky/NN 
 * And/CC night/NN dissolves/VBZ again/RB to/TO morning/NN 
 * Azure/NNP turns/VBZ to/TO gold/NN 
 * Azure/NNP turns/VBZ to/TO gold/NN 
 *  
 * @author rsmal
 * @date
 */

public class SPT_Initial {
	static final String locTagger = "src/AuxiliarFiles/bidirectional-distsim-wsj-0-18.tagger";
	String sourceFolder; // pasta onde estao as liricas
													// a processar
	static final int withTags = 1;
	static final int onlyTags = 2;
	
	int option;
	
	//output folder
	String outputFolder  = "src/Origem_POS/";
	
	String outputFile;
	
	public static void main(String[] args) throws ClassNotFoundException,
	IOException {
		SPT_Initial spt_initial = new SPT_Initial(true,"src/Origem/L001-141.txt",null,2);
	}
	
	public SPT_Initial(boolean onlyOneFile, String input, String outputFile, int option) throws ClassNotFoundException,IOException {
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
		
		
		// TODO Auto-generated method stub
		this.option = option;

		int numberFiles = 0;
		String [] files = null;
		if (!onlyOneFile) {
			ReadOperations rf = new ReadOperations();
			files = rf.openDirectory(sourceFolder);
			numberFiles = rf.filesLength(files);			
		}
		else {
			files = new String [] {input};
			numberFiles = 1;
		}

		// calcula as postags linha a linha e guarda em tagger
		PosTags postags = new PosTags();
		MaxentTagger tagger = postags.initialize(locTagger);
		
		// para cada file...
		for (int i = 0; i < files.length; i++) {
			// localizacao dos files
			// location of the file
			String filename_path = new String();
			if (onlyOneFile) {
				filename_path = input;
			}
			else {
				filename_path = sourceFolder + "/" + files[i];
			}
			System.out.println("i = "+i);
			
			// abrir o file para leitura
			FileReader fileReader = new FileReader(filename_path);
			BufferedReader in = new BufferedReader(fileReader);

			String thisLine;
			String lyric_text = "";

			// guarda cada linha de file (thisLine) numa string linha por linha
			// (lyric_text)
			while ((thisLine = in.readLine()) != null) {
				lyric_text = lyric_text + "\n" + thisLine;
			}

			String[] lyric_line; // array que guarda em cada posicao uma linha
									// completa
			lyric_line = lyric_text.split("\n");

			// calcula as postags linha a linha e guarda em tagger
			//PosTags postags = new PosTags();
			//MaxentTagger tagger = postags.initialize(locTagger);

			String textTags = "";

			if (option == withTags) {
				// para cada frase em lyric_line calcula a frase com tags
				for (int j = 0; j < lyric_line.length; j++) {
					String tagged = postags.tagString(tagger, lyric_line[j]);
					textTags = textTags + "\n" + tagged;
				}
			} else {
				for (int j = 0; j < lyric_line.length; j++) {
					String tagged = postags.tagString(tagger, lyric_line[j]);
					String taggedOnlyTags = postags.convertToOnlyTags(tagged);
					textTags = textTags + "\n" + taggedOnlyTags;
				}
			}

			String[] filename;
			filename = files[i].toString().split("\\.");
			if (filename[0].contains("/")) {
				String [] nome = filename[0].split("/");
				filename[0] = nome[nome.length-1];
			}		
			
			// guarda em file os fich de texto com postags
			WriteOperations wf = new WriteOperations();
			if (onlyOneFile) {
				System.out.println(this.outputFile);
				wf.writeFile(this.outputFile + "_with_POStags.txt", textTags);
			}
			else {
				System.out.println(this.outputFile.replace(".txt", "") + filename[0] + "_with_POStags.txt");
				wf.writeFile(this.outputFile.replace(".txt", "") + filename[0] + "_with_POStags.txt", textTags);
			
			}
		}
	}

}
