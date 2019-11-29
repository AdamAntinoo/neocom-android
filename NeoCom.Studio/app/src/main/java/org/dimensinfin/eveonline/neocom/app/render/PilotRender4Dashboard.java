package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.core.MVCScheduler;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.annotation.RequiresNetwork;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetAlliancesAllianceIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCorporationsCorporationIdOk;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class PilotRender4Dashboard extends NeoComRender {
	// - U I   F I E L D S
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
	private ImageView raceIcon;
	private TextView raceName;
	private TextView cloneType;
	private TextView skillPoints;

	private GetCorporationsCorporationIdOk corporationData;
	private GetAlliancesAllianceIdOk allianceData;

	// - C O N S T R U C T O R S
	public PilotRender4Dashboard( final IAndroidController controller, final Context context ) {
		super( controller, context );
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
		this.avatarIcon = Objects.requireNonNull( this.getView().findViewById( R.id.avatarIcon ) );
		this.cloneIconSmall = Objects.requireNonNull( this.getView().findViewById( R.id.cloneIconSmall ) );
		this.raceIconSmall = Objects.requireNonNull( this.getView().findViewById( R.id.raceIconSmall ) );
		this.corporationDataBlock = Objects.requireNonNull( this.getView().findViewById( R.id.corporationDataBlock ) );
		this.corporationIcon = Objects.requireNonNull( this.getView().findViewById( R.id.corporationIcon ) );
		this.corporationName = Objects.requireNonNull( this.getView().findViewById( R.id.corporationName ) );
		this.allianceDataBlock = Objects.requireNonNull( this.getView().findViewById( R.id.allianceDataBlock ) );
		this.allianceIcon = Objects.requireNonNull( this.getView().findViewById( R.id.allianceIcon ) );
		this.allianceName = Objects.requireNonNull( this.getView().findViewById( R.id.allianceName ) );
		this.pilotName = Objects.requireNonNull( this.getView().findViewById( R.id.pilotName ) );
		this.pilotIdentifier = Objects.requireNonNull( this.getView().findViewById( R.id.pilotIdentifier ) );
		this.raceIcon = Objects.requireNonNull( this.getView().findViewById( R.id.raceIcon ) );
		this.raceName = Objects.requireNonNull( this.getView().findViewById( R.id.raceName ) );
		this.cloneType = Objects.requireNonNull( this.getView().findViewById( R.id.cloneType ) );
		this.skillPoints = Objects.requireNonNull( this.getView().findViewById( R.id.skillPoints ) );
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.setAvatarIcon( this.avatarIcon, this.getController().getModel().getPilotId() );
		final String raceName = this.getController().getModel().getRaceName();
		this.raceName.setText( raceName.toUpperCase() );
		this.raceIcon.setImageDrawable( this.getRaceIcon( raceName ) );
		this.raceIconSmall.setImageDrawable( this.getRaceIcon( raceName ) );
		this.pilotName.setText( this.getController().getModel().getName() );
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
		NeoComLogger.enter();
		MVCScheduler.backgroundExecutor.submit( () -> {
			NeoComLogger.info( "Accessing pilot corporation. {}", this.getController().getModel().getCorporationId() + "" );
			this.corporationData = Objects.requireNonNull( this.getController().getEsiUniverseDataProvider().getCorporationsCorporationId(
					this.getController().getModel().getCorporationId()
			) );
			final String link = this.getCorporationIconUrl( this.getController().getModel().getCorporationId() );
			this.corporationName.setText( this.corporationData.getName() );
			this.corporationDataBlock.setVisibility( View.VISIBLE );
			this.getController().sendChangeEvent( EEvents.EVENT_REFRESHDATA.name() );
			handler.post( () -> Picasso.get().load( link ).into( this.corporationIcon ) );
		} );
		this.corporationDataBlock.setVisibility( View.GONE );
	}

	@RequiresNetwork
	protected void accessAllianceData() {
		NeoComLogger.enter();
		MVCScheduler.backgroundExecutor.submit( () -> {
			if (null != this.corporationData) {
				if (null != this.corporationData.getAllianceId()) {
					this.allianceData = Objects.requireNonNull( this.getController().getEsiUniverseDataProvider().getAlliancesAllianceId(
							this.corporationData.getAllianceId()
					) );
					final String link = this.getAllianceIconUrl( this.corporationData.getAllianceId() );
					this.allianceName.setText( this.allianceData.getName() );
					this.allianceDataBlock.setVisibility( View.VISIBLE );
					this.getController().sendChangeEvent( EEvents.EVENT_REFRESHDATA.name() );
					handler.post( () -> Picasso.get().load( link ).into( this.allianceIcon ) );
				}
			}
		} );
		this.allianceDataBlock.setVisibility( View.GONE );
	}

	private String getCorporationIconUrl( final Integer corporationId ) {
		return "https://images.evetech.net/Corporation/" + corporationId + "_256.png";
	}

	private String getAllianceIconUrl( final Integer allianceId ) {
		return "https://images.evetech.net/Alliance/" + allianceId + "_128.png";
	}
}
