package org.dimensinfin.eveonline.neocom.test.support;

import java.util.ArrayList;
import java.util.List;

public class Filter<C> {
	private List<C> elements = new ArrayList<>();

	public Filter<C> addElement( final C element ) {
		this.elements.add( element );
		return this;
	}

	public List<C> getFilterResults() {
		return this.elements;
	}
	//	private Filter() {}
//
//	// - B U I L D E R
//	public static class Builder {
//		private Filter onConstruction;
//
//		public Builder() {
//			this.onConstruction = new Filter();
//		}
//
//		public Filter build() {
//			return this.onConstruction;
//		}
//	}
}
