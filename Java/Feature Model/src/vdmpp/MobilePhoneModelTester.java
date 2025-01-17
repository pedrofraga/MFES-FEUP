package vdmpp;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class MobilePhoneModelTester extends FeatureModelTester {
  private Parent mobilePhone = new Parent("mobile phone");
  private Feature calls = new Feature("calls");
  private Feature gps = new Feature("gps");
  private Parent screen = new Parent("screen");
  private Parent media = new Parent("media");
  private Feature basic = new Feature("basic");
  private Feature colour = new Feature("colour");
  private Feature highResolution = new Feature("high resolution");
  private Feature camera = new Feature("camera");
  private Feature mp3 = new Feature("mp3");

  public Model createModel() {

    Model model = new Model(mobilePhone);
    media.setParentType(quotes.orParentQuote.getInstance());
    screen.setParentType(quotes.xorParentQuote.getInstance());
    screen.setSubFeatures(SetUtil.set(basic, colour, highResolution));
    media.setSubFeatures(SetUtil.set(camera, mp3));
    mobilePhone.setSubFeatures(SetUtil.set(media, calls, gps, screen));
    gps.setMandatory(false);
    media.setMandatory(false);
    camera.setRequirements(SetUtil.set(highResolution));
    gps.setExclusions(SetUtil.set(basic));
    model.setFeatures();
    return model;
  }

  public void testModel() {

    Model mobilePhoneModel = createModel();
    testParentType(media, quotes.orParentQuote.getInstance());
    testParentType(screen, quotes.xorParentQuote.getInstance());
    testParentType(mobilePhone, quotes.defaultParentQuote.getInstance());
    testSubFeatures(screen, SetUtil.set(basic, colour, highResolution));
    testSubFeatures(media, SetUtil.set(camera, mp3));
    testSubFeatures(mobilePhone, SetUtil.set(media, calls, gps, screen));
    testMandatoryFeature(mobilePhone, true);
    testMandatoryFeature(calls, true);
    testMandatoryFeature(gps, false);
    testMandatoryFeature(screen, true);
    testMandatoryFeature(media, false);
    testMandatoryFeature(basic, false);
    testMandatoryFeature(colour, false);
    testMandatoryFeature(highResolution, false);
    testMandatoryFeature(camera, false);
    testMandatoryFeature(mp3, false);
    testRequirements(mobilePhone, SetUtil.set());
    testRequirements(calls, SetUtil.set());
    testRequirements(gps, SetUtil.set());
    testRequirements(screen, SetUtil.set());
    testRequirements(media, SetUtil.set());
    testRequirements(basic, SetUtil.set());
    testRequirements(colour, SetUtil.set());
    testRequirements(highResolution, SetUtil.set());
    testRequirements(camera, SetUtil.set(highResolution));
    testRequirements(mp3, SetUtil.set());
    testExclusions(mobilePhone, SetUtil.set());
    testExclusions(calls, SetUtil.set());
    testExclusions(gps, SetUtil.set(basic));
    testExclusions(screen, SetUtil.set());
    testExclusions(media, SetUtil.set());
    testExclusions(basic, SetUtil.set());
    testExclusions(colour, SetUtil.set());
    testExclusions(highResolution, SetUtil.set());
    testExclusions(camera, SetUtil.set());
    testExclusions(mp3, SetUtil.set());
    testModelRoot(mobilePhoneModel, mobilePhone);
    testModelFeaturesCount(mobilePhoneModel, 10L);
    validModelConfig(mobilePhoneModel, SetUtil.set("basic", "calls", "mobile phone", "screen"));
    invalidModelConfig(mobilePhoneModel, SetUtil.set("mobile phone"));
    testGeneratedValidConfigs(mobilePhoneModel, mobilePhoneValidConfigs());
  }

  public MobilePhoneModelTester() {}

  public static VDMSet mobilePhoneValidConfigs() {

    return SetUtil.set(
        SetUtil.set("basic", "calls", "media", "mobile phone", "mp3", "screen"),
        SetUtil.set("basic", "calls", "mobile phone", "screen"),
        SetUtil.set(
            "calls", "camera", "gps", "high resolution", "media", "mobile phone", "mp3", "screen"),
        SetUtil.set("calls", "camera", "gps", "high resolution", "media", "mobile phone", "screen"),
        SetUtil.set("calls", "camera", "high resolution", "media", "mobile phone", "mp3", "screen"),
        SetUtil.set("calls", "camera", "high resolution", "media", "mobile phone", "screen"),
        SetUtil.set("calls", "colour", "gps", "media", "mobile phone", "mp3", "screen"),
        SetUtil.set("calls", "colour", "gps", "mobile phone", "screen"),
        SetUtil.set("calls", "colour", "media", "mobile phone", "mp3", "screen"),
        SetUtil.set("calls", "colour", "mobile phone", "screen"),
        SetUtil.set("calls", "gps", "high resolution", "media", "mobile phone", "mp3", "screen"),
        SetUtil.set("calls", "gps", "high resolution", "mobile phone", "screen"),
        SetUtil.set("calls", "high resolution", "media", "mobile phone", "mp3", "screen"),
        SetUtil.set("calls", "high resolution", "mobile phone", "screen"));
  }

  public String toString() {

    return "MobilePhoneModelTester{"
        + "mobilePhone := "
        + Utils.toString(mobilePhone)
        + ", calls := "
        + Utils.toString(calls)
        + ", gps := "
        + Utils.toString(gps)
        + ", screen := "
        + Utils.toString(screen)
        + ", media := "
        + Utils.toString(media)
        + ", basic := "
        + Utils.toString(basic)
        + ", colour := "
        + Utils.toString(colour)
        + ", highResolution := "
        + Utils.toString(highResolution)
        + ", camera := "
        + Utils.toString(camera)
        + ", mp3 := "
        + Utils.toString(mp3)
        + "}";
  }
}
