package org.dimensinfin.eveonline.neocom.mining;

import java.util.concurrent.ExecutionException;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;

import org.joda.time.DateTime;

/**
 * @author Adam Antinoo
 */
public class TodayMiningTimeLimitedMinigExtractionContainer extends NameFacetedMiningExtractionContainer {
	public TodayMiningTimeLimitedMinigExtractionContainer( final String name ) {
		super(name);
	}

	public int addExtraction( final MiningExtraction miningOperation ) {
		this.contents.add(miningOperation);
		// Sum only the values if we are on the same hour that the current hour.
		final int hour = DateTime.now().getHourOfDay();
		final int extractionHour = miningOperation.getExtractionHour();
		if (hour == extractionHour) {
//			try {
//				totalValue += miningOperation.getQuantity() * miningOperation.getHighestBuyerPrice().getPrice();
//			} catch (ExecutionException ee) {
				totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
//			} catch (InterruptedException ie) {
//				totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
//			}
			totalVolume += miningOperation.getQuantity() * miningOperation.getVolume();
		}
		return contents.size();
	}
}
