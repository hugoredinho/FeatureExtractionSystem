package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import AuxiliarFiles.WriteCSVFinal;
import CapitalLetters.CapitalLetters_Initial;
import CombinedFeatures.CombinedFeatures;


@SuppressWarnings("serial")
public class StylisticFeatures extends JFrame{
	JPanel panel_stylistic;
	JButton allfeatures, featuresSlang, featuresCL,back;
	JLabel label;
	String sourceFolder;
	public StylisticFeatures(String sourceFolder) {
		super();
		this.sourceFolder = sourceFolder;
		panel_stylistic = new JPanel();
		panel_stylistic.setLayout(null);
		
		allfeatures = new JButton("Todas as features");
		featuresSlang = new JButton("Features Slang");
		featuresCL = new JButton("Features CapitalLetters");
		back = new JButton("Back");

		label = new JLabel("Quais features estilisticas deseja extrair?");
		
		label.setBounds(75,10,300,25);
		allfeatures.setBounds(25,40,160,25);
		featuresSlang.setBounds(200,40,160,25);
		featuresCL.setBounds(90,80,200,25);
		back.setBounds(200,130,160,25);
		
		featuresCL.addActionListener(new StylisticFeatures.CL_ButtonListener());
		allfeatures.addActionListener(new StylisticFeatures.AllFeaturesButtonListener());
		featuresSlang.addActionListener(new StylisticFeatures.SlangButtonListener());
		back.addActionListener(new BackButtonListener(this));

		panel_stylistic.add(label);
		panel_stylistic.add(allfeatures);
		panel_stylistic.add(featuresSlang);
		panel_stylistic.add(featuresCL);
		panel_stylistic.add(back);
		
		this.add(panel_stylistic);
	}
	private class CL_ButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(false,sourceFolder,null);
				JOptionPane.showMessageDialog(null, "Features CapitalLetters extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class SlangButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CombinedFeatures initial_WD  = new CombinedFeatures(false,0,false,false,false,true,false,sourceFolder,null);
				JOptionPane.showMessageDialog(null, "Features Slang extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class AllFeaturesButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CapitalLetters_Initial capitalLetters = new CapitalLetters_Initial(false,sourceFolder,null);
				CombinedFeatures initial_WD  = new CombinedFeatures(false,0,false,false,false,true,false,sourceFolder,null);
				combineStylistic("src/Output");
				JOptionPane.showMessageDialog(null, "Todas features estilisticas extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void combineStylistic(String sourceFolder) throws IOException {
	    System.out.println("Source folder " + sourceFolder);
	    File folder = new File(sourceFolder);
	    File[] listOfFiles = folder.listFiles();

	    // A map to hold the combined data
	    Map<String, Map<String, String>> combinedData = new LinkedHashMap<>();
	    
	    Set<String> seenIds = new HashSet<>();

	    // The predefined header order
	    List<String> headers = Arrays.asList("Id",
	        "cfcl_Letters", "cacl_Letters","Count_Slang"
	    );

	    List<String> Files = Arrays.asList(
	       "Capital_Letters", "Slang"
	    );

	    for (File file : listOfFiles) {
	        if (file.isFile()) {
	            String baseName = file.getName().replace(".csv", "");
	            String suffix = baseName.split("_").length > 1 ? baseName.split("_")[1] : baseName;

	            if (Files.contains(baseName)) {
	            	System.out.println(baseName);
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
	                        //System.out.println(data);
	                    }
	                }
	            }
	        }
	    }
	    
	    // Write the combined data to the Gazeteers.csv file
	    File outputFile = new File(sourceFolder + "/Capital_Letters_Slang.csv");
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
	            if (Files.contains(baseName)) {
	                if (!file.delete()) {  // Try to delete the file
	                    System.err.println("Failed to delete file: " + file.getName());
	                }
	            }
	        }
	    }
	}
	
	
}
