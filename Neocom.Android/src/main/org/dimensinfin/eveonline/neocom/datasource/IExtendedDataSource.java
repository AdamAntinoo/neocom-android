//	PROJECT:      NeoCom.model (NEOC.M)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	Java 1.8 Library.
//	DESCRIPTION:	Isolated model structures to access and manage Eve Online character data and their
//								available databases.
//								This version includes the access to the latest 6.x version of eveapi libraries to
//								download ad parse the CCP XML API data.
//								Code integration that is not dependent on any specific platform.
package org.dimensinfin.eveonline.neocom.datasource;

//- IMPORT SECTION .........................................................................................
import java.util.ArrayList;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.eveonline.neocom.connector.DataSourceLocator;

// - CLASS IMPLEMENTATION ...................................................................................
public interface IExtendedDataSource extends IDataSource {
	// - M E T H O D - S E C T I O N ..........................................................................
	//	public RootNode collaborate2Model();

	@Deprecated
	public void connect(DataSourceManager dataSourceManager);

	//	public void createContentHierarchy();

	//	@Deprecated
	//	public ArrayList<AbstractAndroidPart> getBodyParts();

	public DataSourceLocator getDataSourceLocator();

	//	public RootNode getHeaderModel();

	@Deprecated
	public ArrayList<AbstractAndroidPart> getHeaderParts();
}

// - UNUSED CODE ............................................................................................
