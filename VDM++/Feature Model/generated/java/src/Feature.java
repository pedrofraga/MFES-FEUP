
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
      for (Iterator iterator_26 = requirements.iterator(); iterator_26.hasNext(); ) {
        Feature requirement = (Feature) iterator_26.next();
        restrictions =
            SetUtil.union(
                Utils.copy(restrictions),
                SetUtil.set(
                    MapUtil.munion(
                        MapUtil.map(new Maplet(name, true)),
                        MapUtil.map(new Maplet(requirement.getName(), false)))));
      }
      for (Iterator iterator_27 = exclusions.iterator(); iterator_27.hasNext(); ) {
        Feature exclusion = (Feature) iterator_27.next();
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

  public Boolean isRespectingReqAndExc(final VDMMap c) {

    VDMSet configuredFeatures = MapUtil.dom(MapUtil.rngResTo(Utils.copy(c), SetUtil.set(true)));
    if (SetUtil.inSet(name, configuredFeatures)) {
      for (Iterator iterator_28 = requirements.iterator(); iterator_28.hasNext(); ) {
        Feature requirement = (Feature) iterator_28.next();
        if (!(SetUtil.inSet(requirement.name, configuredFeatures))) {
          return false;
        }
      }
      for (Iterator iterator_29 = exclusions.iterator(); iterator_29.hasNext(); ) {
        Feature exclusion = (Feature) iterator_29.next();
        if (SetUtil.inSet(exclusion.name, configuredFeatures)) {
          return false;
        }
      }
    }

    return true;
  }

  public Boolean isValidConfiguration(final VDMMap c) {

    Boolean andResult_5 = false;

    if (mandatory) {
      if (!(Utilities.isNameConfigured(name, Utils.copy(c)))) {
        andResult_5 = true;
      }
    }

    if (andResult_5) {
      return false;
    }

    return isRespectingReqAndExc(Utils.copy(c));
  }

  protected Boolean isOptionalSubFeature() {

    return !(mandatory);
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