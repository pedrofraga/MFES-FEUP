package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class FeatureModelTester {
  private void assertTrue(final Boolean cond) {

    return;
  }

  private void assertFalse(final Boolean cond) {

    return;
  }

  protected void testParentType(final Parent parent, final Object type) {

    assertTrue(Utils.equals(parent.getParentType(), type));
  }

  protected void testMandatoryFeature(final Feature feature, final Boolean isMandatory) {

    assertTrue(Utils.equals(feature.isMandatory(), isMandatory));
  }

  protected void testSubFeatures(final Parent parent, final VDMSet subFeatures) {

    assertTrue(Utils.equals(parent.getSubFeatures(), subFeatures));
  }

  protected void testRequirements(final Feature feature, final VDMSet requirements) {

    assertTrue(Utils.equals(feature.getRequirements(), requirements));
  }

  protected void testExclusions(final Feature feature, final VDMSet exclusions) {

    assertTrue(Utils.equals(feature.getExclusions(), exclusions));
  }

  protected void testModelFeaturesCount(final Model model, final Number count) {

    assertTrue(Utils.equals(model.nodeCount(), count));
  }

  protected void validModelConfig(final Model model, final VDMSet config) {

    assertTrue(SetUtil.inSet(config, model.generateValidConfigs()));
  }

  protected void invalidModelConfig(final Model model, final VDMSet config) {

    assertFalse(SetUtil.inSet(config, model.generateValidConfigs()));
  }

  protected void testGeneratedValidConfigs(final Model model, final VDMSet validSet) {

    VDMSet generatedSet = model.generateValidConfigs();
    assertTrue(
        Utils.equals(SetUtil.union(Utils.copy(validSet), Utils.copy(generatedSet)), validSet));
  }

  protected void testModelRoot(final Model model, final Parent root) {

    assertTrue(Utils.equals(model.getRoot(), root));
  }

  protected void testRequirementsBeforeExclusions() {

    {
      final Feature feature1 = new Feature("feature1");
      final Feature feature2 = new Feature("feature2");
      {
        feature1.setRequirements(SetUtil.set(feature2));
        feature1.setExclusions(SetUtil.set(feature2));
      }
    }
  }

  protected void testExclusionsBeforeRequirements() {

    {
      final Feature feature1 = new Feature("feature1");
      final Feature feature2 = new Feature("feature2");
      {
        feature1.setExclusions(SetUtil.set(feature2));
        feature1.setRequirements(SetUtil.set(feature2));
      }
    }
  }

  public static void main() {

    EshopModelTester eshopModelTester = new EshopModelTester();
    MobilePhoneModelTester mobilePhoneModelTester = new MobilePhoneModelTester();
    CarModelTester carModelTester = new CarModelTester();
    eshopModelTester.testModel();
    mobilePhoneModelTester.testModel();
    carModelTester.testModel();
  }

  public FeatureModelTester() {}

  public String toString() {

    return "FeatureModelTester{}";
  }
}
