package org.dimensinfin.eveonline.neocom.sharedcode;

import org.dimensinfin.eveonline.neocom.model.NeoComNode;

/**
 * This class represents the titled label that is shown on top of panels. We can consider the panels as the current fragments. ON the
 * Angular platform it is more clear this distinction but on fragments we have no container drawing so the separation of concepts is a
 * little more diffuse.
 *
 * @author Adam Antinoo
 */
public class PanelTitle extends NeoComNode {
	private String title = "-";

	// - C O N S T R U C T O R S
	public PanelTitle( final String title ) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public PanelTitle setTitle( final String title ) {
		this.title = title;
		return this;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("PanelTitle [ ");
		buffer.append("title: ").append(title).append(" ");
		buffer.append("]");
		return buffer.toString();
	}
}
