package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;

public class PilotRender extends NeoComRenderv2 {
	private ImageView avatarIcon;
	//	private ImageView corporationIconSmall;
	//	private ImageView allianceIconSmall;
	private ImageView cloneIconSmall;
	private ImageView raceIconSmall;
	private ImageView corporationIcon;
	private ImageView allianceIcon;
	//	private ImageView cloneIcon;
	//	private ImageView raceIcon;
	private TextView pilotName;
	private TextView pilotIdentifier;
	private TextView raceName;
	private TextView cloneType;

	// - C O N S T R U C T O R S
	public PilotRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
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
		this.corporationIcon = this.getView().findViewById(R.id.corporationIcon);
		this.allianceIcon = this.getView().findViewById(R.id.allianceIcon);
		this.pilotName = this.getView().findViewById(R.id.pilotName);
		this.pilotIdentifier = this.getView().findViewById(R.id.pilotIdentifier);
		this.raceName = this.getView().findViewById(R.id.raceName);
		this.cloneType = this.getView().findViewById(R.id.cloneType);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.setAvatarIcon(this.avatarIcon, this.getController().getModel().getPilotIdentifier());
		final String raceName = this.getController().getModel().getRaceName();
		this.raceName.setText(raceName);
		this.raceIconSmall.setImageDrawable(this.getRaceIcon(raceName));
		this.pilotName.setText(this.getController().getModel().getName());
		this.pilotIdentifier.setText("[#" + this.getController().getModel().getPilotIdentifier() + "]");
	}
}
