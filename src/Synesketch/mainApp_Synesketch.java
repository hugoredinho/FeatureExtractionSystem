/**
 * 
 */
package Synesketch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import AuxiliarFiles.ReadLyricsDirectoryToAString;
import AuxiliarFiles.ReadOperations;
import Synesketch.Files.Emotion.EmotionalState;
import Synesketch.Files.Emotion.Empathyscope;

/**
 * @author rsmal
 * 
 * Para por a funcionar
 * 1. Mudar a pasta em baixo para ser a pasta onde estão as liricas .txt (no ex. em baixo e "lyrics-1180"...
 * 2. Mudar na classe "ReadLyricsDirectoryToAString" o nome do file que contem os nomes dos ficheiros ordenados 1 por linha
 * Neste ex. temos "lyrics-1180-nomes-ord.txt"
 * 3. O sistema vai guardar os 8 valores Synesketch num ficheiro ESCRITA.txt
 * A ordem e GeneralWeight, getValence, HappinessWeight, SadnessWeight, AngerWeight, FearWeight, DisgustWeight()
 * ,SurpriseWeight 
 * 
 * 
 */
public class mainApp_Synesketch {

	ArrayList<String> ids = new ArrayList();
	String sourceFolder;
	
	String outputFile = "Synesketch.csv";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		mainApp_Synesketch ma = new mainApp_Synesketch();
		// ma.init_1();
		ma.readDirectory(null,null);
	}

	/**
	 * @throws IOException
	 * @input idSong
	 * @output weights-Synesketch
	 */
	private void init_1() throws IOException {
	//	String text = "I hate you!!!";
		ReadLyricsDirectoryToAString rl = new ReadLyricsDirectoryToAString();
		String idSong = rl.menu();
		String lyric = rl.showLyric(idSong);

		EmotionalState state = Empathyscope.getInstance().feel(lyric);

		System.out.println(state);

		// System.out.println(lyric);

	}

	public void readDirectory(String sourceFolder1, String outputFile) throws IOException {		
		if(sourceFolder1 != null && !sourceFolder1.isEmpty()) {
			sourceFolder = sourceFolder1;				
		}
		else {
			sourceFolder = "src/Origem";
		}
		
		System.out.println(sourceFolder);
		
		String outputFolder = "";
		if(outputFile != null && !outputFile.isEmpty()) {
			this.outputFile = outputFile;				
		}
		else {
			outputFolder = "src/Output/";
		}
	
		ReadLyricsDirectoryToAString rl = new ReadLyricsDirectoryToAString();
		//ids = rl.openFileOfIDs(); 

		
		String [] files = null;
		
		ReadOperations ro = new ReadOperations();
		files = ro.openDirectory(sourceFolder);
		
		for(int i = 0; i < files.length; i++) {
			files[i] = files[i].replace(".txt", "");
		}
		
		List<String> ids = new ArrayList<String>(Arrays.asList(files));
		
		for (String s : ids) {
			System.out.println(s);
		}
		
		//ids contem os nomes dos files (liricas) sem o .txt
		Iterator it = ids.iterator();
		
		
			
		String output = outputFolder + this.outputFile;
		
		FileWriter fstream = new FileWriter(output);
		BufferedWriter out = new BufferedWriter(fstream);
		
		out.write("ID,GeneralWeight,Valence,HappinessWeight,SadnessWeight,AngerWeight,FearWeight,DisgustWeight,SurpriseWeight");
		out.newLine();
		
				
		while (it.hasNext()) {
			//idSong e por ex. L001-141
			
			
			String idSong = (String) it.next();
			
			System.out.println(idSong);
			String inputFile = sourceFolder+"/"+idSong+ ".txt";
			
			
			//caminho completo para onde esta a lirica ex. lyrics-180/L001-141.txt
			String lyric = rl.ler(sourceFolder+"/"+idSong+".txt");

			EmotionalState state = Empathyscope.getInstance().feel(lyric);
			System.out.println(state);
			
			System.out.println("Song: "+idSong);
					
			
			String line = idSong + "," + state.getGeneralWeight() + ","
					+ state.getValence() + "," + state.getHappinessWeight()
					+ "," + state.getSadnessWeight() + ","
					+ state.getAngerWeight() + "," + state.getFearWeight()
					+ "," + state.getDisgustWeight() + ","
					+ state.getSurpriseWeight();
			out.write(line);
			out.write("\n");		
		}
		out.close();
	}
	
	public void readFile(String inputFile,String outputFile) throws IOException {
		ReadLyricsDirectoryToAString rl = new ReadLyricsDirectoryToAString();

		String outputFolder = "";
		if(outputFile != null && !outputFile.isEmpty()) {
			this.outputFile = outputFile;				
		}
		else {
			outputFolder = "src/Output/";
		}
		
		String output = outputFolder + this.outputFile;
		
		FileWriter fstream = new FileWriter(output);
		BufferedWriter out = new BufferedWriter(fstream);
		
		out.write("ID,GeneralWeight,Valence,HappinessWeight,SadnessWeight,AngerWeight,FearWeight,DisgustWeight,SurpriseWeight");
		out.newLine();
		
		//caminho completo para onde esta a lirica ex. lyrics-180/L001-141.txt
		String lyric = rl.ler(inputFile);

		EmotionalState state = Empathyscope.getInstance().feel(lyric);
		System.out.println(state);
		
		
		String line = inputFile.replace(".txt","") + "," + state.getGeneralWeight() + ","
				+ state.getValence() + "," + state.getHappinessWeight()
				+ "," + state.getSadnessWeight() + ","
				+ state.getAngerWeight() + "," + state.getFearWeight()
				+ "," + state.getDisgustWeight() + ","
				+ state.getSurpriseWeight();
		out.write(line);
		out.write("\n");
			
		// Close the output stream
		out.close();		
	}

}
