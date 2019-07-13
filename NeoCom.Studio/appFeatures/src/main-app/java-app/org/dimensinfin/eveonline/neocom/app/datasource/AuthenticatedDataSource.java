package org.dimensinfin.eveonline.neocom.app.datasource;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public interface AuthenticatedDataSource extends IDataSource {
	Credential getCredential();
}
