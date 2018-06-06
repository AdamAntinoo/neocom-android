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
package org.dimensinfin.eveonline.neocom.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.ExceptionDataSource;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.core.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.MiningExtraction;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.factory.IndustryPartFactory;
import org.dimensinfin.eveonline.neocom.model.DateFacetedMiningExtractionContainer;
import org.dimensinfin.eveonline.neocom.model.InformationPanel;
import org.dimensinfin.eveonline.neocom.model.PanelTitle;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningLedgerFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningLedgerFragment");

	// - F I E L D - S E C T I O N ............................................................................
	/** Credential to be used on all database requests and data retrievals. */
	protected Credential credential = null;

	// - M E T H O D - S E C T I O N ..........................................................................
	public MiningLedgerFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	// --- P A G E R F R A G M E N T   O V E R R I D E S
	@Override
	public IPartFactory createFactory() {
		return new IndustryPartFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		return "Mining Ledger";
	}

	@Override
	public String getTitle() {
		return "Pilot";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>();
	}

	@Override
	protected IDataSource registerDataSource() {
		logger.info(">< [MiningLedgerFragment.registerDataSource]");
		try {
			final DataSourceLocator identifier = new DataSourceLocator().addIdentifier("MINING-LEDGER")
			                                                            .addIdentifier(getVariant())
			                                                            .addIdentifier(credential.getAccountId());
			return new MiningLedgerDataSource(identifier
					, getVariant()
					, createFactory()
					, getExtras())
					.setCredential(credential)
					.setCacheable(false);
		} catch (NeoComException neoe) {
			neoe.printStackTrace();
			// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
					, new PartFactory(getVariant()), getExtras())
					.registerException(neoe);
		}
	}
	//..........................................................................................................

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class MiningLedgerDataSource extends MVCDataSource {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		/** Credential to be used on all database requests and data retrievals. */
		protected Credential credential = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MiningLedgerDataSource( final DataSourceLocator locator
				, final String variant
				, final IPartFactory factory
				, final Bundle extras ) throws NeoComException {
			super(locator, variant, factory, extras);
			//			// Check the required parameters. They are the Credential.
			//			if(null== credential)
			//				throw new NeoComException("[MiningLedgerDataSource.<constructor>]> Data source has not received the Credential mandatory " +
			//						"parameter.");
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public MiningLedgerDataSource setCredential( final Credential credential ) {
			this.credential = credential;
			return this;
		}

		// --- I D A T A S O U R C E   I N T E R F A C E

		/**
		 * This data source can generate two sets of data.
		 * The first one is on the variant MINING_EXTRACTIONS_TODAY and will report the mining extractions done during today on a hour by
		 * hour aggregation. While on the database we have the total ore of the day for an ore type and location, I should delta this
		 * information with the one from the previous hour so my report will show the real ore mined on the hour and not the accumulated in
		 * existence.
		 *
		 * The second comes from the variant MINING_EXTRACTIONS_ALL and shows the mining extractions grouped by date descending from today to
		 * the rest of the database stored data. We only report the 24 hour slot that is the total of the ore for the ore type / location.
		 */
		@Override
		public void collaborate2Model() {
			logger.info(">> [MiningLedgerDataSource.collaborate2Model]");
			// Check the required parameters. They are the Credential.
			if ( null == credential )
				throw new NeoComRuntimeException("[MiningLedgerDataSource.<constructor>]> Data source has not received the Credential " +
						"mandatory " +
						"parameter.");

			// Check if the data is already on the cache so we should not have to process again the model generation.
			if ( !isCached() ) {
				cleanup();
				try {
					List<MiningExtraction> extractions = new ArrayList<>();

					// --- E X T R A C T I O N S   T O D A Y
					if ( getVariant().equalsIgnoreCase(NeoComApplication.EVariants.MINING_EXTRACTIONS_TEST.name()) ) {
						// Add the panel title up front of the Credential list.
						final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS [-]");
						this.addModelContents(title);

						extractions = AndroidGlobalDataManager.accessTodayMiningExtractions4Pilot(credential);
						if ( extractions.size() < 1 ) {
							// The list ofr today is empty. Put a label to report that case.
							this.addModelContents(new InformationPanel("There are no Mining Extractions Today."));
						}

						// Get the list of ores from all the extractions.
						//						final Set<Integer> ores = new LinkedHashSet<>();
						//						for (MiningExtraction extraction : extractions) {
						//							ores.add(extraction.getTypeId());
						//						}
						// Do the processing one ore at a time so we can track the previous hour extraction and calculate the delta.
						//						for (Integer selectedOreType : ores) {
						//							int currentHour = -1;
						//							MiningExtraction previousExtraction = null;
						for (MiningExtraction extraction : extractions) {
							// Process only the record for this ore type.
							//								if ( extraction.getTypeId() == selectedOreType ) {
							//									// If there was a previous hour
							//									if ( currentHour > 0 ) {
							//										extraction.setQuantity(extraction.getQuantity() - previousExtraction.getQuantity());
							//										currentHour = extraction.getExtractionHour();
							//										previousExtraction = extraction;
							//										this.addModelContents(extraction);
							//										logger.info("-- [MiningLedgerDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
							//												, extraction.getResourceName());
							//									} else{
							//										currentHour = extraction.getExtractionHour();
							//										previousExtraction = extraction;
							this.addModelContents(extraction);
							logger.info("-- [MiningLedgerDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
									, extraction.getResourceName());
							//									}
							//								}
						}
						//						}
						// Update the number of extractions.
						title.setTitle("TODAY MINING EXTRACTIONS [" + extractions.size() + "]");
					}
					if ( getVariant().equalsIgnoreCase(NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name()) ) {
						// Add the panel title up front of the Credential list.
						final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS [-]");
						this.addModelContents(title);

						extractions = AndroidGlobalDataManager.accessTodayMiningExtractions4Pilot(credential);
						if ( extractions.size() < 1 ) {
							// The list ofr today is empty. Put a label to report that case.
							this.addModelContents(new InformationPanel("There are no Mining Extractions Today."));
						}

						// Get the list of ores from all the extractions.
						final Set<Integer> ores = new LinkedHashSet<>();
						for (MiningExtraction extraction : extractions) {
							ores.add(extraction.getTypeId());
						}
						// Do the processing one ore at a time so we can track the previous hour extraction and calculate the delta.
						for (Integer selectedOreType : ores) {
							int currentHour = -1;
							long previousExtraction = 0;
							for (MiningExtraction extraction : extractions) {
								// Process only the record for this ore type.
								if ( extraction.getTypeId() == selectedOreType ) {
									// If there was a previous hour
									if ( currentHour > 0 ) {
										long delta = extraction.getQuantity() - previousExtraction;
										previousExtraction = extraction.getQuantity();
										if ( delta > 0 ) {
											extraction.setQuantity(delta);
											currentHour = extraction.getExtractionHour();
											this.addModelContents(extraction);
											logger.info("-- [MiningLedgerDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
													, extraction.getResourceName());
										}
									} else {
										previousExtraction = extraction.getQuantity();
										currentHour = extraction.getExtractionHour();
										this.addModelContents(extraction);
										logger.info("-- [MiningLedgerDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
												, extraction.getResourceName());
									}
								}
							}
						}
						// Update the number of extractions.
						title.setTitle("TODAY MINING EXTRACTIONS [" + extractions.size() + "]");
					}

					// --- A L L   E X T R A C T I O N S
					if ( getVariant().equalsIgnoreCase(NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()) ) {
						extractions = AndroidGlobalDataManager.accessMiningExtractions4Pilot(credential);

						// Aggregate the extractions by date and system so that information can be removed from the extractions panel.
						String processingDate = "";
						DateFacetedMiningExtractionContainer dateAggregator = null;
						for (MiningExtraction extraction : extractions) {
							// Get the extraction date.
							final String recordDate = extraction.getExtractionDate().split(":")[0];
							// Create a new aggregator if the dates do not match.
							if ( !recordDate.equalsIgnoreCase(processingDate) ) {
								// Create a new aggregator and use it to get the rest of the extractors inside.
								dateAggregator = new DateFacetedMiningExtractionContainer(recordDate);
								this.addModelContents(dateAggregator);
								processingDate = recordDate;
							}
							// Only aggregate extractions marked as hour 24 because the others are daily or redundant data.
							if ( extraction.getExtractionHour() == 24 ) dateAggregator.addMiningExtraction(extraction);
						}
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
	}
}
// - UNUSED CODE ............................................................................................
//[01]
