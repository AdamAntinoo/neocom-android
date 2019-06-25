package org.dimensinfin.eveonline.neocom.planetary.renders;

import java.util.List;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.FactoryType;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.ui.ProgressRingImageView;

import com.squareup.picasso.Picasso;

public class AdvancedIndustryRender extends ResourceFacetedRender {
	private TextView facilityName;
	private FrameLayout input1ResourceBlock;
	private ImageView input1ResourceIcon;
	private TextView input1ResourceName;
	private TextView input1Quantity;
	private FrameLayout input2ResourceBlock;
	private ImageView input2ResourceIcon;
	private TextView input2ResourceName;
	private TextView input2Quantity;

	public AdvancedIndustryRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.advancedindustryfacility4detail;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.facilityName = this.getView().findViewById(R.id.facilityName);
		this.input1ResourceBlock = this.getView().findViewById(R.id.input1ResourceBlock);
		this.input1ResourceIcon = this.getView().findViewById(R.id.input1ResourceIcon);
		this.input1ResourceName = this.getView().findViewById(R.id.input1ResourceName);
		this.input1Quantity = this.getView().findViewById(R.id.input1Quantity);
		this.input2ResourceBlock = this.getView().findViewById(R.id.input2ResourceBlock);
		this.input2ResourceIcon = this.getView().findViewById(R.id.input2ResourceIcon);
		this.input2ResourceName = this.getView().findViewById(R.id.input2ResourceName);
		this.input2Quantity = this.getView().findViewById(R.id.input2Quantity);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.facilityName.setText(this.getController().getModel().getName());
		final List<FactoryFacility.FactoryInputs> contents = ((FactoryFacility) this.getController().getModel()).getInputs();
		if (contents.size() > 0) { // Fill the input number 1
			final FactoryFacility.FactoryInputs input1 = contents.get(0);
			String link = this.getURLForItem(input1.getTypeId());
			Picasso.get().load(link).into(this.input1ResourceIcon);
			this.input1ResourceName.setText(input1.getName());
			this.input1Quantity.setText(this.accessResourceQuantity(0));
			final ImageView inputResourceAvailable = this.generateInputResourceRing(input1);
			this.input1ResourceBlock.addView((ImageView) inputResourceAvailable);
		}
		if (contents.size() > 1) { // Fill the input number 2
			final FactoryFacility.FactoryInputs input2 = contents.get(1);
			String link = this.getURLForItem(input2.getTypeId());
			Picasso.get().load(link).into(this.input2ResourceIcon);
			this.input2ResourceName.setText(input2.getName());
			this.input2Quantity.setText(this.accessResourceQuantity(1));
			final ImageView inputResourceAvailable = this.generateInputResourceRing(input2);
			this.input2ResourceBlock.addView((ImageView) inputResourceAvailable);
		}
	}

	protected String accessResourceQuantity( final int contentIndex ) {
		final List<FactoryFacility.FactoryInputs> contents = ((FactoryFacility) this.getController().getModel()).getInputs();
		final FactoryFacility.FactoryInputs input = contents.get(0);
		return input.getAmount() + " / " + FactoryType.ADVANCED_INDUSTRY.getCapacity().toString();
	}

	protected ImageView generateInputResourceRing( final FactoryFacility.FactoryInputs resource ) {
		return new ProgressRingImageView.Builder(this.getContext())
				       .withUsage(resource.getAmount().floatValue())
				       .withCapacity(FactoryType.ADVANCED_INDUSTRY.getCapacity().floatValue())
				       .withColour(R.color.appwhite)
				       .build();
	}
}
