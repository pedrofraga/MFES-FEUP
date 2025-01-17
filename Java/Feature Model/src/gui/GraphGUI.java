package gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.overture.codegen.runtime.VDMSet;

import vdmpp.Model;

public class GraphGUI extends JFrame {

	private static final long serialVersionUID = -5557695447991471706L;

	GraphGUI(String name, int mode) {
		super(name);
		this.setLayout(new FlowLayout());
		addFileChooser(mode);
		
	}
	private void addFileChooser(int mode) {
		UIManager.put("FileChooser.openDialogTitleText", "Choose model to be loaded");
		
		JSONChooser jsonChooser = new JSONChooser();
		Model model = jsonChooser.getModel();
		VDMSet set = null;
		
		if (mode == Utilities.VALIDATE && model != null) { 
			UIManager.put("FileChooser.openDialogTitleText", "Choose configuration to be loaded");
			jsonChooser = new JSONChooser();
			set = jsonChooser.getConfiguration();
			if (set == null) { System.out.println("There was a problem while loading the configuration file"); return; }
		} else if (mode == Utilities.GENERATE && model != null) {
			ConfigIterator configIterator = new ConfigIterator(model);
			set = configIterator.getSetFromIndex();
			this.add(configIterator);
		}
		if (model != null) { addGraphPanel(model, set); } else { System.out.println("There was a problem while loading the model file"); return; }

		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
	    this.toFront();
	    this.requestFocus();
	    this.setAlwaysOnTop(false);
	}
	private void addGraphPanel(Model model, VDMSet set) {
		GraphPanel graphPanel = new GraphPanel();
		graphPanel.displayModel(model, set);
		this.add(graphPanel);
	}
}
