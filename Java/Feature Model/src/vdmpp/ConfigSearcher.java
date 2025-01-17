package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class ConfigSearcher {
  private Parent root;
  private Number nodeCount;
  private VDMSet restrictedInvalidSubsets;
  private VDMSet validConfigs = SetUtil.set(SetUtil.set());

  public void cg_init_ConfigSearcher_1(final Feature r) {

    root = (Parent) r;
    nodeCount = root.nodeCount();
    restrictedInvalidSubsets = root.invalidSubsets();
    return;
  }

  public ConfigSearcher(final Feature r) {

    cg_init_ConfigSearcher_1(r);
  }

  public VDMSet getValidConfigs() {

    searchFeatureTree(root, true, SetUtil.set());
    applyRestrictions();
    return Utils.copy(validConfigs);
  }

  public void applyRestrictions() {

    VDMSet setCompResult_1 = SetUtil.set();
    VDMSet set_1 = Utils.copy(validConfigs);
    for (Iterator iterator_1 = set_1.iterator(); iterator_1.hasNext(); ) {
      VDMSet elem = ((VDMSet) iterator_1.next());
      Boolean existsExpResult_1 = false;
      VDMSet set_2 = Utils.copy(restrictedInvalidSubsets);
      for (Iterator iterator_2 = set_2.iterator(); iterator_2.hasNext() && !(existsExpResult_1); ) {
        VDMMap restriction = ((VDMMap) iterator_2.next());
        Boolean orResult_1 = false;

        Boolean andResult_1 = false;

        if (Utils.equals(MapUtil.rng(Utils.copy(restriction)), SetUtil.set(true, false))) {
          Boolean andResult_2 = false;

          if (SetUtil.subset(
              MapUtil.dom(MapUtil.rngResTo(Utils.copy(restriction), SetUtil.set(true))), elem)) {
            if (!(SetUtil.subset(
                MapUtil.dom(MapUtil.rngResTo(Utils.copy(restriction), SetUtil.set(false))),
                elem))) {
              andResult_2 = true;
            }
          }

          if (andResult_2) {
            andResult_1 = true;
          }
        }

        if (andResult_1) {
          orResult_1 = true;
        } else {
          Boolean andResult_3 = false;

          if (Utils.equals(MapUtil.rng(Utils.copy(restriction)), SetUtil.set(true))) {
            if (SetUtil.subset(MapUtil.dom(Utils.copy(restriction)), elem)) {
              andResult_3 = true;
            }
          }

          orResult_1 = andResult_3;
        }

        existsExpResult_1 = orResult_1;
      }
      if (!(existsExpResult_1)) {
        setCompResult_1.add(Utils.copy(elem));
      }
    }
    validConfigs = Utils.copy(setCompResult_1);
  }

  public void searchFeatureTree(
      final Object feature, final Boolean hasDefaultParent, final VDMSet optionalParents) {

    if (hasDefaultParent) {
      defaultParentConfigs(((Feature) feature));
    }

    checkOptionalParents(((Feature) feature), Utils.copy(optionalParents));
    if (feature instanceof Parent) {
      VDMSet newOptionalParents = Utils.copy(optionalParents);
      Boolean apply_1 = null;
      if (feature instanceof Parent) {
        apply_1 = ((Parent) feature).isXorParent();
      } else {
        throw new RuntimeException("Missing member: isXorParent");
      }

      if (apply_1) {
        xorParentConfigs(((Parent) feature));
      } else {
        Boolean apply_2 = null;
        if (feature instanceof Parent) {
          apply_2 = ((Parent) feature).isOrParent();
        } else {
          throw new RuntimeException("Missing member: isOrParent");
        }

        if (apply_2) {
          orParentConfigs(((Parent) feature));
        }
      }

      Boolean apply_3 = null;
      if (feature instanceof Feature) {
        apply_3 = ((Feature) feature).isMandatory();
      } else if (feature instanceof Parent) {
        apply_3 = ((Parent) feature).isMandatory();
      } else {
        throw new RuntimeException("Missing member: isMandatory");
      }

      if (!(apply_3)) {
        String apply_4 = null;
        if (feature instanceof Feature) {
          apply_4 = ((Feature) feature).getName();
        } else if (feature instanceof Parent) {
          apply_4 = ((Parent) feature).getName();
        } else {
          throw new RuntimeException("Missing member: getName");
        }

        newOptionalParents = SetUtil.union(Utils.copy(newOptionalParents), SetUtil.set(apply_4));
      }

      VDMSet apply_5 = null;
      if (feature instanceof Parent) {
        apply_5 = ((Parent) feature).getSubFeatures();
      } else {
        throw new RuntimeException("Missing member: getSubFeatures");
      }

      for (Iterator iterator_19 = apply_5.iterator(); iterator_19.hasNext(); ) {
        Feature subFeature = (Feature) iterator_19.next();
        Boolean apply_6 = null;
        if (feature instanceof Parent) {
          apply_6 = ((Parent) feature).isDefaultParent();
        } else {
          throw new RuntimeException("Missing member: isDefaultParent");
        }

        searchFeatureTree(subFeature, apply_6, Utils.copy(newOptionalParents));
      }
    }
  }

  public void defaultParentConfigs(final Feature feature) {

    VDMSet possibilities = SetUtil.set();
    if (feature.isMandatory()) {
      possibilities = SetUtil.set(SetUtil.set(feature.getName()));
    } else {
      possibilities = SetUtil.set(SetUtil.set(feature.getName()), SetUtil.set());
    }

    VDMSet setCompResult_2 = SetUtil.set();
    VDMSet set_3 = Utils.copy(validConfigs);
    for (Iterator iterator_3 = set_3.iterator(); iterator_3.hasNext(); ) {
      VDMSet validConfig = ((VDMSet) iterator_3.next());
      VDMSet set_4 = Utils.copy(possibilities);
      for (Iterator iterator_4 = set_4.iterator(); iterator_4.hasNext(); ) {
        VDMSet possibility = ((VDMSet) iterator_4.next());
        setCompResult_2.add(SetUtil.union(Utils.copy(validConfig), Utils.copy(possibility)));
      }
    }
    validConfigs = Utils.copy(setCompResult_2);
  }

  public void xorParentConfigs(final Parent parent) {

    VDMSet subFeatures = parent.getSubFeaturesNames();
    VDMSet setCompResult_3 = SetUtil.set();
    VDMSet set_5 = Utils.copy(validConfigs);
    for (Iterator iterator_5 = set_5.iterator(); iterator_5.hasNext(); ) {
      VDMSet validConfig = ((VDMSet) iterator_5.next());
      if (!(SetUtil.inSet(parent.getName(), validConfig))) {
        setCompResult_3.add(Utils.copy(validConfig));
      }
    }
    VDMSet configsWithoutParent = Utils.copy(setCompResult_3);
    VDMSet setCompResult_4 = SetUtil.set();
    VDMSet set_6 = Utils.copy(validConfigs);
    for (Iterator iterator_6 = set_6.iterator(); iterator_6.hasNext(); ) {
      VDMSet validConfig = ((VDMSet) iterator_6.next());
      if (SetUtil.inSet(parent.getName(), validConfig)) {
        setCompResult_4.add(Utils.copy(validConfig));
      }
    }
    VDMSet configsWithParent = Utils.copy(setCompResult_4);
    VDMSet setCompResult_5 = SetUtil.set();
    VDMSet set_7 = Utils.copy(configsWithParent);
    for (Iterator iterator_7 = set_7.iterator(); iterator_7.hasNext(); ) {
      VDMSet config = ((VDMSet) iterator_7.next());
      VDMSet setCompResult_6 = SetUtil.set();
      VDMSet set_9 = SetUtil.powerset(Utils.copy(subFeatures));
      for (Iterator iterator_9 = set_9.iterator(); iterator_9.hasNext(); ) {
        VDMSet elem = ((VDMSet) iterator_9.next());
        if (Utils.equals(elem.size(), 1L)) {
          setCompResult_6.add(Utils.copy(elem));
        }
      }
      VDMSet set_8 = Utils.copy(setCompResult_6);
      for (Iterator iterator_8 = set_8.iterator(); iterator_8.hasNext(); ) {
        VDMSet possibility = ((VDMSet) iterator_8.next());
        setCompResult_5.add(SetUtil.union(Utils.copy(config), Utils.copy(possibility)));
      }
    }
    validConfigs = SetUtil.union(Utils.copy(setCompResult_5), Utils.copy(configsWithoutParent));
  }

  public void orParentConfigs(final Parent parent) {

    VDMSet subFeatures = parent.getSubFeaturesNames();
    VDMSet setCompResult_7 = SetUtil.set();
    VDMSet set_10 = Utils.copy(validConfigs);
    for (Iterator iterator_10 = set_10.iterator(); iterator_10.hasNext(); ) {
      VDMSet validConfig = ((VDMSet) iterator_10.next());
      if (!(SetUtil.inSet(parent.getName(), validConfig))) {
        setCompResult_7.add(Utils.copy(validConfig));
      }
    }
    VDMSet configsWithoutParent = Utils.copy(setCompResult_7);
    VDMSet setCompResult_8 = SetUtil.set();
    VDMSet set_11 = Utils.copy(validConfigs);
    for (Iterator iterator_11 = set_11.iterator(); iterator_11.hasNext(); ) {
      VDMSet validConfig = ((VDMSet) iterator_11.next());
      if (SetUtil.inSet(parent.getName(), validConfig)) {
        setCompResult_8.add(Utils.copy(validConfig));
      }
    }
    VDMSet configsWithParent = Utils.copy(setCompResult_8);
    VDMSet setCompResult_9 = SetUtil.set();
    VDMSet set_12 = Utils.copy(configsWithParent);
    for (Iterator iterator_12 = set_12.iterator(); iterator_12.hasNext(); ) {
      VDMSet config = ((VDMSet) iterator_12.next());
      VDMSet setCompResult_10 = SetUtil.set();
      VDMSet set_14 = SetUtil.powerset(Utils.copy(subFeatures));
      for (Iterator iterator_14 = set_14.iterator(); iterator_14.hasNext(); ) {
        VDMSet elem = ((VDMSet) iterator_14.next());
        if (!(Utils.empty(elem))) {
          setCompResult_10.add(Utils.copy(elem));
        }
      }
      VDMSet set_13 = Utils.copy(setCompResult_10);
      for (Iterator iterator_13 = set_13.iterator(); iterator_13.hasNext(); ) {
        VDMSet possibility = ((VDMSet) iterator_13.next());
        setCompResult_9.add(SetUtil.union(Utils.copy(config), Utils.copy(possibility)));
      }
    }
    validConfigs = SetUtil.union(Utils.copy(setCompResult_9), Utils.copy(configsWithoutParent));
  }

  public void checkOptionalParents(final Feature feature, final VDMSet optionalParents) {

    VDMSet setCompResult_11 = SetUtil.set();
    VDMSet set_15 = Utils.copy(validConfigs);
    for (Iterator iterator_15 = set_15.iterator(); iterator_15.hasNext(); ) {
      VDMSet elem = ((VDMSet) iterator_15.next());
      Boolean existsExpResult_2 = false;
      VDMSet set_16 = Utils.copy(optionalParents);
      for (Iterator iterator_16 = set_16.iterator();
          iterator_16.hasNext() && !(existsExpResult_2);
          ) {
        String optionalParent = ((String) iterator_16.next());
        Boolean andResult_4 = false;

        if (!(SetUtil.inSet(optionalParent, elem))) {
          if (SetUtil.inSet(feature.getName(), elem)) {
            andResult_4 = true;
          }
        }

        existsExpResult_2 = andResult_4;
      }
      if (!(existsExpResult_2)) {
        setCompResult_11.add(Utils.copy(elem));
      }
    }
    validConfigs = Utils.copy(setCompResult_11);
  }

  public ConfigSearcher() {}

  public String toString() {

    return "ConfigSearcher{"
        + "root := "
        + Utils.toString(root)
        + ", nodeCount := "
        + Utils.toString(nodeCount)
        + ", restrictedInvalidSubsets := "
        + Utils.toString(restrictedInvalidSubsets)
        + ", validConfigs := "
        + Utils.toString(validConfigs)
        + "}";
  }
}
