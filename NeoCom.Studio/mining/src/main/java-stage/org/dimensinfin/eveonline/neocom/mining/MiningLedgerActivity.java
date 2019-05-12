package org.dimensinfin.eveonline.neocom.mining;

import android.os.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This activity will display the list of Mining Extractions. On the NeoCom implementation we detect the incoming of new data by
 * quantity differentiation and we generate delta records to report the increase of mining ore during time and not only by date
 * aggregation.
 *
 * We need some input data to identify the Pilot or Corporation from where to get that mining records.
 *
 * Activities are the key data repository for the rest of the objects. If the extras do not convey the required information the
 * Activity should stop and display the appropiate message for the user. All checks should be made at this level and during the
 * complete lifecycle. Once control goes to Fragments or DataSources we understand that all checks are validated and that the
 * required data is available.
 *
 * P A R A M E T E R S
 * This Activity requires the <b>CAPSULEER_IDENTIFIER</b> to be able to extract the right credential from the current list of
 * Credentials. If this parameter is not found or the matching credential is not available we should cancel the normal display of
 * the page and go to some Exception reporting panel.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningLedgerActivity extends APublicCredentialActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningLedgerActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [MiningLedgerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			this.addPage(new MiningLedgerFragment().setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name()));
			this.addPage(new MineralResourcesFragment());
			this.addPage(new MiningLedgerFragment().setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()));
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("MiningLedgerActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [MiningLedgerActivity.onCreate]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
