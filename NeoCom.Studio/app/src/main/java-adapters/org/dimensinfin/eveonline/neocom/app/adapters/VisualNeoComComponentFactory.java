package org.dimensinfin.eveonline.neocom.app.adapters;

import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

public class VisualNeoComComponentFactory extends NeoComComponentFactory {

	public void replaceCredentialRepository( final CredentialRepository newCredentialRepository ) {
		this.credentialRepository = newCredentialRepository;
	}
}