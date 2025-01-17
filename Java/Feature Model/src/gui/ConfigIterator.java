package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.overture.codegen.runtime.VDMSet;

import vdmpp.Model;

public class ConfigIterator extends JPanel {

	private static final long serialVersionUID = -3191269539641318432L;
	
	JButton left;
	JButton right;
	JLabel text;
	VDMSet validConfigs;
	
	Model model;

	int iterator = 1;
	int size;

	ConfigIterator(Model model) {
		this.left = new JButton("<");
		this.right = new JButton(">");
		this.text = new JLabel();

		this.validConfigs = model.generateValidConfigs();
		this.size = validConfigs.size();
		this.model = model;
		
		this.setLayout(new GridLayout(1, 3));
		this.add(left);
		this.add(text);
		this.add(right);
		
		updateButtons();

		left.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				iterator--;
				updateGraph();
				updateButtons();
			} 
		} );
		right.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				iterator++;
				updateGraph();
				updateButtons();
			}
		} );
	}
	
	protected void updateGraph() {
		VDMSet set = getSetFromIndex();
		
		GraphPanel graphPanel = GraphPanel.getInstance();
		graphPanel.displayModel(model, set);
	}

	private void updateButtons() {
		if (iterator == 1) {
			left.setEnabled(false);
			right.setEnabled(true);
		} else if (iterator == size) {
			left.setEnabled(true);
			right.setEnabled(false);
		} else {
			left.setEnabled(true);
			right.setEnabled(true);
		}
		text.setText(iterator + " of " + size);
	} 

	protected VDMSet getSetFromIndex() {
		int index = 1;
		for (Iterator<?> i = validConfigs.iterator(); i.hasNext();) {
			VDMSet set = (VDMSet) i.next();
			if (index == iterator) return set;
			index++;
		}
		return null;
	}
}
