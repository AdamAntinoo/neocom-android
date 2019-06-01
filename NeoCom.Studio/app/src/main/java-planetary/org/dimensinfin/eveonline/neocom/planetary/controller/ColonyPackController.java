package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColonyLayoutActivity;

public class ColonyPackController extends AAndroidController<ColonyPack> implements View.OnClickListener, View.OnLongClickListener {
	private Context context;

	public ColonyPackController( @NonNull final ColonyPack model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		this.context = context;
		return new ColonyPack4ListRender(this, context);
	}

	@Override
	public void onClick( final View v ) {
		logger.info(">> [ColonyPackController.onClick]");
		this.toggleExpanded();
		this.sendChangeEvent(SystemWideConstants.events.EVENTCONTENTS_ACTIONEXPANDCOLLAPSE.name());
		//		completeClick();
		this.invalidate();

		//		Toast.makeText(this.context, "Activating Colony layout", Toast.LENGTH_LONG)
		//				.show();
		//		final Intent intent = new Intent(this.context, PlanetaryColonyLayoutActivity.class);
		//		intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
		//				, this.getModel().getCapsuleerIdentifier()); // Credential account identifier
		//		intent.putExtra(EExtras.PLANET_IDENTIFIER.name()
		//				, this.getModel().getPlanetId()); // The colony/planet identifier
		//		intent.putExtra(EExtras.PLANET_TYPE_NAME.name()
		//				, this.getModel().getPlanetType().name()); // The planet type as string
		//		this.context.startActivity(intent);
		logger.info("<< [ColonyPackController.onClick]");
	}

	private void toggleExpanded() {
		final ICollaboration model = this.getModel();
		if (model instanceof IExpandable)
			this.getModel().toggleExpand();
	}

	@Override
	public boolean onLongClick( final View v ) {
		logger.info(">> [ColonyPackController.onClick]");
		Toast.makeText(this.context, "Activating Colony layout", Toast.LENGTH_LONG)
				.show();
		final Intent intent = new Intent(this.context, PlanetaryColonyLayoutActivity.class);
		intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
				, this.getModel().getCapsuleerIdentifier()); // Credential account identifier
		intent.putExtra(EExtras.PLANET_IDENTIFIER.name()
				, this.getModel().getPlanetId()); // The colony/planet identifier
		intent.putExtra(EExtras.PLANET_TYPE_NAME.name()
				, this.getModel().getPlanetType().name()); // The planet type as string
		this.context.startActivity(intent);
		logger.info("<< [ColonyPackController.onClick]");
		return true;
	}

	// - C O L O N Y P A C K 4 L I S T R E N D E R
	public static class ColonyPack4ListRender extends NeoComRenderv2 {
		// - U I   F I E L D S
		private TextView solarSystemName;
		private TextView planetType;
		private TextView installationsNumber;
		private ImageView nodeIcon;
		private ImageView botrightDecorator;

		// - C O N S T R U C T O R S
		public ColonyPack4ListRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public ColonyPackController getController() {
			return (ColonyPackController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.colony4list;
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			this.solarSystemName = this.getView().findViewById(R.id.solarSystemName);
			this.planetType = this.getView().findViewById(R.id.planetType);
			this.installationsNumber = this.getView().findViewById(R.id.installationsNumber);
			this.nodeIcon = this.getView().findViewById(R.id.planetIcon);
			this.botrightDecorator = this.getView().findViewById(R.id.bottomrightDecorator);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			//			Chrono chrono = new Chrono();
			this.solarSystemName.setText(this.getController().getModel().getName());
			this.planetType.setText(this.getController().getModel().getPlanetType().name().toUpperCase());
			this.installationsNumber.setText(this.accessInstallations());
			nodeIcon.setImageResource(R.drawable.planets);
			botrightDecorator.setVisibility(View.VISIBLE);
			switch (this.getController().getModel().getUpgradeLevel()) {
				case 1:
					botrightDecorator.setImageResource(R.drawable.level1white);
					break;
				case 2:
					botrightDecorator.setImageResource(R.drawable.level2white);
					break;
				case 3:
					botrightDecorator.setImageResource(R.drawable.level3white);
					break;
				case 4:
					botrightDecorator.setImageResource(R.drawable.level4white);
					break;
				case 5:
					botrightDecorator.setImageResource(R.drawable.level5white);
					break;
			}
			this.setPlanetIcon();

			// Hide the right arrow while the number on contents is zero.
			//			if (getPart().getCastedModel().isEmpty()) _rightArrow.setVisibility(View.GONE);
		}

		protected void setPlanetIcon() {
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
	}
}
