//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.WorkerThread;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.evanova.adapter.ApplicationCloudAndroidAdapter;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PilotDetailActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.ModelFactory;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.PilotV1;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotInfoPart extends NeoComAbstractPart implements INamedPart, OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -1731066477259354660L;
	private static Logger logger = Logger.getLogger("PilotInfoPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotInfoPart (final PilotV1 pilot) {
		super(pilot);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public PilotV1 getCastedModel () {
		return (PilotV1) this.getModel();
	}

	@Override
	public long getModelID () {
		return 0;
	}

	public String getName () {
		return this.getCastedModel().getName();
	}

	/**
	 * Return the formatted string for the character wallet amount. This is a complex task because it requires to
	 * get access to some character data that may have not been already downloaded. First it has to be performed
	 * on a download task and second we have to get the Pilot information that matches this Character using
	 * the Credential as the index.
	 *
	 * @return Formetted string for the ISK amount of the Character wallet.
	 */
	@WorkerThread
	public String getTWalletAmount () {
		// Get access to the matching character using the credential as index.
		final PilotV1 pilot = ModelFactory.getPilotV1(getCastedModel().getCharacterId());
		if ( null != pilot ) return generatePriceString(pilot.getAccountBalance(), false, true);
		else return "- ISK";
	}

	/**
	 * Instantiates an AssetsManaget for the current Character associated to the Credential to be able to query
	 * the database and get the real value.
	 */
	@WorkerThread
	public String getTAssetsCount () {
		// Get access to the AssetsManager for the current character to get this value
		final Credential credential = NeoComModelStore.getCredential4Id(getCastedModel().getCharacterId());
		final AssetsManager manager = GlobalDataManager.getAssetsManager(credential);
		final long totalAssets = manager.getTotalAssetsNumber();
		return qtyFormatter.format(totalAssets);
	}

	@WorkerThread
	public String getTRaceName () {
		final PilotV1 pilot = ModelFactory.getPilotV1(getCastedModel().getCharacterId());
		if ( null != pilot ) return upperCaseFirst(pilot.getCharacterInfo().getRace().name().toLowerCase());
		else return "-";
	}

	@WorkerThread
	public String getURLForAvatar () {
		final PilotV1 pilot = ModelFactory.getPilotV1(getCastedModel().getCharacterId());
		if ( null != pilot ) return pilot.getURLForAvatar();
		else return null;
	}

	public Spanned getTLastKnownLocation(){
		final EveLocation loc = getCastedModel().getLastKnownLocation();
		double security = Double.valueOf(loc.getSecurity()).doubleValue();
		String securityFormatted = securityFormatter.format(security);
		return Html.fromHtml("["+this.generateSecurityColor(security,securityFormatted)+"] "+loc.getRegion()+AppWideConstants.FLOW_ARROW_RIGHT+loc.getSystem());
	}
	public String getTCharacterID () {
		return "[" + Long.valueOf(getCastedModel().getCharacterId()).toString() + "]";
	}

	/**
	 * If the Pilot is active the click has to show the Pilot Dashboard Activity. If the Pilot is not active the
	 * event should be discarded.<br>
	 * If this part is used on more places we should control the variant so the click can be active for ones and
	 * not active for others.
	 */
	public void onClick (final View view) {
		PilotInfoPart.logger.info(">> [PilotInfoPart.onClick]");
		// Set the pilot selected on the context and then go to the Pilot Dashboard.
		final Object pilotPart = view.getTag();
		if ( pilotPart instanceof PilotInfoPart ) {
			final Intent intent = new Intent(this.getActivity(), PilotDetailActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), getCastedModel().getCharacterId());
			getActivity().startActivity(intent);
		}
		PilotInfoPart.logger.info("<< [PilotInfoPart.onClick]");
	}

	@Override
	protected AbstractRender selectRenderer () {
		// Get the proper holder set for the render mode.
		if ( this.getRenderMode() == ENeoComVariants.CAPSULEER_LIST.name() )
			return new Pilot4ListRender(this, _activity);
		if ( this.getRenderMode() == ENeoComVariants.PILOT_HEADER.name() )
			return new Pilot4HeaderRender(this, _activity);
		else
			return new Pilot4ListRender(this, _activity);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class Pilot4ListRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger	logger									= Logger.getLogger("PilotInfoHolder");

	// - F I E L D - S E C T I O N ............................................................................
	// - L A Y O U T   F I E L D S
	private ImageView nodeIcon = null;
	private TextView pilotName = null;
	private TextView lastKnownLocation = null;
	private TextView accountBalance = null;
	private TextView assetsCount = null;

	// - L A Y O U T   L A B E L S
	private TextView lastKnownLocationLabel = null;
	private TextView accountBalanceLabel = null;
	private TextView assetsCountLabel = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Pilot4ListRender (final AbstractAndroidPart newPart, final Activity context) {
		super(newPart, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public PilotInfoPart getPart () {
		return (PilotInfoPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		pilotName = (TextView) _convertView.findViewById(R.id.pilotName);
		lastKnownLocation = (TextView) _convertView.findViewById(R.id.lastKnownLocation);
		accountBalance = (TextView) _convertView.findViewById(R.id.accountBalance);
		assetsCount = (TextView) _convertView.findViewById(R.id.assetsCount);

		lastKnownLocationLabel = (TextView) _convertView.findViewById(R.id.lastKnownLocationLabel);
		accountBalanceLabel = (TextView) _convertView.findViewById(R.id.accountBalanceLabel);
		assetsCountLabel = (TextView) _convertView.findViewById(R.id.assetsCountLabel);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		//		NeoComCharacter pilot = this.getPart().getCastedModel();
		pilotName.setText(getPart().getCastedModel().getName());
		//		lastKnownLocation.setText(">> " + pilot.getLastKnownLocation());

		accountBalance.setText("- ISK"); // This is the default value.
		assetsCount.setText("-"); // Set a default value while we get the real from the database.
		//		race.setText("-"); // Set a default value while we get an update from the Character.
		ApplicationCloudAndroidAdapter.submit2downloadExecutor(() -> {
			try {
				// Obtain the values to be set on the fileds. This second task should be done on the UI thread.
				final String walletAmount = getPart().getTWalletAmount();
				final String assetcount = getPart().getTAssetsCount();
				final String racename = getPart().getTRaceName();
				final String link = getPart().getURLForAvatar();
				getContext().runOnUiThread(() -> {
							try {
								accountBalance.setText(walletAmount);
								assetsCount.setText(assetcount);
								//								race.setText(racename);
								if ( null != link ) {
									if ( null != nodeIcon ) {
										final Drawable draw = NeoComApp.getSingletonApp().getCacheConnector().getCacheDrawable(link, nodeIcon);
										nodeIcon.setImageDrawable(draw);
									}
								}
							} catch (RuntimeException rtex) {
								rtex.printStackTrace();
							}
						}
				);
			} catch (RuntimeException rtex) {
				rtex.printStackTrace();
			}
		});
	}

	@SuppressLint("InflateParams")
	@Override
	protected void createView () {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
																													.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		_convertView = mInflater.inflate(R.layout.pilotinfo4login, null);
		_convertView.setTag(this);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class Pilot4HeaderRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - L A Y O U T   F I E L D S
	private ImageView pilotAvatar = null;
	private TextView pilotName = null;
	private TextView race = null;
	private TextView pilotIdentifier = null;
	private TextView lastKnownLocation = null;
	private TextView accountBalance = null;
	private TextView assetsCount = null;

	// - L A Y O U T   L A B E L S
	@SuppressWarnings("unused")
	private TextView lastKnownLocationLabel = null;
	@SuppressWarnings("unused")
	private TextView accountBalanceLabel = null;
	@SuppressWarnings("unused")
	private TextView assetsCountLabel = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Pilot4HeaderRender (final AbstractAndroidPart newPart, final Activity context) {
		super(newPart, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public PilotInfoPart getPart () {
		return (PilotInfoPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		pilotAvatar = (ImageView) _convertView.findViewById(R.id.pilotAvatar);
		pilotName = (TextView) _convertView.findViewById(R.id.pilotName);
		race = (TextView) _convertView.findViewById(R.id.race);
		pilotIdentifier = (TextView) _convertView.findViewById(R.id.characterId);
		lastKnownLocation = (TextView) _convertView.findViewById(R.id.lastKnownLocation);
		accountBalance = (TextView) _convertView.findViewById(R.id.accountBalance);
		assetsCount = (TextView) _convertView.findViewById(R.id.assetsCount);

		lastKnownLocationLabel = (TextView) _convertView.findViewById(R.id.lastKnownLocationLabel);
		accountBalanceLabel = (TextView) _convertView.findViewById(R.id.accountBalanceLabel);
		assetsCountLabel = (TextView) _convertView.findViewById(R.id.assetsCountLabel);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		//		NeoComCharacter pilot = this.getPart().getCastedModel();
		pilotName.setText(getPart().getCastedModel().getName());
		pilotIdentifier.setText(getPart().getTCharacterID());
		lastKnownLocation.setText(getPart().getTLastKnownLocation());

		accountBalance.setText("- ISK"); // This is the default value.
		assetsCount.setText("-"); // Set a default value while we get the real from the database.
		race.setText("-"); // Set a default value while we get an update from the Character.
		//		ApplicationCloudAndroidAdapter.submit2downloadExecutor(() -> {
		//			try {
		// Obtain the values to be set on the fields. This second task should be done on the UI thread.
		final String walletAmount = getPart().getTWalletAmount();
		final String assetcount = getPart().getTAssetsCount();
		final String racename = getPart().getTRaceName();
		final String link = getPart().getURLForAvatar();
		//				getContext().runOnUiThread(() -> {
		//							try {
		accountBalance.setText(walletAmount);
		assetsCount.setText(assetcount);
		race.setText(racename);
		if ( null != link ) {
			if ( null != pilotAvatar ) {
				final Drawable draw = NeoComApp.getSingletonApp().getCacheConnector().getCacheDrawable(link, pilotAvatar);
				pilotAvatar.setImageDrawable(draw);
			}
		}
		//							} catch (RuntimeException rtex) {
		//								rtex.printStackTrace();
		//							}
		//						}
		//				);
		//			} catch (RuntimeException rtex) {
		//				rtex.printStackTrace();
		//			}
		//		});
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.pilotinfo4header);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
