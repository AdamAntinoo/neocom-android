package org.dimensinfin.eveonline.neocom.app.datasource;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public abstract class AuthenticatedDataSource extends AMVCDataSource {
	private Credential credential;

	public Credential getCredential() {
		return credential;
	}

	// - B U I L D E R
	public abstract static class Builder<T extends AMVCDataSource, B extends AMVCDataSource.BaseBuilder> extends AMVCDataSource.BaseBuilder<T, B> {
		private AuthenticatedDataSource onConstruction;

		public AuthenticatedDataSource.Builder withCredential( final Credential credential ) {
			this.onConstruction.credential = credential;
			return this;
		}
	}
}
