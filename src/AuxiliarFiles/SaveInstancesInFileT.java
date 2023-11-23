package AuxiliarFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import AuxiliarFiles.ConvertToTFIDF;
import cc.mallet.types.InstanceList;

public class SaveInstancesInFileT {

	public static final String FREQ = "freq";
	public static final String BOOL = "bool";
	public static final String TFIDF = "tfidf";
	public static final String NORM = "norm";
	/*
	 * getSizeAlphabet() get lenght do directorio origem buildMatrix com dim dos
	 * 2 valores anteriores guardar num file o Alphabet fillMatrix - percorrer
	 * as instancias 1 a 1
	 */ 
	InstanceList instances;
	String[][] matrix;
	
	int col, lin;
	String strAlphabet; //O alfabeto todo numa string
	String[] strWords; //lista de palavras do alfabeto

	public SaveInstancesInFileT(InstanceList instances, String arg3) {

		this.instances = instances;
		col = getSizeAlphabet() + 2;
		lin = getNumberOfInstances();
		
		if (arg3.equals("freq")) {
			matrix = new String[lin+1][col];
		} else if (arg3.equals("bool")) {
			matrix = new String[lin+1][col];
		} else if (arg3.equals("tfidf")) {
			matrix = new String[lin+1][col];
		} else if (arg3.equals("norm")) {
			matrix = new String[lin+1][col];
		}
		
		strAlphabet = instances.getAlphabet().toString();
		strWords = strAlphabet.split("\n");
	}

	
	/**
	 * execute_freq(String[] args) throws IOException
	 * 
	 * @param args
	 * @throws IOException
	 */
	public void execute_freq(String arg1, String arg2) throws IOException {

		int i = 0;
		this.initializeMatrix(matrix);
		while (i < lin) {
			// name - o nome do ficheiro
			String name = getNameOfInstance(instances.get(i).getName()
					.toString());
			
			System.out.println("Nome = "  + name);
			
			String[] dat = name.split("/");
			name = dat[1];
			
			
			ArrayList<String> listOfWords = new ArrayList<String>();
			listOfWords = buildListOfWords(i);

			this.writeMatrix(listOfWords, name, i, matrix, col);

			i++;

		}
		
		/*
		 * Guardar em ficheiro as features da freq e a matriz com as freq de
		 * todas as liricas
		 */
		
		//this.writeMatrixInConsole(matrix);
		String nameFile1 = "src/Output/CBF_" + arg1 + "_" + arg2 + "_" + FREQ;
		
		File file2 = new File(nameFile1 + "Tags.csv");
		this.writeMatrixInFile(matrix, file2);

		
		
		// Converter de freq para tfidf
		//ConvertToTFIDF ct = new ConvertToTFIDF();
		//ct.convertTfidf(matrix);
		
	

	}

	
	/**
	 * execute_bool(String[] args)
	 * @param args
	 * @throws IOException
	 */
	
	public void execute_bool(String arg1, String arg2) throws IOException {

		int i = 0;
		this.initializeMatrix(matrix);
		while (i < lin) {
			String name = getNameOfInstance(instances.get(i).getName()
					.toString());
			ArrayList<String> listOfWords = new ArrayList<String>();
			listOfWords = buildListOfWords(i);

			this.writeMatrix(listOfWords, name, i, matrix, col);

			i++;

		}
		
		/*
		 * Guardar em ficheiro as features da freq e a matriz com as freq de
		 * todas as liricas
		 */
		this.calculateBoolValues();
		//this.writeMatrixInConsole(matrix);
		String nameFile2 = "src/Output/CBF_" + arg1 + "_" + arg2 + "_" + BOOL;
		//File file3 = new File(nameFile2 + "F.txt");
		//this.writeNamesOfFeaturesNames(file3);
		File file4 = new File(nameFile2 + "Tags.csv");
		this.writeMatrixInFile(matrix, file4);

	

	}

	
	/**
	 * execute_norm(String[] args)
	 * @param args
	 * @throws IOException
	 */
	
	
	public void execute_norm(String arg1, String arg2) throws IOException {
		int i = 0;
		this.initializeMatrix(matrix);
		while (i < lin) {
			String name = getNameOfInstance(instances.get(i).getName()
					.toString());
			ArrayList<String> listOfWords = new ArrayList<String>();
			listOfWords = buildListOfWords(i);

			this.writeMatrix(listOfWords, name, i, matrix, col);

			i++;

		}
		System.out.println(instances.getAlphabet());

		// aqui ter um if para mandar escrever no ecra uma unica matriz
		// this.writeMatrixInConsole(matrix);

		/*
		 * Guardar em ficheiro as features da freq e a matriz com as freq de
		 * todas as liricas
		 */
		this.calculatenormValues();
		this.writeMatrixInConsole(matrix);
		String nameFile2 = "src/Output/CBF_" + arg1 + "_" + arg2 + "_" + NORM;
		//File file3 = new File(nameFile2 + "F.txt");
		//this.writeNamesOfFeaturesNames(file3);
		File file4 = new File(nameFile2 + "Tags.csv");
		this.writeMatrixInFile(matrix, file4);

	}

