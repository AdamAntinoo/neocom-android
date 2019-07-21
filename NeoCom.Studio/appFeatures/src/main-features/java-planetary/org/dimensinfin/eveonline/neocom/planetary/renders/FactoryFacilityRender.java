package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.ui.ProgressRingImageView;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.List;

public abstract class FactoryFacilityRender extends ResourceFacetedRender {
	protected TextView currentCycleLabel;
	protected ProgressBar currentCycleProgress;
	protected TextView currentCycleTime;

	public FactoryFacilityRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.currentCycleLabel = this.getView().findViewById(R.id.currentCycleLabel);
		this.currentCycleProgress = this.getView().findViewById(R.id.currentCycleProgress);
		this.currentCycleTime = this.getView().findViewById(R.id.currentCycleTime);
	}

	protected int accessCycleProgress() {
		final Integer cycle = ((FactoryFacility) this.getController().getModel()).getCycleTime();
		final DateTime lastCycleStart = ((FactoryFacility) this.getController().getModel()).getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return 100 * (elapsed.getSeconds() % cycle) / cycle;
	}

	protected String accessCycleTime() {
		final Integer cycle = ((FactoryFacility) this.getController().getModel()).getCycleTime();
		final DateTime lastCycleStart = ((FactoryFacility) this.getController().getModel()).getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return this.format2hms(elapsed.getSeconds() % cycle) + " / " + this.format2hms(cycle);
	}

	protected String accessResourceQuantity( final int contentIndex ) {
		final List<FactoryFacility.FactoryInput> contents = ((FactoryFacility) this.getController().getModel()).getInputs();
		final FactoryFacility.FactoryInput input1 = contents.get(contentIndex);
		final Long required = ((FactoryFacility) this.getController().getModel()).getFactoryType().getCapacity();
		return qtyFormatter.format(input1.getAmount()) + " / " + qtyFormatter.format(required);
	}

	protected ImageView generateInputResourceRing( final FactoryFacility.FactoryInput resource ) {
		return new ProgressRingImageView.Builder(this.getContext())
				.withUsage(resource.getAmount().floatValue())
				.withCapacity(((FactoryFacility) this.getController().getModel()).getFactoryType().getCapacity().floatValue())
				.withColour(R.color.appwhite)
				.build();
	}
}
