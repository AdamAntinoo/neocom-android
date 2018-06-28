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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.ANodeBase4ExpandRender;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.SkillTrained;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class SkillTrainedPart extends NeoComAbstractPart<SkillTrained> {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SkillTrainedPart( final SkillTrained model ) {
		super(model);
		this.setThemeColor(Separator.ESeparatorType.LINE_GREEN);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public long getModelId() {
		return this.getCastedModel().getSkillId();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new SkillTrainedRender(this, getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("SkillTrainedPart [ ");
		buffer.append(this.getCastedModel().toString());
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class SkillTrainedRender extends ANodeBase4ExpandRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		protected ProgressBar trainedProgress = null;
		protected TextView skillName = null;
		protected TextView currentLevel = null;
		protected TextView trainingCompleted = null;
		protected ImageView skillLevel1 = null;
		protected ImageView skillLevel2 = null;
		protected ImageView skillLevel3 = null;
		protected ImageView skillLevel4 = null;
		protected ImageView skillLevel5 = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public SkillTrainedRender( final SkillTrainedPart target, final Activity context ) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public SkillTrainedPart getPart() {
			return (SkillTrainedPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			trainedProgress = (ProgressBar) _convertView.findViewById(R.id.trainedProgress);
			skillName = (TextView) _convertView.findViewById(R.id.skillName);
			currentLevel = (TextView) _convertView.findViewById(R.id.currentLevel);
			trainingCompleted = (TextView) _convertView.findViewById(R.id.trainingCompleted);
			skillLevel1 = (ImageView) _convertView.findViewById(R.id.skillLevel1);
			skillLevel2 = (ImageView) _convertView.findViewById(R.id.skillLevel2);
			skillLevel3 = (ImageView) _convertView.findViewById(R.id.skillLevel3);
			skillLevel4 = (ImageView) _convertView.findViewById(R.id.skillLevel4);
			skillLevel5 = (ImageView) _convertView.findViewById(R.id.skillLevel5);
		}

		public void updateContent() {
			super.updateContent();
			nodeIcon.setImageResource(R.drawable.skills);
			skillName.setText(this.getPart().getCastedModel().getSkillName());
			//			trainingCompleted.setText(this.getPart().getTPctDone());
			int trained = this.getPart().getCastedModel().getTrainedSkillLevel();
			int active = this.getPart().getCastedModel().getActiveSkillLevel();
			//			int current = training - 1;
			int requested = active;
			currentLevel.setText("Level " + Integer.valueOf(active).toString());
			// Set color for levels.
			skillLevel1.setBackgroundResource(calculateColor4(1, requested, active, trained));
			skillLevel2.setBackgroundResource(calculateColor4(2, requested, active, trained));
			skillLevel3.setBackgroundResource(calculateColor4(3, requested, active, trained));
			skillLevel4.setBackgroundResource(calculateColor4(4, requested, active, trained));
			skillLevel5.setBackgroundResource(calculateColor4(5, requested, active, trained));
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.skilltrained4list;
		}

		private int calculateColor4( final int i, final int requested, final int active, final int trained ) {
			int color = R.drawable.skillempty;
			if ( i <= active ) color = R.drawable.skilldisallowed;
			if ( i <= trained ) color = R.drawable.skillcompleted;
			return color;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
