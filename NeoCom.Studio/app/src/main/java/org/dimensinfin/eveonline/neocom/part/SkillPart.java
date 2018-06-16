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
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Hashtable;
import java.util.Hashtable;
import java.util.Hashtable;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.Skill;
import org.dimensinfin.eveonline.neocom.part.core.TypedPart;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class SkillPart extends TypedPart<Skill> {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("SkillPart");


	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SkillPart( final Skill model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public long getModelId() {
		return 0;
	}

	@Override
	public AbstractRender selectRenderer() {
		return null;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("SkillPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}


	// - CLASS IMPLEMENTATION ...................................................................................
	public static class SkillRender extends AbstractRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private ImageView	nodeIcon		= null;
		private TextView	itemName		= null;
		private TextView	qtyRequired	= null;
		private ImageView	skillLevel1	= null;
		private ImageView	skillLevel2	= null;
		private ImageView	skillLevel3	= null;
		private ImageView	skillLevel4	= null;
		private ImageView	skillLevel5	= null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public SkillRender(final SkillPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public SkillPart getPart() {
			return (SkillPart) super.getPart();
		}

		@Override
		public void initializeViews() {
//			super.initializeViews();
			nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
			itemName = (TextView) _convertView.findViewById(R.id.itemName);
			qtyRequired = (TextView) _convertView.findViewById(R.id.qtyRequired);
			skillLevel1 = (ImageView) _convertView.findViewById(R.id.skillLevel1);
			skillLevel2 = (ImageView) _convertView.findViewById(R.id.skillLevel2);
			skillLevel3 = (ImageView) _convertView.findViewById(R.id.skillLevel3);
			skillLevel4 = (ImageView) _convertView.findViewById(R.id.skillLevel4);
			skillLevel5 = (ImageView) _convertView.findViewById(R.id.skillLevel5);
		}

		public void updateContent() {
//			super.updateContent();
			itemName.setText(getPart().getCastedModel().getSkillName());
			// Get the skill level requested and the current skill level.
//			int requested = getPart().getCastedModel().getStartDate();
			int current = getPart().getCastedModel().getFinishedLevel();
			int requested=current;
			// Set color for levels.
			skillLevel1.setBackgroundResource(calculateColor4(1, requested, current));
			skillLevel2.setBackgroundResource(calculateColor4(2, requested, current));
			skillLevel3.setBackgroundResource(calculateColor4(3, requested, current));
			skillLevel4.setBackgroundResource(calculateColor4(4, requested, current));
			skillLevel5.setBackgroundResource(calculateColor4(5, requested, current));
			qtyRequired.setText("Level " + requested);
//			if (requested <= current) itemIcon.setBackgroundResource(R.drawable.completeresourceframe);
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.skill4industry;
		}

		private int calculateColor4(final int i, final int requested, final int current) {
			int color = R.drawable.skillempty;
			if (requested >= i) color = R.drawable.skillpending;
			if (current >= i) color = R.drawable.skillcompleted;
			return color;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
