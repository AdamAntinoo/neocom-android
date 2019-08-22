package org.dimensinfin.eveonline.neocom.mining.activity;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.core.AuthenticatedFragment;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceAll;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;

public class MiningLedgerFragment extends AuthenticatedFragment {
	@Override
	public IControllerFactory createFactory() {
		return new MiningControllerFactory(this.getVariant())
				.registerActivity(PageNamesType.RESOURCE_DETAILS.name(), PilotDashboardActivity.class);
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
					       .withCredential(this.getCredential())
						   .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
					       .withMiningRepository(NeoComComponentFactory.getSingleton().getMiningRepository())
					       .build();
		if (this.getVariant().equalsIgnoreCase(PageNamesType.MINING_EXTRACTIONS_ALL.name()))
			return new MiningLedgerDataSourceAll.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCredential(this.getCredential())
					       .withMiningRepository(NeoComComponentFactory.getSingleton().getMiningRepository())
					       .build();
		throw new NeoComRuntimeException("Mining variant has not defined Data Source to generate content.");
	}
}
