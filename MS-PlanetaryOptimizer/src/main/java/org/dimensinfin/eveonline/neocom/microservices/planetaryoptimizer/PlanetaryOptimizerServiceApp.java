//	PROJECT:      MS-PlanetaryOptimizer (MS-PO)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	SpringBoot-MS-Java 1.8.
//	DESCRIPTION:	Initial step to have an Eve application based on the Android code. This version exports
//								a list of assets and also has access to the Eve Item CCP database to get some of the
//								asset items properties..
package org.dimensinfin.eveonline.neocom.microservices.planetaryoptimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.databases.NeocomDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.planetary.ResourceListHandler;
import org.dimensinfin.neocom.models.PlanetaryResource;
import org.dimensinfin.poc.asb.planetaryoptimizer.main.NeoComApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// - CLASS IMPLEMENTATION ...................................................................................
@SpringBootApplication
@Controller
public class PlanetaryOptimizerServiceApp {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("PlanetaryOptimizerServiceApp");

	// - M A I N   E N T R Y P O I N T ........................................................................
	/**
	 * Just create the Spring application and launch it to run.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		// Instance and connect the Adaptors.
		SpringApplication.run(NeoComApplication.class, args);
	}

	// - F I E L D - S E C T I O N ............................................................................
	private NeocomDatabaseAdapter dbNeocomAdapter = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryOptimizerServiceApp() {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@CrossOrigin()
	@RequestMapping(value = "/api/v1/resourcelists", method = RequestMethod.GET, produces = "application/json")
	public List<ResourceListHandler> resourcelists() {
		logger.info(">> [NeoComApplication.resourcelist]");
		logger.info("-- [NeoComApplication.resourcelist]> name: " + name);
		// Search the list name in the repository.
		ArrayList<PlanetaryResource> hitlist = listRepository.get(name);
		if (null == hitlist) {
			//			// Get the demo list of the resource list and return it to the caller
			//			ResourceList newrl = new ResourceList();
			//			newrl.mockup();
			//			Map<String, List<PlanetaryResource>> rl = new HashMap<String, List<PlanetaryResource>>();
			//			List<PlanetaryResource> rllist = new ArrayList<PlanetaryResource>();
			//			rllist.add(new PlanetaryResource(123, 234.0));
			//			rllist.add(new PlanetaryResource(234, 345.0));
			//			rl.put("PruebaInicial", rllist);
			//			logger.info("<< [NeoComApplication.addresourcelist]");
			//			return newrl;
			//		} else {
			hitlist = new ArrayList<PlanetaryResource>();
			//			hitlist.add(newlist.getData());
			listRepository.put(name, hitlist);
		}
		logger.info("<< [NeoComApplication.addresourcelist]");
		ResourceList newrl = new ResourceList();
		newrl.setName(name);
		newrl.setList(hitlist);
		return newrl;
	}
}
// - UNUSED CODE ............................................................................................
