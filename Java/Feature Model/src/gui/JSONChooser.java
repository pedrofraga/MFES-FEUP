package gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.overture.codegen.runtime.VDMSet;

import vdmpp.Feature;
import vdmpp.Model;
import vdmpp.Parent;


public class JSONChooser extends JFileChooser {

	private static final long serialVersionUID = -4296681359271965826L;
	
	protected Model getModel() {
		int returnVal = this.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			String path = this.getSelectedFile().getAbsolutePath();
			return getModelFromJson(path);
		} else {
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	private Model getModelFromJson(String path) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(path));
			Map <String, Feature> featuresMap = new HashMap<>();
			String root = (String) jsonObject.get("root");
			JSONArray features = (JSONArray) jsonObject.get("features");
			
			Iterator<JSONObject> iterator = features.iterator();
			
            while (iterator.hasNext()) {
            	JSONObject featurejson = iterator.next();
            	String name = (String) featurejson.get("name");
            	String type = (String) featurejson.get("type");
            	Boolean mandatory = (Boolean) featurejson.get("mandatory");

            	Feature feature = type.equals("leaf") ? new Feature(name) : new Parent(name);
            	if (!mandatory) feature.setMandatory(false);
            	if (type.equals("orParent"))
            		((Parent)feature).setParentType(quotes.orParentQuote.getInstance()); 
        		else if (type.equals("xorParent"))
        			((Parent)feature).setParentType(quotes.xorParentQuote.getInstance());
            	
            	featuresMap.put(name, feature);
            }
            iterator = features.iterator();
            while (iterator.hasNext()) {
            	JSONObject featurejson = iterator.next();
            	String name = (String) featurejson.get("name");
            	JSONArray requirements = (JSONArray) featurejson.get("requirements");
        		JSONArray exclusions = (JSONArray) featurejson.get("exclusions");
            	
            	Feature feature = featuresMap.get(name);	
            	
            	addRequirements(featuresMap, requirements, feature);
        		addExclusions(featuresMap, exclusions, feature);
        		
            	if (feature instanceof Parent) {
             		Parent parent = (Parent) feature;
            		JSONArray subFeatures = (JSONArray) featurejson.get("subFeatures");
            		
            		addFeatures(featuresMap, subFeatures, parent);
            	}
            }
            Feature rootFeature = featuresMap.get(root);
            return new Model(rootFeature);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private void addFeatures(Map <String, Feature> featuresMap, JSONArray subFeatures, Parent parent) {
		VDMSet set = new VDMSet();
		for (int i = 0; i < subFeatures.size(); i++) {
			JSONObject subFeatureJson = (JSONObject) subFeatures.get(i);
			String subFeatureName = (String) subFeatureJson.get("name");
			Feature subFeature = featuresMap.get(subFeatureName);
			set.add(subFeature);
		}
		parent.setSubFeatures(set);
	}
	@SuppressWarnings("unchecked")
	private void addRequirements(Map <String, Feature> featuresMap, JSONArray requirements, Feature feature) {
		VDMSet set = new VDMSet();
		for (int i = 0; i < requirements.size(); i++) {
			JSONObject subFeatureJson = (JSONObject) requirements.get(i);
			String subFeatureName = (String) subFeatureJson.get("name");
			Feature subFeature = featuresMap.get(subFeatureName);
			set.add(subFeature);
		}
		feature.setRequirements(set);
	}
	@SuppressWarnings("unchecked")
	private void addExclusions(Map <String, Feature> featuresMap, JSONArray exclusions, Feature feature) {
		VDMSet set = new VDMSet();
		for (int i = 0; i < exclusions.size(); i++) {
			JSONObject subFeatureJson = (JSONObject) exclusions.get(i);
			String subFeatureName = (String) subFeatureJson.get("name");
			Feature subFeature = featuresMap.get(subFeatureName);
			set.add(subFeature);
		}
		feature.setExclusions(set);
	}
	@Override
	protected JDialog createDialog(Component parent)
			throws HeadlessException {
		JDialog dialog = super.createDialog(parent);
		dialog.setLocationRelativeTo(null);
		dialog.setLocationByPlatform(true);
		dialog.setAlwaysOnTop(true);
		return dialog;
	}
	
	public VDMSet getConfiguration() {
		int returnVal = this.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			String path = this.getSelectedFile().getAbsolutePath();
			return getConfigFromJson(path);
		} else {
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	private VDMSet getConfigFromJson(String path) {
		JSONParser parser = new JSONParser();
		try {
			JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(path));
			VDMSet set = new VDMSet();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				String name = (String) jsonObject.get("name");
				Boolean status = (Boolean) jsonObject.get("status");
				if (status) {
					set.add(name);
				}
			}
			return set;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
