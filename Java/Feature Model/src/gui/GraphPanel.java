package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;

import javax.swing.JPanel;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.overture.codegen.runtime.SetUtil;
import org.overture.codegen.runtime.VDMSet;

import vdmpp.Feature;
import vdmpp.Model;
import vdmpp.Parent;

public class GraphPanel extends JPanel{

	private static final long serialVersionUID = -8632379117522107537L;
	private static GraphPanel instance;
	private Graph graph;

	GraphPanel() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		this.setLayout(new GridLayout(1, 2));
		this.setPreferredSize(new Dimension(640, 480));
		GraphPanel.instance = this;
		this.graph = new MultiGraph("Model Viewer");

		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		View view = viewer.addDefaultView(false);
		viewer.disableAutoLayout();

		this.add((Component) view);
	}

	protected void displayModel(Model model, VDMSet set) {
		graph.clear();

		String bodyStyle = "";
		if (set != null) {
			Boolean isValidConfig = SetUtil.inSet(set, model.generateValidConfigs());
			bodyStyle = isValidConfig ? "graph { fill-color: #e4ffe0; } " : "graph { fill-color: #ffe0e0; } ";
		}
		graph.addAttribute("ui.stylesheet", bodyStyle + "node { size: 40px; text-padding: 3px, 2px; text-background-mode: rounded-box; text-background-color: #EB2; text-color: #222; } node .true { fill-color: #71c164; } node .false { fill-color: #c16464; }");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");

		int nodeCount = model.nodeCount().intValue();
		displayFromRoot(graph, set, null, model.getRoot(), 0, 1, nodeCount);

	}

	private void displayFromRoot(Graph graph, VDMSet set, Parent parent, Feature feature, float x, float y, float xSpace) {

		graph.addNode(feature.getName());
		Node node = graph.getNode(feature.getName());

		String name = !feature.isMandatory() &&
				(parent == null || !(parent.isOrParent() || parent.isXorParent())) ? feature.getName() + " (Optional)" : feature.getName();
		node.addAttribute("ui.label", name);
		node.setAttribute("y", y);
		node.setAttribute("x", x);

		if (set != null) {
			Boolean status =	SetUtil.inSet(feature.getName(), set);
			if (status) { node.addAttribute("ui.class", "true"); } else { node.addAttribute("ui.class", "false"); }
		}

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
				displayFromRoot(graph, set, (Parent) feature, subFeature, newx + xDelta * i + xDelta / 2, y - 1, xDelta);
			}
		}
	}

	public static GraphPanel getInstance() {
		return instance;
	}

}
