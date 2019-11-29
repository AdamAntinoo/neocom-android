package org.dimensinfin.eveonline.neocom.assets.activity;

import android.view.View;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.render.RenderViewGenerator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.domain.EExtras;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.AuthenticatedFragment;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.asset.provider.AssetProvider;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;

public class AssetsByLocationFragment extends AuthenticatedFragment {
	private AssetProvider assetProvider;

	public AssetProvider getAssetProvider() {
		return this.assetProvider;
	}

	public AssetsByLocationFragment setAssetProvider( final AssetProvider assetProvider ) {
		this.assetProvider = assetProvider;
		return this;
	}

	@Override
	public IControllerFactory createFactory() {
		return new AssetControllerFactory( this.getVariant() )
				       .registerActivity( PageNamesType.ASSETS_BYLOCATION.name(), AssetDisplayActivity.class );
	}

	@Override
	public IDataSource createDS() {
		if (getVariant() == PageNamesType.ASSETS_BYLOCATION.name()) {
			return new AssetsByLocationDataSource.Builder()
					       .addIdentifier( this.getVariant() )
					       .addIdentifier( this.getCredential().getAccountId() )
					       .addIdentifier( "ASSETS-BYLOCATION" )
					       .withVariant( this.getVariant() )
					       .withExtras( this.getExtras() )
					       .withFactory( this.createFactory() )
					       .withCredential( this.getCredential() )
					       .withAssetProvider( this.getAssetProvider() )
					       .build();
		}
		if (getVariant() == PageNamesType.ASSETS_SEARCH.name()) {
			return new AssetsByLocationDataSource.Builder()
					       .addIdentifier( this.getVariant() )
					       .addIdentifier( this.getCredential().getAccountId() )
					       .addIdentifier( "ASSETS-SEARCH" )
					       .withVariant( this.getVariant() )
					       .withExtras( this.getExtras() )
					       .withFactory( this.createFactory() )
					       .withCredential( this.getCredential() )
					       .withAssetProvider( this.getAssetProvider() )
					       .optionalFilter( this.getExtras().getString( EExtras.ASSETFILTER_DEFINITION.name() ) )
					       .build();
		}
		throw new NeoComRuntimeException( "Data Source Variant selector not found. Unable to create a data source." );
	}

	@Override
	public View generateActionBarView() {
		return new RenderViewGenerator.Builder<PlanetaryActionBar>()
				       .withContext( this.getActivityContext() )
				       .withModel( new PlanetaryActionBar.Builder()
						                   .withTitleTopLeft( this.getCredential().getAccountName() )
						                   .withTitleTopRight( "[#" + this.getCredential().getAccountId() + "]" )
						                   .withSubtitle( PageNamesType.ASSETS_BYLOCATION.getPageTitle() )
						                   .withActivityIconRef( R.drawable.assets )
						                   .withPilotIdentifier( this.getCredential().getAccountId() )
						                   .build() )
				       .withFactory( new AssetControllerFactory( this.getVariant() ) )
				       .generateView();
	}
}
