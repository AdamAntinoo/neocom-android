package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetAlliancesAllianceIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCorporationsCorporationIdOk;

public class PilotRender extends NeoComRenderv2 {
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
	private TextView raceName;
	private TextView cloneType;
	private TextView skillPoints;
	private TextView walletAmount;

	// - C O N S T R U C T O R S
	public PilotRender( final IAndroidController controller, final Context context ) {
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
		this.raceName = this.getView().findViewById(R.id.raceName);
		this.cloneType = this.getView().findViewById(R.id.cloneType);
		this.skillPoints = this.getView().findViewById(R.id.skillPoints);
		this.walletAmount = this.getView().findViewById(R.id.walletAmount);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.setAvatarIcon(this.avatarIcon, this.getController().getModel().getPilotIdentifier());
		final String raceName = this.getController().getModel().getRaceName();
		this.raceName.setText(raceName.toUpperCase());
		this.raceIconSmall.setImageDrawable(this.getRaceIcon(raceName));
		this.pilotName.setText(this.getController().getModel().getName());
		this.pilotIdentifier.setText("[#" + this.getController().getModel().getPilotIdentifier() + "]");

		this.accessCorporationData();
		this.accessAlliancenData();
		this.walletAmount.setText(this.accessWalletAmount());
	}

	protected void accessCorporationData() {
		final GetCorporationsCorporationIdOk corporationData = this.getController().getModel().getCorporationData();
		if (null != corporationData) {
			final String link = this.getController().getModel().getCorporationIconUrl();
			Picasso.get().load(link).into(corporationIcon);
			this.corporationName.setText(corporationData.getName());
		} else this.corporationDataBlock.setVisibility(View.GONE);
	}

	protected void accessAlliancenData() {
		final GetAlliancesAllianceIdOk allianceData = this.getController().getModel().getAllianceData();
		if (null != allianceData) {
			final String link = this.getController().getModel().getAllianceIconUrl();
			Picasso.get().load(link).into(allianceIcon);
			this.allianceName.setText(allianceData.getName());
		} else this.allianceDataBlock.setVisibility(View.GONE);
	}

	protected String accessWalletAmount() {
		final Credential credential = this.getController().getModel().getCredential();
		if (null != credential)
			return this.generatePriceString(credential.getWalletBalance());
		else return "-";
	}
}
