package org.dimensinfin.eveonline.neocom.mining.activity;

import java.text.MessageFormat;

import android.os.Bundle;
import androidx.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.dimensinfin.eveonline.neocom.sharedcode.EExtras;
import org.dimensinfin.eveonline.neocom.sharedcode.RuntimeErrorInfo;
import org.dimensinfin.eveonline.neocom.sharedcode.activity.NeoComActionBarActivity;

public abstract class ACredentialActivity extends NeoComActionBarActivity {
	public enum EMiningVariants {
		MINING_LEDGER, MINING_EXTRACTIONS_TODAY
	}

	/** When the user selects a Credential we pass to the target Activity the pilot identifier. */
	//	protected int pilotIdentifier = 0;
	/** Recovered credential extracted from the current list of Credentials using the pilot identifier as a key. */
	protected Credential credential = null;
	/** Reference to the Pilot associated to the Credential. This fields should be valid to be exported to DS and Parts. */
	protected PilotV2 pilot = null;

	protected Credential searchCredential( final int identifier ) {
		for (Credential cred : AndroidGlobalDataManager.accessAllCredentials()) {
			if (cred.getAccountId() == identifier) return cred;
		}
		return null;
	}

	// - A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [ACredentialActivity.onCreate]");
		super.onCreate(savedInstanceState);
		//		try {
		// Get the pilot identifier. This is a mandatory parameter and should be validated.
		if (null == extras) this.raiseException(RuntimeErrorInfo.CREDENTIAL_NOT_FOUND
				, "ACredentialActivity.onCreate");
		final int pilotIdentifier = extras.getInt(EExtras.CAPSULEER_IDENTIFIER.name(), 0);
		if (pilotIdentifier > 0) this.validateCredential(pilotIdentifier);
		//			// Validate the pointer Credential that should be made available to this Activity fragments.
		//			this.credential = searchCredential(pilotIdentifier);
		//			if (null == this.credential)
		//				this.raiseException(RuntimeErrorInfo.CREDENTIAL_NOT_FOUND
		//						, "ACredentialActivity.onCreate"
		//						, Integer.valueOf(pilotIdentifier).toString());
		//			// TODO - Define the action bar for the new set of MVC 4 activities.
		//			//					this.setupActionBar(credential); // Configure the new custom Action Bar
		//		}

		//		} catch (RuntimeException rtex) {
		//			AndroidGlobalDataManager.registerRuntimeException("APublicCredentialActivity.onCreate"
		//					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
		//			rtex.printStackTrace();
		//			this.addPage(new ApplicationExceptionReportFragment());
		//		}
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
		final String message = MessageFormat.format(error.getErrorMessage(), arguments);
		throw new NeoComRuntimeException(message);
	}

	protected void validateCredential( final int pilotIdentifier ) {
		this.credential = searchCredential(pilotIdentifier);
		if (null == this.credential)
			this.raiseException(RuntimeErrorInfo.CREDENTIAL_NOT_FOUND
					, "ACredentialActivity.onCreate"
					, Integer.valueOf(pilotIdentifier).toString());
		// TODO - Define the action bar for the new set of MVC 4 activities.
		//					this.setupActionBar(credential); // Configure the new custom Action Bar
	}
}
