package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.exception.RuntimeErrorInfo;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;

import java.text.MessageFormat;
import java.util.Objects;

public abstract class ACredentialActivity extends ANeoComActivity {
	/** Recovered credential extracted from the current list of Credentials using the pilot identifier as a key. */
	protected Credential credential = null;

	// - C O M P O N E N T S
	private SharedPreferences preferences;
	private CredentialRepository credentialRepository;

	protected Credential searchCredential( final int identifier ) {
		final String esiServer = this.preferences.getString("prefkey_dataSourceList",
				NeoComComponentFactory.DEFAULT_ESI_SERVER).toLowerCase();
		for (Credential cred : this.credentialRepository.findAllByServer(esiServer)) {
			if (cred.getAccountId() == identifier) return cred;
		}
		return null;
	}

	public Credential getCredential() {
		Objects.requireNonNull(this.credential);
		return this.credential;
	}

	// - A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [ACredentialActivity.onCreate]");
		this.injectComponents(); // Inject components.
		super.onCreate(savedInstanceState);
		final int pilotIdentifier = this.extras.getInt(EExtras.CAPSULEER_IDENTIFIER.name(), 0);
		if (pilotIdentifier > 0) this.validateCredential(pilotIdentifier);
		logger.info("<< [ACredentialActivity.onCreate]");
	}

	@Override
	protected void onRestoreInstanceState( final Bundle savedInstanceState ) {
		final int pilotIdentifier = savedInstanceState.getInt(EExtras.CAPSULEER_IDENTIFIER.name());
		credential = searchCredential(pilotIdentifier);
		this.validateCredential(pilotIdentifier);
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of reactivation we can follow the data chain and
	 * reconstruct the AppModel state.
	 */
	@Override
	protected void onSaveInstanceState( final Bundle outState ) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putInt(EExtras.CAPSULEER_IDENTIFIER.name(), this.credential.getAccountId());
	}

	protected void raiseException( final RuntimeErrorInfo error, final String... arguments ) {
		// Prepare the message.
		final String message = MessageFormat.format(error.getErrorMessage(), (Object[]) arguments);
		throw new NeoComRuntimeException(message);
	}

	protected void validateCredential( final int pilotIdentifier ) {
		this.credential = this.searchCredential(pilotIdentifier);
		if (null == this.credential)
			this.raiseException(RuntimeErrorInfo.CREDENTIAL_NOT_FOUND
					, "ACredentialActivity.onCreate"
					, Integer.toString(pilotIdentifier));
	}

	protected void injectComponents() {
		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.credentialRepository = NeoComComponentFactory.getSingleton().getCredentialRepository();
	}
}
