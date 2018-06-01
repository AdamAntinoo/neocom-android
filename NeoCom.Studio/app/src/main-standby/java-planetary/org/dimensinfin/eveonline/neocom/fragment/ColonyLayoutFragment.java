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
package org.dimensinfin.eveonline.neocom.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.ColonyStructure;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ColonyLayoutFragment extends Fragment implements IPartFactory {
	public enum EPlanetaryStructureType {
		DEFAULT, COMMAND_CENTER, EXTRACTOR, BASIC_INDUSTRY, ADVANCED_INDUSTRY, HIGH_TECH_PRODUCTION, STORAGE, LAUNCHPAD
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(ColonyLayoutFragment.class);

	// - F I E L D - S E C T I O N ............................................................................
	private ViewGroup _container = null;
	private ViewGroup canvas = null;
	private Instant startPoint=Instant.now();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ColonyLayoutFragment () {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		logger.info(">> [ColonyLayoutFragment.onCreateView]");
		super.onCreateView(inflater, container, savedInstanceState);
		try {
			_container = (ViewGroup) inflater.inflate(R.layout.fragment_colonylayout, container, false);
			canvas = (ViewGroup) _container.findViewById(R.id.emptyCanvas);
			// TODO Check that the canvat and the container are the same
			canvas.removeAllViews();
			canvas.setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick (final View v) {
							canvas.removeAllViews();
							generateStructures();
							_container.invalidate();
						}
					});
		} catch (final RuntimeException rtex) {
			logger.error("RTEX [ColonyLayoutFragment.onCreateViewSuper]> " + rtex.getMessage());
			rtex.printStackTrace();
		}
		logger.info("<< [ColonyLayoutFragment.onCreateView]");
		return _container;
	}
	public void onStart () {
		super.onStart();
		generateStructures();
	}

	private void generateStructures () {
		// Use the MVC header setup to load the structures into the frame.
		final List<ColonyStructure> model = createMockUpModel();
		for (ColonyStructure structure : model) {
			final IPart structurePart = createPart(structure);
			addViewtoHeader((IAndroidPart) structurePart);
		}
	}

	public IPart createPart (final ICollaboration node) {
		logger.info("-- [ColonyLayoutFragment.createPart]> Node class: " + node.getClass().getName());
		IPart part = null;
//		if ( node instanceof ColonyStructure ) {
//			if(((ColonyStructure) node).getStructureTypeCode()==EPlanetaryStructureType.BASIC_INDUSTRY)
//				part = new BasicStructurePart((Pin) node).setFactory(this);
//			if(((ColonyStructure) node).getStructureTypeCode()==EPlanetaryStructureType.STORAGE)
//				part = new BasicStructurePart((Pin) node).setFactory(this);
//		}
		return part;
	}

	@Override
	public String getVariant () {
		return "POC";
	}

	private List<ColonyStructure> createMockUpModel () {
//		// Command center location.
//		//		final double center_longitude = -4.74107389672607;
//		//		final double center_latitude = 0.808934460793102;
//		ColonyStructure pinStructure = null;
//		PinContent content = null;
		List<ColonyStructure> resultingModel = new Vector();
//
//		// Basic Industry Facility
//		pinStructure = new ColonyStructure();
//		pinStructure.setId(1024811525162L);
//		pinStructure.setTypeId(2473);
//		pinStructure.setLatitude(0.802738796915);
//		pinStructure.setLongitude(4.75563428247);
//		pinStructure.setSchematicsId(126);
//		pinStructure.setLastCycleStart(startPoint.getMillis());
//		content = new PinContent();
//		content.setTypeId(2267);
//		content.setAmount(1837);
//		pinStructure.addContent(content);
//		resultingModel.add(pinStructure);
//
//		// Basic Industry Facility
//		pinStructure = new ColonyStructure();
//		pinStructure.setId(1024811525164L);
//		pinStructure.setTypeId(2473);
//		pinStructure.setLatitude(0.81560809073);
//		pinStructure.setLongitude(4.755343046);
//		pinStructure.setSchematicsId(127);
//		pinStructure.setLastCycleStart(startPoint.getMillis());
//		content = new PinContent();
//		content.setTypeId(2270);
//		content.setAmount(3000);
//		pinStructure.addContent(content);
//		resultingModel.add(pinStructure);
//
//		// Basic Industry Facility
//		pinStructure = new ColonyStructure();
//		pinStructure.setId(1024814927793L);
//		pinStructure.setTypeId(2473);
//		pinStructure.setLatitude(0.815408799205);
//		pinStructure.setLongitude(4.7256722388);
//		pinStructure.setSchematicsId(127);
//		pinStructure.setLastCycleStart(startPoint.getMillis());
//		content = new PinContent();
//		content.setTypeId(2270);
//		content.setAmount(2000);
//		pinStructure.addContent(content);
//		resultingModel.add(pinStructure);
//
//		// Storage
//		pinStructure = new ColonyStructure();
//		pinStructure.setId(1024811499528L);
//		pinStructure.setTypeId(2541);
//		pinStructure.setLatitude(0.796796216569);
//		pinStructure.setLongitude(4.74084864085);
//		content = new PinContent();
//		content.setTypeId(2270);
//		content.setAmount(38546);
//		pinStructure.addContent(content);
//		content = new PinContent();
//		content.setTypeId(2399);
//		content.setAmount(5100);
//		pinStructure.addContent(content);
//		resultingModel.add(pinStructure);

		return resultingModel;
	}

	private void addViewtoHeader (final IAndroidPart target) {
		logger.info(">> [ColonyLayoutFragment.addViewtoHeader]");
		try {
			final AbstractRender holder = target.getRenderer(getActivity());
			holder.initializeViews();
			holder.updateContent();
			final View hv = holder.getView();
			canvas.addView(hv);
			// Add the connection to the click listener
			//			if ( target instanceof OnClickListener ) {
			//				hv.setClickable(true);
			//				hv.setOnClickListener((OnClickListener) target);
			//			}
			canvas.setVisibility(View.VISIBLE);
		} catch (final RuntimeException rtex) {
			logger.info("RTEX [ColonyLayoutFragment.addViewtoHeader]> Problem generating view for: " + target.toString());
			logger.info("RTEX [ColonyLayoutFragment.addViewtoHeader]> RuntimeException. " + rtex.getMessage());
			rtex.printStackTrace();
		}
		logger.info("<< ColonyLayoutFragment.addViewtoHeader");
	}
	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("ColonyLayoutFragment [");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