	private void calculatenormValues() {

		double[][] featmax = new double[1][this.getSizeAlphabet()];
		//double[][] featmin = new double[1][this.getSizeAlphabet()];

		for (int j = 1; j < this.getSizeAlphabet() + 1; j++) {
			double max = 0;
			double min = 0;
			for (int i = 1; i < this.getNumberOfInstances(); i++) {
				if (Double.parseDouble(matrix[i][j]) > max) {
					max = Double.parseDouble(matrix[i][j]);
				}
				//if (Double.parseDouble(matrix[i][j]) < min) {
					//min = Double.parseDouble(matrix[i][j]);
				//}
			}
			featmax[0][j - 1] = max;
			//featmin[0][j - 1] = min;
		}

		//double[][] matrix_aux = new double[lin][col];
		for (int j = 1; j < this.getSizeAlphabet() + 1; j++) {
			for (int i = 1; i < this.getNumberOfInstances(); i++) {
//				matrix_aux[i][j] = (Double.parseDouble(matrix[i][j]) - featmin[0][j - 1])
//						/ (featmax[0][j - 1] - featmin[0][j - 1]);
				matrix[i][j] = Double.toString((Double.parseDouble(matrix[i][j]) / (featmax[0][j - 1])));
			}
		}

		// copiar uma matriz para outra
//		DecimalFormat aux2 = new DecimalFormat("0.##");
//		for (int i = 0; i < lin; i++) {
//			for (int j = 1; j < col; j++) {
//				String aux3 = aux2.format(matrix_aux[i][j]);
//				matrix[i][j] = aux3;
//			}
//
//		}

	}

	
//	private void calculatenormValues() {
//
//		double[][] featmax = new double[1][this.getSizeAlphabet()];
//		double[][] featmin = new double[1][this.getSizeAlphabet()];
//
//		for (int j = 1; j < this.getSizeAlphabet() + 1; j++) {
//			double max = 0;
//			double min = 0;
//			for (int i = 0; i < this.getNumberOfInstances(); i++) {
//				if (Double.parseDouble(matrix[i][j]) > max) {
//					max = Double.parseDouble(matrix[i][j]);
//				}
//				if (Double.parseDouble(matrix[i][j]) < min) {
//					min = Double.parseDouble(matrix[i][j]);
//				}
//			}
//			featmax[0][j - 1] = max;
//			featmin[0][j - 1] = min;
//		}
//
//		double[][] matrix_aux = new double[lin][col];
//		for (int j = 1; j < this.getSizeAlphabet() + 1; j++) {
//			for (int i = 0; i < this.getNumberOfInstances(); i++) {
//				matrix_aux[i][j] = (Double.parseDouble(matrix[i][j]) - featmin[0][j - 1])
//						/ (featmax[0][j - 1] - featmin[0][j - 1]);
//			}
//		}
//
//		// copiar uma matriz para outra
//		DecimalFormat aux2 = new DecimalFormat("0.##");
//		for (int i = 0; i < lin; i++) {
//			for (int j = 1; j < col; j++) {
//				String aux3 = aux2.format(matrix_aux[i][j]);
//				matrix[i][j] = aux3;
//			}
//
//		}
//
//	}
	
	
	/**
	 * execute_tfidf(String[] args)
	 * @param args
	 * @throws IOException
	 */
	
	
	public void execute_tfidf(String arg1, String arg2) throws IOException {

		int i = 0;
		this.initializeMatrix(matrix);
		while (i < lin) {
			String name = getNameOfInstance(instances.get(i).getName()
					.toString());
			ArrayList<String> listOfWords = new ArrayList<String>();
			listOfWords = buildListOfWords(i);

			this.writeMatrix(listOfWords, name, i, matrix, col);

			i++;

		}
		
		System.out.println(instances.getAlphabet());

		// aqui ter um if para mandar escrever no ecra uma unica matriz
		// this.writeMatrixInConsole(matrix);

		/*
		 * Guardar em ficheiro as features da freq e a matriz com as freq de
		 * todas as liricas
		 */
		
		
		this.calculatetfidfValues();
		
		this.writeMatrixInConsole(matrix);
		
		String nameFile2 = "src/Output/CBF_" + arg1 + "_" + arg2 + "_" + TFIDF;
		//File file3 = new File(nameFile2 + "F.txt");
		//this.writeNamesOfFeaturesNames(file3);
		
		File file4 = new File(nameFile2 + "Tags.csv");
		this.writeMatrixInFile(matrix, file4);

	}

