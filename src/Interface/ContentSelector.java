package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.*;

import CBF.CBF_Initial;
import CBF.CBF_Initial_Tags;

public class ContentSelector extends JFrame{
	JPanel panel_content;
	JRadioButton unig,big,trig,grams4,grams5,nada,st,sw,st_sw,freq,bool,tfidf,norm;
	JButton confirm,back;
	JRadioButton tags;
	JLabel label;
	String sourceFolder;
	public ContentSelector(String sourceFolder,ContentFeatures content_features) {
		super();
		content_features.dispose();
		this.sourceFolder = sourceFolder;
		panel_content = new JPanel();
		panel_content.setLayout(null);
		
		confirm = new JButton("Confirmar");
		
		unig = new JRadioButton("unig");
		big = new JRadioButton("big"); 
		trig = new JRadioButton("trig"); 
		grams4 = new JRadioButton("4grams");
		grams5 = new JRadioButton("5grams");
		
		unig.setBounds(50,30,70,25);
		big.setBounds(50,50,70,25);
		trig.setBounds(50,70,70,25);
		grams4.setBounds(50,90,70,25);
		grams5.setBounds(50,110,70,25);
		
		unig.setSelected(true);
		
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(unig);bg1.add(big);bg1.add(trig);bg1.add(grams4);bg1.add(grams5);
		
		//-----------------------------------
		
		nada = new JRadioButton("nada");
		st = new JRadioButton("st"); 
		sw = new JRadioButton("sw"); 
		st_sw = new JRadioButton("st_sw"); 
		
		nada.setBounds(150,50,70,25);
		st.setBounds(150,70,70,25);
		sw.setBounds(150,90,70,25);
		st_sw.setBounds(150,110,70,25);
		
		nada.setSelected(true);
		
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(nada);bg2.add(st);bg2.add(sw);bg2.add(st_sw);
		
		//-----------------------------------
		
		freq = new JRadioButton("freq");
		bool = new JRadioButton("bool"); 
		tfidf = new JRadioButton("tfidf"); 
		norm = new JRadioButton("norm"); 
		
		freq.setBounds(250,50,70,25);
		bool.setBounds(250,70,70,25);
		tfidf.setBounds(250,90,70,25);
		norm .setBounds(250,110,70,25);
		
		freq.setSelected(true);
		
		ButtonGroup bg3 = new ButtonGroup();
		bg3.add(freq);bg3.add(bool);bg3.add(tfidf);bg3.add(norm );
		
		
		back = new JButton("Back");
		
		tags = new JRadioButton("POS_Tags");
		
		label = new JLabel("Quais features de conteudo deseja extrair?");
		
		label.setBounds(75,10,300,25);


		back.setBounds(240,150,90,25);
		confirm.setBounds(40,150,90,25);
		tags.setBounds(140,150,90,25);
		
		
		back.addActionListener(new BackButtonListener(this));
 
		panel_content.add(label);
		panel_content.add(unig);panel_content.add(big);panel_content.add(trig);panel_content.add(grams4);panel_content.add(grams5);
		panel_content.add(nada);panel_content.add(st);panel_content.add(sw);panel_content.add(st_sw);
		panel_content.add(freq);panel_content.add(bool);panel_content.add(tfidf);panel_content.add(norm);
		panel_content.add(confirm);
		panel_content.add(tags);
		panel_content.add(back);
		
		this.add(panel_content);
		
		
		confirm.addActionListener(new confirmButtonListener(bg1,bg2,bg3));
	}
	private class confirmButtonListener implements ActionListener {
		ButtonGroup bg1,bg2,bg3;

		public confirmButtonListener(ButtonGroup bg1,ButtonGroup bg2,ButtonGroup bg3) {
			this.bg1 = bg1;
			this.bg2 = bg2;
			this.bg3 = bg3;
		}	
		@Override
		public void actionPerformed(ActionEvent e) {
			// now we want to get the value of the tags radio button

			boolean POS_tags = tags.isSelected();

			String s_bg1 = getSelectedButtonText(bg1);
			String s_bg2 = getSelectedButtonText(bg2);
			String s_bg3 = getSelectedButtonText(bg3);
			System.out.println( s_bg3 );
		
			try {
				CBF_Initial initial = new CBF_Initial(sourceFolder,s_bg1,s_bg2,s_bg3, POS_tags);
				JOptionPane.showMessageDialog(null, "Todas CBF features extraidas", "Mensagem", JOptionPane.PLAIN_MESSAGE);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

    public String getSelectedButtonText(ButtonGroup buttonGroup) {
    	
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	    

}
