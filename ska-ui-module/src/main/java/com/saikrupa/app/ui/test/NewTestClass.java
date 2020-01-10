package com.saikrupa.app.ui.test;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewTestClass {

	public NewTestClass() {
		CusomPanel p = new CusomPanel();
		
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.add(p);
	    frame.pack();
	    frame.setMinimumSize(frame.getPreferredSize());
	    frame.setVisible(true);
	}
	
	public static void main(String[] args) {
	    new NewTestClass();
	  }
	
	class CusomPanel extends JPanel {
		  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CusomPanel() {
		    JTextField labelA = new JTextField("Your A component");
		    JTextField labelB = new JTextField("Your B component");
		    JTextField labelC = new JTextField("Your C component");
		    JTextField labelD = new JTextField("Top Right D");

		    JPanel north = new JPanel(new GridBagLayout());
		    GridBagConstraints gbc = new GridBagConstraints();
		    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		    gbc.weightx = 1;
		    gbc.insets = new Insets(10, 10, 10, 10);
		    north.add(labelD, gbc);

		    JPanel south = new JPanel(new GridBagLayout());
		    gbc.anchor = GridBagConstraints.CENTER;
		    gbc.gridy = 0;
		    south.add(labelA, gbc);
		    gbc.gridy = 1;
		    south.add(labelB, gbc);
		    gbc.gridy = 2;
		    south.add(labelC, gbc);

		    setLayout(new BorderLayout());
		    add(north, BorderLayout.NORTH);
		    add(south, BorderLayout.CENTER);
		  }
	}

}
