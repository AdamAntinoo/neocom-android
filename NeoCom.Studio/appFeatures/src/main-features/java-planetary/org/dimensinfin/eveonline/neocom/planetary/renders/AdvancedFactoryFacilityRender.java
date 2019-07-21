package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.Schematics;
import org.dimensinfin.eveonline.neocom.planetary.controller.FactoryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;

import java.util.List;

public class AdvancedFactoryFacilityRender extends FactoryFacilityRender {
	private TextView facilityName;
	private FrameLayout input1ResourceBlock;
	private ImageView input1ResourceIcon;
	private TextView input1ResourceName;
	private TextView input1Quantity;
	private FrameLayout input2ResourceBlock;
	private ImageView input2ResourceIcon;
	private TextView input2ResourceName;
	private TextView input2Quantity;
	private ImageView outputResourceIcon;
	private TextView outputResourceName;
	private TextView outputQuantity;

	public AdvancedFactoryFacilityRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public FactoryFacilityController getController() {
		return (FactoryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.advancedfactoryfacility4detail;
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
		this.outputResourceIcon = this.getView().findViewById(R.id.outputResourceIcon);
		this.outputResourceName = this.getView().findViewById(R.id.outputResourceName);
		this.outputQuantity = this.getView().findViewById(R.id.outputQuantity);

		this.input1ResourceName.setVisibility(View.INVISIBLE);
		this.input1Quantity.setVisibility(View.INVISIBLE);
		this.input2ResourceName.setVisibility(View.INVISIBLE);
		this.input2Quantity.setVisibility(View.INVISIBLE);
		this.outputResourceName.setVisibility(View.INVISIBLE);
		this.outputQuantity.setVisibility(View.INVISIBLE);
	}

	/**
	 * We assume that if there is content then it should be more that ZERO and should be shown.
	 */
	@Override
	public void updateContent() {
		super.updateContent();
		this.facilityName.setText(this.getController().getModel().getName());
		final List<FactoryFacility.FactoryInput> contents =  this.getController().getModel().getInputs();
		// Output is known from schematics
		final Schematics output = this.getController().getModel().getOutput();
		if ( null != output){
			String link = this.getURLForItem(output.getTypeId());
			Picasso.get().load(link).into(this.outputResourceIcon);
			this.outputResourceName.setVisibility(View.VISIBLE);
			this.outputQuantity.setVisibility(View.VISIBLE);
			this.outputResourceName.setText(output.getName());
			this.outputQuantity.setText(qtyFormatter.format(output.getQuantity()));
		}
		if (contents.size() > 0) { // Fill the input number 1
			final FactoryFacility.FactoryInput input1 = contents.get(0);
			this.input1ResourceName.setVisibility(View.VISIBLE);
			this.input1Quantity.setVisibility(View.VISIBLE);
			String link = this.getURLForItem(input1.getTypeId());
			Picasso.get().load(link).into(this.input1ResourceIcon);
			this.input1ResourceName.setText(input1.getName());
			this.input1Quantity.setText(this.accessResourceQuantity(0));
			final ImageView inputResourceAvailable = this.generateInputResourceRing(input1);
			this.input1ResourceBlock.addView(inputResourceAvailable);
		}
		if (contents.size() > 1) { // Fill the input number 2
			final FactoryFacility.FactoryInput input2 = contents.get(0);
			this.input2ResourceName.setVisibility(View.VISIBLE);
			this.input2Quantity.setVisibility(View.VISIBLE);
			String link = this.getURLForItem(input2.getTypeId());
			Picasso.get().load(link).into(this.input2ResourceIcon);
			this.input2ResourceName.setText(input2.getName());
			this.input2Quantity.setText(this.accessResourceQuantity(0));
			final ImageView inputResourceAvailable = this.generateInputResourceRing(input2);
			this.input2ResourceBlock.addView(inputResourceAvailable);
		}
		if (contents.size() < 2) { // A resource is missing from input. Factory idle
			this.currentCycleLabel.setText(Html.fromHtml("<font color='#FF0000'>Waiting resources</font>"));
			this.currentCycleProgress.setProgress(0);
			final Integer cycle = ((FactoryFacility) this.getController().getModel()).getCycleTime();
			this.currentCycleTime.setText(this.format2hms(0) + " / " + this.format2hms(cycle));
		} else { // factory running
			this.currentCycleLabel.setText(Html.fromHtml("<font color='#186E00'>In production</font>"));
			this.currentCycleProgress.setProgress(this.accessCycleProgress());
			this.currentCycleTime.setText(this.accessCycleTime());
		}
	}
}
