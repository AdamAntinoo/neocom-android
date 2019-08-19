package org.dimensinfin.eveonline.neocom.app.controller.core;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IEventReceiver;
import org.dimensinfin.eveonline.neocom.adapters.NeoComUpdaterFactory;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;

public abstract class NeoComUpdatableController<M extends ICollaboration> extends NeoComController<M> implements IEventReceiver {
	protected NeoComUpdater<M> updater;

	public NeoComUpdatableController( @NonNull final M model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	protected NeoComUpdater<M> getUpdater() {
		if (null == this.updater) {
			this.updater = NeoComUpdaterFactory.buildUpdater(this.getModel()); // Create the updater for this model.
			if (null != this.updater) this.updater.addEventListener( this);
		}
		return this.updater;
	}

	/**
	 * Connect the updater just after the Controller is linked to the DataSource.
	 *
	 * @param listener the new listener to connect to this instance messages.
	 */
	@Override
	public void addEventListener( final IEventReceiver listener ) {
		super.addEventListener(listener);
		if (null != this.getUpdater())
			this.getUpdater().refresh(); // Start the refresh process if required before leaving this thread.
	}
}
