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
import android.widget.TextView;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.core.OneShotTask;
import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.core.util.Chrono.ChonoOptions;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.database.entity.Colony;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.NewBaseExpandableRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ColonyPart extends NeoComExpandablePart {
	// - CLASS IMPLEMENTATION ...................................................................................
	public static class ColonyRender extends NewBaseExpandableRender {
		// - S T A T I C - S E C T I O N ..........................................................................
		private static Logger logger = LoggerFactory.getLogger("AssetRender");

		// - F I E L D - S E C T I O N ............................................................................
//		private ImageView nodeIcon = null;
//		private ImageView botrightDecorator = null;
		private TextView solarSystemName = null;
		private TextView planetType = null;
		private TextView installationsNumber = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public ColonyRender (final ColonyPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public ColonyPart getPart () {
			return (ColonyPart) super.getPart();
		}

		@Override
		public void initializeViews () {
			super.initializeViews();
//			this.nodeIcon = (ImageView) this._convertView.findViewById(R.id.nodeIcon);
//			this.botrightDecorator = (ImageView) this._convertView.findViewById(R.id.botrightDecorator);
			this.solarSystemName = (TextView) this._convertView.findViewById(R.id.solarSystemName);
			this.planetType = (TextView) this._convertView.findViewById(R.id.planetType);
			this.installationsNumber = (TextView) this._convertView.findViewById(R.id.installationsNumber);
		}

		@Override
		public void updateContent () {
			super.updateContent();
			Chrono chrono = new Chrono();
			this.solarSystemName.setText(getPart().getTSolarSystemName());
			this.planetType.setText(getPart().getTPlanetType());
			this.installationsNumber.setText(getPart().getTInstallations());
			nodeIcon.setImageResource(R.drawable.planets);
//			if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP ) {
//				nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.planets, getContext().getTheme()));
//			} else nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.planets));
			botrightDecorator.setVisibility(View.VISIBLE);
			switch (getPart().getCastedModel().getUpgradeLevel()) {
				case 1:
					botrightDecorator.setImageResource(R.drawable.level1white);
					break;
				case 2:
					botrightDecorator.setImageResource(R.drawable.level2white);
					break;
				case 3:
					botrightDecorator.setImageResource(R.drawable.level3white);
					break;
				case 4:
					botrightDecorator.setImageResource(R.drawable.level4white);
					break;
				case 5:
					botrightDecorator.setImageResource(R.drawable.level5white);
					break;
			}
			setPlanetIcon();

			// Hide the right arrow while the number on contents is zero.
			int modelContents = getPart().getCastedModel().getNumPins();
			if ( getPart().getCastedModel().isEmpty() ) _rightArrow.setVisibility(View.GONE);
			logger.debug("-- [ColonyRender.updateContent]> Rendering time: " + chrono.printElapsed(ChonoOptions.SHOWMILLIS));
		}

		protected void setPlanetIcon () {
			// Get the planet type name and set the icon depending on that value.
			final String planetTypeName = getPart().getCastedModel().getPlanetType();
			if ( planetTypeName.equalsIgnoreCase("barren") )
				nodeIcon.setImageResource(R.drawable.planet_barren_102_128_3);
			if ( planetTypeName.equalsIgnoreCase("lava") )
				nodeIcon.setImageResource(R.drawable.planet_lava_102_128_1);
			if ( planetTypeName.equalsIgnoreCase("plasma") )
				nodeIcon.setImageResource(R.drawable.planet_plasma_103_128_2);
			if ( planetTypeName.equalsIgnoreCase("temperate") )
				nodeIcon.setImageResource(R.drawable.planet_temperate_102_128_4);
			if ( planetTypeName.equalsIgnoreCase("ice") )
				nodeIcon.setImageResource(R.drawable.planet_ice_103_128_1);
			if ( planetTypeName.equalsIgnoreCase("oceanic") )
				nodeIcon.setImageResource(R.drawable.planet_oceanic_104_128_2);
			if ( planetTypeName.equalsIgnoreCase("storm") )
				nodeIcon.setImageResource(R.drawable.planet_storm_102_128_2);
			if ( planetTypeName.equalsIgnoreCase("gas") )
				nodeIcon.setImageResource(R.drawable.planet_gas_103_128_3);
		}

		@Override
		protected void createView () {
			_convertView = inflateView(R.layout.colony4list);
			_convertView.setTag(this);
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 8910031270706432316L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ColonyPart (final Colony node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Colony getCastedModel () {
		return (Colony) this.getModel();
	}

	public long getModelID () {
		return getCastedModel().getPlanetId();
	}

	public String getName () {
		return getCastedModel().getSolarSystemName();
	}

	public Spanned getTSolarSystemName () {
		double security = this.getCastedModel().getSecurityValue();
		security = 1.0;
		return Html.fromHtml(this.generateSecurityColor(security, this.getCastedModel().getSolarSystemName()
				+ AppWideConstants.FLOW_ARROW_RIGHT + this.getCastedModel().getPlanetName()));
	}

	public String getTInstallations () {
		return Long.valueOf(getCastedModel().getNumPins()).toString();
	}

	public String getTPlanetType () {
		return upperCaseFirst(getCastedModel().getPlanetType().toLowerCase());
	}

	public boolean ifModelEmpty () {
		return getCastedModel().isEmpty();
		//		return true;
	}
//[01]
	/**
	 * The render selected will depend on the Structure type stored on the Model.
	 */
	protected AbstractRender selectRenderer () {
		return new ColonyRender(this, _activity);
	}
//[02]
	public void onClick (final View view) {
		logger.info(">> [ColonyPart.onClick]");
		// Do the standard expand/collapse and the fire another message for the update of the model.
		super.onClick(view);
		GlobalAndroidDataManager.submitOnClick(
				new OneShotTask<NeoComExpandablePart>(this) {
					public void run () {
						logger.info(">> [ColonyPart.onClick.OneShotTask.run]");
						getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name(), this, this);
						logger.info("<< [ColonyPart.onClick.OneShotTask.run]");
					}
				});
		logger.info("<< [ColonyPart.onClick]");
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("ColonyPart [");
		//		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(getCastedModel().toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
//	@Override
//	public void propertyChange (final PropertyChangeEvent evt) {
//		final ColonyPart thePart=this;
//		getActivity().runOnUiThread(new Runnable() {
//			@Override
//			public void run () {
//				try {
//					if ( evt.getPropertyName().equalsIgnoreCase(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name()) ) {
//						//						getActivity().runOnUiThread(new Runnable() {
//						//							@Override
//						//							public void run () {
//						invalidate();
//						notifyDataSetChanged();
//						//							}
//						//						});
//						ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(
//								new OneShotTask<ColonyPart>(thePart) {
//									public void run () {
//										logger.info(">> [Location4AssetsPart.onClick.OneShotTask.run]");
//										getTarget().invalidate();
//										getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name(), this, this);
//										getTarget().notifyDataSetChanged();
//										logger.info("<< [Location4AssetsPart.onClick.OneShotTask.run]");
//									}
//								}, CoreConstants.HUNDRETH);
//					}
//				} catch (RuntimeException rtex) {
//					rtex.printStackTrace();
//				}
//			}
//		});
//	}

//[02]
//	/**
//	 * During the expansion check if we have the structures for the colony already available at the node. If not
//	 * launch hte download of that data while we show the spinner.
//	 * @return
//	 */
//	public boolean toggleExpanded () {
//		getCastedModel().downloadColonyStructures();
//	}


//	@Override
//	public void onClick (final View view) {
//		logger.info(">> [Location4AssetsPart.onClick]");
//		// Do the standard expand/collapse and the fire another message for the update of the model.
//		onParentClick(view);
//		ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(
//				new OneShotTask<ColonyPart>(this) {
//					public void run () {
//						logger.info(">> [Location4AssetsPart.onClick.OneShotTask.run]");
//						getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_DOWNLOADDATA.name(), this, this);
//						logger.info("<< [Location4AssetsPart.onClick.OneShotTask.run]");
//					}
//				}, CoreConstants.HUNDRETH);
//		logger.info("<< [Location4AssetsPart.onClick]");
//	}
//
//	public void onParentClick (final View view) {
//		logger.info(">> [NeoComExpandablePart.onClick]");
//		// Signal the action may take some time and launch it on the background.
//		activateClick();
//		// Force the update of the listview.
//		invalidate();
//		notifyDataSetChanged();
//		// Launch the actions on a new thread.
//		ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(
//				new OneShotTask<ColonyPart>(this) {
//					public void run () {
//						logger.info(">> [NeoComExpandablePart.onClick.OneShotTask.run]");
//						Chrono chrono = new Chrono();
//						getTarget().toggleExpanded();
//						//						getActivity().runOnUiThread(new Runnable() {
//						//							@Override
//						//							public void run () {
//						getTarget().invalidate();
//						getTarget().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
//						//							}
//						//						});
//						completeClick();
//						logger.info("<< [NeoComExpandablePart.onClick.OneShotTask.run]> Time Elapsed: " + chrono.printElapsed(Chrono.ChonoOptions.SHOWMILLIS));
//					}
//				}, CoreConstants.HUNDRETH);
//		logger.info("<< [NeoComExpandablePart.onClick]");
//	}
