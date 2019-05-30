package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.BuildConfig;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.AssetsManagerActivity;
import org.dimensinfin.eveonline.neocom.app.controller.NeoComDashboardMenuController;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColoniesActivity;
import org.dimensinfin.eveonline.neocom.app.EExtras;

public class NeoComDashboardMenuRender extends NeoComRenderv2 {
	// - U I   F I E L D S
	private Button neocomMenu = null;
	private Button assetsButton = null;
	private Button miningButton = null;
	private Button planetaryButton = null;
	private Button skillsButton = null;


	// - C O N S T R U C T O R S
	public NeoComDashboardMenuRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public NeoComDashboardMenuController getController() {
		return (NeoComDashboardMenuController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.neocommenubar4header;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		neocomMenu = this.getView().findViewById(R.id.neocomMenu);
		this.assetsButton = this.getView().findViewById(R.id.assetsButton);
		this.miningButton = this.getView().findViewById(R.id.miningButton);
		this.planetaryButton = this.getView().findViewById(R.id.planetaryButton);
		skillsButton = this.getView().findViewById(R.id.planetaryButton);
		// Set the onClick actions.
		assetsButton.setOnClickListener(( View v ) -> {
					if (BuildConfig.DEBUG)
						Toast.makeText(getContext(),
								"assetsButton Clicked!", Toast.LENGTH_SHORT).show();
					// With the new implementation get just the identifier for the Credential.
					final int identifier = this.getController().getModel().getCredential().getAccountId();
					final Intent intent = new Intent(this.getContext(), AssetsManagerActivity.class);
					// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
					this.getContext().startActivity(intent);
				}
		);
		assetsButton.setVisibility(View.GONE);
		miningButton.setOnClickListener(( View v ) -> {
					if (BuildConfig.DEBUG)
						Toast.makeText(getContext(),
								"miningButton Clicked!", Toast.LENGTH_SHORT).show();
					// With the new implementation get just the identifier for the Credential.
					final int identifier = this.getController().getModel().getCredential().getAccountId();
					final Intent intent = new Intent(this.getContext(), MiningLedgerActivity.class);
					// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
					this.getContext().startActivity(intent);
				}
		);
		miningButton.setVisibility(View.VISIBLE);
		planetaryButton.setOnClickListener(( View v ) -> {
					if (BuildConfig.DEBUG)
						Toast.makeText(getContext(),
								"Planetary Dashboard selected!", Toast.LENGTH_SHORT).show();
					// With the new implementation get just the identifier for the Credential.
					final int identifier = this.getController().getModel().getCredential().getAccountId();
					final Intent intent = new Intent(this.getContext(), PlanetaryColoniesActivity.class);
					// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
					this.getContext().startActivity(intent);
				}
		);
		planetaryButton.setVisibility(View.VISIBLE);
//		skillsButton.setOnClickListener(( View v ) -> {
//					if (BuildConfig.DEBUG)
//						Toast.makeText(getContext(),
//								"skillsButton Clicked!", Toast.LENGTH_LONG).show();
//					// With the new implementation get just the identifier for the Credential.
//					final int identifier = this.getController().getModel().getCredential().getAccountId();
//					final Intent intent = new Intent(this.getContext(), SkillsManagerActivity.class);
//					// Add all the identification chain as in the backend Angular calls. This is currently the Credential identifier
//					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
//					this.getContext().startActivity(intent);
//				}
//		);
//		skillsButton.setVisibility(View.GONE);
	}

	@Override
	public void updateContent() {
		super.updateContent();
	}
}
