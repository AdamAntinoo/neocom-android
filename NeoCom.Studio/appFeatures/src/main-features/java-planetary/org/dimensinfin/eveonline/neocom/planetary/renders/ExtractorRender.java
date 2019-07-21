package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.ui.HeadsCountImageView;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class ExtractorRender extends ResourceFacetedRender {
	private TextView facilityName;
	private ImageView extractingResourceIcon;
	private TextView extractorStatus;
	private TextView extractingResourceName;
	private ProgressBar currentCycleProgress;
	private TextView currentCycleTime;
	private TextView extractionCycleOutputValue;
	private TextView cycleExpirationTime;
	private ImageView headsCounter;

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
		this.facilityName = this.getView().findViewById(R.id.facilityName);
		this.extractingResourceIcon = this.getView().findViewById(R.id.extractingResourceIcon);
		this.extractorStatus = this.getView().findViewById(R.id.extractorStatus);
		this.extractingResourceName = this.getView().findViewById(R.id.extractingResourceName);
		this.currentCycleProgress = this.getView().findViewById(R.id.currentCycleProgress);
		this.currentCycleTime = this.getView().findViewById(R.id.currentCycleTime);
		this.extractionCycleOutputValue = this.getView().findViewById(R.id.extractionCycleOutputValue);
		this.cycleExpirationTime = this.getView().findViewById(R.id.cycleExpirationTime);
		this.headsCounter = this.getView().findViewById(R.id.headsCounter);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.facilityName.setText(this.getController().getModel().getName());
		final ExtractorFacility.ExtractorState state = ((ExtractorFacility) this.getController().getModel()).getState();
		if (state == ExtractorFacility.ExtractorState.EXTRACTING) {
			String link = ((ExtractorFacility) this.getController().getModel()).getExtractingResourceURLForItem();
			Picasso.get().load(link).into(this.extractingResourceIcon);
			this.extractingResourceName.setText(this.accessExtractingResourceName());
			this.currentCycleProgress.setProgress(this.accessCycleProgress());
			this.currentCycleTime.setText(this.accessCycleTime());
			this.extractionCycleOutputValue.setText(this.accessExtractionOutputValue());
			this.cycleExpirationTime.setText(this.accessCycleExpirationTime());
		} else { // The extractor is idling
			this.extractorStatus.setText(Html.fromHtml("<font color='#FF0000'>Extractor not running.</font>"));
			this.extractingResourceName.setVisibility(View.INVISIBLE);
			this.currentCycleProgress.setProgress(0);
			this.currentCycleTime.setText(this.accessCycleTimeWhenIdle());
			this.extractionCycleOutputValue.setVisibility(View.INVISIBLE);
			this.cycleExpirationTime.setVisibility(View.INVISIBLE);
		}
		final int headsCount = ((ExtractorFacility) this.getController().getModel()).getHeadsCount();
		((HeadsCountImageView) this.headsCounter).setHeadsCount(headsCount);
//		final ImageView headsCountView = new HeadsCountImageView.Builder(this.getContext())
//				.withHeadsCount(headsCount)
//				.build();
//		final Drawable headsDrawable = headsCountView.getDrawable();
//		bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//		Canvas canvas = new Canvas(bitmap);
//		d.draw(canvas);
//		this.headsCountImage.setImageBitmap(inputResourceAvailable.getIma);
	}

	private String accessCycleExpirationTime() {
		return "Extraction Ends: " + ((ExtractorFacility) this.getController().getModel()).getCycleExpirationTime();
	}

	private String accessExtractionOutputValue() {
		return "Next Output: " + this.qtyFormatter.format(((ExtractorFacility) this.getController().getModel()).getNextOutput());
	}

	private Spanned accessExtractingResourceName() {
		final String name = ((ExtractorFacility) this.getController().getModel()).getExtractingResourceName();
		final String resourceName = "<font color='#186E00'>".concat(name).concat("</font>");
		return Html.fromHtml(resourceName);
	}

	private int accessCycleProgress() {
		final Integer cycle = ((ExtractorFacility) this.getController().getModel()).getCycleTime();
		final DateTime lastCycleStart = this.getController().getModel().getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return 100 * (elapsed.getSeconds() % cycle) / cycle;
	}

	private String accessCycleTime() {
		final Integer cycle = ((ExtractorFacility) this.getController().getModel()).getCycleTime();
		final DateTime lastCycleStart = this.getController().getModel().getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return this.format2hms(elapsed.getSeconds() % cycle) + " / " + this.format2hms(cycle);
	}

	private String accessCycleTimeWhenIdle() {
		final Integer cycle = ((ExtractorFacility) this.getController().getModel()).getCycleTime();
		return this.format2hms(0) + " / " + this.format2hms(cycle);
	}
}
