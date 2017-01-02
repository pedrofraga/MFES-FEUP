package gui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import org.overture.codegen.runtime.VDMSeq;

import vdmpp.EshopModelTester;
import vdmpp.Feature;
import vdmpp.Model;
import vdmpp.Parent;

public class InAppGUI {

	public static void main(String[] args) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		InAppGUI gui = new InAppGUI();
		gui.displayModel(new EshopModelTester().createModel());
	}

	private void displayModel(Model model) {
		Graph graph = new MultiGraph("Model Viewer");
		graph.addAttribute("ui.stylesheet", "node { size: 40px; text-alignment: at-right; text-padding: 3px, 2px; text-background-mode: rounded-box; text-background-color: #EB2; text-color: #222; }");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		
		displayFromRoot(graph, null, model.getRoot());
		// TODO Auto-generated method stub
		Viewer viewer = graph.display();
		viewer.disableAutoLayout();
		viewer.enableAutoLayout();
	}
	
	private void displayFromRoot(Graph graph, Parent parent, Feature feature) {
		graph.addNode(feature.getName());
		if (parent != null) { graph.addEdge(parent.getName() + "-" + feature.getName(), parent.getName(), feature.getName()); }
		if (feature instanceof Parent){
			Parent f = (Parent) feature;
			VDMSeq subFeatures = f.getSubFeatures();
			for (int i = 0; i < subFeatures.size(); i++){
				displayFromRoot(graph, (Parent) feature, (Feature) subFeatures.get(i));
			}
		}
	}

}