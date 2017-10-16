//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.part;

// - IMPORT SECTION .........................................................................................
import java.text.DecimalFormat;
import java.util.logging.Logger;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.PilotDetailActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.EveAbstractHolder;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.model.Pilot;
import org.dimensinfin.eveonline.neocom.refactoring.AExtendedPart;
import org.dimensinfin.eveonline.neocom.render.PilotInfoHolder;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotInfoPart extends AExtendedPart implements INamedPart, OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long	serialVersionUID	= -1731066477259354660L;
	private static Logger			logger						= Logger.getLogger("PilotInfoPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotInfoPart(final AbstractComplexNode pilot) {
		super(pilot);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	//	/**
	//	 * The result of this method depends on the variant use but this is not already supported. For the initial
	//	 * usage of this part at the Pilot List Activity we just expand to itself.
	//	 */
	//	@Override
	//	public ArrayList<IPart> collaborate2View() {
	//		ArrayList<IPart> result = new ArrayList<IPart>();
	//		result.add(this);
	//		return result;
	//	}

	@Override
	public Pilot getCastedModel() {
		return (Pilot) this.getModel();
	}

	@Override
	public long getModelID() {
		return 0;
	}

	public String getName() {
		return this.getCastedModel().getName();
	}

	public String getTransformedAssetsCount() {
		final DecimalFormat formatter = new DecimalFormat("###,### Items");
		final long assetCount = this.getCastedModel().getAssetCount();
		final String countString = formatter.format(assetCount);
		return countString;
	}

	public String getTransformedBalance() {
		final DecimalFormat formatter = new DecimalFormat("#,###.00 ISK");
		final String strbalance = formatter.format(this.getCastedModel().getAccountBalance());
		return strbalance;
	}

	/**
	 * If the Pilot is active the click has to show the Pilot Dashboard Activity. If the Pilot is not active the
	 * event should be discarded.<br>
	 * If this part is used on more places we should control the variant so the click can be active for ones and
	 * not active for others.
	 */
	public void onClick(final View view) {
		PilotInfoPart.logger.info(">> [PilotInfoPart.onClick]");
		// Set the pilot selected on the context and then go to the Pilot Dashboard.
		final Object pilotPart = view.getTag();
		if (pilotPart instanceof PilotInfoPart) {
			// TODO This is to keep compatibility with the old data management.
			// Pilot are expected to be at the global context
			final NeoComCharacter pilot = ((PilotInfoPart) pilotPart).getCastedModel();
			// TODO This should be done at the destination Activity
			// NeoComAppConnector.getSingleton.getModelStore().activatePilot(pilot.getCharacterID());
			//			final Intent intent = new Intent(this.getActivity(), DirectorsBoardActivity.class);
			final Intent intent = new Intent(this.getActivity(), PilotDetailActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot
			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), pilot.getCharacterID());
			NeoComModelStore.getSingleton().getActivity().startActivity(intent);
		}
		PilotInfoPart.logger.info("<< [PilotInfoPart.onClick]");
	}

	@Override
	protected AbstractRender selectRenderer() {
		// Get the proper holder set for the render mode.
		if (this.getRenderMode() == ENeoComVariants.CAPSULEER_LIST.hashCode())
			return new Pilot4ListHolder(this, _activity);
		else
			return new PilotInfoHolder(this, _activity);
		// If holder not located return a default view for a sample and modeless Part.
		//		return super.selectHolder();
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class Pilot4ListHolder extends EveAbstractHolder {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger	logger									= Logger.getLogger("PilotInfoHolder");

	// - F I E L D - S E C T I O N ............................................................................
	// - L A Y O U T   F I E L D S
	private ImageView	pilotAvatar					= null;
	private TextView	pilotName						= null;
	//	private TextView	lastKnownLocation				= null;
	private TextView	accountBalance			= null;
	private TextView	assetsCount					= null;

	// - L A Y O U T   L A B E L S
	//	@SuppressWarnings("unused")
	//	private TextView	lastKnownLocationLabel	= null;
	@SuppressWarnings("unused")
	private TextView	accountBalanceLabel	= null;
	@SuppressWarnings("unused")
	private TextView	assetsCountLabel		= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Pilot4ListHolder(final AbstractAndroidPart newPart, final Activity context) {
		super(newPart, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public PilotInfoPart getPart() {
		return (PilotInfoPart) super.getPart();
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		pilotAvatar = (ImageView) _convertView.findViewById(R.id.pilotAvatar);
		pilotName = (TextView) _convertView.findViewById(R.id.pilotName);
		//		lastKnownLocation = (TextView) _convertView.findViewById(R.id.lastKnownLocation);
		accountBalance = (TextView) _convertView.findViewById(R.id.accountBalance);
		assetsCount = (TextView) _convertView.findViewById(R.id.assetsCount);

		//		lastKnownLocationLabel = (TextView) _convertView.findViewById(R.id.lastKnownLocationLabel);
		accountBalanceLabel = (TextView) _convertView.findViewById(R.id.accountBalanceLabel);
		assetsCountLabel = (TextView) _convertView.findViewById(R.id.assetsCountLabel);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		Pilot pilot = this.getPart().getCastedModel();
		pilotName.setText(pilot.getName());
		//		lastKnownLocation.setText(">> " + pilot.getLastKnownLocation());
		accountBalance.setText(this.getPart().getTransformedBalance());
		assetsCount.setText(this.getPart().getTransformedAssetsCount());

		if (null != pilotAvatar) {
			String link = pilot.getURLForAvatar();
			final Drawable draw = NeoComApp.getSingletonApp().getCacheConnector().getCacheDrawable(link, pilotAvatar);
			pilotAvatar.setImageDrawable(draw);
		}
	}

	@SuppressLint("InflateParams")
	@Override
	protected void createView() {
		final LayoutInflater mInflater = (LayoutInflater) this.getContext()
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		_convertView = mInflater.inflate(R.layout.pilotinfo4login, null);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
