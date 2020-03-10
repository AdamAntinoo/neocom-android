package org.dimensinfin.eveonline.neocom.app.persisters;

import java.sql.SQLException;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

public class CredentialPersistent {
	private CredentialRepository credentialRepository;

	public void persist( final Credential credential ) throws SQLException {
		this.credentialRepository.persist( credential );
	}

	// - B U I L D E R
	public static class Builder {
		private CredentialPersistent onConstruction;

		public Builder() {
			this.onConstruction = new CredentialPersistent();
		}

		public Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
			Objects.requireNonNull( credentialRepository );
			this.onConstruction.credentialRepository = credentialRepository;
			return this;
		}

		public CredentialPersistent build() {
			Objects.requireNonNull( this.onConstruction.credentialRepository );
			return this.onConstruction;
		}
	}
}
