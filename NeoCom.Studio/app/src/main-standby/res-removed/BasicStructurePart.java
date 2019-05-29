package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.widget.FrameLayout;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.fragment.ColonyLayoutFragment.EPlanetaryStructureType;
import org.dimensinfin.eveonline.neocom.model.Pin;
import org.dimensinfin.eveonline.neocom.planetary.ColonyStructure;
import org.dimensinfin.eveonline.neocom.ui.FacilityImageView;
import org.dimensinfin.eveonline.neocom.ui.StorageImageView;

/**
 * Created by Adam on 06/01/2018.
 */

public class BasicStructurePart extends AbstractAndroidPart {
	private static final double center_latitude = 0.808934460793;
	private static final double center_longitude = 4.74107389673;

	public BasicStructurePart (final ICollaboration model) {
		super(model);
	}

	public double getCenterLongitude () {
		return center_longitude;
	}

	public double getCenterLatitude () {
		return center_latitude;
	}

	public ColonyStructure getCastedModel () {
		return (Pin) getModel();
	}

	@Override
	public long getModelID () {
		return getCastedModel().getId();
	}

	protected AbstractRender selectRenderer () {
		final EPlanetaryStructureType type = getCastedModel().getStructureTypeCode();
		if ( getCastedModel().getStructureTypeCode() == EPlanetaryStructureType.BASIC_INDUSTRY )
			return new BasicStructureRender(this, _activity);
		if ( getCastedModel().getStructureTypeCode() == EPlanetaryStructureType.STORAGE )
			return new StorageStructureRender(this, _activity);
		return new BasicStructureRender(this, _activity);
	}

	public AbstractRender getRenderer (Activity activity) {
		//		if(fragment instanceof PlanetaryColonyLayoutFragment ) {
		//		this._fragment = (Fragment)fragment;
		this._activity = activity;
		return this.selectRenderer();
		//		} else {
		//			throw new RuntimeException("Using on MVC fragments that are not compatible.");
		//		}
	}
}

final class BasicStructureRender extends AbstractRender {

	private FrameLayout layoutStructureContainer;
	private FacilityImageView structureImage;

	public BasicStructureRender (final BasicStructurePart target, final Activity context) {
		super(target, context);
	}

	public BasicStructurePart getPart () {
		return (BasicStructurePart) super.getPart();
	}

	/**
	 * Initialize the custom Image from the model values.
	 */
	public void initializeViews () {
		//		layoutStructureContainer = new FrameLayout(getContext());
		//		structureImage = new FacilityImageView(getContext());
	}

	@Override
	public void updateContent () {
		final double longitudeDelta = getPart().getCastedModel().getLongitude() - getPart().getCenterLongitude();
		final double latitudeDelta = getPart().getCastedModel().getLatitude() - getPart().getCenterLatitude();
		// The rest of the parameters should be hidden on the View itself.
		structureImage.setLocation(longitudeDelta, latitudeDelta);
		structureImage.setStructure(getPart().getCastedModel(), R.drawable.facility80_white);
		//		logger.debug("-- [ColonyRender.updateContent]> Rendering time: " + chrono.printElapsed(ChonoOptions.SHOWMILLIS));
	}

	@Override
	protected void createView () {
		layoutStructureContainer = new FrameLayout(getContext());
		structureImage = new FacilityImageView(getContext());
		layoutStructureContainer.addView(structureImage);
		_convertView = layoutStructureContainer;
		_convertView.setTag(this);
	}
}
final class StorageStructureRender extends AbstractRender {

	private FrameLayout layoutStructureContainer;
	private StorageImageView structureImage;

	public StorageStructureRender (final BasicStructurePart target, final Activity context) {
		super(target, context);
	}

	public BasicStructurePart getPart () {
		return (BasicStructurePart) super.getPart();
	}

	/**
	 * Initialize the custom Image from the model values.
	 */
	public void initializeViews () {
		//		layoutStructureContainer = new FrameLayout(getContext());
		//		structureImage = new FacilityImageView(getContext());
	}

	@Override
	public void updateContent () {
		final double longitudeDelta = getPart().getCastedModel().getLongitude() - getPart().getCenterLongitude();
		final double latitudeDelta = getPart().getCastedModel().getLatitude() - getPart().getCenterLatitude();
		// The rest of the parameters should be hidden on the View itself.
		structureImage.setLocation(longitudeDelta, latitudeDelta);
		structureImage.setStructure(getPart().getCastedModel(), R.drawable.storage80_white);
		//		logger.debug("-- [ColonyRender.updateContent]> Rendering time: " + chrono.printElapsed(ChonoOptions.SHOWMILLIS));
	}

	@Override
	protected void createView () {
		layoutStructureContainer = new FrameLayout(getContext());
		structureImage = new StorageImageView(getContext());
		layoutStructureContainer.addView(structureImage);
		_convertView = layoutStructureContainer;
		_convertView.setTag(this);
	}
}
