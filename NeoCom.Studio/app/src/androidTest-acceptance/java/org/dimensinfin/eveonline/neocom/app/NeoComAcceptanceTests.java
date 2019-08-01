package org.dimensinfin.eveonline.neocom.app;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/androidTest-acceptance/resources/features"},
		glue = {"org.dimensinfin.eveonline.neocom.app.steps"},
		plugin = {"pretty", "json:target/cucumber_report.json"},
		tags = {"not @skip_scenario", "not @front", "not @duplication"})
public class NeoComAcceptanceTests{}