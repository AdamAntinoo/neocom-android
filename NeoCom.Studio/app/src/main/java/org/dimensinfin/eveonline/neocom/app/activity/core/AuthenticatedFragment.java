package org.dimensinfin.eveonline.neocom.app.activity.core;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public abstract class AuthenticatedFragment extends NeoComFragment {
	private Credential credential;

	public Credential getCredential() {
		return credential;
	}

	public AuthenticatedFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}
}
