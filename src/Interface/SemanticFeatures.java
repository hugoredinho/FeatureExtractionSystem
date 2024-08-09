package Interface;

import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import CombinedFeatures.CombinedFeatures;
import GI.Initial_GI;
import Synesketch.mainApp_Synesketch;
import AuxiliarFiles.WriteCSVFinal;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@SuppressWarnings("serial")
public class SemanticFeatures extends JFrame{
	JPanel panel_semantic;
	JButton allfeatures, features_GI, featuresSynesketch, featuresDAL_ANEW, featuresGazeteers, featuresWarriner, featuresNRCVAD, back;
	JLabel label;
	String sourceFolder;
	public SemanticFeatures(String sourceFolder) {
		super();
		this.sourceFolder = sourceFolder;
		panel_semantic = new JPanel();
		panel_semantic.setLayout(null);
		
		allfeatures = new JButton("Todas as features");
		features_GI = new JButton("Features GI");
		featuresSynesketch = new JButton("Features Synesktech");
		featuresDAL_ANEW = new JButton("Features DAL_ANEW");
		featuresGazeteers = new JButton("Features Gazeteers");
		featuresWarriner = new JButton("Features Warrinner");
		featuresNRCVAD = new JButton("Features NRCVAD");
		
		back = new JButton("Back");
		
		label = new JLabel("Quais features semanticas deseja extrair?");
		
		label.setBounds(75,10,300,25);
		allfeatures.setBounds(25,40,160,25);
		features_GI.setBounds(200,40,160,25);
		featuresSynesketch.setBounds(25,80,160,25);
		featuresDAL_ANEW.setBounds(200,80,160,25);
		featuresGazeteers.setBounds(200,120,160,25);
		featuresWarriner.setBounds(25,120,160,25);
		featuresNRCVAD.setBounds(25,160,160,25);

		back.setBounds(200,180,160,25);
		
		featuresGazeteers.addActionListener(new SemanticFeatures.GazeteersButtonListener());
		featuresDAL_ANEW.addActionListener(new SemanticFeatures.DAL_ANEWButtonListener());
		features_GI.addActionListener(new SemanticFeatures.GI_ButtonListener());
		featuresSynesketch.addActionListener(new SemanticFeatures.SynesketchButtonListener());
		allfeatures.addActionListener(new SemanticFeatures.AllFeaturesListener());
		featuresWarriner.addActionListener(new SemanticFeatures.WarrinerButtonListener());
		featuresNRCVAD.addActionListener(new SemanticFeatures.NRCVADButtonlistener());
		back.addActionListener(new BackButtonListener(this));
 
		panel_semantic.add(label);
		panel_semantic.add(allfeatures);
		panel_semantic.add(features_GI);
		panel_semantic.add(featuresSynesketch);
		panel_semantic.add(featuresDAL_ANEW);
		panel_semantic.add(featuresGazeteers);
		panel_semantic.add(featuresWarriner);
		panel_semantic.add(featuresNRCVAD);
		panel_semantic.add(back);
		
		this.add(panel_semantic);
	}
	private class GazeteersButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CombinedFeatures initial_gazeteers = new CombinedFeatures(false,1,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers1 = new CombinedFeatures(false,2,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers2 = new CombinedFeatures(false,3,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers3 = new CombinedFeatures(false,4,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers4 = new CombinedFeatures(false,5,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
			
				combineGazeteers("src/Output");
				JOptionPane.showMessageDialog(null, "Features Gazeteers extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	private class DAL_ANEWButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CombinedFeatures dal  = new CombinedFeatures(false,0,true,false,false,false,false,sourceFolder,null); // dal
				CombinedFeatures anew  = new CombinedFeatures(false,0,false,true,false,false,false,sourceFolder,null); // anew
				JOptionPane.showMessageDialog(null, "Features DAL_ANEW extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class WarrinerButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CombinedFeatures initial  = new CombinedFeatures(false,0,false,false,true,false,false,sourceFolder,null); 
				JOptionPane.showMessageDialog(null, "Features Warriner extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class NRCVADButtonlistener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CombinedFeatures NRCvad = new CombinedFeatures(false,6,false,false,false,false,true,sourceFolder,null); // false true vai ser gazeteers
				JOptionPane.showMessageDialog(null, "Features NRC VAD extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	private class GI_ButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				Initial_GI initial= new Initial_GI(sourceFolder);
				JOptionPane.showMessageDialog(null, "Features GI extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	private class SynesketchButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
				mainApp_Synesketch ma = new mainApp_Synesketch();
				try {
					ma.readDirectory(sourceFolder,null);
					JOptionPane.showMessageDialog(null, "Features Synesketch extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}		
		}
	
	
	

	private class AllFeaturesListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//CombinedFeatures initial_GAZ  = new CombinedFeatures(false,1,false,false,false,false,sourceFolder,null);
				CombinedFeatures initial_gazeteers = new CombinedFeatures(false,1,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers1 = new CombinedFeatures(false,2,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers2 = new CombinedFeatures(false,3,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers3 = new CombinedFeatures(false,4,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers4 = new CombinedFeatures(false,5,false,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				
				CombinedFeatures initial_dal  = new CombinedFeatures(false,0,true,false,false,false,false,sourceFolder,null); // dal
				CombinedFeatures initial_anew  = new CombinedFeatures(false,0,false,true,false,false,false,sourceFolder,null); // dal
				CombinedFeatures initial_Warriner = new CombinedFeatures(false,0,false,false,true,false,false,sourceFolder,null); 
				CombinedFeatures initial_NRCVAD = new CombinedFeatures(false,0,false,false,false,false,true,sourceFolder,null); 
				
				combineGazeteers("src/Output");
				Initial_GI initial_GI = new Initial_GI(sourceFolder);
				mainApp_Synesketch ma = new mainApp_Synesketch();
				ma.readDirectory(sourceFolder,null);
				WriteCSVFinal writecsv = new WriteCSVFinal();
				writecsv.WriteSemantic(null,null,null,null,null);
				JOptionPane.showMessageDialog(null, "Todas as features semanticas extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		}
	}
	
	public static void combineGazeteers(String sourceFolder) throws IOException {
	    System.out.println("Source folder " + sourceFolder);
	    File folder = new File(sourceFolder);
	    File[] listOfFiles = folder.listFiles();

	    // A map to hold the combined data
	    Map<String, Map<String, String>> combinedData = new LinkedHashMap<>();
	    
	    Set<String> seenIds = new HashSet<>();

	    // The predefined header order
	    /*List<String> headers = Arrays.asList("Id",
	        "AvgValence_Q1", "AvgArousal_Q1", "AvgValence_Q2", "AvgArousal_Q2",
	        "AvgValence_Q3", "AvgArousal_Q3", "AvgValence_Q4", "AvgArousal_Q4",
	        "#Count_Q1", "#Count_Q2", "#Count_Q3", "#Count_Q4", "#Count_NRCVAD",
	        "AvgValence_All", "AvgArousal_All", "AvgValence_Dal", "AvgArousal_Dal", "AvgDominance_Dal",
	        "AvgValence_Anew", "AvgArousal_Anew", "AvgDominance_Anew", "AvgValence_NRCVAD", "AvgArousal_NRCVAD"
	    );*/

		List<String> headers = Arrays.asList("Id",
	        "AvgValence_Q1", "AvgArousal_Q1", "StdDevValence_Q1", "SkewnessValence_Q1", "KurtosisValence_Q1", "MaxValence_Q1", "MinValence_Q1", "MedianValence_Q1",
			"StdDevArousal_Q1", "SkewnessArousal_Q1", "KurtosisArousal_Q1", "MaxArousal_Q1", "MinArousal_Q1", "MedianArousal_Q1",
			
			"AvgValence_Q2", "AvgArousal_Q2", "StdDevValence_Q2", "SkewnessValence_Q2", "KurtosisValence_Q2", "MaxValence_Q2", "MinValence_Q2", "MedianValence_Q2",
			"StdDevArousal_Q2", "SkewnessArousal_Q2", "KurtosisArousal_Q2", "MaxArousal_Q2", "MinArousal_Q2", "MedianArousal_Q2",

	        "AvgValence_Q3", "AvgArousal_Q3", "StdDevValence_Q3", "SkewnessValence_Q3", "KurtosisValence_Q3", "MaxValence_Q3", "MinValence_Q3", "MedianValence_Q3",
			"StdDevArousal_Q3", "SkewnessArousal_Q3", "KurtosisArousal_Q3", "MaxArousal_Q3", "MinArousal_Q3", "MedianArousal_Q3",
			
			"AvgValence_Q4", "AvgArousal_Q4", "StdDevValence_Q4", "SkewnessValence_Q4", "KurtosisValence_Q4", "MaxValence_Q4", "MinValence_Q4", "MedianValence_Q4",
			"StdDevArousal_Q4", "SkewnessArousal_Q4", "KurtosisArousal_Q4", "MaxArousal_Q4", "MinArousal_Q4", "MedianArousal_Q4",

			"AvgValence_All", "AvgArousal_All", "StdDevValence_All", "SkewnessValence_All", "KurtosisValence_All", "MaxValence_All", "MinValence_All", "MedianValence_All",
			"StdDevArousal_All", "SkewnessArousal_All", "KurtosisArousal_All", "MaxArousal_All", "MinArousal_All", "MedianArousal_All",

			// dal, anew, warriner and NRC VAD also have dominance
			"AvgValence_Dal", "AvgArousal_Dal", "AvgDominance_Dal", "StdDevValence_Dal", "SkewnessValence_Dal", "KurtosisValence_Dal", "MaxValence_Dal", "MinValence_Dal", "MedianValence_Dal",
			"StdDevArousal_Dal", "SkewnessArousal_Dal", "KurtosisArousal_Dal", "MaxArousal_Dal", "MinArousal_Dal", "MedianArousal_Dal", 
			"StdDevDominance_Dal", "SkewnessDominance_Dal", "KurtosisDominance_Dal", "MaxDominance_Dal", "MinDominance_Dal", "MedianDominance_Dal",

			"AvgValence_Anew", "AvgArousal_Anew", "AvgDominance_Anew", "StdDevValence_Anew", "SkewnessValence_Anew", "KurtosisValence_Anew", "MaxValence_Anew", "MinValence_Anew", "MedianValence_Anew",
			"StdDevArousal_Anew", "SkewnessArousal_Anew", "KurtosisArousal_Anew", "MaxArousal_Anew", "MinArousal_Anew", "MedianArousal_Anew", 
			"StdDevDominance_Anew", "SkewnessDominance_Anew", "KurtosisDominance_Anew", "MaxDominance_Anew", "MinDominance_Anew", "MedianDominance_Anew",

			"AvgValence_Warriner", "AvgArousal_Warriner", "AvgDominance_Warriner", "StdDevValence_Warriner", "SkewnessValence_Warriner", "KurtosisValence_Warriner", "MaxValence_Warriner", "MinValence_Warriner", "MedianValence_Warriner",
			"StdDevArousal_Warriner", "SkewnessArousal_Warriner", "KurtosisArousal_Warriner", "MaxArousal_Warriner", "MinArousal_Warriner", "MedianArousal_Warriner", 
			"StdDevDominance_Warriner", "SkewnessDominance_Warriner", "KurtosisDominance_Warriner", "MaxDominance_Warriner", "MinDominance_Warriner", "MedianDominance_Warriner",

			"AvgValence_NRCVAD", "AvgArousal_NRCVAD", "AvgDominance_NRCVAD", "StdDevValence_NRCVAD", "SkewnessValence_NRCVAD", "KurtosisValence_NRCVAD", "MaxValence_NRCVAD", "MinValence_NRCVAD", "MedianValence_NRCVAD",
			"StdDevArousal_NRCVAD", "SkewnessArousal_NRCVAD", "KurtosisArousal_NRCVAD", "MaxArousal_NRCVAD", "MinArousal_NRCVAD", "MedianArousal_NRCVAD", 
			"StdDevDominance_NRCVAD", "SkewnessDominance_NRCVAD", "KurtosisDominance_NRCVAD", "MaxDominance_NRCVAD", "MinDominance_NRCVAD", "MedianDominance_NRCVAD",

	        "#Count_Q1", "#Count_Q2", "#Count_Q3", "#Count_Q4", "#Count_All", "#Count_Dal", "#Count_Anew", "#Count_Warriner", "#Count_NRCVAD"

	    );

	    List<String> gazeteerFiles = Arrays.asList(
	        "Gazeteers_Q1", "Gazeteers_Q2", "Gazeteers_Q3", "Gazeteers_Q4", "Gazeteers_All"
	    );
	    List<String> specialFiles = Arrays.asList("Anew", "Dal","NRCVAD","Warriner");

	    for (File file : listOfFiles) {
	        if (file.isFile()) {
	            String baseName = file.getName().replace(".csv", "");
	            String suffix = baseName.split("_").length > 1 ? baseName.split("_")[1] : baseName;
	            
	            System.out.println(baseName);

	            if (gazeteerFiles.contains(baseName) || specialFiles.contains(baseName)) {
	                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	                    // Reading headers
	                    String line = reader.readLine();
	                    String[] currentHeaders = line != null ? line.split(",") : null;

	                    // Reading data
	                    while ((line = reader.readLine()) != null) {
	                        String[] values = line.split(",");
	                        String id = values[0];

	                        if (!seenIds.contains(id)) {
	                            seenIds.add(id);
	                            combinedData.putIfAbsent(id, new HashMap<>());
	                        }

	                        Map<String, String> data = combinedData.get(id);
	                        for (int i = 1; i < values.length; i++) {
	                            data.put(currentHeaders[i] + "_" + suffix, values[i]);
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
	    System.out.println(combinedData);
	    
	    // Write the combined data to the Gazeteers.csv file
	    File outputFile = new File(sourceFolder + "/Gazeteers_DAL_ANEW_Warriner_NRCVAD.csv");
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
	        // Writing headers
	        writer.write(String.join(",", headers));
	        writer.newLine();

	        // Writing data
	        for (Map.Entry<String, Map<String, String>> entry : combinedData.entrySet()) {
	            List<String> row = new ArrayList<>();
	            row.add(entry.getKey());

	            for (int i = 1; i < headers.size(); i++) {
	                row.add(entry.getValue().getOrDefault(headers.get(i), ""));
	            }

	            writer.write(String.join(",", row));
	            writer.newLine();
	        }
	    }
	
	
	    
	    for (File file : listOfFiles) {
	        if (file.isFile()) {
	            String baseName = file.getName().replace(".csv", "");
	            if (gazeteerFiles.contains(baseName) || specialFiles.contains(baseName)) {
	                if (!file.delete()) {  // Try to delete the file
	                    System.err.println("Failed to delete file: " + file.getName());
	                }
	            }
	        }
	    }
	}
	
}
