package gui;

import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import org.overture.codegen.runtime.VDMSet;

import vdmpp.EshopModelTester;
import vdmpp.Feature;
import vdmpp.Model;
import vdmpp.Parent;

public class InAppGUI {
	
	InAppGUI() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		InAppGUI gui = new InAppGUI();
		gui.displayModel(new EshopModelTester().createModel());
	}

	private void displayModel(Model model) {
		Graph graph = new MultiGraph("Model Viewer");
		graph.addAttribute("ui.stylesheet", "node { size: 40px; text-padding: 3px, 2px; text-background-mode: rounded-box; text-background-color: #EB2; text-color: #222; }");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		
		int nodeCount = model.nodeCount().intValue();
		displayFromRoot(graph, null, model.getRoot(), 0, 1, nodeCount);

		Viewer viewer = graph.display();
		viewer.disableAutoLayout();
	}
	
	private void displayFromRoot(Graph graph, Parent parent, Feature feature, float x, float y, float xSpace) {
		
		graph.addNode(feature.getName());
		Node node = graph.getNode(feature.getName());
		
		String name = !feature.isMandatory() &&
		!(parent.isOrParent() || parent.isXorParent()) ? feature.getName() + " (Optional)" : feature.getName();
		node.addAttribute("ui.label", name);
		node.setAttribute("y", y);
		node.setAttribute("x", x);
		
		if (parent != null) { 
			graph.addEdge(parent.getName() + "-" + feature.getName(), parent.getName(), feature.getName());
			Edge edge = graph.getEdge(parent.getName() + "-" + feature.getName());
			edge.addAttribute("ui.label", parent.getParentType());
		}
		if (feature instanceof Parent){
			Parent f = (Parent) feature;
			VDMSet subFeatures = f.getSubFeatures();
			float xDelta = xSpace / subFeatures.size();
			float newx = x - xSpace / 2;
			int i = 0;
			for (@SuppressWarnings("unchecked")
			Iterator<Feature> it = subFeatures.iterator(); it.hasNext();i++) {
				Feature subFeature = it.next();
				displayFromRoot(graph, (Parent) feature, subFeature, newx + xDelta * i + xDelta / 2, y - 1, xDelta);
			}
		}
	}

}
