package org.dimensinfin.eveonline.neocom.appvisualtest.app.credential;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.app.persisters.CredentialPersistent;
import org.dimensinfin.eveonline.neocom.core.EEvents;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

import java.beans.PropertyChangeEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VTCredentialsDataSource extends MVCDataSource {
	private ESIDataAdapter esiDataAdapter;
	private CredentialRepository credentialRepository;
	private List<Credential> testCredentials = new ArrayList<>();

	@Override
	public void prepareModel() {
		final Credential newCredential = new Credential.Builder(92223647)
				                                 .withAccountName("New Credential Instance")
				                                 .build();
		final Credential validCredential = new Credential.Builder(92223647)
				                                   .withAccountName("Complete Instance")
				                                   .build();
		validCredential.setAssetsCount(3241);
		validCredential.setWalletBalance(3876234098.0);
		validCredential.setRaceName("CALDARI");

		final Credential credentialAdam = new Credential.Builder(92002067)
				                                  .withAccountId(92002067)
				                                  .withAccountName("Adam Antinoo")
				                                  .withAccessToken("lfS7LIBbjLKnglJsujkNERgbwgOE0dCDiudhCdyrBxbxRp1xtFYzTRMxY2G2EssiS44UvvdMfRrXiLtn0SW9Zw")
				                                  .withRefreshToken("oCHpz8dm7MJNZ6PYqRvpWU6IkaD_Z5PsNx9SkI54UkvBY92yIUqEpIiFv03nxnLLnx-w_uTBBmsITYxM7WqzjUio4pXTJJN-GUGb-YNBfe0YNia_fl-NUNlmIGCwIMQCQhLpDZEUmECKUt7Do4T9ZW7FimJrhJUyw5xumUPN-d64oeY7Nd-UO4mc-By8i3aQ")
				                                  .withDataSource("tranquility")
				                                  .withScope("publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1")
				                                  .build();

		this.testCredentials.add(newCredential);
		this.testCredentials.add(validCredential);
		this.testCredentials.add(credentialAdam);
	}

	@Override
	public void collaborate2Model() {
		for (Credential cred : testCredentials) {
			this.addModelContents(cred);
		}
	}

	/**
	 * Detect events from the controllers and if the affect the Credentials (because they have updaters) then try to persist the
	 * new instance values on the repository.
	 *
	 * @param event the event to be processed. Event have a property name that is used as a selector.
	 */
	@Override
	public synchronized void propertyChange( final PropertyChangeEvent event ) {
		if (event.getPropertyName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
			final Object target = event.getNewValue();
			if ((null != target) && (target instanceof CredentialController)) { // This only applies to Credentials
				try {
					new CredentialPersistent.Builder()
							.withCredentialRepository(this.credentialRepository)
							.build()
							.persist(((CredentialController) target).getModel());
				} catch (SQLException sqle) {
					logger.info("EX [VTCredentialsDataSource.propertyChange]> Exception persisting Credential update: {}", sqle.getMessage());
				}
			}
			return;
		}
		super.propertyChange(event);
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.BaseBuilder<VTCredentialsDataSource, VTCredentialsDataSource.Builder> {
		private VTCredentialsDataSource onConstruction;

		@Override
		protected VTCredentialsDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VTCredentialsDataSource();
			return this.onConstruction;
		}

		@Override
		protected VTCredentialsDataSource.Builder getActualBuilder() {
			return this;
		}

		public VTCredentialsDataSource.Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			Objects.requireNonNull(esiDataAdapter);
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}

		public VTCredentialsDataSource.Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
			Objects.requireNonNull(credentialRepository);
			this.onConstruction.credentialRepository = credentialRepository;
			return this;
		}
	}
}
