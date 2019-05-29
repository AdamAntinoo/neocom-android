package org.dimensinfin.eveonline.neocom.planetary.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.activity.ASimplePagerFragment;
import org.dimensinfin.android.mvc.core.AppCompatibilityUtils;
import org.dimensinfin.android.mvc.core.IConverter;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.POCDrawingCanvasApplication;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroid;
import org.dimensinfin.eveonline.neocom.components.ESINetworkManagerMock;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.factory.POCAndroidComponentFactory;
import org.dimensinfin.eveonline.neocom.features.credential.adapters.persistence.CredentialRepository;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.PlanetType;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.PlanetaryStructureType;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.Structure;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.StructureGroup;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.controllers.PlanetaryInteractionControllerFactory;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.ui.APlanetaryStructureImageView;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryDataSource;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import static org.dimensinfin.eveonline.neocom.activity.ColonyLayoutActivity.ESI_TARGET;

public class ColonyLayoutFragment extends AMVCPagerFragment {
	private Credential credential;
	@Override
	public String getSubtitle() {
		return "POC Single Colony";
	}

	@Override
	public String getTitle() {
		return "PLANETARY DATA";
	}

	@Override
	public IControllerFactory createFactory() {
		return new PlanetaryControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(PlanetaryColoniesActivity.EPlanetaryVariants.PLANETARY_COLONIES.name()))
			return new PlanetaryDataSource.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCredential(this.getCredential())
					       .withEsiAdapter(NeoComAndroid.getSingleton().getEsiAdapter())
					       .build();
		throw new NeoComRuntimeException("Planetary variant has not defined Data Source to generate content.");
	}
	public Credential getCredential() {
		return credential;
	}

	public ColonyLayoutFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	public static class ColonyDataSource extends AMVCDataSource {
		protected CredentialRepository credentialRepository = POCAndroidComponentFactory.getInstance().getCredentialRepository();
		//		private List<GetCharactersCharacterIdPlanets200Ok> colonies; // List of character colonies
		private GetCharactersCharacterIdPlanets200Ok colony; // The selected colony
		//		private GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer;
		private List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pins; // List of pins for the selected planet.
		/**
		 * There are a list of planet structures that are from the ESI domain and then this same list of structures but converted
		 * to our model domain. We should keep the ESI domain data because contains the detailed structure information. That detailed infromation
		 * should be connected to the model data as a <b>delegate</b>.
		 */
		private List<Structure> colonyStructures = new ArrayList<>();
		private Structure commandCenter;
		/**
		 * Aggregation map for the selected planet structures. Each entry on the map aggregates the instances and the number of the
		 * map entry type planet planetary interaction structures.
		 */
		private Map<Integer, List<GetCharactersCharacterIdPlanetsPlanetIdOkPins>> structureMap = new HashMap<>();

		protected ColonyDataSource() { }

		@Override
		public synchronized void collaborate2Model() {
			try {
				logger.info(">> [ColonyDataSource.collaborate2Model]");
				// - S E C T I O N   H E A D E R
				logger.info("-- [ColonyDataSource.collaborate2Model]> Section Header");
				for (Map.Entry<Integer, List<GetCharactersCharacterIdPlanetsPlanetIdOkPins>> structures : this.structureMap.entrySet()) {
					try {
						this.addHeaderContents(new PinGroup2StructureGroupConverter().convert(structures.getValue()));
					} catch (RuntimeException rtex) {
						logger.info("-- [ColonyDataSource.collaborate2Model]>Skip model because there is no item information");
					}
				}
				// - S E C T I O N   D A T A
				for (Structure structure : this.colonyStructures) {
					// Transform and insert the structure model into the data source.
					//					final Structure structure = new EsiPin2StructureConverter().convert( structure )
					//							                            .setPlanetType();
					this.addModelContents(structure.setCommandCenter(this.commandCenter));
				}
			} catch (RuntimeException runtime) {
				runtime.printStackTrace();
			}
			//			this.addModelContents(new ColonyMap());
			logger.info("<< [ColonyDataSource.collaborate2Model]");
		}

		/**
		 * This is the action to signal the data source to start searching for its data to process it and generate
		 * the contents for the root model list. The model generated is still not processed and no controllers are created until
		 * the viewer requires them. This call is to create any internal data source data structureContainer that are to be used when extracting the
		 * header and data section models.
		 */
		@Override
		public synchronized void prepareModel() {
			logger.info(">> [ColonyDataSource.prepareModel]");
			// Get a valid Credential to be able to call the Services.
			AppCompatibilityUtils.assertNotNull(this.getExtras());
			final String esiTarget = AppCompatibilityUtils.assertNotNull(this.getExtras().getString(ESI_TARGET));
			final long characterIdentifier = this.getExtras().getLong(ColonyLayoutActivity.CHARACTER_ACCOUNT_IDENTIFIER);
			final long colonyIdentifier = this.getExtras().getLong(ColonyLayoutActivity.COLONY_IDENTIFIER);
			// Get the credential to be able to access the repository data.
			final String credentialIdentifier = esiTarget + "/" + characterIdentifier;
			final Credential credential = AppCompatibilityUtils.assertNotNull(credentialRepository.findById(credentialIdentifier));

			// Read the colony data and generate the support structureContainer, both for the header and for the main.
			final List<GetCharactersCharacterIdPlanets200Ok> colonies = ESINetworkManagerMock.getCharactersCharacterIdPlanets(credential.getAccountId()
					, credential.getRefreshToken()
					, credential.getDataSource());
			for (GetCharactersCharacterIdPlanets200Ok colonyEsi : colonies) {
				// Search for only the requested colony.
				if (colonyEsi.getPlanetId() == colonyIdentifier) {
					this.colony = colonyEsi;
					break;
				}
			}
			// If we have found the colony then get the colony structureContainer.
			if (null != this.colony) {
				final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = ESINetworkManagerMock.getCharactersCharacterIdPlanetsPlanetId(credential.getAccountId()
						, Long.valueOf(colonyIdentifier).intValue()
						, credential.getRefreshToken()
						, credential.getDataSource());
				this.pins = structureContainer.getPins();
				this.colonyStructures = Stream.of(structureContainer.getPins())
						                        .map(pin -> {
							                        // Aggregate pins of identical type into a pin map.
							                        List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> found = this.structureMap.get(pin.getTypeId());
							                        if (null == found) {
								                        found = new ArrayList<>();
								                        found.add(pin);
								                        this.structureMap.put(pin.getTypeId(), found);
							                        } else found.add(pin);
							                        return pin;
						                        })
						                        .map(pin -> {
							                        // Transform each pin into a model structure.
							                        final Structure struc = new Structure.Builder()
									                                                .withPin(pin)
									                                                .withPlanetType(PlanetType.valueOf(this.colony.getPlanetType().name()))
									                                                .build();
							                        return struc;
						                        })
						                        .map(structure -> {
							                        // Search for the command center and store it on the field.
							                        if (structure.getStructureType() == PlanetaryStructureType.COMMAND_CENTER)
								                        this.commandCenter = structure;
							                        return structure;
						                        })
						                        .collect(Collectors.toList());
			}
			logger.info("<< [ColonyDataSource.prepareModel]");
		}

		//		/**
		//		 * This is the function that will complete the conversion from the ESI data structures to the data source domain model entities that
		//		 * later will become the node sources for the Controllers and from them to the Renders to present the data.
		//		 *
		//		 * The first section will process the structures and will aggregate them into a map to group equal structures so the counter can be presented
		//		 * on the render.
		//		 *
		//		 * @param container the ESI entity the contains all the colony structure and connection elements.
		//		 */
		//		private void processStructureContainer(final GetCharactersCharacterIdPlanetsPlanetIdOk container) {
		//			this.pins = container.getPins();
		//			for (GetCharactersCharacterIdPlanetsPlanetIdOkPins pin : container.getPins()) {
		//				List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> found = this.structures.get(pin.getTypeId());
		//				if (null == found) {
		//					found = new ArrayList<>();
		//					found.add(pin);
		//					this.structures.put(pin.getTypeId(), found);
		//				} else found.add(pin);
		//			}
		//		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<ColonyDataSource, Builder> {
			@Override
			protected ColonyDataSource getActual() {
				return new ColonyDataSource();
			}

			@Override
			protected Builder getActualBuilder() {
				return this;
			}
		}
	}

	public static class PinGroup2StructureGroupConverter implements IConverter<List<GetCharactersCharacterIdPlanetsPlanetIdOkPins>, StructureGroup> {
		@Override
		public StructureGroup convert( final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pinList ) {
			// For each structure get the Item data definition.
			final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = pinList.get(0);
			final GetUniverseTypesTypeIdOk item = ESINetworkManagerMock.getUniverseTypeById("tranquility"
					, pin.getTypeId());
			AppCompatibilityUtils.testAssertNotNull(item);
			// Convert each pin group into a header structure aggregator instance.
			return new StructureGroup.Builder(item.getName())
						   .withGroupId(item.getGroupId())
						   .withIconReference(PlanetaryStructureType.getStructureGroup(item.getGroupId()).getIconReferenceId())
					       .withGroupCount(pinList.size())
					       .build();
		}
	}
}
