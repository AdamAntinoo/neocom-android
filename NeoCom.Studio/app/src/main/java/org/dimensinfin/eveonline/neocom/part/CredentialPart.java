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

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PilotDetailActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory.ComparatorType;
import org.dimensinfin.eveonline.neocom.model.Credential;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

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

	public String getURLForAvatar () {
		return "http://image.eveonline.com/character/" + getCastedModel().getAccountId() + "_256.jpg";
	}

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


	@Override
	protected AbstractRender selectRenderer () {
		return new CredentialRender(this, _activity);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class CredentialRender extends BaseExpandableRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S

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
		//		name = (TextView) _convertView.findViewById(R.id.name);
		//		childCount = (TextView) _convertView.findViewById(R.id.childCount);
		//
		//		titleLabel = (TextView) _convertView.findViewById(R.id.titleLabel);
		//		locationLabel = (TextView) _convertView.findViewById(R.id.locationLabel);
	}

	@Override
	public void updateContent () {
		logger.info(">> [CredentialRender.updateContent]");
		super.updateContent();
		_rightArrow.setVisibility(View.GONE);
		if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP )
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nopilot, getContext().getTheme()));
		else nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nopilot));
		if ( null != nodeIcon ) {
			String link = getPart().getURLForAvatar();
			final Drawable draw = NeoComApp.getSingletonApp().getCacheConnector().getCacheDrawable(link, nodeIcon);
			nodeIcon.setImageDrawable(draw);
		}
		name.setText(this.getPart().getName());
		//		childCount.setText(this.getPart().getTContentCount());

		// Set this variant labels
		classLabel.setText("CREDENTIAL");
		categoryLabel.setText("CHARACTERS");
		categoryLabel.setVisibility(View.GONE);
		childCount.setVisibility(View.GONE);
		if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP )
			_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred, getContext().getTheme()));
		else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred));
		logger.info("<< [CredentialRender.updateContent]");
	}
}

// - UNUSED CODE ............................................................................................
