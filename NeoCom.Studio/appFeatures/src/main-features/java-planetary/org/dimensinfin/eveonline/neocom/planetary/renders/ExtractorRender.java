package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.squareup.picasso.Picasso;

public class ExtractorRender extends ResourceFacetedRender {
	private ImageView extractingResourceIcon;
	private TextView facilityName;
	private TextView extractingResourceName;
	private ProgressBar currentCycleProgress;
	private TextView currentCycleTime;

	public ExtractorRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.extractor4colony;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.extractingResourceIcon = this.getView().findViewById(R.id.extractingResourceIcon);
		this.facilityName = this.getView().findViewById(R.id.facilityName);
		this.extractingResourceName = this.getView().findViewById(R.id.extractingResourceName);
		this.currentCycleProgress = this.getView().findViewById(R.id.currentCycleProgress);
		this.currentCycleTime = this.getView().findViewById(R.id.currentCycleTime);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		String link = ((ExtractorFacility) this.getController().getModel()).getExtractingResourceURLForItem();
		Picasso.get().load(link).into(this.extractingResourceIcon);
		this.facilityName.setText(this.getController().getModel().getName());
		this.extractingResourceName.setText(((ExtractorFacility) this.getController().getModel()).getExtractingResourceName());
		this.currentCycleProgress.setProgress(this.accessCycleProgress());
		this.currentCycleTime.setText(this.accessCycleTime());
	}

	private int accessCycleProgress() {
		final Integer cycle = this.getController().getModel().getExtractorDetails().getCycleTime();
		final DateTime lastCycleStart = this.getController().getModel().getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return 100 * (elapsed.getSeconds() % cycle) / cycle;
	}

	private String accessCycleTime() {
		final Integer cycle = this.getController().getModel().getExtractorDetails().getCycleTime();
		final DateTime lastCycleStart = this.getController().getModel().getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		final DateTimeFormatter formatter = DateTimeFormat.forPattern("HH.mm:ss");
		return formatter.print((elapsed.getSeconds() % cycle) * 1000) + " / " + formatter.print(cycle * 1000);
	}
}
