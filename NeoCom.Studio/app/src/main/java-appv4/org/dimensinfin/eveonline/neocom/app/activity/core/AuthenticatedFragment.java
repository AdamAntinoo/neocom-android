package org.dimensinfin.eveonline.neocom.app.activity.core;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public abstract class AuthenticatedFragment extends AMVCPagerFragment {
	private Credential credential;

	public Credential getCredential() {
		return credential;
	}

	public AuthenticatedFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}
}
