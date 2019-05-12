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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.AssetsManagerActivity;
import org.dimensinfin.eveonline.neocom.activity.SkillsManagerActivity;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.ui.NeoComMenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComMenuBarPart extends NeoComAbstractPart<NeoComMenuBar> {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("NeoComMenuBarPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComMenuBarPart( final NeoComMenuBar model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	//	@Override
	//	public NeoComMenuBar getCastedModel() {
	//		return (NeoComMenuBar) this.getModel();
	//	}
	@Override
	public long getModelId() {
		return 0;
	}

	@Override
	public AbstractRender selectRenderer() {
		return new NeoComMenuBarRender(this, this.getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("NeoComMenuBarPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	public static class NeoComMenuBarRender extends AbstractRender {
		private Button neocomMenu = null;
		private Button assetsButton = null;
		private Button miningButton = null;
		private Button skillsButton = null;

		public NeoComMenuBarRender( AbstractPart newPart, Activity context ) {
			super(newPart, context);
		}

		public NeoComMenuBarPart getPart() {
			return (NeoComMenuBarPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			neocomMenu = (Button) _convertView.findViewById(R.id.neocomMenu);
			assetsButton = (Button) _convertView.findViewById(R.id.assetsButton);
			miningButton = (Button) _convertView.findViewById(R.id.miningButton);
			skillsButton = (Button) _convertView.findViewById(R.id.skillsButton);
			// Set the onClick actions.
			assetsButton.setOnClickListener(( View v ) -> {
						Toast.makeText(getContext(),
								"assetsButton Clicked!", Toast.LENGTH_SHORT).show();
						// With the new implementation get just the identifier for the Credential.
						final int identifier = this.getPart().getCastedModel().getCredential().getAccountId();
						final Intent intent = new Intent(this.getContext(), AssetsManagerActivity.class);
						// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
						intent.putExtra(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
						this.getContext().startActivity(intent);
					}
			);
			miningButton.setOnClickListener(( View v ) -> {
						Toast.makeText(getContext(),
								"miningButton Clicked!", Toast.LENGTH_SHORT).show();
						// With the new implementation get just the identifier for the Credential.
						final int identifier = this.getPart().getCastedModel().getCredential().getAccountId();
						final Intent intent = new Intent(this.getContext(), MiningLedgerActivity.class);
						// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
						intent.putExtra(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
						this.getContext().startActivity(intent);
					}
			);
			skillsButton.setOnClickListener(( View v ) -> {
						Toast.makeText(getContext(),
								"skillsButton Clicked!", Toast.LENGTH_LONG).show();
						// With the new implementation get just the identifier for the Credential.
						final int identifier = this.getPart().getCastedModel().getCredential().getAccountId();
						final Intent intent = new Intent(this.getContext(), SkillsManagerActivity.class);
						// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
						intent.putExtra(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
						this.getContext().startActivity(intent);
					}
			);
		}

		@Override
		public void updateContent() {
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.neocommenubar4header;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
