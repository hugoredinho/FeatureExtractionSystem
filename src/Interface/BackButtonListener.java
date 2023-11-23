package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import AuxiliarFiles.WriteCSVFinal;

public class BackButtonListener implements ActionListener {
    ContentFeatures content_panel;
    ContentSelector content_selector;
    SemanticFeatures semantic_panel;
    StylisticFeatures stylistic_panel;
    StructuralFeatures structural_panel;
    boolean backtoMain = false;
    boolean backtoCBF = false;
    
    
	public BackButtonListener(ContentFeatures content_panel) {
		this.content_panel = content_panel;
		this.backtoMain = true;
		this.backtoCBF = false;
    }
	
	public BackButtonListener(ContentSelector content_selector) {
		this.content_selector = content_selector;
		this.backtoMain = false;
		this.backtoCBF = true;
    }
	
	public BackButtonListener(SemanticFeatures semantic_panel) {
		this.semantic_panel = semantic_panel;
		this.backtoMain = true;
		this.backtoCBF = false;
    }
	public BackButtonListener(StylisticFeatures stylistic_panel) {
		this.stylistic_panel = stylistic_panel;
		this.backtoMain = true;
		this.backtoCBF = false;
    }
	
	public BackButtonListener(StructuralFeatures structural_panel) {
		this.structural_panel = structural_panel;
		this.backtoMain = true;
		this.backtoCBF = false;
    }
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (backtoMain) {
			MainInterface main_frame = null;
			if (content_panel != null) {
				content_panel.dispose();
				main_frame = new MainInterface(content_panel.sourceFolder);
			}
			else if (semantic_panel!=null) {
				semantic_panel.dispose();
				main_frame = new MainInterface(semantic_panel.sourceFolder);
			}
			else if (stylistic_panel!=null) {
				stylistic_panel.dispose();
				main_frame = new MainInterface(stylistic_panel.sourceFolder);
			}	
			else if (structural_panel!=null) {
				structural_panel.dispose();
				main_frame = new MainInterface(structural_panel.sourceFolder);
			}
			main_frame.setTitle("Janela principal do programa");
			main_frame.setSize(475,225);
			main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			main_frame.setVisible(true);	
		}
	     else if(backtoCBF) {
	    	MainInterface main_frame = null;
	    	 
	    	content_selector.dispose();
	    	main_frame = new MainInterface(content_selector.sourceFolder);
			/*
			 * frame_content = new ContentFeatures(content_selector.sourceFolder);
			frame_content.setTitle("Seleçao de features de conteudo");
			frame_content.setSize(400,220);
			frame_content.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame_content.setVisible(true);	
			*/
		}

    }

}
