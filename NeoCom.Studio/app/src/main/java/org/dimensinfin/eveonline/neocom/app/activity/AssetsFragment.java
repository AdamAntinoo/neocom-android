package org.dimensinfin.eveonline.neocom.app.activity;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.AuthenticatedFragment;
import org.dimensinfin.eveonline.neocom.app.datasource.AssetsByLocationDataSource;
import org.dimensinfin.eveonline.neocom.app.factory.AssetControllerFactory;
import org.dimensinfin.eveonline.neocom.asset.provider.AssetProvider;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;

public class AssetsFragment extends AuthenticatedFragment {
	private AssetProvider assetsProvider;

	public AssetProvider getAssetsProvider() {
		return this.assetsProvider;
	}

	public AssetsFragment setAssetsProvider( final AssetProvider assetsProvider ) {
		this.assetsProvider = assetsProvider;
		return this;
	}

	@Override
	public IControllerFactory createFactory() {
		return new AssetControllerFactory( this.getVariant() );
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
					       .withAssetProvider( this.getAssetsProvider() )
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
					       .withAssetsProvider( this.getAssetsProvider() )
					       .optionalFilter( this.getExtras().getString( EExtras.ASSETFILTER_DEFINITION.name() ) )
					       .build();
		}
		throw new NeoComRuntimeException( "Data Source Variant selector not found. Unable to create a data source." );
	}
}
