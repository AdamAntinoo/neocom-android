///	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.evanova.adapter.ApplicationCloudAdapter;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PilotDetailActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory.ComparatorType;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.Credential;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class CredentialPart extends NeoComExpandablePart implements INamedPart, View.OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -7696249781992547826L;
	private static DecimalFormat itemCountFormatter = new DecimalFormat("###,##0");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public CredentialPart (final Credential node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Credential getCastedModel () {
		return (Credential) this.getModel();
	}

	public long getModelID () {
		return this.getCastedModel().getAccountName().hashCode();
	}

	public String getName () {
		return this.getCastedModel().getAccountName();
	}

	public String getTContentCount () {
		return "-";
	}

	//	public String getURLForAvatar () {
	//		return "http://image.eveonline.com/character/" + getCastedModel().getAccountId() + "_256.jpg";
	//	}

	/**
	 * Sort the Regions by their names.
	 */
	@Override
	public List<IPart> runPolicies (final List<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, PartComparatorFactory.createPartComparator(ComparatorType.COMPARATOR_NAME));
		return targets;
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("CredentialPart [");
		buffer.append(this.getCastedModel().getName());
		buffer.append(" ]");
		return buffer.toString();
	}

	/**
	 * If the Pilot is active the click has to show the Pilot Dashboard Activity. If the Pilot is not active the
	 * event should be discarded.<br>
	 * If this part is used on more places we should control the variant so the click can be active for ones and
	 * not active for others.
	 */
	public void onClick (final View view) {
		logger.info(">> [CredentialPart.onClick]");
		// Set the pilot selected on the context and then go to the Pilot Dashboard.
		final Object pilotPart = view.getTag();
		if ( pilotPart instanceof CredentialPart ) {
			// With the new implementation get just the identifier for the Credential.
			final long identifier = ((CredentialPart) pilotPart).getCastedModel().getAccountId();
			NeoComModelStore.getSingleton().activateCredential(identifier);
			// TODO This is to keep compatibility with the old data management.
			// Pilot are expected to be at the global context
			//			final NeoComCharacter pilot = ((PilotInfoPart) pilotPart).getCastedModel();
			// TODO This should be done at the destination Activity
			// NeoComAppConnector.getSingleton.getModelStore().activatePilot(pilot.getCharacterID());
			//			final Intent intent = new Intent(this.getActivity(), DirectorsBoardActivity.class);
			final Intent intent = new Intent(this.getActivity(), PilotDetailActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot
			//			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), identifier);
			getActivity().startActivity(intent);
		}
		logger.info("<< [CredentialPart.onClick]");
	}
	public void propertyChange (final PropertyChangeEvent evt) {
		if(evt.getPropertyName().equalsIgnoreCase(SystemWideConstants.events.EVENTSTRUCTURE_NEWDATA.name())){
			fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_NEWDATA.name(), this, this);
		}
	}

	@Override
	protected AbstractRender selectRenderer () {
		return new CredentialRender(this, _activity);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class CredentialRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private TextView pilotName = null;
	private TextView accountBalance = null;
	private TextView assetsCount = null;
	private TextView race = null;
	private TextView cloneType = null;
	private TextView expirationTime = null;

	// - L A Y O U T   F I E L D S
	private ViewGroup nodeIconBlock = null;
	private ImageView targetIcon = null;
	private ImageView esiDecorator = null;
	private ImageView xmlApiDecorator = null;
	private ViewGroup rightBlock = null;
	private ImageView separator = null;
	private ImageView menuIcon = null;
	private ImageView rightArrow = null;
	private TextView nodeClass = null;


	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public CredentialRender (final CredentialPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public CredentialPart getPart () {
		return (CredentialPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		// Layout decorator items.
		nodeIconBlock = (ViewGroup) _convertView.findViewById(R.id.nodeIconBlock);
		targetIcon = (ImageView) _convertView.findViewById(R.id.targetIcon);
		esiDecorator = (ImageView) _convertView.findViewById(R.id.esiDecorator);
		xmlApiDecorator = (ImageView) _convertView.findViewById(R.id.xmlApiDecorator);
		rightBlock = (ViewGroup) _convertView.findViewById(R.id.rightBlock);
		separator = (ImageView) _convertView.findViewById(R.id.separator);
		menuIcon = (ImageView) _convertView.findViewById(R.id.menuIcon);
		rightArrow = (ImageView) _convertView.findViewById(R.id.rightArrow);
		nodeClass = (TextView) _convertView.findViewById(R.id.nodeClass);

		// Content layout fields.
		pilotName = (TextView) _convertView.findViewById(R.id.pilotName);
		accountBalance = (TextView) _convertView.findViewById(R.id.accountBalance);
		assetsCount = (TextView) _convertView.findViewById(R.id.assetsCount);
		race = (TextView) _convertView.findViewById(R.id.race);
		cloneType = (TextView) _convertView.findViewById(R.id.cloneType);
		expirationTime = (TextView) _convertView.findViewById(R.id.expirationTime);
	}

	@Override
	public void updateContent () {
		logger.info(">> [CredentialRender.updateContent]");
		super.updateContent();
		// Setup the initial layout configuration. No expandable and no menu.
		menuIcon.setVisibility(View.GONE);
		rightArrow.setVisibility(View.GONE);
		_rightArrow.setVisibility(View.GONE);
		separator.setVisibility(View.VISIBLE);
		if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP )
			_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred, getContext().getTheme()));
		else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred));
		nodeClass.setText("CREDENTIAL");

		// Set content fields values.
		pilotName.setText(this.getPart().getName());
		if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP )
			targetIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nopilot, getContext().getTheme()));
		else targetIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nopilot));
		if ( null != targetIcon ) {
			String link = getPart().getCastedModel().getURLForAvatar();
			final Drawable draw = NeoComApp.getSingletonApp().getCacheConnector().getCacheDrawable(link, targetIcon);
			targetIcon.setImageDrawable(draw);
		}
				accountBalance.setText(this.getPart().getCastedModel().getLocation().getFullLocation());
		ApplicationCloudAdapter.submit2uiExecutor(
				new Runnable() {
					@Override
					public void run () {
						// Get the number of assets on the AssetsManager.
						final AssetsManager manager = new AssetsManager(getPart().getCastedModel());
						final long totalAssets = manager.getTotalAssetsNumber();
						getContext().runOnUiThread(
								new Runnable() {
									@Override
									public void run () {
										assetsCount.setText(qtyFormatter.format(totalAssets));
									}
								});
					}
				});
		logger.info("<< [CredentialRender.updateContent]");
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.pilot4credential);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
