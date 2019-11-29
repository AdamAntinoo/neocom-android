package org.dimensinfin.eveonline.neocom.app.controller.core;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.core.domain.IntercommunicationEvent;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.datasource.AuthenticatedDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

import java.util.Objects;

public abstract class NeoComUpdatableAuthorizedController<M extends ICollaboration> extends NeoComUpdatableController<M> {
	private Credential credential;

	public NeoComUpdatableAuthorizedController( final M model, final IControllerFactory factory ) {
		super(model, factory);
	}

	protected Credential getCredential() {
		Objects.requireNonNull(this.credential);
		return this.credential;
	}

	@Override
	public AndroidController setDataSource( final IDataSource dataSource ) {
		if (dataSource instanceof AuthenticatedDataSource)
			this.credential = ((AuthenticatedDataSource) dataSource).getCredential();
		return super.setDataSource(dataSource);
	}

	// - I E V E N T R E C E I V E R
	@Override
	public void receiveEvent( final IntercommunicationEvent event ) {
		if (event.getPropertyName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
			this.sendChangeEvent(event);
			this.notifyDataModelChange();
		}
	}
}
