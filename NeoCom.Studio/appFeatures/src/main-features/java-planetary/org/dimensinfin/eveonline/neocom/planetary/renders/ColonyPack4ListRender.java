package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.planetary.controller.ColonyPackController4Header;
import org.dimensinfin.eveonline.neocom.planetary.ui.StorageProgressImageView;

// - C O L O N Y P A C K 4 L I S T R E N D E R
public class ColonyPack4ListRender extends NeoComRenderv2 {
	// - U I   F I E L D S
	private ViewGroup nodeFrameContainer;
	private TextView solarSystemName;
	private TextView planetType;
	private TextView installationsNumber;
	private ImageView nodeIcon;
	private ImageView levelDecorator;
	private TextView storageUsed;
	private FrameLayout colonyStorageCanvas;
	private TextView resourcesMarketValue;

	// - C O N S T R U C T O R S
	public ColonyPack4ListRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public ColonyPackController4Header getController() {
		return (ColonyPackController4Header) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.colony4list;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.nodeFrameContainer = this.getView().findViewById(R.id.nodeFrameContainer);
		this.solarSystemName = this.getView().findViewById(R.id.solarSystemName);
		this.planetType = this.getView().findViewById(R.id.planetType);
		this.installationsNumber = this.getView().findViewById(R.id.installationsNumber);
		this.colonyStorageCanvas = this.getView().findViewById(R.id.colonyStorageCanvas);
		this.nodeIcon = this.getView().findViewById(R.id.planetIcon);
		this.levelDecorator = this.getView().findViewById(R.id.bottomrightDecorator);
		this.storageUsed = this.getView().findViewById(R.id.storageUsed);
		this.resourcesMarketValue = this.getView().findViewById(R.id.resourcesMarketValue);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.nodeFrameContainer.setBackground(this.getContext().getResources().getDrawable(this.getController().drawableBorder()));
		this.solarSystemName.setText(this.getController().getModel().getName());
		this.planetType.setText(this.getController().getModel().getPlanetType().name().toUpperCase());
		this.installationsNumber.setText(this.accessInstallations());
		this.storageUsed.setText(this.accessStorageUsed());
		this.nodeIcon.setImageResource(R.drawable.planets);
		this.levelDecorator.setVisibility(View.VISIBLE);
		this.resourcesMarketValue.setText(this.accessResourcesMarketvalue());
		switch (this.getController().getModel().getUpgradeLevel()) {
			case 1:
				levelDecorator.setImageResource(R.drawable.level1white);
				break;
			case 2:
				levelDecorator.setImageResource(R.drawable.level2white);
				break;
			case 3:
				levelDecorator.setImageResource(R.drawable.level3white);
				break;
			case 4:
				levelDecorator.setImageResource(R.drawable.level4white);
				break;
			case 5:
				levelDecorator.setImageResource(R.drawable.level5white);
				break;
			default:
				levelDecorator.setImageResource(R.drawable.level1white);
		}
		this.setPlanetIcon();
		final Handler handler = new Handler(Looper.getMainLooper());
		handler.post(() -> {
			final ImageView storageImage = this.generateStorageImage();
			this.colonyStorageCanvas.addView( storageImage);
		});
	}

	private ImageView generateStorageImage() {
		return new StorageProgressImageView.Builder(this.getContext())
				       .withUsedVolume(this.getController().getModel().getStorageInUse())
				       .withCapacity(this.getController().getModel().getStorageCapacity())
				       .build();
	}

	private void setPlanetIcon() {
		// Get the planet type name and set the icon depending on that value.
		final String planetTypeName = this.getController().getModel().getPlanetType().name().toLowerCase();
		if (planetTypeName.equalsIgnoreCase("barren"))
			nodeIcon.setImageResource(R.drawable.planet_barren_102_128_3);
		if (planetTypeName.equalsIgnoreCase("lava"))
			nodeIcon.setImageResource(R.drawable.planet_lava_102_128_1);
		if (planetTypeName.equalsIgnoreCase("plasma"))
			nodeIcon.setImageResource(R.drawable.planet_plasma_103_128_2);
		if (planetTypeName.equalsIgnoreCase("temperate"))
			nodeIcon.setImageResource(R.drawable.planet_temperate_102_128_4);
		if (planetTypeName.equalsIgnoreCase("ice"))
			nodeIcon.setImageResource(R.drawable.planet_ice_103_128_1);
		if (planetTypeName.equalsIgnoreCase("oceanic"))
			nodeIcon.setImageResource(R.drawable.planet_oceanic_104_128_2);
		if (planetTypeName.equalsIgnoreCase("storm"))
			nodeIcon.setImageResource(R.drawable.planet_storm_102_128_2);
		if (planetTypeName.equalsIgnoreCase("gas"))
			nodeIcon.setImageResource(R.drawable.planet_gas_103_128_3);
	}

	private String accessInstallations() {
		return qtyFormatter.format(this.getController().getModel().getInstallationsCount());
	}

	private String accessStorageUsed() {
		final Float storageInUse = this.getController().getModel().getStorageInUse();
		final Float storageCapacity = this.getController().getModel().getStorageCapacity();
		return volumeFormatter.format(storageInUse) + " m3/" + volumeFormatter.format(storageCapacity) + " m3";
	}
	private String accessResourcesMarketvalue(){
		return this.generatePriceString(this.getController().getModel().getResourcesMarketValue());
	}
}
