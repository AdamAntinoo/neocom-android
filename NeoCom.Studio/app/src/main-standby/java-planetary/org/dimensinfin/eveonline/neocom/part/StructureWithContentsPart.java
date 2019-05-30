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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.planetary.ColonyStructure;
import org.dimensinfin.eveonline.neocom.render.NewBaseExpandableRender;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class StructureWithContentsPart extends NeoComExpandablePart /*implements View.OnClickListener*/ {
	// - CLASS IMPLEMENTATION ...................................................................................
	public static class StructureRender extends NewBaseExpandableRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		protected ImageView nodeIcon = null;
		protected TextView structureName = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public StructureRender (final StructureWithContentsPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public StructureWithContentsPart getPart () {
			return (StructureWithContentsPart) super.getPart();
		}

		@Override
		public void initializeViews () {
			super.initializeViews();
			nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
			structureName = (TextView) _convertView.findViewById(R.id.structureName);
		}

		@Override
		public void updateContent () {
			super.updateContent();
			final ColonyStructure model = getPart().getCastedModel();
			final long type = model.getTypeId();
			final String name = model.getStructureTypeCode().name();
			structureName.setText(name);
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.planets));
		}

		protected void createView () {
			_convertView = inflateView(R.layout.structure4list);
			_convertView.setTag(this);
		}
	}

	public static class CommandCenterRender extends NewBaseExpandableRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		protected TextView structureType = null;
		protected TextView fillLevelLabel = null;
		protected TextView fillLevelNumbers = null;
		protected ProgressBar storageUsedProgress = null;
		protected TextView cpuLevel = null;
		protected TextView cpuLevelData = null;
		protected ProgressBar cpuProgress = null;
		protected TextView powerLevel = null;
		protected TextView powerLevelData = null;
		protected ProgressBar powerProgress = null;

		protected ViewGroup commandCenterInfoBlock = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public CommandCenterRender (final StructureWithContentsPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public StructureWithContentsPart getPart () {
			return (StructureWithContentsPart) super.getPart();
		}

		@Override
		public void initializeViews () {
			super.initializeViews();
			structureType = (TextView) _convertView.findViewById(R.id.structureType);
			fillLevelLabel = (TextView) _convertView.findViewById(R.id.fillLevelLabel);
			fillLevelNumbers = (TextView) _convertView.findViewById(R.id.fillLevelNumbers);
			storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
			cpuLevel = (TextView) _convertView.findViewById(R.id.cpuLevel);
			cpuLevelData = (TextView) _convertView.findViewById(R.id.cpuLevelData);
			cpuProgress = (ProgressBar) _convertView.findViewById(R.id.cpuProgress);
			powerLevel = (TextView) _convertView.findViewById(R.id.powerLevel);
			powerLevelData = (TextView) _convertView.findViewById(R.id.powerLevelData);
			powerProgress = (ProgressBar) _convertView.findViewById(R.id.powerProgress);

			commandCenterInfoBlock = (ViewGroup) _convertView.findViewById(R.id.commandCenterInfoBlock);
		}

		@Override
		public void updateContent () {
			super.updateContent();
			final ColonyStructure model = getPart().getCastedModel();
			structureType.setText("Command Center");
			fillLevelLabel.setText(getPart().getTFillLevel());
			fillLevelNumbers.setText(getPart().getTFillData());
			storageUsedProgress.setMax(100);
			storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
			storageUsedProgress.setScaleY(2f);
			cpuLevel.setText(getPart().getTCPULevel());
			cpuLevelData.setText("7,945/12,456 tf");
			nodeIcon.setImageResource(R.drawable.commandcenter80_white);
			// Update the right arrow depending on model content and status.
			if ( model instanceof IExpandable ) {
				if ( ((IExpandable) model).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
			} else _rightArrow.setVisibility(View.GONE);
		}

		protected void createView () {
			_convertView = inflateView(R.layout.commandcenter4colony);
			_convertView.setTag(this);
		}
	}

	public static class LaunchpadRender extends CommandCenterRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		//		protected TextView structureType = null;
		//		protected TextView fillLevelLabel = null;
		//		protected TextView fillLevelNumbers = null;
		//		protected ProgressBar storageUsedProgress = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public LaunchpadRender (final StructureWithContentsPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public StructureWithContentsPart getPart () {
			return (StructureWithContentsPart) super.getPart();
		}

		//		@Override
		//		public void initializeViews () {
		//			super.initializeViews();
		//			structureType = (TextView) _convertView.findViewById(R.id.structureType);
		//			fillLevelLabel = (TextView) _convertView.findViewById(R.id.fillLevelLabel);
		//			fillLevelNumbers = (TextView) _convertView.findViewById(R.id.fillLevelNumbers);
		//			storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
		//		}

		@Override
		public void updateContent () {
			super.updateContent();
			final ColonyStructure model = getPart().getCastedModel();
			structureType.setText("Launch Pad");
			//						fillLevelLabel.setText(getPart().getTFillLevel());
			//			fillLevelNumbers.setText(getPart().getTFillData());
			//			storageUsedProgress.setMax(100);
			//			storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
			//			storageUsedProgress.setScaleY(2f);
			nodeIcon.setImageResource(R.drawable.launchpad80_white);
			// Hide the cpu and power blocks that are not used by this planetaryFacility.
			commandCenterInfoBlock.setVisibility(View.GONE);
		}
	}

	public static class StorageRender extends CommandCenterRender {
		public StorageRender (final StructureWithContentsPart target, final Activity context) {
			super(target, context);
		}

		public void updateContent () {
			super.updateContent();
			structureType.setText("Storage");
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.storage80_white));
			nodeIcon.setImageResource(R.drawable.storage80_white);
			// Hide the cpu and power blocks that are not used by this planetaryFacility.
			commandCenterInfoBlock.setVisibility(View.GONE);
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public StructureWithContentsPart (final ColonyStructure node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ColonyStructure getCastedModel () {
		return (ColonyStructure) this.getModel();
	}

	public long getModelID () {
		return getCastedModel().getPinId();
	}

	/**
	 * The render selected will depend on the PlanetaryFacility type stored on the Model.
	 */
	protected AbstractRender selectRenderer () {
		final ColonyStructure.EPlanetaryStructureType structureType = getCastedModel().getStructureTypeCode();
		switch (structureType) {
			case COMMAND_CENTER:
				return new CommandCenterRender(this, _activity);
			case LAUNCHPAD:
				return new LaunchpadRender(this, _activity);
			case STORAGE:
				return new StorageRender(this, _activity);
		}
		return new StructureRender(this, _activity);
	}

	public String getTFillLevel () {
		return new StringBuffer("Capacity Used: ")
				.append(percentageFormat.format(this.getCastedModel().getVolumeUsedPct())).append("%")
				.toString();
	}

	public String getTFillData () {
		final double used = this.getCastedModel().getVolumeUsed();
		final double capacity = this.getCastedModel().getCapacity();
		return new StringBuffer(itemCountFormatter.format(used))
				.append("/")
				.append(itemCountFormatter.format(capacity))
				.toString();
	}

	public String getTCPULevel () {
		// Check if we can access the parent Part that should be a Colony.
		final IPart colony = getParentPart();
		return "41.0%";
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("StructureWithContentsPart [");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
