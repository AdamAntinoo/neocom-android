package org.dimensinfin.eveonline.neocom.mining.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroid;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceAll;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;

public class MiningLedgerFragment extends AMVCPagerFragment {
	private Credential credential;

	@Override
	public IControllerFactory createFactory() {
		return new MiningControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(MiningLedgerActivity.EMiningVariants.MINING_EXTRACTIONS_TODAY.name()))
			return new MiningLedgerDataSourceToday.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
//					       .withCacheStatus(false)
					       .withCredential(this.getCredential())
					       .withMiningRepository(NeoComAndroid.getSingleton().getMiningRepository())
					       .build();
		if (this.getVariant().equalsIgnoreCase(MiningLedgerActivity.EMiningVariants.MINING_EXTRACTIONS_ALL.name()))
			return new MiningLedgerDataSourceAll.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
//					       .withCacheStatus(false)
					       .withCredential(this.getCredential())
					       .withMiningRepository(NeoComAndroid.getSingleton().getMiningRepository())
					       .build();
		throw new NeoComRuntimeException("Mining variant has not defined Data Source to generate content.");
	}

	public Credential getCredential() {
//		if (null == this.credential) return new Credential.Builder(1).build();
		return credential;
	}

	public MiningLedgerFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}
}
