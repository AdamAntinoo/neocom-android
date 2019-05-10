//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.activity.core.APublicCredentialActivity;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.SkillsFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class SkillsManagerActivity extends APublicCredentialActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("SkillsManagerActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		logger.info(">> [SkillsManagerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		// Initialization performed on superclass. We just can be centered on the creation of the fragments.
		try {
			this.addPage(new SkillsFragment().setVariant(NeoComApplication.EVariants.SKILLS_QUEUE.name()));
			this.addPage(new SkillsFragment().setVariant(NeoComApplication.EVariants.SKILLS_TRAINED.name()));
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("SkillsManagerActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [SkillsManagerActivity.onCreate]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
