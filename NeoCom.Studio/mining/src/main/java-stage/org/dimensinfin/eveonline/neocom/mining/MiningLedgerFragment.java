//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.mining;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.datasource.ExceptionDataSource;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.factory.IndustryPartFactory;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.model.InformationPanel;
import org.dimensinfin.eveonline.neocom.model.NameFacetedMiningExtractionContainer;
import org.dimensinfin.eveonline.neocom.model.PanelTitle;
import org.dimensinfin.eveonline.neocom.model.TodayMiningTimeLimitedMinigExtractionContainer;

import java.beans.PropertyChangeEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningLedgerFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("MiningLedgerFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- P A G E R F R A G M E N T   O V E R R I D E S
	@Override
	public IPartFactory createFactory() {
		return new IndustryPartFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		if (this.getVariant() == NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name())
			return "TODAY MINING EXTRACTIONS DELTA";
		if (this.getVariant() == NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name())
			return "ALL MINING EXTRACTIONS BY DATE";
		return "-";
	}

	@Override
	public String getTitle() {
		final Credential credential = ((ICredentialActivity) this.getAppContext()).getCredential();
		if (null != credential) return credential.getAccountName();
		else return "-";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		List<ICollaboration> headerContents = new ArrayList<>();
		headerContents.add(new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]"));
		return new ArrayList<>();
	}

	@Override
	protected org.dimensinfin.android.mvc.interfaces.IPartsDataSource registerDataSource() {
		logger.info(">< [MiningLedgerFragment.registerDataSource]");
		try {
			if (this.getAppContext() instanceof ICredentialActivity) {
				int pilotIdentifier = this.getExtras().getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
				final DataSourceLocator identifier = new DataSourceLocator()
						.addIdentifier(getVariant())
						.addIdentifier(pilotIdentifier)
						.addIdentifier("MINING-LEDGER");
				return new MiningLedgerDataSource(identifier
						, getVariant()
						, createFactory()
						, getExtras())
						.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
						.setRefreshTime(5)
						.setCacheable(false);
			} else
				return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
						, new PartFactory(getVariant()), getExtras())
						.registerException(new NeoComException("[MiningLedgerFragment.registerDataSource]> Parent Activity has not declared " +
								"Credential."));
		} catch (NeoComException neoe) {
			neoe.printStackTrace();
			// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
					, new PartFactory(getVariant()), getExtras())
					.registerException(neoe);
		} catch (RuntimeException rte) {
			rte.printStackTrace();
			// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
					, new PartFactory(getVariant()), getExtras())
					.registerException(rte);
		}
	}
	//..........................................................................................................

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class MiningLedgerDataSource extends MVCDataSource {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		/** Credential to be used on all database requests and data retrievals. */
		protected Credential credential = null;
		protected int refreshTime = 1;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MiningLedgerDataSource( final DataSourceLocator locator
				, final String variant
				, final IPartFactory factory
				, final Bundle extras ) throws NeoComException {
			super(locator, variant, factory, extras);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public MiningLedgerDataSource setCredential( final Credential credential ) {
			this.credential = credential;
			return this;
		}

		public MiningLedgerDataSource setRefreshTime( final int time ) {
			this.refreshTime = time;
			return this;
		}
		// --- I D A T A S O U R C E   I N T E R F A C E

		/**
		 * This data source can generate two sets of data.
		 * The first one is on the variant MINING_EXTRACTIONS_TODAY and will report the mining extractions done during today on a hour by
		 * hour aggregation. While on the database we have the total ore of the day for an ore type and location, I should delta this
		 * information with the one from the previous hour so my report will show the real ore mined on the hour and not the accumulated
		 * in
		 * existence.
		 * <p>
		 * The second comes from the variant MINING_EXTRACTIONS_ALL and shows the mining extractions grouped by date descending from today
		 * to
		 * the rest of the database stored data. We only report the 24 hour slot that is the total of the ore for the ore type / location.
		 */
		@Override
		public void collaborate2Model() {
			logger.info(">> [MiningLedgerDataSource.collaborate2Model]");
			// Check the required parameters. They are the Credential.
			if (null == credential)
				throw new NeoComRuntimeException("[MiningLedgerDataSource.<constructor>]> Data source has not received the Credential " +
						"mandatory " +
						"parameter.");

			// Check if the data is already on the cache so we should not have to process again the model generation.
			if (!isCached()) {
				cleanup();
				try {
					List<MiningExtraction> extractions = new ArrayList<>();

					// --- E X T R A C T I O N S   T O D A Y
					if (getVariant() == NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name()) {
						// Add the panel title up front of the Credential list.
						final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]");
						int extractionCount = 0;
						this.addModelContents(title);

						extractions = AndroidGlobalDataManager.accessTodayMiningExtractions4Pilot(credential);
						if (extractions.size() < 1) {
							// The list ofr today is empty. Put a label to report that case.
							this.addModelContents(new InformationPanel("There are no Mining Extractions Today."));
						}

						// Create a map of all the record ids
						Map<String, MiningExtraction> ids = new HashMap();
						for (MiningExtraction extraction : extractions) {
							ids.put(extraction.getRecordId(), extraction);
						}
						// Process the records in order. Aggregate by system.
						int processingSystem = -1;
						NameFacetedMiningExtractionContainer systemAggregator = null;
						for (MiningExtraction extraction : extractions) {
							// Get the extraction date and the hour.
							final String recordDate = extraction.getExtractionDate().split(":")[0];
							final int hour = extraction.getExtractionHour();
							final int system = extraction.getSolarSystemId();

							// Search on the ids if this record should have a previous hour record. This is the current id less 1.
							final String searchId = MiningExtraction.generateRecordId(recordDate, hour - 1
									, extraction.getTypeId(), system, extraction.getOwnerId());
							// Check if the id exists.
							if (ids.keySet().contains(searchId)) {
								// Get the previous record and perform the update before adding the record.
								final MiningExtraction previous = ids.get(searchId);
								long delta = extraction.getQuantity() - previous.getQuantity();
								// Skip if there is no change during the period.
								if (delta <= 0) continue;
								else {
									extraction.setDelta(delta);
									extraction.store();
								}
							}

							// Add the record but aggregate by system.
							if (processingSystem != system) {
								// Create a new aggregator and use it to get the rest of the extractors inside.
								systemAggregator = new TodayMiningTimeLimitedMinigExtractionContainer(extraction.getSystemName());
								this.addModelContents(systemAggregator);
								processingSystem = extraction.getSolarSystemId();
							}
							systemAggregator.addExtraction(extraction);
							extractionCount++;
						}
						// Update the number of extractions.
						title.setTitle("TODAY MINING EXTRACTIONS [" + extractionCount + "]");
						scheduleRefresh(this.refreshTime);
					}

					// --- A L L   E X T R A C T I O N S
					if (getVariant() == NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()) {
						// Add the panel title up front of the Credential list.
						final PanelTitle title = new PanelTitle("ALL MINING EXTRACTIONS BY DATE");
						this.addModelContents(title);

						extractions = AndroidGlobalDataManager.accessMiningExtractions4Pilot(credential);

						// Aggregate the extractions by date and system so that information can be removed from the extractions panel.
						String processingDate = "";
						NameFacetedMiningExtractionContainer dateAggregator = null;
						for (MiningExtraction extraction : extractions) {
							// Get the extraction date.
							final String recordDate = extraction.getExtractionDate().split(":")[0];
							// Create a new aggregator if the dates do not match.
							if (!recordDate.equalsIgnoreCase(processingDate)) {
								// Create a new aggregator and use it to get the rest of the extractors inside.
								dateAggregator = new NameFacetedMiningExtractionContainer(recordDate);
								this.addModelContents(dateAggregator);
								processingDate = recordDate;
							}
							// Only aggregate extractions marked as hour 24 because the others are daily or redundant data.
							if (extraction.getExtractionHour() == 24) dateAggregator.addExtraction(extraction);
						}
						scheduleRefresh(this.refreshTime);
					}
				} catch (SQLException sqle) {
					AndroidGlobalDataManager.registerException("MiningLedgerDataSource.collaborate2Model"
							, sqle, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
					sqle.printStackTrace();
				} catch (RuntimeException rtex) {
					AndroidGlobalDataManager.registerRuntimeException("MiningLedgerDataSource.collaborate2Model"
							, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
					rtex.printStackTrace();
				}
			}
			logger.info("<< [MiningLedgerDataSource.collaborate2Model]");
		}

		protected void scheduleRefresh( final int time ) {
			// Schedule an event in 5 minutes to update this datasource and then the view.
			final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
			Runnable delayedTask = new Runnable() {
				@Override
				public void run() {
					logger.info("-- [MiningLedgerDataSource.collaborate2Model]> Firing data source refresh process.");
					propertyChange(new PropertyChangeEvent(this, SystemWideConstants.events.EVENTSTRUCTURE_REFRESHDATA.name()
							, null, null));
				}
			};
			mainThreadHandler.postDelayed(delayedTask, TimeUnit.MINUTES.toMillis(time));
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
