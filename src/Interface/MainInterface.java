package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import AuxiliarFiles.WriteCSVFinal;
import CBF.CBF_Initial;
import CapitalLetters.CapitalLetters_Initial;
import CombinedFeatures.CombinedFeatures;
import GI.Initial_GI;
import StanfordPosTagger.SPT_Initial;
import StructuralFeatures.countTitle;
import Synesketch.mainApp_Synesketch;

@SuppressWarnings("serial")
public class MainInterface extends JFrame{
	private JButton semantic_button, stylistic_button, directoryButton, content_button, structural_button,standardpostagger, all_features;
	JPanel panel;
	JLabel warning;
	String sourceFolder;
	
	public MainInterface(String sourceFolder1) {
		super();
		this.sourceFolder = sourceFolder1;
		semantic_button = new JButton("Features Semanticas");
		stylistic_button = new JButton("Features Estilisticas");
		content_button = new JButton("Content-Based Features");
		structural_button = new JButton("Structural Based Features");
		standardpostagger = new JButton("Representacao "+ "POSTags");
		all_features = new JButton("All features");
		
		directoryButton = new JButton("Escolher pasta");
		
		warning = new JLabel("Se nao for escolhida diretoria, vai ser usada a pasta Origem por omissão.");
		
		semantic_button.setBounds(30,20,200,25);
		stylistic_button.setBounds(240,20,200,25);
		content_button.setBounds(30,60,200,25);
		directoryButton.setBounds(30,100,200,25);
		structural_button.setBounds(240,60,200,25);
		standardpostagger.setBounds(240,100,200,25);
		all_features.setBounds(150,130,200,25);
		
		ContentFeatures C = new ContentFeatures(sourceFolder);
		
		warning.setBounds(25,150,450,25);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.add(semantic_button);
		panel.add(stylistic_button);
		panel.add(content_button);
		panel.add(structural_button);
		panel.add(standardpostagger);
		panel.add(directoryButton);
		panel.add(warning);
		panel.add(all_features);
		semantic_button.addActionListener(new SemanticButtonListener(this));
		stylistic_button.addActionListener(new StylisticButtonListener(this));
		content_button.addActionListener(new MainInterface.CBF_ButtonListener(C));
		structural_button.addActionListener(new StructuralButtonListener(this));
		standardpostagger.addActionListener(new MainInterface.SPT_ButtonListener());
		directoryButton.addActionListener(new DirectoryButtonListener());
		all_features.addActionListener(new AllFeaturesListener());
		
		
		
		this.add(panel);
	}
	
	
	
	private class SPT_ButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				SPT_Initial initial = new SPT_Initial(false,sourceFolder,null,2);
				JOptionPane.showMessageDialog(null, "Todas StandardPosTagger features extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
	}
	
	
	private class CBF_ButtonListener implements ActionListener {	
		ContentFeatures content_features;

