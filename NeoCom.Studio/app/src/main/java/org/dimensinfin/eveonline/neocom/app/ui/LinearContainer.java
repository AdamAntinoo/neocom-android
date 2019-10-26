package org.dimensinfin.eveonline.neocom.app.ui;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Objects;

public class LinearContainer {
	private Activity activity;
	private LinearLayout initializationLayout;
	private ArrayList<View> contents = new ArrayList<>();

	// - C O N S T R U C T O R S
	private LinearContainer() {
	}

	public void clear() {
		this.initializationLayout.removeAllViews();
		this.contents.clear();
	}

	public int size() {
		return this.contents.size();
	}
	// - D E L E G A T E D

	public void addView( final View child ) {
		this.contents.add(child);
		this.initializationLayout.addView(child);
	}

	// - B U I L D E R
	public static class Builder {
		private LinearContainer onConstruction;

		public Builder() {
			this.onConstruction = new LinearContainer();
		}

		public LinearContainer.Builder withActivity( final Activity activity ) {
			Objects.requireNonNull(activity);
			this.onConstruction.activity = activity;
			return this;
		}

		public LinearContainer.Builder withLayoutId( final Integer layoutId ) {
			Objects.requireNonNull(layoutId);
			this.onConstruction.initializationLayout = this.onConstruction.activity.findViewById(layoutId);
			Objects.requireNonNull(this.onConstruction.initializationLayout);
			return this;
		}

		public LinearContainer build() {
			Objects.requireNonNull(this.onConstruction.activity);
			Objects.requireNonNull(this.onConstruction.initializationLayout);
			return this.onConstruction;
		}
	}
}