package org.dimensinfin.eveonline.neocom.assets.domain;

import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.domain.container.FacetedExpandableContainer;
import org.dimensinfin.eveonline.neocom.domain.space.SpaceRegion;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRegionsRegionIdOk;

public class RegionMVCWrapper implements SpaceRegion, IExpandable {
	private FacetedExpandableContainer<SpaceRegion, ?> container;
	private SpaceRegion facet;

	public RegionMVCWrapper( final FacetedExpandableContainer container ) {
		Objects.requireNonNull( container );
		this.container = container;
		this.facet = this.container.getFacet();
	}

	// -  I C O N T A I N E R
	public int getContentCount() {return container.getContentCount();}

	public List<? extends ICollaboration> getContents() {return container.getContents();}

	// - R E G I O N
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
}
