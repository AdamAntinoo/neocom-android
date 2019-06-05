package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;

public class VisualTestsDataSource extends AMVCDataSource {
	@Override
	public void prepareModel() {
 // - S P L A S H

	}

	@Override
	public void collaborate2Model() {
		// - S P L A S H
		this.addModelContents(new AppVersion());
		this.addModelContents(new AppVersion().setName("Visual Test Name"));
		this.addModelContents(new AppVersion().setVersion("Visual Test Version"));

	}

	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<VisualTestsDataSource, Builder> {
		private VisualTestsDataSource onConstruction;

		@Override
		protected VisualTestsDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VisualTestsDataSource();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}

		//		public Builder withName( final String name ) {
		//			this.onConstruction.name = name;
		//			return this;
		//		}
		//
		//		public Builder withVersion( final String version ) {
		//			this.onConstruction.version = version;
		//			return this;
		//		}
		//
		//		//			public Builder withApplicationContext( final Context applicationContext ) {
		//		//				this.getActual().setApplicationContext(applicationContext);
		//		//				return this;
		//		//			}
		//
		//		public Builder withEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
		//			this.onConstruction.esiAdapter = esiAdapter;
		//			return this;
		//		}
		//
		//		public Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
		//			this.onConstruction.credentialRepository = credentialRepository;
		//			return this;
		//		}
	}
}
