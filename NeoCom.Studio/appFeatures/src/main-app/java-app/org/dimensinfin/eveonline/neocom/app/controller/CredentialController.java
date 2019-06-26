package org.dimensinfin.eveonline.neocom.app.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.events.EEvents;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EActivities;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.pilot.PilotV3;
import org.dimensinfin.eveonline.neocom.app.render.CredentialRender;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.services.DataDownloaderService;

import com.squareup.picasso.Picasso;

public class CredentialController extends AAndroidController<Credential> implements PropertyChangeListener, View.OnClickListener {
	private Context context;
	private DataDownloaderService downloaderService;

	// - E X T E N D E D   M O D E L
	private PilotV3 pilot;

	// - C O N S T R U C T O R S
	protected CredentialController( @NonNull final Credential model, @NonNull final IControllerFactory factory, final DataDownloaderService downloaderService ) {
		super(model, factory);
		this.downloaderService = downloaderService;
		// Controller creation is the place where to request extended model data.
		this.pilot = new PilotV3.Builder(model, this.downloaderService).build();
		this.pilot.addPropertyChangeListener(this);
	}

	// - P R O P E R T Y C H A N G E L I S T E N E R
	@Override
	public void propertyChange( final PropertyChangeEvent evt ) {
		if (evt.getPropertyName().equalsIgnoreCase(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name())) {
			this.notifyDataModelChange();
		}
	}

	// - O N C L I C K L I S T E N E R   I N T E R F A C E

	/**
	 * If the Pilot is active the click has to change the current Activity to the Pilot Dashboard Activity. If the Pilot is not active
	 * the
	 * event should be discarded.
	 * If this part is used on more places we should control the variant so the click can be active for ones and not active for others.
	 */
	public void onClick( final View view ) {
		logger.info(">> [CredentialController.onClick]");
		final Intent intent = this.getControllerFactory().prepareActivity(EActivities.PILOT_DASHBOARD_ACTIVITY.name(), this.context);
		intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
				, this.getModel().getAccountId()); // Credential account identifier
		view.getContext().startActivity(intent);
		logger.info("<< [CredentialController.onClick]");
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		this.context = context;
		return new CredentialRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().getAccountId();
	}

	// - M O D E L   A D A P T E R   M E T H O D S
	public String getPilotName() {
		return this.getModel().getName();
	}

	public String getPilotIdentifier() {
		return Integer.valueOf(this.getModel().getAccountId()).toString();
	}

	public double getPilotAccountBalance() {
		return this.accessPilot().getAccountBalance();
	}

	public long getPilotAssetsCount() {
		return this.accessPilot().getTotalAssetsNumber();
	}

	public String getURLForPilotAvatar() {
		return this.accessPilot().getUrlforAvatar();
	}

	public PilotV3 accessPilot() {
		Objects.requireNonNull(this.pilot);
		return this.pilot;
	}

	// - B U I L D E R
	public static class Builder {
		private CredentialController onConstuction;

		public Builder( @NonNull final Credential model
				, @NonNull final IControllerFactory factory
				, final DataDownloaderService downloaderService ) {
			this.onConstuction = new CredentialController(model, factory, downloaderService);
		}

		//		public Builder withDownloaderService( final DataDownloaderService downloaderService ) {
		//			this.onConstuction.downloaderService = downloaderService;
		//			return this;
		//		}

		public CredentialController build() {
			Objects.requireNonNull(this.onConstuction.downloaderService);
			return this.onConstuction;
		}
	}
}
