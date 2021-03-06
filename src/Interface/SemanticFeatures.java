package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import CombinedFeatures.CombinedFeatures;
import GI.Initial_GI;
import Synesketch.mainApp_Synesketch;
import AuxiliarFiles.WriteCSVFinal;

@SuppressWarnings("serial")
public class SemanticFeatures extends JFrame{
	JPanel panel_semantic;
	JButton allfeatures, features_GI, featuresSynesketch, featuresDAL_ANEW, featuresGazeteers, back;
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
		back = new JButton("Back");
		
		label = new JLabel("Quais features semanticas deseja extrair?");
		
		label.setBounds(75,10,300,25);
		allfeatures.setBounds(25,40,160,25);
		features_GI.setBounds(200,40,160,25);
		featuresSynesketch.setBounds(25,80,160,25);
		featuresDAL_ANEW.setBounds(200,80,160,25);
		featuresGazeteers.setBounds(200,120,160,25);
		back.setBounds(200,180,160,25);
		
		featuresGazeteers.addActionListener(new SemanticFeatures.GazeteersButtonListener());
		featuresDAL_ANEW.addActionListener(new SemanticFeatures.DAL_ANEWButtonListener());
		features_GI.addActionListener(new SemanticFeatures.GI_ButtonListener());
		featuresSynesketch.addActionListener(new SemanticFeatures.SynesketchButtonListener());
		allfeatures.addActionListener(new SemanticFeatures.AllFeaturesListener());
		back.addActionListener(new BackButtonListener(this));
 
		panel_semantic.add(label);
		panel_semantic.add(allfeatures);
		panel_semantic.add(features_GI);
		panel_semantic.add(featuresSynesketch);
		panel_semantic.add(featuresDAL_ANEW);
		panel_semantic.add(featuresGazeteers);
		panel_semantic.add(back);
		
		this.add(panel_semantic);
	}
	private class GazeteersButtonListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				CombinedFeatures initial_gazeteers = new CombinedFeatures(false,true,false,false,sourceFolder,null); // false true vai ser gazeteers
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
				CombinedFeatures initial  = new CombinedFeatures(false,false,true,false,sourceFolder,null); // false true vai ser gazeteers
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
				CombinedFeatures initial_GAZ  = new CombinedFeatures(false,true,false,false,sourceFolder,null);
				CombinedFeatures initial_ANEW  = new CombinedFeatures(false,false,true,false,sourceFolder,null);
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
	
	
}
