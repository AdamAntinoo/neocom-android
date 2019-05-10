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
package org.dimensinfin.eveonline.neocom.model;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class SkillGroupContainer extends NameFacetedNodeContainer {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private int groupId = -1;
	private int completed = 0;
	private long totalSkillPoints = 0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SkillGroupContainer( final String name ) {
		super(name);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public int getGroupId() {
		return groupId;
	}

	public SkillGroupContainer setGroupId( final int groupId ) {
		this.groupId = groupId;
		return this;
	}

	public int addSkill( final SkillTrained skill ) {
		if (skill.getTrainedSkillLevel() == 5) this.completed++;
		totalSkillPoints += skill.getSkillpointsInSkill();
		return super.addNode(skill);
	}

	public int completedSkills() {
		return this.completed;
	}

	public long getTotalSkillPoints() {
		return this.totalSkillPoints;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
