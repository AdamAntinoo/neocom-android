package org.dimensinfin.eveonline.neocom.app.ui;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;

import java.util.ArrayList;
import java.util.Objects;

public class LinearContainer {
	private Activity activity;
	private LinearLayout initializationLayout;
	private ControllerFactory factory;
	private ArrayList<NeoComNode> contents = new ArrayList<>();

	// - C O N S T R U C T O R S
	private LinearContainer() { }

	public void addModel( final NeoComNode model ) {
		this.contents.add(model);
		this.initializationLayout.addView(this.createView(model));
	}

	public void clear() {
		this.initializationLayout.removeAllViews();
//		this.contents.clear();
	}

	public int size() {
		return this.contents.size();
	}

	public void needsUpdate() {
		this.clear();
		for (NeoComNode node : this.contents)
			this.initializationLayout.addView(this.createView(node));
		this.initializationLayout.invalidate();
	}

	private View createView( final NeoComNode model ) {
		final IRender render = this.factory.createController(model)
				                           .buildRender(this.activity.getApplicationContext());
		final View view = render.getView();
		render.updateContent();
		return view;
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

		public LinearContainer.Builder withFactory( final ControllerFactory factory ) {
			Objects.requireNonNull(factory);
			this.onConstruction.factory = factory;
			return this;
		}

		public LinearContainer build() {
			Objects.requireNonNull(this.onConstruction.activity);
			Objects.requireNonNull(this.onConstruction.initializationLayout);
			Objects.requireNonNull(this.onConstruction.factory);
			return this.onConstruction;
		}
	}
}