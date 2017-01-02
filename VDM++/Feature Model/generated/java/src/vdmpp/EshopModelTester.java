package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class EshopModelTester extends FeatureModelTester {
  private Parent eshop = new Parent("e-shop");
  private Feature catalogue = new Feature("catalogue");
  private Parent payment = new Parent("payment");
  private Parent security = new Parent("security");
  private Feature search = new Feature("search");
  private Feature bankTransfer = new Feature("bank transfer");
  private Feature creditCard = new Feature("credit card");
  private Feature high = new Feature("high");
  private Feature standard = new Feature("standard");

  public Model createModel() {

    payment.setParentType(quotes.orParentQuote.getInstance());
    security.setParentType(quotes.xorParentQuote.getInstance());
    payment.setSubFeatures(SeqUtil.seq(bankTransfer, creditCard));
    security.setSubFeatures(SeqUtil.seq(high, standard));
    eshop.setSubFeatures(SeqUtil.seq(catalogue, payment, security, search));
    search.setMandatory(false);
    creditCard.setRequirements(SeqUtil.seq(high));
    return new Model(eshop);
  }

  public void testModel() {

    Model eshopModel = createModel();
    testParentType(payment, quotes.orParentQuote.getInstance());
    testParentType(security, quotes.xorParentQuote.getInstance());
    testParentType(eshop, quotes.defaultParentQuote.getInstance());
    testSubFeatures(payment, SeqUtil.seq(bankTransfer, creditCard));
    testSubFeatures(security, SeqUtil.seq(high, standard));
    testSubFeatures(eshop, SeqUtil.seq(catalogue, payment, security, search));
    testMandatoryFeature(eshop, true);
    testMandatoryFeature(catalogue, true);
    testMandatoryFeature(payment, true);
    testMandatoryFeature(security, true);
    testMandatoryFeature(search, false);
    testMandatoryFeature(bankTransfer, false);
    testMandatoryFeature(creditCard, false);
    testMandatoryFeature(high, false);
    testMandatoryFeature(standard, false);
    testRequirements(eshop, SeqUtil.seq());
    testRequirements(catalogue, SeqUtil.seq());
    testRequirements(payment, SeqUtil.seq());
    testRequirements(security, SeqUtil.seq());
    testRequirements(search, SeqUtil.seq());
    testRequirements(bankTransfer, SeqUtil.seq());
    testRequirements(creditCard, SeqUtil.seq(high));
    testRequirements(high, SeqUtil.seq());
    testRequirements(standard, SeqUtil.seq());
    testExclusions(eshop, SeqUtil.seq());
    testExclusions(catalogue, SeqUtil.seq());
    testExclusions(payment, SeqUtil.seq());
    testExclusions(security, SeqUtil.seq());
    testExclusions(search, SeqUtil.seq());
    testExclusions(bankTransfer, SeqUtil.seq());
    testExclusions(creditCard, SeqUtil.seq());
    testExclusions(high, SeqUtil.seq());
    testExclusions(standard, SeqUtil.seq());
    testModelRoot(eshopModel, eshop);
    testModelFeaturesCount(eshopModel, 9L);
    validModelConfig(
        eshopModel,
        MapUtil.map(
            new Maplet("e-shop", true),
            new Maplet("catalogue", true),
            new Maplet("payment", true),
            new Maplet("bank transfer", true),
            new Maplet("credit card", false),
            new Maplet("security", true),
            new Maplet("high", true),
            new Maplet("standard", false),
            new Maplet("search", false)));
    invalidModelConfig(
        eshopModel,
        MapUtil.map(
            new Maplet("e-shop", true),
            new Maplet("catalogue", false),
            new Maplet("payment", true),
            new Maplet("bank transfer", true),
            new Maplet("credit card", false),
            new Maplet("security", true),
            new Maplet("high", true),
            new Maplet("standard", true),
            new Maplet("search", false)));
    testGeneratedValidConfigs(eshopModel, Utilities.eshopValidConfigs());
  }

  public EshopModelTester() {}

  public String toString() {

    return "EshopModelTester{"
        + "eshop := "
        + Utils.toString(eshop)
        + ", catalogue := "
        + Utils.toString(catalogue)
        + ", payment := "
        + Utils.toString(payment)
        + ", security := "
        + Utils.toString(security)
        + ", search := "
        + Utils.toString(search)
        + ", bankTransfer := "
        + Utils.toString(bankTransfer)
        + ", creditCard := "
        + Utils.toString(creditCard)
        + ", high := "
        + Utils.toString(high)
        + ", standard := "
        + Utils.toString(standard)
        + "}";
  }
}