	private void calculatetfidfValues() {

		// calcular o maxtfij
		double[][] maxfij = new double[this.getNumberOfInstances()][1];
		for (int i = 1; i <= this.getNumberOfInstances(); i++) {
			double max = 0;
			for (int j = 1; j <= this.getSizeAlphabet(); j++) {
				if (Double.parseDouble(matrix[i][j]) > max) {
					max = Double.parseDouble(matrix[i][j]);
				}
			}
			// ponto 2)
			maxfij[i-1][0] = max;

		}

		
		// idfi = log2(N/ni)
		int N = this.getNumberOfInstances();
		double[][] ni = new double[1][this.getSizeAlphabet()];

		for (int j = 1; j <= this.getSizeAlphabet(); j++) {
			double max = 0;
			for (int i = 1; i <= this.getNumberOfInstances(); i++) {
				if (Double.parseDouble(matrix[i][j]) > max) {
					max = Double.parseDouble(matrix[i][j]);
				}
			}
			// ponto 4)
			ni[0][j - 1] = max;
		}

		double[][] idfi = new double[1][this.getSizeAlphabet()];
		for (int i = 0; i < idfi[0].length; i++) {
			double aux = N / ni[0][i];
			// ponto 5
			idfi[0][i] = this.log(aux, 2);
		}

	
		//System.gc();
		// calcular tfij = fij / maxtfij
		for (int i = 1; i <= this.getNumberOfInstances(); i++) {
			for (int j = 1; j < this.getSizeAlphabet() + 1; j++) {
				double aux = (Double.parseDouble(matrix[i][j])) / (maxfij[i-1][0]);
						// ponto 3
				matrix[i][j] = Double.toString(aux);
			}

		}
				

		// wij = tfi * idfi
		DecimalFormat aux3 = new DecimalFormat("0.##");
		for (int i = 1; i < this.getNumberOfInstances(); i++) {
			for (int j = 1; j < this.getSizeAlphabet()+1; j++) {
				double aux2 = (Double.parseDouble(matrix[i][j]))
						* (idfi[0][j - 1]);
				String aux4 = aux3.format(aux2).replace(",",".");

				matrix[i][j] = aux4;

			}

		}

	}

