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
import android.support.annotation.WorkerThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.AllianceV1;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.part.core.TypedPart;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotDashbordPart extends TypedPart<PilotV2> {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("PilotDashbordPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotDashbordPart( final PilotV2 model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return 0;
	}

	@Override
	public AbstractRender selectRenderer() {
		return new PilotDashbordRender4Header(this, this.getActivity());
	}

	@Override
	public PilotV2 getCastedModel() {
		return (PilotV2) this.getModel();
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N

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
		return NeoComAbstractPart.generatePriceString(this.getCastedModel().getAccountBalance(), false, true);
	}

	/**
	 * Instantiates an AssetsManaget for the current Character associated to the Credential to be able to query
	 * the database and get the real value.
	 */
	@WorkerThread
	public String getTAssetsCount() {
		final long totalAssets = this.getCastedModel().getTotalAssetsNumber();
		return NeoComAbstractPart.qtyFormatter.format(totalAssets);
	}

	@WorkerThread
	public String getTRaceName() {
		return NeoComAbstractPart.upperCaseFirst(this.getCastedModel().getRace().getName().toLowerCase());
	}

	@WorkerThread
	public String getURLForAvatar() {
		return this.getCastedModel().getUrlforAvatar();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("PilotDashbordPart [ ");
		buffer.append("model: ").append(this.getModel().toString());
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class PilotDashbordRender4Header extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................
		private ImageView targetIcon = null;
		private ImageView toprightDecorator = null;
		private ImageView allianceIcon = null;
		private ImageView corporationIcon = null;

		private TextView pilotName = null;
		private TextView pilotIdentifier = null;
		private TextView accountBalance = null;
		private TextView nodeClass = null;
		private TextView race = null;
		private TextView cloneType = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public PilotDashbordRender4Header( PilotDashbordPart part, Activity activity ) {
			super(part, activity);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public PilotDashbordPart getPart() {
			return (PilotDashbordPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			this.targetIcon = (ImageView) this._convertView.findViewById(R.id.targetIcon);
			this.toprightDecorator = (ImageView) this._convertView.findViewById(R.id.toprightDecorator);
			this.allianceIcon = (ImageView) this._convertView.findViewById(R.id.allianceIcon);
			this.corporationIcon = (ImageView) this._convertView.findViewById(R.id.corporationIcon);

			this.pilotName = (TextView) this._convertView.findViewById(R.id.pilotName);
			this.pilotIdentifier = (TextView) this._convertView.findViewById(R.id.pilotIdentifier);
			this.accountBalance = (TextView) this._convertView.findViewById(R.id.accountBalance);
			this.nodeClass = (TextView) this._convertView.findViewById(R.id.nodeClass);
			this.race = (TextView) this._convertView.findViewById(R.id.race);
			this.cloneType = (TextView) this._convertView.findViewById(R.id.cloneType);
		}

		@Override
		public void updateContent() {
			final String link = getPart().getURLForAvatar();
			if( (null != link) && (null != targetIcon))
					Picasso.get().load(link).into(targetIcon);
			toprightDecorator.setImageResource(R.drawable.omega);
			toprightDecorator.setVisibility(View.VISIBLE);
			AllianceV1 alliance = this.getPart().getCastedModel().getAlliance();

			this.pilotName.setText(getPart().getCastedModel().getName());
			this.pilotIdentifier.setText(Integer.valueOf(this.getPart().getCastedModel().getCharacterId()).toString());
			this.accountBalance.setText(this.getPart().getTWalletAmount());
			this.nodeClass.setText("CREDENTIAL");
			this.race.setText(this.getPart().getTRaceName());
//			this.cloneType.setText(this.getPart().getTRaceName());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.pilotdetail4header;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
