//	PROJECT:      MS-PlanetaryOptimizer (MS-PO)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	SpringBoot-MS-Java 1.8.
//	DESCRIPTION:	Initial step to have an Eve application based on the Android code. This version exports
//								a list of assets and also has access to the Eve Item CCP database to get some of the
//								asset items properties..
package org.dimensinfin.eveonline.neocom.microservices.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.database.NeocomDBHelper;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.ResourceListHandler;
import org.dimensinfin.eveonline.poc.planetary.PlanetaryProcessor;
import org.dimensinfin.eveonline.poc.planetary.PlanetaryScenery;
import org.dimensinfin.eveonline.poc.planetary.ProcessingAction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

// - CLASS IMPLEMENTATION ...................................................................................
@RestController
public class PlanetaryOptimizerController {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("PlanetaryOptimizerServiceApp");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@CrossOrigin()
	@RequestMapping(value = "/api/v1/newresource", method = RequestMethod.POST, produces = "application/json")
	public Vector<Resource> newresource(@RequestBody final ResourcePack newlist) {
		logger.info(">> [PlanetaryOptimizerController.newresource]");
		Vector<Resource> planetaryAssets = new Vector<Resource>();

		// Create the new resource to be stored on the database.
		PlanetaryResource newr = newlist.getData();
		newr.setOwnerListid(newlist.getId());
		try {
			AppConnector.getDBConnector().getPlanetaryResourceDao().create(newr);

			// Generate the list of transformed resources and return it to the UI.
			logger.info("<< [PlanetaryOptimizerController.newresource]");
			planetaryAssets = transformList(newlist.getId());
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return planetaryAssets;
	}

	@CrossOrigin()
	@RequestMapping(value = "/api/v1/resourcelist/{name}", method = RequestMethod.GET, produces = "application/json")
	public List<PlanetaryResource> resourcelist4Name(@PathVariable final String name) {
		logger.info(">> [PlanetaryOptimizerController.resourcelist4Name]");
		logger.info(">> [PlanetaryOptimizerController.resourcelist4Name]> name: " + name);
		// Get the resources for this specific list name.
		List<PlanetaryResource> resources = new Vector<PlanetaryResource>(1);
		try {
			Dao<ResourceListHandler, String> resourceListDao = AppConnector.getDBConnector().getResourceListDao();
			Dao<PlanetaryResource, String> planetaryResourceDao = AppConnector.getDBConnector().getPlanetaryResourceDao();
			QueryBuilder<ResourceListHandler, String> queryList = resourceListDao.queryBuilder();
			QueryBuilder<PlanetaryResource, String> queryResource = planetaryResourceDao.queryBuilder();
			Where<ResourceListHandler, String> where = queryList.where();
			where.eq("name", name);
			queryResource.join(queryList);

			PreparedQuery<PlanetaryResource> preparedQuery = queryResource.prepare();
			resources = planetaryResourceDao.query(preparedQuery);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		logger.info("<< [PlanetaryOptimizerController.resourcelist4Name]");
		return resources;
	}

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

	private Vector<Resource> transformList(int listid) throws SQLException {
		// Get all the resources on the list and process them.
		List<PlanetaryResource> resources = AppConnector.getDBConnector().getPlanetaryResourceDao()
				.queryForEq(NeocomDBHelper.OWNERLISTID_FN, listid);
		// Thansform the list of planetary resources into standard Eve resources.
		Vector<Resource> planetaryAssets = new Vector<Resource>();
		for (PlanetaryResource pr : resources) {
			Resource pa = new Resource(pr.getTypeid(), Double.valueOf(pr.getQuantity()).intValue());
			planetaryAssets.add(pa);
		}

		// The Planetary Advisor requires a list of Planetary Resources to be stocked to start the profit calculations.
		PlanetaryScenery scenery = new PlanetaryScenery();
		scenery.stock(planetaryAssets);

		// Create the initial processing point and start the optimization recursively.
		PlanetaryProcessor proc = new PlanetaryProcessor(scenery);
		// Start running the best profit search.
		Vector<ProcessingAction> bestScenario = proc.startProfitSearch(null);
		// Print the output
		return proc.getResources();
	}
}

final class ResourcePack implements Serializable {
	private static final long	serialVersionUID	= 9043918926214241311L;
	public int								id;
	public PlanetaryResource	data;

	public PlanetaryResource getData() {
		return data;
	}

	public int getId() {
		return id;
	}

	public void setData(PlanetaryResource data) {
		this.data = data;
	}

	public void setId(int id) {
		this.id = id;
	}

}
// - UNUSED CODE ............................................................................................
