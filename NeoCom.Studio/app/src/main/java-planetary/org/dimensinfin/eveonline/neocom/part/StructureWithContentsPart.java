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
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure;
import org.dimensinfin.eveonline.neocom.render.NewBaseExpandableRender;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class StructureWithContentsPart extends NeoComExpandablePart /*implements View.OnClickListener*/{
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
			final ColonyCoreStructure model = getPart().getCastedModel();
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
		protected TextView fillLevelNumber = null;
		protected ProgressBar storageUsedProgress = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public CommandCenterRender(final StructureWithContentsPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public StructureWithContentsPart getPart() {
			return (StructureWithContentsPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			structureType = (TextView) _convertView.findViewById(R.id.structureType);
			fillLevelNumber = (TextView) _convertView.findViewById(R.id.fillLevelNumber);
			storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			final ColonyCoreStructure model = getPart().getCastedModel();
			structureType.setText("Command Center");
			fillLevelNumber.setText(getPart().getTFillLevel());
			storageUsedProgress.setMax(100);
			storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
			storageUsedProgress.setScaleY(2f);
			//	setFillLevel();
			//		installationsNumber.setText(model.get);
			//		final long time = model.getLastLaunchTime();
			//		lastLaunchTime.setText("-");
			nodeIcon.setImageResource(R.drawable.commandcenter80_white);

			//		loadNodeIcon(nodeIcon, model.getTypeId());
			// Update the right arrow depending on model content and status.
			if ( model instanceof IExpandable ) {
				if ( ((IExpandable) model).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
			} else _rightArrow.setVisibility(View.GONE);
		}

		//        private void setFillLevel() {
		//            final int capacity = 12000;
		//            double fillLevel = 0.0;
		//            final List<PinContent> contents = getPart().getCastedModel().getContent();
		//            if ( null != contents ) {
		//                fillLevel = 0.0;
		//                for (PinContent content : contents) {
		//                    fillLevel += content.getAmount() * 0.1;
		//                }
		//            }
		//            final double progress = fillLevel / capacity * 100.0;
		//        }

		protected void createView() {
			_convertView = inflateView(R.layout.commandcenter4colony);
			_convertView.setTag(this);
		}
	}

	public static class LaunchpadRender extends CommandCenterRender {
		//        protected TextView structureType = null;
		//        protected TextView fillLevelNumber = null;
		//        protected ProgressBar storageUsedProgress = null;

		public LaunchpadRender(final StructureWithContentsPart target, final Activity context) {
			super(target, context);
		}

		//        @Override
		//        public ColonyStructurePart getPart() {
		//            return (ColonyStructurePart) super.getPart();
		//        }
		//
		//        @Override
		//        public void initializeViews() {
		//            super.initializeViews();
		//            structureType = (TextView) _convertView.findViewById(R.id.structureType);
		//            fillLevelNumber = (TextView) _convertView.findViewById(R.id.fillLevelNumber);
		//            storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
		//        }

		@Override
		public void updateContent() {
			super.updateContent();
			//      final Pin model = getPart().getCastedModel();
			structureType.setText("LAUNCHPAD");
			//            fillLevelNumber.setText(getPart().getTFillLevel());
			//            storageUsedProgress.setMax(100);
			//            storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
			//            storageUsedProgress.setScaleY(2f);
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.launchpad80_white));
			// Update the right arrow depending on model content and status.
			//            if ( getPart().getCastedModel() instanceof IExpandable ) {
			//                if ( ((IExpandable) getPart().getCastedModel()).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
			//            } else _rightArrow.setVisibility(View.GONE);
		}

		//         protected void createView() {
		//            _convertView = inflateView(R.layout.commandcenter4colony);
		//            _convertView.setTag(this);
		//        }
	}

	public static class StorageRender extends CommandCenterRender {
		//        protected TextView structureType = null;
		//        protected TextView fillLevelNumber = null;
		//        protected ProgressBar storageUsedProgress = null;

		public StorageRender(final StructureWithContentsPart target, final Activity context) {
			super(target, context);
		}

		//        @Override
		//        public ColonyStructurePart getPart() {
		//            return (ColonyStructurePart) super.getPart();
		//        }
		//
		//        @Override
		//        public void initializeViews() {
		//            super.initializeViews();
		//            structureType = (TextView) _convertView.findViewById(R.id.structureType);
		//            fillLevelNumber = (TextView) _convertView.findViewById(R.id.fillLevelNumber);
		//            storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
		//        }

		@Override
		public void updateContent() {
			super.updateContent();
			//      final Pin model = getPart().getCastedModel();
			structureType.setText("STORAGE");
			//            fillLevelNumber.setText(getPart().getTFillLevel());
			//            storageUsedProgress.setMax(100);
			//            storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
			//            storageUsedProgress.setScaleY(2f);
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.storage80_white));
			// Update the right arrow depending on model content and status.
			//            if ( getPart().getCastedModel() instanceof IExpandable ) {
			//                if ( ((IExpandable) getPart().getCastedModel()).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
			//            } else _rightArrow.setVisibility(View.GONE);
		}

		//         protected void createView() {
		//            _convertView = inflateView(R.layout.commandcenter4colony);
		//            _convertView.setTag(this);
		//        }
	}

	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public StructureWithContentsPart(final ColonyCoreStructure node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ColonyCoreStructure getCastedModel() {
		return (ColonyCoreStructure) this.getModel();
	}

	public long getModelID() {
		return -1;
	}

	/**
	 * The render selected will depend on the Structure type stored on the Model.
	 */
	protected AbstractRender selectRenderer() {
		final ColonyCoreStructure.EPlanetaryStructureType structureType = getCastedModel().getStructureTypeCode();
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
	public Spanned getTFillLevel () {
		return Html.fromHtml(this.generateLevelColor(this.getCastedModel().getVolumeUsedPct()
				, this.getCastedModel().getVolumeUsedPct()) + "%");
	}

	protected String generateLevelColor (double level, final double data) {
		StringBuffer htmlFragmentWithColor = new StringBuffer();
		String levelColor = "#00A000";
		// Get the color from the table.
		if ( level > 75.0 ) {
			levelColor = "#A0A000";
		}
		if ( level > 90.0 ) {
			levelColor = "#A00000";
		}
		htmlFragmentWithColor.append("<font color='").append(levelColor).append("'>").append(qtyFormatter.format(data)).append("</font>");
		//       htmlFragmentWithColor.append("%");
		return htmlFragmentWithColor.toString();
	}

//	@Override
//	public void onClick(final View view) {
//		logger.info(">> [StructureWithContentsPart.onClick]");
//		// Do the standard expand/collapse and the fire another message for the update of the model.
//		super.onClick(view);
//		GlobalAndroidDataManager.submitOnClick(
//				new OneShotTask<NeoComExpandablePart>(this) {
//					public void run () {
//						logger.info(">> [StructureWithContentsPart.onClick.OneShotTask.run]");
//						getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name(), this, this);
//						logger.info("<< [StructureWithContentsPart.onClick.OneShotTask.run]");
//					}
//				});
//		logger.info("<< [StructureWithContentsPart.onClick]");
//	}
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
