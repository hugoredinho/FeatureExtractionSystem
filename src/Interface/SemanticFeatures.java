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
	JButton allfeatures, features_GI, featuresSynesketch, featuresDAL_ANEW, featuresGazeteers, featuresWarriner, back;
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
		back = new JButton("Back");
		
		label = new JLabel("Quais features semanticas deseja extrair?");
		
		label.setBounds(75,10,300,25);
		allfeatures.setBounds(25,40,160,25);
		features_GI.setBounds(200,40,160,25);
		featuresSynesketch.setBounds(25,80,160,25);
		featuresDAL_ANEW.setBounds(200,80,160,25);
		featuresGazeteers.setBounds(200,120,160,25);
		featuresWarriner.setBounds(25,120,160,25);
		back.setBounds(200,180,160,25);
		
		featuresGazeteers.addActionListener(new SemanticFeatures.GazeteersButtonListener());
		featuresDAL_ANEW.addActionListener(new SemanticFeatures.DAL_ANEWButtonListener());
		features_GI.addActionListener(new SemanticFeatures.GI_ButtonListener());
		featuresSynesketch.addActionListener(new SemanticFeatures.SynesketchButtonListener());
		allfeatures.addActionListener(new SemanticFeatures.AllFeaturesListener());
		featuresWarriner.addActionListener(new SemanticFeatures.WarrinerButtonListener());
		back.addActionListener(new BackButtonListener(this));
 
		panel_semantic.add(label);
		panel_semantic.add(allfeatures);
		panel_semantic.add(features_GI);
		panel_semantic.add(featuresSynesketch);
		panel_semantic.add(featuresDAL_ANEW);
		panel_semantic.add(featuresGazeteers);
		panel_semantic.add(featuresWarriner);
		panel_semantic.add(back);
		
		this.add(panel_semantic);
	}
	private class GazeteersButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CombinedFeatures initial_gazeteers = new CombinedFeatures(false,1,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers1 = new CombinedFeatures(false,2,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers2 = new CombinedFeatures(false,3,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers3 = new CombinedFeatures(false,4,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers4 = new CombinedFeatures(false,5,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
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
				CombinedFeatures dal  = new CombinedFeatures(false,0,true,false,false,false,sourceFolder,null); // dal
				CombinedFeatures anew  = new CombinedFeatures(false,0,false,true,false,false,sourceFolder,null); // anew
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
				CombinedFeatures initial  = new CombinedFeatures(false,0,false,false,true,false,sourceFolder,null); 
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
				CombinedFeatures initial_gazeteers = new CombinedFeatures(false,1,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers1 = new CombinedFeatures(false,2,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers2 = new CombinedFeatures(false,3,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers3 = new CombinedFeatures(false,4,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				CombinedFeatures initial_gazeteers4 = new CombinedFeatures(false,5,false,false,false,false,sourceFolder,null); // false true vai ser gazeteers
				
				CombinedFeatures initial_dal  = new CombinedFeatures(false,0,true,false,false,false,sourceFolder,null); // dal
				CombinedFeatures initial_anew  = new CombinedFeatures(false,0,false,true,false,false,sourceFolder,null); // dal
				CombinedFeatures initial_Warriner = new CombinedFeatures(false,0,false,false,true,false,sourceFolder,null); 
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
	    List<String> headers = Arrays.asList("Id",
	        "AvgValence_Q1", "AvgArousal_Q1", "AvgValence_Q2", "AvgArousal_Q2",
	        "AvgValence_Q3", "AvgArousal_Q3", "AvgValence_Q4", "AvgArousal_Q4",
	        "#Count_Q1", "#Count_Q2", "#Count_Q3", "#Count_Q4",
	        "AvgValence_All", "AvgArousal_All", "AvgValence_Dal", "AvgArousal_Dal", "AvgDominance_Dal",
	        "AvgValence_Anew", "AvgArousal_Anew", "AvgDominance_Anew"
	    );

	    List<String> gazeteerFiles = Arrays.asList(
	        "Gazeteers_Q1", "Gazeteers_Q2", "Gazeteers_Q3", "Gazeteers_Q4", "Gazeteers_All"
	    );
	    List<String> specialFiles = Arrays.asList("Anew", "Dal");

	    for (File file : listOfFiles) {
	        if (file.isFile()) {
	            String baseName = file.getName().replace(".csv", "");
	            String suffix = baseName.split("_").length > 1 ? baseName.split("_")[1] : baseName;

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
	    // Write the combined data to the Gazeteers.csv file
	    File outputFile = new File(sourceFolder + "/Gazeteers_DAL_ANEW.csv");
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
