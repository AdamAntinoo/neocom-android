package org.dimensinfin.eveonline.neocom.app.persisters;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

class CredentialPersistentTest {
	@Test
	void buildComplete() {
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		final CredentialPersistent credentialPersistent = new CredentialPersistent.Builder()
				                                                  .withCredentialRepository( credentialRepository ).build();
		Assertions.assertNotNull( credentialPersistent );
	}

	@Test
	void buildFailure() {
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final CredentialPersistent credentialPersistent = new CredentialPersistent.Builder()
					                                                  .withCredentialRepository( null ).build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final CredentialPersistent credentialPersistent = new CredentialPersistent.Builder().build();
		} );
	}

	@Test
	void persist() throws SQLException {
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		final Credential credential=Mockito.mock(Credential.class);
		final CredentialPersistent credentialPersistent = new CredentialPersistent.Builder()
				                                                  .withCredentialRepository( credentialRepository ).build();
		credentialPersistent.persist( credential );
		Mockito.verify( credentialRepository, Mockito.times( 1 ) ).persist( Mockito.any( Credential.class ) );
	}
}
