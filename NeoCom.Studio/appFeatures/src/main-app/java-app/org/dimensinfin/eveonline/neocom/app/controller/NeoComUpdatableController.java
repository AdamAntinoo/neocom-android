package org.dimensinfin.eveonline.neocom.app.controller;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.core.IEventReceiver;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;

public abstract class NeoComUpdatableController<M extends ICollaboration> extends NeoComController<M> implements IEventReceiver {
	protected NeoComUpdater<M> updater;

	public NeoComUpdatableController( @NonNull final M model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.updater = ((INeoComControllerFactory) this.getControllerFactory()).buildUpdater(model); // Create the updater for this model.
		this.updater.addEventListener(this);
		this.updater.refresh(); // Start the refresh process if required before leaving this thread.
	}

	public NeoComUpdater<M> getUpdater() {
		return this.updater;
	}
}