	// private void calculatetfidfValues() {
	//
	// // calcular o maxtfij
	// double[][] maxfij = new double[this.getNumberOfInstances()][1];
	// for (int i = 0; i < this.getNumberOfInstances(); i++) {
	// double max = 0;
	// for (int j = 1; j < this.getSizeAlphabet(); j++) {
	// if (Double.parseDouble(matrix[i][j]) > max) {
	// max = Double.parseDouble(matrix[i][j]);
	// }
	// }
	// // ponto 2)
	// maxfij[i][0] = max;
	//
	// }
	//
	// // copiar uma matriz para outra
	// String[][] maxtfidf = new String[lin][col];
	// for (int i = 0; i < lin; i++) {
	// for (int j = 0; j < col; j++) {
	// maxtfidf[i][j] = matrix[i][j];
	// }
	//
	// }
	//
	// // calcular tfij = fij / maxtfij
	// for (int i = 0; i < this.getNumberOfInstances(); i++) {
	// for (int j = 1; j < this.getSizeAlphabet() + 1; j++) {
	//
	// double aux = (Double.parseDouble(matrix[i][j]))
	// / (maxfij[i][0]);
	// // ponto 3
	// maxtfidf[i][j] = Double.toString(aux);
	//
	// }
	//
	// }
	//
	// // idfi = log2(N/ni)
	// int N = this.getNumberOfInstances();
	// double[][] ni = new double[1][this.getSizeAlphabet()];
	//
	// for (int j = 1; j < this.getSizeAlphabet() + 1; j++) {
	// double max = 0;
	// for (int i = 0; i < this.getNumberOfInstances(); i++) {
	// if (Double.parseDouble(matrix[i][j]) > max) {
	// max = Double.parseDouble(matrix[i][j]);
	// }
	// }
	// // ponto 4)
	// ni[0][j - 1] = max;
	// }
	//
	// double[][] idfi = new double[1][this.getSizeAlphabet()];
	// for (int i = 0; i < idfi[0].length; i++) {
	// double aux = N / ni[0][i];
	// // ponto 5
	// idfi[0][i] = this.log(aux, 2);
	// }
	//
	// // wij = tfi * idfi
	// DecimalFormat aux3 = new DecimalFormat("0.##");
	// for (int i = 0; i < this.getNumberOfInstances(); i++) {
	// for (int j = 1; j < this.getSizeAlphabet(); j++) {
	// double aux2 = (Double.parseDouble(maxtfidf[i][j]))
	// * (idfi[0][j - 1]);
	// String aux4 = aux3.format(aux2);
	//
	// matrix[i][j] = aux4;
	//
	// }
	//
	// }
	//
	// }

	static double log(double x, int base) {
		return (double) (Math.log(x) / Math.log(base));
	}

