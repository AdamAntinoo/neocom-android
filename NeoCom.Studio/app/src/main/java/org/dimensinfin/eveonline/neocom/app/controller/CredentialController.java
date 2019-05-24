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
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.pilot.PilotV3;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.sharedcode.EExtras;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroid;

import com.squareup.picasso.Picasso;

public class CredentialController extends AAndroidController<Credential> implements PropertyChangeListener, View.OnClickListener {
	private Context context;

	// - E X T E N D E D   M O D E L
	private PilotV3 pilot;

	// - C O N S T R U C T O R S
	public CredentialController( @NonNull final Credential model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		// Controller creation is the place where to request extended model data.
		this.pilot = new PilotV3.Builder(model, NeoComAndroid.getSingleton().getDownloaderService())
				             .build();
		this.pilot.addPropertyChangeListener(this);
	}

	//	protected CredentialController( @NonNull final ControllerAdapter<Credential> delegate, @NonNull final IControllerFactory factory ) {
	//		super(delegate, factory);
	//	}

	// - O N C L I C K L I S T E N E R   I N T E R F A C E

	/**
	 * If the Pilot is active the click has to change the current Activity to the Pilot Dashboard Activity. If the Pilot is not active
	 * the
	 * event should be discarded.
	 * If this part is used on more places we should control the variant so the click can be active for ones and not active for others.
	 */
	public void onClick( final View view ) {
		logger.info(">> [CredentialController.onClick]");
		final Intent intent = new Intent(this.context, PilotDashboardActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

	private String getPilotRaceName() {
		final String raceName = this.accessPilot().getRaceName();
		if (null == raceName) return "-";
		else return raceName.toUpperCase();
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

	private PilotV3 accessPilot() {
		Objects.requireNonNull(this.pilot);
		return this.pilot;
	}

	// - P R O P E R T Y C H A N G E L I S T E N E R
	@Override
	public void propertyChange( final PropertyChangeEvent evt ) {
		if (evt.getPropertyName().equalsIgnoreCase(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name())) {
			this.notifyDataModelChange();
		}
	}

	// - C R E D E N T I A L B U I L D E R
	public static class CredentialRender extends NeoComRenderv2 {
		// - U I   F I E L D S
		private TextView pilotName = null;
		private TextView pilotIdentifier = null;
		private TextView accountBalance = null;
		private TextView assetsCount = null;
		private TextView race = null;
		private TextView cloneType = null;
		private TextView expirationTime = null;
		private TextView nodeClass = null;

		// - L A Y O U T   F I E L D S
		private ViewGroup nodeIconBlock = null;
		private ImageView targetIcon = null;
		//		private ImageView rightArrow = null;

		// - C O N S T R U C T O R S
		public CredentialRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public CredentialController getController() {
			return (CredentialController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public void initializeViews() {
			super.initializeViews();
			// Layout fields
			nodeIconBlock = this.getView().findViewById(R.id.nodeIconBlock);
			targetIcon = this.getView().findViewById(R.id.targetIcon);
			//			rightArrow = this.getView().findViewById(R.id.rightArrow);

			// Content  fields.
			pilotName = this.getView().findViewById(R.id.pilotName);
			pilotIdentifier = this.getView().findViewById(R.id.pilotIdentifier);
			accountBalance = this.getView().findViewById(R.id.accountBalance);
			assetsCount = this.getView().findViewById(R.id.assetsCount);
			race = this.getView().findViewById(R.id.race);
			cloneType = this.getView().findViewById(R.id.cloneType);
			expirationTime = this.getView().findViewById(R.id.expirationTime);
			nodeClass = this.getView().findViewById(R.id.nodeClass);
		}

		@Override
		public void updateContent() {
			super.updateContent();
//			logger.info(">> [CredentialRender.updateContent]");
			if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP)
				this.getView().setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderrose, getContext().getTheme()));
			else this.getView().setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderrose));
			nodeClass.setText("CREDENTIAL");

			// Set content fields values.
			pilotName.setText(this.getController().getPilotName());
			pilotIdentifier.setText(this.getController().getPilotIdentifier());
			accountBalance.setText(this.accessAccountBalance());
			assetsCount.setText(this.accessAssetsCount()); // Set a default value while we get the real from the database.
			cloneType.setText(this.getController().getPilotRaceName());
			// Obtain the values to be set on the fiels. This second task should be done on the UI thread.
			//			final String walletAmount = getPart().getTWalletAmount();
			//			final String assetcount = getPart().getTAssetsCount();
			//			final String racename = getPart().getTRaceName();
			//			accountBalance.setText(walletAmount);
			//			assetsCount.setText(assetcount);
			//			race.setText(racename);
			final String link = this.getController().getURLForPilotAvatar();
			if (null != link) {
				if (null != targetIcon) {
					Picasso.get().load(link).into(targetIcon);
				}
			}
			// TODO - Review the access to other elements.
			//				getContext().runOnUiThread(() -> {
			//							try {
			//								accountBalance.setText(walletAmount);
			//								assetsCount.setText(assetcount);
			//								race.setText(racename);
			//								if ( null != link ) {
			//									if ( null != targetIcon ) {
			//										final Drawable draw = NeoComApp.getSingletonApp().getCacheConnector().getCacheDrawable(link, targetIcon);
			//										targetIcon.setImageDrawable(draw);
			//									}
			//								}
			//							} catch (RuntimeException rtex) {
			//								rtex.printStackTrace();
			//							}
			//						}
			//				);
			//			} catch (RuntimeException rtex) {
			//				rtex.printStackTrace();
			//			}
			//		});

			// Set the icon decorators deènding on the Credential type.
			// TODO - Activate when the new interface is valid.
			//			botrightDecorator.setVisibility(View.INVISIBLE);
			//			botleftDecorator.setVisibility(View.INVISIBLE);
			//			if ( getPart().getCastedModel().isESICompatible() ) {
			//				botrightDecorator.setImageResource(R.drawable.hamburger_icon_white);
			//				botrightDecorator.setVisibility(View.VISIBLE);
			//			}
			//			if ( getPart().getCastedModel().isXMLCompatible() ) {
			//				botleftDecorator.setImageResource(R.drawable.xml_icon);
			//				botleftDecorator.setVisibility(View.VISIBLE);
			//			}

			//		logger.info("<< [CredentialRender.updateContent]");
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.credential4list;
		}

		private String accessAccountBalance() {
			final double balance = this.getController().getPilotAccountBalance();
			if (balance < 0.0) return "- ISK"; // Adapt response while real data is accessed.
			else return this.generatePriceString(balance);
		}

		private String accessAssetsCount() {
			final long count = this.getController().getPilotAssetsCount();
			if (count < 0) return "-"; // This is the response while accessing the number of assets from backend.
			else return this.qtyFormatter.format(count);
		}

//		private String accessRace() {
//			final String raceName = this.getController().getModel().getRaceName();
//			if (null == raceName) return "-";
//			else return raceName.toUpperCase();
//		}
	}
}
