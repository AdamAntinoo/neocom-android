package org.dimensinfin.eveonline.neocom.mining;

import java.util.List;

import android.app.Activity;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;

/**
 * @author Adam Antinoo
 */
public class NameFacetedMiningExtractionContainerPart extends AbstractAndroidPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("NameFacetedMiningExtractionContainerPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NameFacetedMiningExtractionContainerPart( final NameFacetedMiningExtractionContainer node ) {
		super(node);
		//		this.setThemeColor(Separator.ESeparatorType.LINE_WHITE);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public NameFacetedMiningExtractionContainer getCastedModel() {
		return (NameFacetedMiningExtractionContainer) this.getModel();
	}

	public String getTContentsValue() {
		return NeoComAbstractPart.generatePriceString(getCastedModel().getTotalValue(), true, true);
	}

	public String getTContentsVolume() {
		return NeoComAbstractPart.qtyFormatter.format(getCastedModel().getTotalVolume()) + " m3";
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return getCastedModel().getTitle().hashCode();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new DateFacetedMiningExtractionContainerRender(this, getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("PanelTitlePart [ ");
		buffer.append("title: ").append(getCastedModel().getTitle());
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	@Override
	public List<IPart> runPolicies( final List<IPart> targets ) {
		return super.runPolicies(targets);
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class DateFacetedMiningExtractionContainerRender extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................
		// - U I   F I E L D S
		protected TextView title = null;
		protected TextView contentsVolume = null;
		protected TextView contentsValue = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public DateFacetedMiningExtractionContainerRender( final AbstractPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public NameFacetedMiningExtractionContainerPart getPart() {
			return (NameFacetedMiningExtractionContainerPart) super.getPart();
		}

		// --- I R E N D E R   I N T E R F A C E
		@Override
		public void initializeViews() {
			//			super.initializeViews();
			this.title = (TextView) this._convertView.findViewById(R.id.title);
			this.contentsVolume = (TextView) this._convertView.findViewById(R.id.contentsVolume);
			this.contentsValue = (TextView) this._convertView.findViewById(R.id.contentsValue);
		}

		@Override
		public void updateContent() {
			//			super.updateContent();
			this.title.setText(this.getPart().getCastedModel().getTitle());
			this.contentsVolume.setText(this.getPart().getTContentsVolume());
			this.contentsValue.setText(this.getPart().getTContentsValue());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.namefaceted4group;
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
//[01]
