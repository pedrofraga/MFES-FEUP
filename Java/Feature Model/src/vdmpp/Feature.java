package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Feature {
  public String name;
  protected Boolean mandatory = true;
  protected VDMSet requirements = SetUtil.set();
  protected VDMSet exclusions = SetUtil.set();

  public void cg_init_Feature_1(final String n) {

    name = n;
    return;
  }

  public Feature(final String n) {

    cg_init_Feature_1(n);
  }

  public void setMandatory(final Boolean b) {

    mandatory = b;
  }

  public void setRequirements(final VDMSet fs) {

    requirements = Utils.copy(fs);
  }

  public void setExclusions(final VDMSet fs) {

    exclusions = Utils.copy(fs);
  }

  public VDMSet getRequirements() {

    return Utils.copy(requirements);
  }

  public VDMSet getExclusions() {

    return Utils.copy(exclusions);
  }

  public String getName() {

    return name;
  }

  public VDMSet features() {

    return SetUtil.set(name);
  }

  public VDMSet invalidSubsets() {

    VDMSet restrictions = getReqAndExcRestrictions();
    return Utils.copy(restrictions);
  }

  public VDMSet getReqAndExcRestrictions() {

    VDMSet restrictions = SetUtil.set();
    if (requirements.size() + exclusions.size() > 0L) {
      for (Iterator iterator_20 = requirements.iterator(); iterator_20.hasNext(); ) {
        Feature requirement = (Feature) iterator_20.next();
        restrictions =
            SetUtil.union(
                Utils.copy(restrictions),
                SetUtil.set(
                    MapUtil.munion(
                        MapUtil.map(new Maplet(name, true)),
                        MapUtil.map(new Maplet(requirement.getName(), false)))));
      }
      for (Iterator iterator_21 = exclusions.iterator(); iterator_21.hasNext(); ) {
        Feature exclusion = (Feature) iterator_21.next();
        restrictions =
            SetUtil.union(
                Utils.copy(restrictions),
                SetUtil.set(
                    MapUtil.munion(
                        MapUtil.map(new Maplet(name, true)),
                        MapUtil.map(new Maplet(exclusion.getName(), true)))));
      }
    }

    return Utils.copy(restrictions);
  }

  public Number nodeCount() {

    return 1L;
  }

  public Boolean isMandatory() {

    return mandatory;
  }

  public Feature() {}

  public String toString() {

    return "Feature{"
        + "name := "
        + Utils.toString(name)
        + ", mandatory := "
        + Utils.toString(mandatory)
        + ", requirements := "
        + Utils.toString(requirements)
        + ", exclusions := "
        + Utils.toString(exclusions)
        + "}";
  }
}
