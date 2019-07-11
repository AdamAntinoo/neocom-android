package org.dimensinfin.eveonline.neocom.app.controller;

import java.beans.PropertyChangeListener;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.adapters.NeoComUpdaterFactory;
import org.dimensinfin.eveonline.neocom.core.IEventReceiver;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;

public abstract class NeoComUpdatableController<M extends ICollaboration> extends NeoComController<M> implements IEventReceiver {
	protected NeoComUpdater<M> updater;

	public NeoComUpdatableController( @NonNull final M model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	public NeoComUpdater<M> getUpdater() {
		if (null == this.updater) {
			this.updater = NeoComUpdaterFactory.buildUpdater(this.getModel()); // Create the updater for this model.
			this.updater.addEventListener(this);
		}
		return this.updater;
	}

	/**
	 * Connect the updater just after the Controller is linked to the DataSource.
	 *
	 * @param listener the new listener to connect to this instance messages.
	 */
	@Override
	public void addPropertyChangeListener( final PropertyChangeListener listener ) {
		super.addPropertyChangeListener(listener);
//		this.updater = NeoComUpdaterFactory.buildUpdater(this.getModel()); // Create the updater for this model.
//		this.updater.addEventListener(this);
		this.getUpdater().refresh(); // Start the refresh process if required before leaving this thread.
	}
}
