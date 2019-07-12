package org.dimensinfin.eveonline.neocom.app.controller.core;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
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
	public AAndroidController setDataSource( final IDataSource dataSource ) {
		if (dataSource instanceof AuthenticatedDataSource)
			this.credential = ((AuthenticatedDataSource) dataSource).getCredential();
		return super.setDataSource(dataSource);
	}
}
