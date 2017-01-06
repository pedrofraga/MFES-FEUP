package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class ConfigSearcher {
  private Parent root;
  private Number nodeCount;
  private VDMSet restrictedInvalidSubsets;
  private VDMSet validConfigs = SetUtil.set();

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

    searchFeatureTree(root, true, SeqUtil.seq());
    applyRestrictions();
    return Utils.copy(validConfigs);
  }

  public void applyRestrictions() {

    VDMSet newValidConfigs = SetUtil.set();
    Boolean firstIteration = true;
    for (Iterator iterator_11 = restrictedInvalidSubsets.iterator(); iterator_11.hasNext(); ) {
      VDMMap restrictedInvalidSubset = (VDMMap) iterator_11.next();
      if (firstIteration) {
        VDMSet setCompResult_1 = SetUtil.set();
        VDMSet set_1 = Utils.copy(validConfigs);
        for (Iterator iterator_1 = set_1.iterator(); iterator_1.hasNext(); ) {
          VDMMap elem = ((VDMMap) iterator_1.next());
          if (!(SetUtil.subset(
              Utilities.mapToSet(Utils.copy(restrictedInvalidSubset)),
              Utilities.mapToSet(Utils.copy(elem))))) {
            setCompResult_1.add(Utils.copy(elem));
          }
        }
        newValidConfigs = Utils.copy(setCompResult_1);

        firstIteration = false;

      } else {
        VDMSet setCompResult_2 = SetUtil.set();
        VDMSet set_2 = Utils.copy(validConfigs);
        for (Iterator iterator_2 = set_2.iterator(); iterator_2.hasNext(); ) {
          VDMMap elem = ((VDMMap) iterator_2.next());
          if (!(SetUtil.subset(
              Utilities.mapToSet(Utils.copy(restrictedInvalidSubset)),
              Utilities.mapToSet(Utils.copy(elem))))) {
            setCompResult_2.add(Utils.copy(elem));
          }
        }
        newValidConfigs =
            SetUtil.intersect(Utils.copy(newValidConfigs), Utils.copy(setCompResult_2));
      }
    }
    if (!(Utils.empty(newValidConfigs))) {
      validConfigs = Utils.copy(newValidConfigs);
    }
  }

  public void searchFeatureTree(
      final Object feature, final Boolean hasDefaultParent, final VDMSeq optionalParents) {

    if (hasDefaultParent) {
      defaultParentConfigs(((Object) feature));
    }

    checkOptionalParents(((Feature) feature), Utils.copy(optionalParents));
    if (feature instanceof Parent) {
      VDMSeq newOptionalParents = Utils.copy(optionalParents);
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

        newOptionalParents = SeqUtil.conc(Utils.copy(newOptionalParents), SeqUtil.seq(apply_4));
      }

      VDMSet apply_5 = null;
      if (feature instanceof Parent) {
        apply_5 = ((Parent) feature).getSubFeatures();
      } else {
        throw new RuntimeException("Missing member: getSubFeatures");
      }

      for (Iterator iterator_12 = apply_5.iterator(); iterator_12.hasNext(); ) {
        Feature subFeature = (Feature) iterator_12.next();
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

  public void defaultParentConfigs(final Object feature) {

    VDMSet possibilities = SetUtil.set();
    Boolean apply_7 = null;
    if (feature instanceof Feature) {
      apply_7 = ((Feature) feature).isMandatory();
    } else if (feature instanceof Parent) {
      apply_7 = ((Parent) feature).isMandatory();
    } else {
      throw new RuntimeException("Missing member: isMandatory");
    }

    if (apply_7) {
      String apply_8 = null;
      if (feature instanceof Feature) {
        apply_8 = ((Feature) feature).getName();
      } else if (feature instanceof Parent) {
        apply_8 = ((Parent) feature).getName();
      } else {
        throw new RuntimeException("Missing member: getName");
      }

      possibilities = SetUtil.set(MapUtil.map(new Maplet(apply_8, true)));

    } else {
      String apply_9 = null;
      if (feature instanceof Feature) {
        apply_9 = ((Feature) feature).getName();
      } else if (feature instanceof Parent) {
        apply_9 = ((Parent) feature).getName();
      } else {
        throw new RuntimeException("Missing member: getName");
      }

      String apply_10 = null;
      if (feature instanceof Feature) {
        apply_10 = ((Feature) feature).getName();
      } else if (feature instanceof Parent) {
        apply_10 = ((Parent) feature).getName();
      } else {
        throw new RuntimeException("Missing member: getName");
      }

      possibilities =
          SetUtil.set(
              MapUtil.map(new Maplet(apply_9, true)), MapUtil.map(new Maplet(apply_10, false)));
    }

    if (Utils.equals(validConfigs.size(), 0L)) {
      validConfigs = Utils.copy(possibilities);

    } else {
      VDMSet newValidConfigs = SetUtil.set();
      for (Iterator iterator_13 = validConfigs.iterator(); iterator_13.hasNext(); ) {
        VDMMap validConfig = (VDMMap) iterator_13.next();
        for (Iterator iterator_14 = possibilities.iterator(); iterator_14.hasNext(); ) {
          VDMMap possibility = (VDMMap) iterator_14.next();
          newValidConfigs =
              SetUtil.union(
                  Utils.copy(newValidConfigs),
                  SetUtil.set(MapUtil.munion(Utils.copy(validConfig), Utils.copy(possibility))));
        }
      }
      validConfigs = Utils.copy(newValidConfigs);
    }
  }

  public void xorParentConfigs(final Parent parent) {

    VDMSet subFeatures = parent.getSubFeatures();
    VDMSet newValidConfigs = SetUtil.set();
    for (Iterator iterator_15 = validConfigs.iterator(); iterator_15.hasNext(); ) {
      VDMMap validConfig = (VDMMap) iterator_15.next();
      for (Iterator iterator_16 = subFeatures.iterator(); iterator_16.hasNext(); ) {
        Feature subFeaturesI = (Feature) iterator_16.next();
        VDMMap elements = MapUtil.map();
        for (Iterator iterator_17 = subFeatures.iterator(); iterator_17.hasNext(); ) {
          Feature subFeaturesJ = (Feature) iterator_17.next();
          Feature subFeature = subFeaturesJ;
          VDMMap element = MapUtil.map(new Maplet(subFeature.getName(), false));
          if (Utils.equals(subFeaturesI, subFeaturesJ)) {
            element = MapUtil.map(new Maplet(subFeature.getName(), true));
          }

          elements = MapUtil.munion(Utils.copy(elements), Utils.copy(element));
        }
        newValidConfigs =
            SetUtil.union(
                Utils.copy(newValidConfigs),
                SetUtil.set(MapUtil.munion(Utils.copy(validConfig), Utils.copy(elements))));
      }
      Boolean andResult_1 = false;

      if (!(parent.isMandatory())) {
        if (SetUtil.subset(
            Utilities.mapToSet(MapUtil.map(new Maplet(parent.getName(), false))),
            Utilities.mapToSet(Utils.copy(validConfig)))) {
          andResult_1 = true;
        }
      }

      if (andResult_1) {
        VDMMap elements = MapUtil.map();
        for (Iterator iterator_18 = subFeatures.iterator(); iterator_18.hasNext(); ) {
          Feature subFeature = (Feature) iterator_18.next();
          elements =
              MapUtil.munion(
                  Utils.copy(elements), MapUtil.map(new Maplet(subFeature.getName(), false)));
        }
        newValidConfigs =
            SetUtil.union(
                Utils.copy(newValidConfigs),
                SetUtil.set(MapUtil.munion(Utils.copy(validConfig), Utils.copy(elements))));
      }
    }
    validConfigs = Utils.copy(newValidConfigs);
  }

  public void orParentConfigs(final Parent parent) {

    VDMSet subFeatures = parent.getSubFeaturesNames();
    VDMSeq possibilities = SeqUtil.seq();
    VDMSet validPossibilities = SetUtil.set();
    for (Iterator iterator_19 = subFeatures.iterator(); iterator_19.hasNext(); ) {
      String subFeature = (String) iterator_19.next();
      VDMSet setCompResult_3 = SetUtil.set();
      VDMSet set_3 = SetUtil.set(true, false);
      for (Iterator iterator_3 = set_3.iterator(); iterator_3.hasNext(); ) {
        Boolean b2 = ((Boolean) iterator_3.next());
        setCompResult_3.add(MapUtil.map(new Maplet(subFeature, b2)));
      }
      possibilities =
          SeqUtil.conc(Utils.copy(possibilities), SeqUtil.seq(Utils.copy(setCompResult_3)));
    }
    for (Iterator iterator_20 =
            consumeOrPossibilities(parent, Utils.copy(possibilities), possibilities.size())
                .iterator();
        iterator_20.hasNext();
        ) {
      VDMMap element = (VDMMap) iterator_20.next();
      for (Iterator iterator_21 = validConfigs.iterator(); iterator_21.hasNext(); ) {
        VDMMap validConfig = (VDMMap) iterator_21.next();
        validPossibilities =
            SetUtil.union(
                Utils.copy(validPossibilities),
                SetUtil.set(MapUtil.munion(Utils.copy(validConfig), Utils.copy(element))));
      }
    }
    validConfigs = Utils.copy(validPossibilities);
  }

  public VDMSeq consumeOrPossibilities(
      final Feature parent, final VDMSeq possibilities, final Number originalLength) {

    VDMSeq validPossibilities = SeqUtil.seq();
    if (Utils.equals(possibilities.size(), 1L)) {
      for (Iterator iterator_22 = ((VDMSet) Utils.get(possibilities, 1L)).iterator();
          iterator_22.hasNext();
          ) {
        VDMMap possibility = (VDMMap) iterator_22.next();
        validPossibilities =
            SeqUtil.conc(Utils.copy(validPossibilities), SeqUtil.seq(Utils.copy(possibility)));
      }
    } else {
      if (possibilities.size() > 1L) {
        for (Iterator iterator_23 = ((VDMSet) Utils.get(possibilities, 1L)).iterator();
            iterator_23.hasNext();
            ) {
          VDMMap possibility = (VDMMap) iterator_23.next();
          for (Iterator iterator_24 =
                  consumeOrPossibilities(
                          parent, SeqUtil.tail(Utils.copy(possibilities)), originalLength)
                      .iterator();
              iterator_24.hasNext();
              ) {
            VDMMap element = (VDMMap) iterator_24.next();
            {
              VDMMap conf = MapUtil.munion(Utils.copy(possibility), Utils.copy(element));
              Boolean andResult_2 = false;

              if (Utils.equals(possibilities.size(), originalLength)) {
                Boolean andResult_3 = false;

                if (Utils.equals(MapUtil.rng(Utils.copy(conf)), SetUtil.set(false))) {
                  if (parent.isMandatory()) {
                    andResult_3 = true;
                  }
                }

                if (andResult_3) {
                  andResult_2 = true;
                }
              }

              if (!(andResult_2)) {
                validPossibilities =
                    SeqUtil.conc(
                        Utils.copy(validPossibilities),
                        SeqUtil.seq(MapUtil.munion(Utils.copy(possibility), Utils.copy(element))));
              }
            }
          }
        }
      }
    }

    return Utils.copy(validPossibilities);
  }

  public void checkOptionalParents(final Feature feature, final VDMSeq optionalParents) {

    VDMSet newValidConfigs = SetUtil.set();
    for (Iterator iterator_25 = optionalParents.iterator(); iterator_25.hasNext(); ) {
      String optionalParent = (String) iterator_25.next();
      {
        VDMMap invalidConfig =
            MapUtil.map(new Maplet(optionalParent, false), new Maplet(feature.getName(), true));
        if (Utils.empty(newValidConfigs)) {
          VDMSet setCompResult_4 = SetUtil.set();
          VDMSet set_4 = Utils.copy(validConfigs);
          for (Iterator iterator_4 = set_4.iterator(); iterator_4.hasNext(); ) {
            VDMMap elem = ((VDMMap) iterator_4.next());
            if (!(SetUtil.subset(
                Utilities.mapToSet(Utils.copy(invalidConfig)),
                Utilities.mapToSet(Utils.copy(elem))))) {
              setCompResult_4.add(Utils.copy(elem));
            }
          }
          newValidConfigs = Utils.copy(setCompResult_4);

        } else {
          VDMSet setCompResult_5 = SetUtil.set();
          VDMSet set_5 = Utils.copy(validConfigs);
          for (Iterator iterator_5 = set_5.iterator(); iterator_5.hasNext(); ) {
            VDMMap elem = ((VDMMap) iterator_5.next());
            if (!(SetUtil.subset(
                Utilities.mapToSet(Utils.copy(invalidConfig)),
                Utilities.mapToSet(Utils.copy(elem))))) {
              setCompResult_5.add(Utils.copy(elem));
            }
          }
          newValidConfigs =
              SetUtil.intersect(Utils.copy(newValidConfigs), Utils.copy(setCompResult_5));
        }
      }
    }
    if (!(Utils.empty(newValidConfigs))) {
      validConfigs = Utils.copy(newValidConfigs);
    }
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