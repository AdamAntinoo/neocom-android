//	PROJECT:        MS-PlanetaryOptimizer (MS-PO)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	SpringBoot-MS-Java 1.8.
//	DESCRIPTION:	Initial step to have an Eve application based on the Android code. This version exports
//								a list of assets and also has access to the Eve Item CCP database to get some of the
//								asset items properties..
package org.dimensinfin.eveonline.neocom.planetary;

import java.util.logging.Logger;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// - CLASS IMPLEMENTATION ...................................................................................
@DatabaseTable(tableName = "resourcelist")
public class ResourceListHandler {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger	logger	= Logger.getLogger("ResourceListHandler");

	// - F I E L D - S E C T I O N ............................................................................
	@DatabaseField(id = true)
	public int						id			= -1;
	@DatabaseField
	public String					name		= "<NAME>";

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ResourceListHandler() {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}
}

// - UNUSED CODE ............................................................................................
