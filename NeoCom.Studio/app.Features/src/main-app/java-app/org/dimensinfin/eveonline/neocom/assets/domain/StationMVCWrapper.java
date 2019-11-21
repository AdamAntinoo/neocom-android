package org.dimensinfin.eveonline.neocom.assets.domain;

import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.domain.container.FacetedExpandableContainer;
import org.dimensinfin.eveonline.neocom.domain.space.Station;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseConstellationsConstellationIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRegionsRegionIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseStationsStationIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseSystemsSystemIdOk;

public class StationMVCWrapper implements Station, IExpandable {
	private FacetedExpandableContainer<Station, ?> container;
	private Station facet;

	public StationMVCWrapper( final FacetedExpandableContainer container ) {
		Objects.requireNonNull( container );
		this.container = container;
		this.facet = this.container.getFacet();
	}

	// -  I C O N T A I N E R
	public int getContentCount() {return container.getContentCount();}

	public List<? extends ICollaboration> getContents() {return container.getContents();}

	// - S T A T I O N
	@Override
	public Integer getStationId() {return facet.getStationId();}

	@Override
	public GetUniverseStationsStationIdOk getStation() {return facet.getStation();}

	@Override
	public String getStationName() {return facet.getStationName();}

	@Override
	public Integer getSolarSystemId() {return facet.getSolarSystemId();}

	@Override
	public GetUniverseSystemsSystemIdOk getSolarSystem() {return facet.getSolarSystem();}

	@Override
	public String getSolarSystemName() {return facet.getSolarSystemName();}

	@Override
	public Integer getConstellationId() {return facet.getConstellationId();}

	@Override
	public GetUniverseConstellationsConstellationIdOk getConstellation() {return facet.getConstellation();}

	@Override
	public String getConstellationName() {return facet.getConstellationName();}

	@Override
	public Integer getRegionId() {return facet.getRegionId();}

	@Override
	public GetUniverseRegionsRegionIdOk getRegion() {return facet.getRegion();}

	@Override
	public String getRegionName() {return facet.getRegionName();}

	@Override
	public Long getLocationId() {return facet.getLocationId();}

	// - C O L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {return container.collaborate2Model( variant );}

	// - I E X P A N D A B L E
	@Override
	public boolean isEmpty() {return container.isEmpty();}

	@Override
	public boolean collapse() {return container.collapse();}

	@Override
	public boolean expand() {return container.expand();}

	@Override
	public boolean toggleExpand() {return container.toggleExpand();}

	@Override
	public boolean isExpanded() {return container.isExpanded();}

	@Override
	public int compareTo( final Object target ) {return container.compareTo( target );}
	// - B U I L D E R
//	public static class Builder {
//		private StationMVCWrapper onConstruction;
//
//		public Builder() {
//			this.onConstruction = new StationMVCWrapper( facet );
//		}
//
//		public StationMVCWrapper build() {
//			return this.onConstruction;
//		}
//	}
}