	/**
	 * constroi a matriz_bool com 1 caso a feature exista para a lirica ou 0
	 * caso contrário i-linha; j-coluna
	 * 
	 */
	private void calculateBoolValues() {
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[i].length; j++) {
				String s = matrix[i][j];
				if (s != "0")
					matrix[i][j] = "1";

			}
		}

	}

	private void writeNamesOfFeaturesNames(File file) throws IOException {

		boolean isFile = file.exists();

		// se o ficheiro nao existir cria um novo, caso contrario
		// escreve por cima
		if (!isFile) {
			file.createNewFile();
		}

		// abre o ficheiro anterior para escrita
		BufferedWriter out = new BufferedWriter(new FileWriter(file));

		out.write(instances.getAlphabet().toString());

		out.close();

	}

	/**
	 * escreve a matriz num file .txt
	 * 
	 * @throws IOException
	 */
	private void writeMatrixInFile(String[][] matrix, File file)
			throws IOException {

		boolean isFile = file.exists();

		// se o ficheiro nao existir cria um novo, caso contrario
		// escreve por cima
		if (!isFile) {
			file.createNewFile();
		}

		System.out.println(file.getAbsolutePath());
		// abre o ficheiro anterior para escrita
		BufferedWriter out = new BufferedWriter(new FileWriter(file));

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (j == matrix[i].length-1) {
					out.write(matrix[i][j]);
				} else out.write(matrix[i][j] + ",");
				//out.write(matrix[i][j] + " ");
			}
			out.newLine();
		}
		out.close();
	}

	/**
	 * inicializa uma matriz a zeros
	 */
	private void initializeMatrix(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				String s = matrix[i][j];
				if (s == null)
					matrix[i][j] = "0";

			}
		}
	}

	/**
	 * escreve a matriz criada no ecra
	 */
	private void writeMatrixInConsole(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * escreve numa matriz com nºlinhas igual ao nº de instancias e nºcolunas
	 * igual ao nº de features+1
	 * 
	 * @param listOfWords
	 * @param name
	 *            (identifica o nome da instancia atual)
	 * @param i
	 *            (identifica a instancia atual)
	 * @throws IOException 
	 */
	private void writeMatrix(ArrayList<String> listOfWords, String name, int i,
			String[][] matrix, int col) throws IOException {
		ArrayList<String> al = this.initializeQuadrantFile();
		Iterator<String> it = listOfWords.iterator();
		while (it.hasNext()) {
			String line = it.next();
			//System.out.println(line);
			int ind1 = getIndex(line, "(") + 1;
			int ind2 = getIndex(line, ")");
			int ind3 = getIndex(line, "=") + 1;
			int ind4 = getIndex(line, ".");
			String pos = line.substring(ind1, ind2);
			String freq = line.substring(ind3, ind4);
			
			// tirar o .txt
			int ind5 = getIndex(name, ".");
			matrix[0][0] = "Nome";
			
			
			matrix[0][Integer.parseInt(pos) + 1] = strWords[Integer.parseInt(pos)];
			
			matrix[i+1][0] = name.substring(0, ind5);

			matrix[0][col-1] = "Classe"; 
			String quadrant = this.getQuadrant(al, matrix[i+1][0]);
			matrix[i+1][col-1] = quadrant;
			matrix[i+1][Integer.parseInt(pos) + 1] = freq;
						
		}
	}

	/**
	 * devolve o indice de um dado caracter numa string
	 * 
	 * @param line
	 * @param ch
	 * @return line.indexOf(ch)
	 */
	private int getIndex(String line, String ch) {
		return line.indexOf(ch);
	}

	/**
	 * os dados de cada instancia i entao armazenados na forma
	 * "defense(68)=1.0 \n son(69)=1.0 \n ..." na mesma variavel. este metodo
	 * pretende colocar dentro de um arraylist em que cada uma das suas entradas
	 * corresponde a uma palavra e a sua frequencia ex: defense(68)=1.0
	 * 
	 * @param int i (instancia i)
	 * @return ArrayList<String> lw
	 */
	private ArrayList<String> buildListOfWords(int i) {
		// TODO Auto-generated method stub
		String list[] = instances.get(i).getData().toString().split("\n");
		int len = list.length;
		ArrayList<String> lw = new ArrayList<String>();
		for (int j = 0; j < len; j++) {
			lw.add(list[j]);
		}
		return lw;
	}

	
	/**
	 * MUDAR AQUI O NOME DA PASTA
	 * 
	 */
	
	
	/**
	 * devolve o nome do file da lyric que corresponde ao id da musica. as
	 * lyrics tem obrigatoriamente de estar armazenadas num directorio origem
	 * 
	 * @param name
	 * @return String data[1]
	 */
	private String getNameOfInstance(String name) {
		// atencao a esta instrucao que so funciona se o file estiver na pasta
		// origem/
		String data[] = name.split("Origem");
		//String data[] = name.split("771");
		return data[1];
	}

	/**
	 * devolve o nº de palavras de todos os documentos exceptuando as stopwords.
	 * estas palavras serao as features associadas a cada instancia
	 * 
	 * @return int instances.getAlphabet().size();
	 */
	private int getSizeAlphabet() {
		return instances.getAlphabet().size();
	}

	/**
	 * devolve o nº de letras no directorio origem
	 * 
	 * @return int instances.size()
	 */
	private int getNumberOfInstances() {
		return instances.size();
	}
	
	/**
	 * MUDAR AQUI O NOME DO FILE
	 * @return
	 * @throws IOException
	 */
	private ArrayList<String> initializeQuadrantFile() throws IOException {
		FileReader fileReader = new FileReader("src/AuxiliarFiles/Classes-Quadrantes.txt");
		BufferedReader in = new BufferedReader(fileReader);

		String thisLine;
		// String[] data;
		ArrayList<String> al = new ArrayList<String>();

		// guarda cada linha de file (thisLine) numa string linha por linha
		// (lyric_text)
		while ((thisLine = in.readLine()) != null) {
			al.add(thisLine);

		}

		return al;
	}

	private String getQuadrant(ArrayList<String> al, String name) {
		String quadrant = "";

		// estas 2 linhas seguintes foram feitas so para a extracao de CBF das frases
		//String[] dat = name.split("/");
		//name = dat[1];		
			
		
		
		Iterator<String> it = al.iterator();
		while (it.hasNext()) {
			String obj = it.next();
			String[] data;
			data = obj.split(" ");

			if (data[0].equals(name)) {
				// System.out.println(data[0]+" "+data[1]);
				quadrant = data[1];
				return quadrant;
			}

		}
		return "Nao foi encontrado...";

	}

	

}
