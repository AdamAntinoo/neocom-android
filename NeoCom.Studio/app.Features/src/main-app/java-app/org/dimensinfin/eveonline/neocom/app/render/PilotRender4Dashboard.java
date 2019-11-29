package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.core.MVCScheduler;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.annotation.RequiresNetwork;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;

public class PilotRender4Dashboard extends NeoComRender {
//	private static final ESIDataProvider esiDataProvider = NeoComCom
	private ImageView avatarIcon;
	private ImageView cloneIconSmall;
	private ImageView raceIconSmall;
	private ViewGroup corporationDataBlock;
	private ImageView corporationIcon;
	private TextView corporationName;
	private ViewGroup allianceDataBlock;
	private ImageView allianceIcon;
	private TextView allianceName;
	private TextView pilotName;
	private TextView pilotIdentifier;
	private ImageView raceIconName;
	private TextView raceName;
	private TextView cloneType;
	private TextView skillPoints;

	// - C O N S T R U C T O R S
	public PilotRender4Dashboard( final IAndroidController controller, final Context context ) {
		super(controller, context);
	}

	@Override
	public PilotController getController() {
		return (PilotController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.pilotdetail4header;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.avatarIcon = Objects.requireNonNull(this.getView().findViewById(R.id.avatarIcon));
		this.cloneIconSmall = Objects.requireNonNull(this.getView().findViewById(R.id.cloneIconSmall));
		this.raceIconSmall = Objects.requireNonNull(this.getView().findViewById(R.id.raceIconSmall));
		this.corporationDataBlock = Objects.requireNonNull(this.getView().findViewById(R.id.corporationDataBlock));
		this.corporationIcon = Objects.requireNonNull(this.getView().findViewById(R.id.corporationIcon));
		this.corporationName = Objects.requireNonNull(this.getView().findViewById(R.id.corporationName));
		this.allianceDataBlock = Objects.requireNonNull(this.getView().findViewById(R.id.allianceDataBlock));
		this.allianceIcon = Objects.requireNonNull(this.getView().findViewById(R.id.allianceIcon));
		this.allianceName = Objects.requireNonNull(this.getView().findViewById(R.id.allianceName));
		this.pilotName = Objects.requireNonNull(this.getView().findViewById(R.id.pilotName));
		this.pilotIdentifier = Objects.requireNonNull(this.getView().findViewById(R.id.pilotIdentifier));
		this.raceIconName = Objects.requireNonNull(this.getView().findViewById(R.id.raceIconName));
		this.raceName = Objects.requireNonNull(this.getView().findViewById(R.id.raceName));
		this.cloneType = Objects.requireNonNull(this.getView().findViewById(R.id.cloneType));
		this.skillPoints = Objects.requireNonNull(this.getView().findViewById(R.id.skillPoints));
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.setAvatarIcon(this.avatarIcon, this.getController().getModel().getPilotId());
		final String raceName = this.getController().getModel().getRaceName();
		this.raceName.setText(raceName.toUpperCase());
		this.raceIconName.setImageDrawable(this.getRaceIcon(raceName));
		this.raceIconSmall.setImageDrawable(this.getRaceIcon(raceName));
		this.pilotName.setText(this.getController().getModel().getName());
		this.pilotIdentifier.setText( String.format( "[#%d]", this.getController().getModel().getPilotId() ) );

		this.accessCorporationData();
		this.accessAllianceData();
	}

	/**
	 * Corporation data will require a background network access to it is wrapped in a background thread. But background access is not
	 * preconfigured for Render instances. I should use a helper to get access to the ESI data provider.
	 */
	@RequiresNetwork
	protected void accessCorporationData() {
		MVCScheduler.backgroundExecutor.submit( ()->{
			this.corporationData =
		} );
//		final GetCorporationsCorporationIdOk corporationData = this.getController().getModel().getCorporationData();
//		if (null != corporationData) {
		// TODO - Pilots no longer have their corporation data. This is another object
//			final String link = this.getController().getModel().getCorporationIconUrl();
//			Picasso.get().load(link).into(corporationIcon);
//			this.corporationName.setText(corporationData.getName());
//		} else
		this.corporationDataBlock.setVisibility(View.GONE);
	}

	protected void accessAllianceData() {
		// TODO - Pilot no longer has the alliance data. This is another object
//		final GetAlliancesAllianceIdOk allianceData = this.getController().getModel().getAllianceData();
//		if (null != allianceData) {
//			final String link = this.getController().getModel().getAllianceIconUrl();
//			Picasso.get().load(link).into(allianceIcon);
//			this.allianceName.setText(allianceData.getName());
//		} else
			this.allianceDataBlock.setVisibility(View.GONE);
	}
}
