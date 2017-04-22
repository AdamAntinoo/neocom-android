//	PROJECT:      MS-PlanetaryOptimizer (MS-PO)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	SpringBoot-MS-Java 1.8.
//	DESCRIPTION:	Initial step to have an Eve application based on the Android code. This version exports
//								a list of assets and also has access to the Eve Item CCP database to get some of the
//								asset items properties..
package org.dimensinfin.eveonline.neocom.microservices.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.planetary.ResourceListHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// - CLASS IMPLEMENTATION ...................................................................................
@RestController
public class PlanetaryOptimizerController {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("PlanetaryOptimizerServiceApp");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@CrossOrigin()
	@RequestMapping(value = "/api/v1/resourcelists", method = RequestMethod.GET, produces = "application/json")
	public List<ResourceListHandler> resourcelists() {
		logger.info(">> [PlanetaryOptimizerController.resourcelists]");
		// Get all the resource lists from the database.
		List<ResourceListHandler> resourceLists = new Vector<ResourceListHandler>(1);
		try {
			resourceLists = AppConnector.getDBConnector().getResourceListDao().queryForAll();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		logger.info("<< [PlanetaryOptimizerController.resourcelists]");
		return resourceLists;
	}
}
// - UNUSED CODE ............................................................................................
