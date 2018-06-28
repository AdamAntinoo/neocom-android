//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.WorkerThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerExceptions;
import org.dimensinfin.eveonline.neocom.exception.NeoComRegisteredException;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

import java.beans.PropertyChangeEvent;

// - CLASS IMPLEMENTATION ...................................................................................
public class CredentialPart extends NeoComAbstractPart implements View.OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 2985342150508204740L;

	// - F I E L D - S E C T I O N ............................................................................
	private PilotV2 pilot = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public CredentialPart( final Credential node ) {
		super(node);

		// TODO - testing code to check if this can be equivalent to the runDependencies call.
		try {
			pilot = AndroidGlobalDataManager.fastRequestPilotV2(node);
		} catch (NeoComRegisteredException neore) {
			AndroidGlobalDataManager.registerException("CredentialPart.<constructor>"
					, neore, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			neore.printStackTrace();
		}
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Credential getCastedModel() {
		return (Credential) this.getModel();
	}

	public long getModelId() {
		return this.getCastedModel().getAccountId();
	}

	public String getName() {
		return this.getCastedModel().getAccountName();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new CredentialRender(this, _activity);
	}

	// --- C O N T E N T   M E T H O D S

	/**
	 * Return the formatted string for the character wallet amount. This is a complex task because it requires to
	 * get access to some character data that may have not been already downloaded. First it has to be performed
	 * on a download task and second we have to get the Pilot information that matches this Character using
	 * the Credential as the index.
	 * @return Formatted string for the ISK amount of the Character wallet.
	 */
	@WorkerThread
	public String getTWalletAmount() {
		// Get access to the matching character using the credential as index.
		//		 pilot = ModelFactory.getPilotV1(getCastedModel().getAccountId());
		if ( null != pilot ) return generatePriceString(pilot.getAccountBalance(), false, true);
		else return "- ISK";
	}

	/**
	 * Instantiates an AssetsManaget for the current Character associated to the Credential to be able to query
	 * the database and get the real value.
	 */
	@WorkerThread
	public String getTAssetsCount() {
		// Get access to the AssetsManager for the current character to get this value
		//		manager = ManagerStore.getAssetsManager(getCastedModel().getAccountId());
		if ( null != pilot ) {
			final long totalAssets = this.pilot.getTotalAssetsNumber();
			return qtyFormatter.format(totalAssets);
		} else return "-";
	}

	@WorkerThread
	public String getTRaceName() {
		//		pilot = ModelFactory.getPilotV1(getCastedModel().getAccountId());
		if ( null != pilot ) return upperCaseFirst(pilot.getRace().getName().toLowerCase());
		else return "-";
	}

	@WorkerThread
	public String getURLForAvatar() {
		//		pilot = ModelFactory.getPilotV1(getCastedModel().getAccountId());
		if ( null != pilot ) return pilot.getUrlforAvatar();
		else return "http://image.eveonline.com/character/1_256.jpg";
	}

	//	/**
	//	 * Sort the Credentials by their names.
	//	 */
	//	@Override
	//	public List<IPart> runPolicies( final List<IPart> targets ) {
	//		logger.info(">> [CredentialPart.runPolicies]");
	//		// Order the characters by alphabetical name.
	//		Collections.sort(targets, PartComparatorFactory.createPartComparator(ComparatorType.COMPARATOR_NAME));
	//		logger.info("<< [CredentialPart.runPolicies]");
	//		return targets;
	//	}
	//	@Override
	public boolean runDependencies() {
		logger.info(">> [CredentialPart.runDependencies]");
		try {
			// Make access to the rendering dependencies during this preparation phase.
			pilot = AndroidGlobalDataManager.fastRequestPilotV2(getCastedModel());
		} catch (NeoComRegisteredException neore) {
			AndroidGlobalDataManager.registerException("CredentialPart.runDependencies", neore
					, GlobalDataManagerExceptions.EExceptionSeverity.SEVERE);
			neore.printStackTrace();
		} finally {
			logger.info("<< [CredentialPart.runDependencies]");
			if ( null == pilot ) return false;
			else return true;
		}
	}

	// --- O N C L I C K L I S T E N E R   I N T E R F A C E

	/**
	 * If the Pilot is active the click has to change the current Activity to the Pilot Dashboard Activity. If the Pilot is not active
	 * the
	 * event should be discarded.
	 * If this part is used on more places we should control the variant so the click can be active for ones and not active for others.
	 */
	public void onClick( final View view ) {
		logger.info(">> [CredentialPart.onClick]");
		// Set the pilot selected on the context and then go to the Pilot Dashboard.
		final Object pilotPart = view.getTag();
		if ( pilotPart instanceof CredentialPart ) {
			// With the new implementation get just the identifier for the Credential.
			final int identifier = ((CredentialPart) pilotPart).getCastedModel().getAccountId();
			//					NeoComModelStore.activateCredential(identifier);
			final Intent intent = new Intent(this.getActivity(), PilotDashboardActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
			intent.putExtra(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
			getActivity().startActivity(intent);
		}
		logger.info("<< [CredentialPart.onClick]");
	}

	public void propertyChange( final PropertyChangeEvent evt ) {
		if ( evt.getPropertyName().equalsIgnoreCase(SystemWideConstants.events.EVENTSTRUCTURE_NEWDATA.name()) ) {
			fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_NEWDATA.name(), this, this);
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("CredentialPart [");
		buffer.append(this.getCastedModel().getName());
		buffer.append(" ]");
		return buffer.toString();
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class CredentialRender extends AbstractRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
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
		//		private ImageView botrightDecorator = null;
		//		private ImageView botleftDecorator = null;
		//		private ViewGroup rightBlock = null;
		//		private ImageView separator = null;
		//		private ImageView menuIcon = null;
		private ImageView rightArrow = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public CredentialRender( final CredentialPart target, final Activity context ) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public CredentialPart getPart() {
			return (CredentialPart) super.getPart();
		}

		// --- I R E N D E R   I N T E R F A C E
		@Override
		public void initializeViews() {
			//			super.initializeViews();
			// Layout decorator items.
			nodeIconBlock = (ViewGroup) _convertView.findViewById(R.id.nodeIconBlock);
			targetIcon = (ImageView) _convertView.findViewById(R.id.targetIcon);
			//			botrightDecorator = (ImageView) _convertView.findViewById(R.id.botrightDecorator);
			//			botleftDecorator = (ImageView) _convertView.findViewById(R.id.botleftDecorator);
			//			rightBlock = (ViewGroup) _convertView.findViewById(R.id.rightBlock);
			//			separator = (ImageView) _convertView.findViewById(R.id.separator);
			//			menuIcon = (ImageView) _convertView.findViewById(R.id.menuIcon);
			rightArrow = (ImageView) _convertView.findViewById(R.id.rightArrow);
			nodeClass = (TextView) _convertView.findViewById(R.id.nodeClass);

			// Content layout fields.
			pilotName = (TextView) _convertView.findViewById(R.id.pilotName);
			pilotIdentifier = (TextView) _convertView.findViewById(R.id.pilotIdentifier);
			accountBalance = (TextView) _convertView.findViewById(R.id.accountBalance);
			assetsCount = (TextView) _convertView.findViewById(R.id.assetsCount);
			race = (TextView) _convertView.findViewById(R.id.race);
			cloneType = (TextView) _convertView.findViewById(R.id.cloneType);
			expirationTime = (TextView) _convertView.findViewById(R.id.expirationTime);
		}

		@Override
		public void updateContent() {
			//		logger.info(">> [CredentialRender.updateContent]");
			//			super.updateContent();
			// Setup the initial layout configuration. No expandable and no menu.
			//			menuIcon.setVisibility(View.GONE);
			//			rightArrow.setVisibility(View.GONE);
			//			if ( null != _rightArrow ) _rightArrow.setVisibility(View.GONE);
			//			if ( null != separator ) separator.setVisibility(View.VISIBLE);
			if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP )
				_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderrose, getContext().getTheme()));
			else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderrose));
			nodeClass.setText("CREDENTIAL");

			// Set content fields values.
			pilotName.setText(this.getPart().getName());
			pilotIdentifier.setText(Integer.valueOf(this.getPart().getCastedModel().getAccountId()).toString());
			accountBalance.setText("- ISK"); // This is the default value.
			assetsCount.setText("-"); // Set a default value while we get the real from the database.
			race.setText("-"); // Set a default value while we get an update from the Character.
			//		ApplicationCloudAndroidAdapter.submit2downloadExecutor(() -> {
			//			try {
			// Obtain the values to be set on the fileds. This second task should be done on the UI thread.
			final String walletAmount = getPart().getTWalletAmount();
			final String assetcount = getPart().getTAssetsCount();
			final String racename = getPart().getTRaceName();
			accountBalance.setText(walletAmount);
			assetsCount.setText(assetcount);
			race.setText(racename);
			final String link = getPart().getURLForAvatar();
			if ( null != link ) {
				if ( null != targetIcon ) {
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

		protected void loadEveIcon( final ImageView targetIcon, final int typeId ) {
			this.loadEveIcon(targetIcon, typeId, false);
		}

		/**
		 * Downloads and caches the item icon from the CCP server. The new implementation check for special cases
		 * such as locations. Stations on locations have an image that can be downloaded from the same place.
		 */
		protected void loadEveIcon( final ImageView targetIcon, final int typeId, final boolean station ) {
			// Check if the layout has the icon placeholder.
			if ( null != targetIcon ) {
				// If the flag signals an station change the code.
				String link = AndroidGlobalDataManager.getURLForItem(typeId);
				if ( station ) {
					link = AndroidGlobalDataManager.getURLForStation(typeId);
				}
				Picasso.get().load(link).into(targetIcon);
				//			try {
				//			final Future<Drawable> draw = AndroidGlobalDataManager.getCacheDrawable(link, targetIcon, getContext());
				//				targetIcon.setImageDrawable(draw.get());
				//			} catch (InterruptedException e) {
				//				e.printStackTrace();
				//			} catch (ExecutionException e) {
				//				e.printStackTrace();
				//			}
			}
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.credential4list;
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
