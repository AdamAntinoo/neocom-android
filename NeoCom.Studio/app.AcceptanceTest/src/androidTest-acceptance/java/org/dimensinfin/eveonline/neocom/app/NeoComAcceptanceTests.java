package org.dimensinfin.eveonline.neocom.app;

import cucumber.api.CucumberOptions;

//@RunWith(AndroidJUnit4.class)
@CucumberOptions(
		features = { "src/androidTest-acceptance/resources/features" },
		glue = { "org.dimensinfin.eveonline.neocom.app.steps" },
		plugin = { "pretty", "json:target/cucumber_report.json" },
		tags = { "not @skip_scenario", "not @front", "not @duplication" })
public class NeoComAcceptanceTests {
//	@Test
//	public void launchShowTheApplicationName() throws InterruptedException {
////		final ShowTheApplicationName test = new ShowTheApplicationName(new AppSplashActivityWorld());
////		test.the_activity_AppSplashActivity();
////		test.the_activity_is_started();
////		test.we_have_an_application_panel();
////		test.that_panel_has_an_ApplicationName_field();
////		test.that_field_shows_the_application_name_that_matches("NeoCom");
//	}
}