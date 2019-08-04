package org.dimensinfin.eveonline.neocom.app.support.worlds;

import android.view.ViewGroup;
import android.widget.ListView;

public class NeoComWorld {
	private ViewGroup headerContainer;
	private ListView dataSectionContainer;

	public ViewGroup getHeaderContainer() {
		return this.headerContainer;
	}

	public NeoComWorld setHeaderContainer( final ViewGroup headerContainer ) {
		this.headerContainer = headerContainer;
		return this;
	}

	public ListView getDataSectionContainer() {
		return this.dataSectionContainer;
	}

	public NeoComWorld setDataSectionContainer( final ListView dataSectionContainer ) {
		this.dataSectionContainer = dataSectionContainer;
		return this;
	}
}
