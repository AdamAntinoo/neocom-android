package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.ui.ProgressRingImageView;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.List;

public class BasicIndustryRender extends ResourceFacetedRender {
	private TextView facilityName;
	private FrameLayout input1ResourceBlock;
	private ImageView input1ResourceIcon;
	private TextView input1ResourceName;
	private TextView input1Quantity;
	private TextView currentCycleLabel;
	private ProgressBar currentCycleProgress;
	private TextView currentCycleTime;

	public BasicIndustryRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.basicindustryfacility4detail;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.facilityName = this.getView().findViewById(R.id.facilityName);
		this.input1ResourceBlock = this.getView().findViewById(R.id.input1ResourceBlock);
		this.input1ResourceIcon = this.getView().findViewById(R.id.input1ResourceIcon);
		this.input1ResourceName = this.getView().findViewById(R.id.input1ResourceName);
		this.input1Quantity = this.getView().findViewById(R.id.input1Quantity);
		this.currentCycleLabel = this.getView().findViewById(R.id.currentCycleLabel);
		this.currentCycleProgress = this.getView().findViewById(R.id.currentCycleProgress);
		this.currentCycleTime = this.getView().findViewById(R.id.currentCycleTime);
	}

	/**
	 * There is not any field to show when a factory is running or not. There are three cases:
	 * <ul>
	 * <li>The facility is empty and then idle.</li>
	 * <li>The facility has some of the resources to run but not all and the previous cycle has completed. This
	 * is the difficult to detect case.</li>
	 * <li>The facility has some or all the resources and it is already running</li>
	 * </ul>
	 * The difficult case can only be detected if there are no more resources of the required type on the colony so the
	 * factory is awaiting for the rest of the quantity to be allowed to start a new cycle.
	 */
	@Override
	public void updateContent() {
		super.updateContent();
		this.facilityName.setText(this.getController().getModel().getName());
		final List<FactoryFacility.FactoryInputs> contents = ((FactoryFacility) this.getController().getModel()).getInputs();
		if (contents.size() > 0) { // Fill the input number 1
			final FactoryFacility.FactoryInputs input1 = contents.get(0);
			if (input1.getAmount() < 1)  // The facility is empty
				this.cleanFacility();
			else {
				String link = this.getURLForItem(input1.getTypeId());
				Picasso.get().load(link).into(this.input1ResourceIcon);
				this.input1ResourceName.setText(input1.getName());
				this.input1Quantity.setText(this.accessResourceQuantity(0));
				final ImageView inputResourceAvailable = this.generateInputResourceRing(input1);
				this.input1ResourceBlock.addView((ImageView) inputResourceAvailable);
				this.currentCycleLabel.setText(Html.fromHtml("<font color='#186E00'>In production</font>"));
				this.currentCycleProgress.setProgress(this.accessCycleProgress());
				this.currentCycleTime.setText(this.accessCycleTime());
			}
		} else this.cleanFacility();
	}

	private void cleanFacility() {
		this.input1ResourceName.setVisibility(View.INVISIBLE);
		this.input1Quantity.setVisibility(View.INVISIBLE);
		this.currentCycleLabel.setText(Html.fromHtml("<font color='#FF0000'>Waiting resources</font>"));
		this.currentCycleProgress.setProgress(0);
		final Integer cycle = ((FactoryFacility) this.getController().getModel()).getCycleTime();
		this.currentCycleTime.setText(this.format2hms(0) + " / " + this.format2hms(cycle));
	}

	private int accessCycleProgress() {
		final Integer cycle = ((FactoryFacility) this.getController().getModel()).getCycleTime();
		final DateTime lastCycleStart = this.getController().getModel().getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return 100 * (elapsed.getSeconds() % cycle) / cycle;
	}

	private String accessCycleTime() {
		final Integer cycle = ((FactoryFacility) this.getController().getModel()).getCycleTime();
		final DateTime lastCycleStart = this.getController().getModel().getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return this.format2hms(elapsed.getSeconds() % cycle) + " / " + this.format2hms(cycle);
	}

	private String accessResourceQuantity( final int contentIndex ) {
		final List<FactoryFacility.FactoryInputs> contents = ((FactoryFacility) this.getController().getModel()).getInputs();
		final FactoryFacility.FactoryInputs input1 = contents.get(contentIndex);
		final Long required = ((FactoryFacility) this.getController().getModel()).getFactoryType().getCapacity();
		return qtyFormatter.format(input1.getAmount()) + " / " + qtyFormatter.format(required);
	}

	private ImageView generateInputResourceRing( final FactoryFacility.FactoryInputs resource ) {
		return new ProgressRingImageView.Builder(this.getContext())
				.withUsage(resource.getAmount().floatValue())
				.withCapacity(((FactoryFacility) this.getController().getModel()).getFactoryType().getCapacity().floatValue())
				.withColour(R.color.appwhite)
				.build();
	}
}
