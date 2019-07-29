package org.dimensinfin.eveonline.neocom.mining.activity;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.EActivities;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceAll;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;

public class MiningLedgerFragment extends MVCPagerFragment {
	private Credential credential;

	@Override
	public IControllerFactory createFactory() {
		return new MiningControllerFactory(this.getVariant())
				.registerActivity(EActivities.RESOURCE_DETAIL_ACTIVITY.name(), PilotDashboardActivity.class);
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(PageNamesType.MINING_EXTRACTIONS_TODAY.name()))
			return new MiningLedgerDataSourceToday.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       //					       .withCacheStatus(false)
					       .withCredential(this.getCredential())
					       .withMiningRepository(NeoComComponentFactory.getSingleton().getMiningRepository())
					       .build();
		if (this.getVariant().equalsIgnoreCase(PageNamesType.MINING_EXTRACTIONS_ALL.name()))
			return new MiningLedgerDataSourceAll.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       //					       .withCacheStatus(false)
					       .withCredential(this.getCredential())
					       .withMiningRepository(NeoComComponentFactory.getSingleton().getMiningRepository())
					       .build();
		throw new NeoComRuntimeException("Mining variant has not defined Data Source to generate content.");
	}

	public Credential getCredential() {
		return this.credential;
	}

	public MiningLedgerFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

//	@Override
//	public String getSubtitle() {
//		return null;
//	}
//
//	@Override
//	public String getPageTitle() {
//		return null;
//	}
}
