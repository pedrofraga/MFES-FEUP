package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Model {
  protected Feature root;
  private VDMSet features = SetUtil.set();
  private ConfigGenerator configGenerator;

  public void cg_init_Model_1(final Feature r) {

    root = (Parent) r;
    configGenerator = new ConfigGenerator(this);
    return;
  }

  public Model(final Feature r) {

    cg_init_Model_1(r);
  }

  public Feature getRoot() {

    return root;
  }

  public Number nodeCount() {

    return root.nodeCount();
  }

  public void setFeatures() {

    features = root.features();
  }

  public Boolean isValidConfiguration(final VDMMap config) {

    if (!(root.isValidConfiguration(Utils.copy(config)))) {
      return false;
    }

    return true;
  }

  public VDMSet generateValidConfigs() {

    setFeatures();
    return configGenerator.generateValidConfigs();
  }

  public Model() {}

  public String toString() {

    return "Model{"
        + "root := "
        + Utils.toString(root)
        + ", features := "
        + Utils.toString(features)
        + ", configGenerator := "
        + Utils.toString(configGenerator)
        + "}";
  }
}