		public CBF_ButtonListener(ContentFeatures content) {
			this.content_features = content;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			ContentSelector content_selector = null;
			content_selector = new ContentSelector(sourceFolder,this.content_features);
			content_selector.setTitle("Selecao de op�oes de conteudo");
			content_selector.setSize(400,220);
			content_selector.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			content_selector.setVisible(true);			
		}
	}
	
	
	public static void main(String[] args) {
		if (args.length == 3) {
			String tipoExtracao = args[0];
			String inputFile = args[1];
			String outputFile = args[2];	
			HandleRequest(tipoExtracao,inputFile,outputFile);
		}
		else if (args.length == 4) {
			if (!args[0].equals("features_titulo")) {
				System.out.println("O metodo de extracao com 4 argumentos tem que ser \"features_titulo\"");
				System.out.println("Uso: \"features_titulo\" inputFile outputFile tituloMusica");
			}
			else {
				String inputFile = args[1];
				String outputFile = args[2];
				String titulo = args[3];
				countTitle numero_titulo = new countTitle(titulo,inputFile,outputFile);
			}
		}
		else if (args.length == 5) {
			if (!args[0].equals("features_cbf")) {
				System.out.println("O metodo de extracao com 5 argumentos tem que ser \"features_cbf\"");
				System.out.println("Uso: \"features_cbf\" inputFile arg1 arg2 arg3");
				System.out.println("arg1 pode ser: 'unig', 'big', 'trig'");
				System.out.println("arg2 pode ser: 'nada', 'st', 'sw', 'st+sw'");
				System.out.println("arg3 pode ser: 'freq', 'bool', 'tfidf', 'norm'");
				System.out.println("O output file vai ser a combinacao, por exemplo 'unig_nada_freq.csv'");
			}
			else {
				String inputFolder = args[1];
				ArrayList<String> lista_arg3 = new ArrayList<String> (Arrays.asList("unig","big","trig","4grams","5grams"));
				ArrayList<String> lista_arg4 = new ArrayList<String> (Arrays.asList("nada","st","sw","st+sw"));
				ArrayList<String> lista_arg5 = new ArrayList<String> (Arrays.asList("freq","bool","tfidf","norm"));

				String arg3 = args[2];
				String arg4 = args[3];
				String arg5 = args[4];
				if (!lista_arg3.contains(arg3) || !lista_arg4.contains(arg4) || !lista_arg5.contains(arg5)) {
					if (!lista_arg3.contains(arg3)) {
						System.out.println("Argumento 1 incorreto.");
						System.out.println("arg1 pode ser: 'unig', 'big', 'trig','4grams','5grams'");
					}
					if (!lista_arg4.contains(arg4)) {
						System.out.println("Argumento 2 incorreto.");
						System.out.println("arg2 pode ser: 'nada', 'st', 'sw', 'st+sw'");
					}
					if (!lista_arg5.contains(arg5)) {
						System.out.println("Argumento 3 incorreto.");
						System.out.println("arg3 pode ser: 'freq', 'bool', 'tfidf', 'norm'");
					}														
				}				
				else {
					try {
						CBF_Initial initial = new CBF_Initial(inputFolder,arg3,arg4,arg5, false);				
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}
			}
		}
		else {
			//System.out.println("correr normal!");
			MainInterface main_frame = new MainInterface(null);
			main_frame.setTitle("Janela principal do programa");
			main_frame.setSize(475,225);
			main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			main_frame.setVisible(true);
		}
	}
	
	private class DirectoryButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean flag = true;

			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setDialogTitle("Escolha a pasta com os ficheiros de origem: ");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			while(flag) {
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					if (jfc.getSelectedFile().isDirectory()) {
						//System.out.println("You selected the directory: " + jfc.getSelectedFile());
						if (jfc.getSelectedFile().list().length > 0){
							sourceFolder = jfc.getSelectedFile().getAbsolutePath();
							flag = false;
						}
						else {
							JOptionPane.showMessageDialog(null, "A pasta escolhida nao tem ficheiros, por favor escolha outra", "Aviso", JOptionPane.PLAIN_MESSAGE);	
						}
					}
				}
				else if (returnValue == JFileChooser.CANCEL_OPTION) {
					flag = false;
				}
			}
			
		}		
	}
	
	private class AllFeaturesListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("Button clicked");
			mainApp_Synesketch synesktech = new mainApp_Synesketch();
			WriteCSVFinal writecsv = new WriteCSVFinal();
			// System.out.println("Directory");
			try {
				// semantic
				String inputFile = sourceFolder;
				
				
				
				
			    // gazeteers
				CombinedFeatures gaz = new CombinedFeatures(false,1,false,false,false,false,inputFile,null);
				CombinedFeatures gaz1 = new CombinedFeatures(false,2,false,false,false,false,inputFile,null);
				CombinedFeatures gaz2 = new CombinedFeatures(false,3,false,false,false,false,inputFile,null);
				CombinedFeatures gaz3 = new CombinedFeatures(false,4,false,false,false,false,inputFile,null);
				CombinedFeatures gaz4 = new CombinedFeatures(false,5,false,false,false,false,inputFile,null);
				
				CombinedFeatures dal = new CombinedFeatures(false,0,true,false,false,false,inputFile,null); // dal anew
				CombinedFeatures anew = new CombinedFeatures(false,0,false,true,false,false,inputFile,null); // dal anew
				
				
				// now we have to combine gazeteers and dal and anew
				
				SemanticFeatures.combineGazeteers("src/Output");
				
				synesktech.readDirectory(inputFile, null); // synesktech
				System.out.println(inputFile);
				if (inputFile == null) {
					Initial_GI initial= new Initial_GI("src/Origem",null); // features_gi
				}
				else {
					Initial_GI initial= new Initial_GI(inputFile,null); // features_gi
				}
				//writecsv.WriteSemantic(null,null,null,null,null); // then we write to output file
				
				// stylistic
				
				CombinedFeatures slang  = new CombinedFeatures(false,0,false,false,false,true,inputFile,null); 
				CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(false,inputFile,null);
				StylisticFeatures.combineStylistic("src/Output");
				
				// now we have to extract the CBF
				
				CBF_Initial cbf = new CBF_Initial(sourceFolder,"unig","nada","freq", false);
				cbf = new CBF_Initial(sourceFolder,"unig","nada","tfidf", false);
				cbf = new CBF_Initial(sourceFolder,"unig","st","freq", false);
				cbf = new CBF_Initial(sourceFolder,"unig","st","tfidf", false);
				cbf = new CBF_Initial(sourceFolder,"unig","st_sw","freq", false);
				cbf = new CBF_Initial(sourceFolder,"unig","st_sw","tfidf", false);
				cbf = new CBF_Initial(sourceFolder,"big","nada","freq", false);
				cbf = new CBF_Initial(sourceFolder,"big","nada","tfidf", false);
				cbf = new CBF_Initial(sourceFolder,"big","st","freq", false);
				cbf = new CBF_Initial(sourceFolder,"big","st","tfidf", false);
				cbf = new CBF_Initial(sourceFolder,"trig","nada","freq", false);
				cbf = new CBF_Initial(sourceFolder,"trig","nada","tfidf", false);
				cbf = new CBF_Initial(sourceFolder,"trig","st","freq", false);
				cbf = new CBF_Initial(sourceFolder,"trig","st","tfidf", false);
				
				// now with pos
				cbf = new CBF_Initial(sourceFolder,"unig","nada","tfidf", true);
				cbf = new CBF_Initial(sourceFolder,"big","nada","freq", true);
				cbf = new CBF_Initial(sourceFolder,"big","nada","tfidf", true);
				cbf = new CBF_Initial(sourceFolder,"trig","nada","freq", true);
				cbf = new CBF_Initial(sourceFolder,"trig","nada","tfidf", true);
				cbf = new CBF_Initial(sourceFolder,"4grams","nada","freq", true);
				cbf = new CBF_Initial(sourceFolder,"5grams","nada","freq", true);
				
				
			} catch (IOException except) {
				// TODO Auto-generated catch block						
				except.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		}
		

	}
	
	public static void HandleRequest(String tipoExtracao, String inputFile, String outputFile) {
		String [] listaOpcoes= {"all_features","stylistic_features","semantic_features","features_gi","features_synesktech","features_dal_anew","features_gazeteers","features_slang","features_capitalletters","features_standardPOS","features_cbf","features_titulo"};
		File file = new File(inputFile);
			
		switch(tipoExtracao) {
			case "features_gi":
			try {
				Initial_GI initial= new Initial_GI(inputFile,outputFile);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "features_synesktech":
				mainApp_Synesketch ma = new mainApp_Synesketch();
				if (file.exists()){
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							ma.readDirectory(inputFile, outputFile);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							ma.readFile(inputFile, outputFile);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}				
				break;
			case "features_dal_anew":
				if (file.exists()){
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							CombinedFeatures capitalLetters = new CombinedFeatures(false,0,true,false,false, false,inputFile,outputFile); // dal
							CombinedFeatures capitalletters = new CombinedFeatures(false,0,false,true,false,false, inputFile, outputFile); // anew
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							CombinedFeatures dal = new CombinedFeatures(true,0,true,false,false,false,inputFile,outputFile); // dal 
							CombinedFeatures anew = new CombinedFeatures(true,0,false,true,false,false,inputFile,outputFile); // anew
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}							
				break;
			case "features_gazeteers":
				if (file.exists()){
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							CombinedFeatures capitalLetters = new CombinedFeatures(false,1,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters1 = new CombinedFeatures(false,2,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters2 = new CombinedFeatures(false,3,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters3 = new CombinedFeatures(false,4,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters4 = new CombinedFeatures(false,5,false,false,false,false,inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							CombinedFeatures capitalLetters = new CombinedFeatures(true,1,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters1 = new CombinedFeatures(true,2,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters2 = new CombinedFeatures(true,3,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters3 = new CombinedFeatures(true,4,false,false,false,false,inputFile,outputFile);
							CombinedFeatures capitalLetters4 = new CombinedFeatures(true,5,false,false,false,false,inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			case "features_slang":
				if (file.exists()){
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							CombinedFeatures initial_WD  = new CombinedFeatures(false,0,false,false,false,true,inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							CombinedFeatures initial_WD  = new CombinedFeatures(true,0,false,false,false,true,inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}			
				break;
			case "features_capitalletters":				
				if (file.exists()){
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(false,inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(true,inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}			
				break;
			case "features_standardPOS":
				if (file.exists()){
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							SPT_Initial spt_initial = new SPT_Initial(false,inputFile,outputFile,1);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							SPT_Initial spt_initial = new SPT_Initial(true,inputFile,outputFile,1);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			case "features_cbf":	
				System.out.println("Para usar \"features_cbf\" tem que fazer o seguinte:");
				System.out.println("Uso: \"features_cbf\" inputFile arg1 arg2 arg3");
				System.out.println("arg1 pode ser: 'unig', 'big', 'trig'");
				System.out.println("arg2 pode ser: 'nada', 'st', 'sw', 'st+sw'");
				System.out.println("arg3 pode ser: 'freq', 'bool', 'tfidf', 'norm'");
				System.out.println("O output file vai ser a combinacao, por exemplo 'unig_nada_freq.csv'");
				break;
			case "features_titulo":
				System.out.println("O titulo tem o seguinte uso: \nUso: \"features_titulo\" inputFile outputFile tituloMusica");
				break;
			case "semantic_features":
				if (file.exists()){
					mainApp_Synesketch synesktech = new mainApp_Synesketch();
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							
							CombinedFeatures gaz = new CombinedFeatures(false,1,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz1 = new CombinedFeatures(false,2,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz2 = new CombinedFeatures(false,3,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz3 = new CombinedFeatures(false,4,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz4 = new CombinedFeatures(false,5,false,false,false,false,inputFile,outputFile);
							
							CombinedFeatures dal = new CombinedFeatures(false,0,true,false,false,false,inputFile,null); // dal
							CombinedFeatures anew = new CombinedFeatures(false,0,false,true,false,false,inputFile,null); // anew

							synesktech.readDirectory(inputFile, null); // synesktech
							Initial_GI initial= new Initial_GI(inputFile,null); // features_gi
							WriteCSVFinal writecsv = new WriteCSVFinal();
							writecsv.WriteSemantic(null,null,null,null,outputFile); // then we write to output file
							System.out.printf("All semantic features of folder %s outputed to %s\n",inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block						
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							
							CombinedFeatures gaz_file = new CombinedFeatures(true,1,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file1 = new CombinedFeatures(true,2,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file2 = new CombinedFeatures(true,3,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file3 = new CombinedFeatures(true,4,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file4 = new CombinedFeatures(true,5,false,false,false,false,inputFile,null);
				
							CombinedFeatures dal = new CombinedFeatures(true,0,true,false,false,false,inputFile,null); // dal
							CombinedFeatures anew = new CombinedFeatures(true,0,false,true,false,false,inputFile,null); // anew
							synesktech.readFile(inputFile, null); 
							Initial_GI initial= new Initial_GI(inputFile,null);
							WriteCSVFinal writecsv = new WriteCSVFinal();
							writecsv.WriteSemantic(null,null,null,null,outputFile); // then we write to output file
							System.out.printf("All semantic features of file %s outputed to %s\n",inputFile,outputFile);													
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}								
				break;
			case "stylistic_features":
				if (file.exists()){
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							CombinedFeatures slang  = new CombinedFeatures(false,0,false,false,false,true,inputFile,null); 
							CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(false,inputFile,null);							
							WriteCSVFinal writecsv = new WriteCSVFinal();
							writecsv.WriteStylistic(null,null,outputFile); // then we write to output file
							System.out.printf("All stylistic features of folder %s outputed to %s\n",inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block						
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							CombinedFeatures slang_file  = new CombinedFeatures(true,0,false,false,false,true,inputFile,null);
							CapitalLetters_Initial capitalLetters_file = new CapitalLetters_Initial(true,inputFile,null);
							WriteCSVFinal writecsv = new WriteCSVFinal();
							writecsv.WriteStylistic(null,null,outputFile); // then we write to output file
							System.out.printf("All stylistic features of file %s outputed to %s\n",inputFile,outputFile);													
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}	
				break;
			case "all_features":
				if (file.exists()){
					mainApp_Synesketch synesktech = new mainApp_Synesketch();
					WriteCSVFinal writecsv = new WriteCSVFinal();
					if (file.isDirectory()) {
						// System.out.println("Directory");
						try {
							// semantic
							
						    // gazeteers
							CombinedFeatures gaz = new CombinedFeatures(false,1,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz1 = new CombinedFeatures(false,2,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz2 = new CombinedFeatures(false,3,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz3 = new CombinedFeatures(false,4,false,false,false,false,inputFile,outputFile);
							CombinedFeatures gaz4 = new CombinedFeatures(false,5,false,false,false,false,inputFile,outputFile);
							
							CombinedFeatures dal = new CombinedFeatures(false,0,true,false,false,false,inputFile,null); // dal anew
							CombinedFeatures anew = new CombinedFeatures(false,0,false,true,false,false,inputFile,null); // dal anew
							
							
							// now we have to combine gazeteers and dal and anew
							
							SemanticFeatures.combineGazeteers("src/Output");
							
							synesktech.readDirectory(inputFile, null); // synesktech
							Initial_GI initial= new Initial_GI(inputFile,null); // features_gi
							//writecsv.WriteSemantic(null,null,null,null,null); // then we write to output file
							
							// stylistic
							
							CombinedFeatures slang  = new CombinedFeatures(false,0,false,false,false,true,inputFile,null); 
							CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(false,inputFile,null);
							StylisticFeatures.combineStylistic("src/Output");
							
							
							System.out.printf("All features of folder %s outputed to %s\n",inputFile,outputFile);
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block						
							e.printStackTrace();
						}
					}
					else if (file.isFile()) {
						//System.out.println("File");
						try {
							// semantic
							
							
							CombinedFeatures gaz_file = new CombinedFeatures(true,1,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file1 = new CombinedFeatures(true,2,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file2 = new CombinedFeatures(true,3,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file3 = new CombinedFeatures(true,4,false,false,false,false,inputFile,null);
							CombinedFeatures gaz_file4 = new CombinedFeatures(true,5,false,false,false,false,inputFile,null);
							
							CombinedFeatures dal_file = new CombinedFeatures(true,0,true,false,false,false,inputFile,null); // dal
							CombinedFeatures anew_file = new CombinedFeatures(true,0,false,true,false,false,inputFile,null); // anew
							synesktech.readFile(inputFile, null); 
							Initial_GI initial= new Initial_GI(inputFile,null);
							writecsv.WriteSemantic(null,null,null,null,null); // then we write to output file
													
							
							// stylistic
							
							CombinedFeatures slang_file  = new CombinedFeatures(true,0,false,false,false,true,inputFile,null);
							CapitalLetters_Initial capitalLetters_file = new CapitalLetters_Initial(true,inputFile,null);
							writecsv.WriteStylistic(null,null,null); // then we write to output file
							
							writecsv.WriteAll(outputFile); // then we write to output file
							System.out.printf("All features of file %s outputed to %s\n",inputFile,outputFile);													
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}		
				break;
			default:
				System.out.println("Opcao errada! As opcoes sao as seguintes");
				for (String s : listaOpcoes) {
					System.out.println(s);
				}
				break;
		}
	}
	
}
	

	
