package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class CarModelTester extends FeatureModelTester {
  private Parent car = new Parent("car");
  private Feature body = new Feature("body");
  private Parent transmission = new Parent("transmission");
  private Feature automatic = new Feature("automatic");
  private Feature manual = new Feature("manual");
  private Parent pullsTrailler = new Parent("pullsTrailler");
  private Parent heavyTrailler = new Parent("heavyTrailler");
  private Feature armor = new Feature("armor");
  private Feature lightTrailler = new Feature("lightTrailler");
  private Parent engine = new Parent("engine");
  private Feature gasoline = new Feature("gasoline");
  private Feature electric = new Feature("electric");

  public Model createModel() {

    pullsTrailler.setParentType(quotes.xorParentQuote.getInstance());
    transmission.setParentType(quotes.xorParentQuote.getInstance());
    engine.setParentType(quotes.orParentQuote.getInstance());
    car.setSubFeatures(SeqUtil.seq(body, transmission, pullsTrailler, engine));
    transmission.setSubFeatures(SeqUtil.seq(automatic, manual));
    engine.setSubFeatures(SeqUtil.seq(gasoline, electric));
    pullsTrailler.setSubFeatures(SeqUtil.seq(lightTrailler, heavyTrailler));
    heavyTrailler.setSubFeatures(SeqUtil.seq(armor));
    pullsTrailler.setMandatory(false);
    armor.setMandatory(false);
    return new Model(car);
  }

  public void testModel() {

    Model carModel = createModel();
    testParentType(pullsTrailler, quotes.xorParentQuote.getInstance());
    testParentType(transmission, quotes.xorParentQuote.getInstance());
    testParentType(engine, quotes.orParentQuote.getInstance());
    testParentType(car, quotes.defaultParentQuote.getInstance());
    testParentType(heavyTrailler, quotes.defaultParentQuote.getInstance());
    testSubFeatures(car, SeqUtil.seq(body, transmission, pullsTrailler, engine));
    testSubFeatures(transmission, SeqUtil.seq(automatic, manual));
    testSubFeatures(engine, SeqUtil.seq(gasoline, electric));
    testSubFeatures(pullsTrailler, SeqUtil.seq(lightTrailler, heavyTrailler));
    testSubFeatures(heavyTrailler, SeqUtil.seq(armor));
    testMandatoryFeature(car, true);
    testMandatoryFeature(body, true);
    testMandatoryFeature(transmission, true);
    testMandatoryFeature(automatic, false);
    testMandatoryFeature(manual, false);
    testMandatoryFeature(pullsTrailler, false);
    testMandatoryFeature(heavyTrailler, false);
    testMandatoryFeature(lightTrailler, false);
    testMandatoryFeature(armor, false);
    testMandatoryFeature(engine, true);
    testMandatoryFeature(gasoline, false);
    testMandatoryFeature(electric, false);
    testRequirements(car, SeqUtil.seq());
    testRequirements(body, SeqUtil.seq());
    testRequirements(transmission, SeqUtil.seq());
    testRequirements(automatic, SeqUtil.seq());
    testRequirements(manual, SeqUtil.seq());
    testRequirements(pullsTrailler, SeqUtil.seq());
    testRequirements(heavyTrailler, SeqUtil.seq());
    testRequirements(lightTrailler, SeqUtil.seq());
    testRequirements(armor, SeqUtil.seq());
    testRequirements(engine, SeqUtil.seq());
    testRequirements(gasoline, SeqUtil.seq());
    testRequirements(electric, SeqUtil.seq());
    testExclusions(car, SeqUtil.seq());
    testExclusions(body, SeqUtil.seq());
    testExclusions(transmission, SeqUtil.seq());
    testExclusions(automatic, SeqUtil.seq());
    testExclusions(manual, SeqUtil.seq());
    testExclusions(pullsTrailler, SeqUtil.seq());
    testExclusions(heavyTrailler, SeqUtil.seq());
    testExclusions(lightTrailler, SeqUtil.seq());
    testExclusions(armor, SeqUtil.seq());
    testExclusions(engine, SeqUtil.seq());
    testExclusions(gasoline, SeqUtil.seq());
    testExclusions(electric, SeqUtil.seq());
    testModelRoot(carModel, car);
    testModelFeaturesCount(carModel, 12L);
    validModelConfig(
        carModel,
        MapUtil.map(
            new Maplet("car", true),
            new Maplet("body", true),
            new Maplet("transmission", true),
            new Maplet("automatic", true),
            new Maplet("manual", false),
            new Maplet("pullsTrailler", true),
            new Maplet("lightTrailler", true),
            new Maplet("heavyTrailler", false),
            new Maplet("armor", false),
            new Maplet("engine", true),
            new Maplet("electric", true),
            new Maplet("gasoline", true)));
    invalidModelConfig(
        carModel,
        MapUtil.map(
            new Maplet("car", false),
            new Maplet("body", true),
            new Maplet("transmission", true),
            new Maplet("automatic", true),
            new Maplet("manual", false),
            new Maplet("pullsTrailler", true),
            new Maplet("lightTrailler", true),
            new Maplet("heavyTrailler", false),
            new Maplet("armor", false),
            new Maplet("engine", true),
            new Maplet("electric", false),
            new Maplet("gasoline", false)));
    testGeneratedValidConfigs(carModel, Utilities.carValidConfigs());
  }

  public CarModelTester() {}

  public String toString() {

    return "CarModelTester{"
        + "car := "
        + Utils.toString(car)
        + ", body := "
        + Utils.toString(body)
        + ", transmission := "
        + Utils.toString(transmission)
        + ", automatic := "
        + Utils.toString(automatic)
        + ", manual := "
        + Utils.toString(manual)
        + ", pullsTrailler := "
        + Utils.toString(pullsTrailler)
        + ", heavyTrailler := "
        + Utils.toString(heavyTrailler)
        + ", armor := "
        + Utils.toString(armor)
        + ", lightTrailler := "
        + Utils.toString(lightTrailler)
        + ", engine := "
        + Utils.toString(engine)
        + ", gasoline := "
        + Utils.toString(gasoline)
        + ", electric := "
        + Utils.toString(electric)
        + "}";
  }
}