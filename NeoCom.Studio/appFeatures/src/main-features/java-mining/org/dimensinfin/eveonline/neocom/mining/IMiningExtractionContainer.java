package org.dimensinfin.eveonline.neocom.mining;

import java.util.List;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;

/**
 * @author Adam Antinoo
 */
public interface IMiningExtractionContainer {
	int addExtraction( MiningExtraction node );

	List<MiningExtraction> getExtractions();

	int getContentSize();

	double getTotalValue();

	double getTotalVolume();
}
