package gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import vdmpp.Model;

public class GraphGUI extends JFrame {

	private static final long serialVersionUID = -5557695447991471706L;

	GraphGUI(String name) {
		super(name);
		this.setLayout(new FlowLayout());
		addFileChooser();
		
	}
	private void addFileChooser() {
		UIManager.put("FileChooser.openDialogTitleText", "Choose model to be loaded");
		
		JSONChooser jsonChooser = new JSONChooser();
		Model model = jsonChooser.getModel();
		if (model != null) { addGraphPanel(model); } else { System.out.println("There was a problem while loading the file"); return; }
		
		this.pack();
		this.setVisible(true);
		this.setAlwaysOnTop(true);
	    this.toFront();
	    this.requestFocus();
	    this.setAlwaysOnTop(false);
	}
	private void addGraphPanel(Model model) {
		GraphPanel graphPanel = new GraphPanel();
		graphPanel.displayModel(model);
		this.add(graphPanel);
	}
}
