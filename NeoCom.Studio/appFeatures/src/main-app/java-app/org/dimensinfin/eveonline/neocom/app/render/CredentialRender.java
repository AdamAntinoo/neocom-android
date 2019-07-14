package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;

public class CredentialRender extends NeoComRenderv2 {
	// - U I   F I E L D S
	private ImageView nodeIcon;
	private TextView pilotName;
	private TextView pilotIdentifier;
	private TextView accountBalance;
	private TextView assetsCount;
	private ImageView raceIcon;
	private TextView race;
	private TextView cloneType;
	private TextView expirationTime;
	private TextView nodeClass;

	// - L A Y O U T   F I E L D S
	private ViewGroup nodeIconBlock;
	private ViewGroup pilotRaceDetailBlock;

	// - C O N S T R U C T O R S
	public CredentialRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public CredentialController getController() {
		return (CredentialController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.credential4list;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		// Layout fields
		this.nodeIconBlock = this.getView().findViewById(R.id.nodeIconBlock);
		this.pilotRaceDetailBlock = this.getView().findViewById(R.id.pilotRaceDetailBlock);
		// Content  fields.
		this.nodeIcon = this.getView().findViewById(R.id.nodeIcon);
		this.pilotName = this.getView().findViewById(R.id.pilotName);
		this.pilotIdentifier = this.getView().findViewById(R.id.pilotIdentifier);
		this.accountBalance = this.getView().findViewById(R.id.accountBalance);
		this.assetsCount = this.getView().findViewById(R.id.assetsCount);
		this.raceIcon = this.getView().findViewById(R.id.raceIcon);
		this.race = this.getView().findViewById(R.id.race);
		this.cloneType = this.getView().findViewById(R.id.cloneType);
		this.expirationTime = this.getView().findViewById(R.id.expirationTime);
		this.nodeClass = this.getView().findViewById(R.id.nodeClass);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			this.getView().setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderrose, getContext().getTheme()));
		else this.getView().setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderrose));
		this.accessPilotIcon();
		this.nodeClass.setText("CREDENTIAL");
		this.pilotName.setText(this.getController().getPilotName());
		this.pilotIdentifier.setText("[#" + this.getController().getModel().getAccountId() + "]");
		this.accountBalance.setText(this.accessAccountBalance());
		this.assetsCount.setText(this.accessAssetsCount()); // Set a default value while we get the real from the database.
		this.race.setText(this.accessPilotRaceName());
		this.accessRaceIcon();
	}

	protected String accessAssetsCount() {
		final long count = this.getController().getModel().getAssetsCount();
		if (count < 0) return "-"; // This is the response while accessing the number of assets from backend.
		else return this.qtyFormatter.format(count);
	}

	protected String accessAccountBalance() {
		final double balance = this.getController().getModel().getWalletBalance();
		if (balance < 0.0) return "- ISK"; // Adapt response while real data is accessed.
		else return this.generatePriceString(balance);
	}

	protected String accessPilotRaceName() {
		final String raceName = this.getController().getModel().getRaceName();
		if (null == raceName) return "-";
		else return raceName.toUpperCase();
	}

	protected void accessPilotIcon() {
		if (null != this.nodeIcon) {
			final String link = this.getUrl4Avatar(this.getController().getModel().getAccountId());
			Picasso.get().load(link).into(nodeIcon);
		}
	}

	protected String getUrl4Avatar( final int pilotIdentifier ) {
		return "http://image.eveonline.com/character/" + pilotIdentifier + "_256.jpg";
	}

	protected void accessRaceIcon() {
		final String raceName = this.getController().getModel().getRaceName();
		if (null != raceName)
			switch (raceName.toUpperCase()) {
				case "MINMATAR":
					this.raceIcon.setImageDrawable(this.getDrawable(R.drawable.minmatar));
					break;
				case "CALDARI":
					this.raceIcon.setImageDrawable(this.getDrawable(R.drawable.caldari));
					break;
				case "AMARR":
					this.raceIcon.setImageDrawable(this.getDrawable(R.drawable.amarr));
					break;
				case "GALLENTE":
					this.raceIcon.setImageDrawable(this.getDrawable(R.drawable.gallente));
					break;
			}
	}
}
