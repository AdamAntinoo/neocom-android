package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;

public class PilotRender4Dashboard extends NeoComRenderv2 {
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
		this.avatarIcon = this.getView().findViewById(R.id.avatarIcon);
		this.cloneIconSmall = this.getView().findViewById(R.id.cloneIconSmall);
		this.raceIconSmall = this.getView().findViewById(R.id.raceIconSmall);
		this.corporationDataBlock = this.getView().findViewById(R.id.corporationDataBlock);
		this.corporationIcon = this.getView().findViewById(R.id.corporationIcon);
		this.corporationName = this.getView().findViewById(R.id.corporationName);
		this.allianceDataBlock = this.getView().findViewById(R.id.allianceDataBlock);
		this.allianceIcon = this.getView().findViewById(R.id.allianceIcon);
		this.allianceName = this.getView().findViewById(R.id.allianceName);
		this.pilotName = this.getView().findViewById(R.id.pilotName);
		this.pilotIdentifier = this.getView().findViewById(R.id.pilotIdentifier);
		this.raceIconName = this.getView().findViewById(R.id.raceIconName);
		this.raceName = this.getView().findViewById(R.id.raceName);
		this.cloneType = this.getView().findViewById(R.id.cloneType);
		this.skillPoints = this.getView().findViewById(R.id.skillPoints);
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
		this.pilotIdentifier.setText("[#" + this.getController().getModel().getPilotId() + "]");

		this.accessCorporationData();
		this.accessAllianceData();
	}

	protected void accessCorporationData() {
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
