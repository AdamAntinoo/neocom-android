package org.dimensinfin.eveonline.neocom.app;

import org.dimensinfin.eveonline.neocom.datamngmt.FileSystemAndroidImplementation;

public class AndroidFileSystemAdapter extends FileSystemAndroidImplementation {
	// - C O N S T R U C T O R
	public AndroidFileSystemAdapter( final String applicationStoreDirectory ) {
		super(applicationStoreDirectory);
	}

	// - I F I L E S Y S T E M   I N T E R F A C E
	// - B U I L D E R
	public static class Builder {
		private AndroidFileSystemAdapter onConstruction;

		public Builder( final String applicationStoreDirectory ) {
			this.onConstruction = new AndroidFileSystemAdapter(applicationStoreDirectory);
		}

		public AndroidFileSystemAdapter build() {
			return this.onConstruction;
		}
	}

}
